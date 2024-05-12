package co.udea.airline.api.services.seats.service;

import co.udea.airline.api.model.jpa.model.seats.Seat;

import java.util.Optional;

public interface ISeatService {
    //Seat save(Seat s);
    //Seat update(Seat seat);
    Optional<Seat> findSeatById(Long id);
    
}
