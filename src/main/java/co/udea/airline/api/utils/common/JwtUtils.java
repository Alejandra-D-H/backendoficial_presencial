package co.udea.airline.api.utils.common;

import java.security.KeyPair;
import java.time.Instant;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.stereotype.Component;

import co.udea.airline.api.model.jpa.model.security.Person;

@Component
public class JwtUtils {

    @Autowired
    JwtEncoder jwtEncoder;

    @Autowired
    JwtDecoder jwtDecoder;

    @Autowired
    KeyPair keyPair;

    private final Long EXPIRATION = 8 * 60 * 60L; // in seconds

    public Jwt createToken(Person person) {

        Instant now = Instant.now();

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .subject(person.getEmail())
                .claim("roles", person.getPositions())
                .claim("privileges", person.getAuthorities())
                .issuer("https://airline-api.com")
                .issuedAt(now)
                .expiresAt(now.plusSeconds(EXPIRATION))
                .build();

        return jwtEncoder.encode(JwtEncoderParameters.from(claims));

    }

    public Jwt getToken(String token) {
        return jwtDecoder.decode(token);
    }

    public boolean validateToken(Jwt jwt) throws JwtException {
        if (Instant.now().isAfter(jwt.getExpiresAt())) {
            throw new JwtException("token has expired");
        }
        return false;
    }

}