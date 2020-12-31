package ua.kpi.tef.demo_ticket.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ua.kpi.tef.demo_ticket.entity.Trip;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface TripRepository extends JpaRepository<Trip, Long> {

    Optional<Trip> findTripById(Long id);

    @Query(nativeQuery = true, value ="select * from trip where from_where = :fromWhere " +
                                        "and where_to= :whereTo and departure_date = :departureDate")
    List<Trip> findByFromWhereAndWhereToAndDepartureDate(@Param("fromWhere") String fromWhere,
                                                         @Param("whereTo") String whereTo,
                                                         @Param("departureDate") LocalDate departureDate);

    @Query(nativeQuery = true, value ="select * from trip where from_where = :fromWhere and where_to= :whereTo " +
            "and departure_date = :departureDate and arrival_date = :arrivalDate")
    List<Trip> findByFromWhereAndWhereToAndDepartureDateAndArrivalDate(@Param("fromWhere") String fromWhere,
                                                                       @Param("whereTo") String whereTo,
                                                                       @Param("departureDate") LocalDate departureDate,
                                                                       @Param("arrivalDate") LocalDate arrivalDate);
    //List<Trip> findByFromWhereAndWhereTo(String fromWhere, String whereTo);
    //Optional<Trip> findTripByTripName(@NonNull String tripName);
}
