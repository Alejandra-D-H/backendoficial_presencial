package co.udea.airline.api.utils.config.security;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import co.udea.airline.api.utils.common.JwtUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class JWTTokenFilter extends OncePerRequestFilter {

    @Autowired
    JwtUtils jwtUtils;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String bearerToken = request.getHeader("Authorization");

        if (bearerToken == null || !bearerToken.startsWith("Bearer ")) {
            SecurityContextHolder.clearContext();
        } else {

            try {
                Jwt jwt = jwtUtils.getToken(bearerToken.substring("Bearer ".length()));
                jwtUtils.validateToken(jwt);
                Authentication auth = new JwtAuthenticationToken(jwt);
                SecurityContextHolder.getContext().setAuthentication(auth);

            } catch (JwtException e) {
                // this is very important, since it guarantees the user is not authenticated at
                // all
                SecurityContextHolder.clearContext();
                log.debug("Bearer token was rejected: %s".formatted(bearerToken));
            }
        }

        filterChain.doFilter(request, response);

    }
    
}
