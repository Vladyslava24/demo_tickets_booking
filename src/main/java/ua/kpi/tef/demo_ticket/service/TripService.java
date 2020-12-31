package ua.kpi.tef.demo_ticket.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ua.kpi.tef.demo_ticket.controller.exception.*;
import ua.kpi.tef.demo_ticket.dto.TripDto;
import ua.kpi.tef.demo_ticket.entity.AviaTicket;
import ua.kpi.tef.demo_ticket.entity.Hotel;
import ua.kpi.tef.demo_ticket.entity.Trip;
import ua.kpi.tef.demo_ticket.entity.enums.TicketStatus;
import ua.kpi.tef.demo_ticket.mappers.LocalizedDtoMapper;
import ua.kpi.tef.demo_ticket.repository.TripRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class TripService {
    private final TripRepository tripRepository;

    @Autowired
    public TripService(TripRepository tripRepository) {
        this.tripRepository = tripRepository;
    }

    /*public Trip loadTripByTripName(@NonNull String tripName) throws TripNameNotFoundException {
        return tripRepository.findTripByTripName(tripName).orElseThrow(() -> new TripNameNotFoundException("Trip " + tripName + " not found!"));
    }*/
    public List<TripDto> getAllTripDto() {
        //List<Trip> trips = new ArrayList<>();
        //tripRepository.findAll().forEach(trips::add);
        return tripRepository.findAll().stream()
                .map(m -> getLocalizedDTO().map(m))
                //.map(TripMapper.INSTANCE::tripToTripDto)
                .collect(Collectors.toList());
    }

    public List<TripDto> getAllAviaTripDto() {
        return tripRepository.findAll().stream()
                .map(m -> getLocalizedDTO().map(m))
                .filter(p->p.getTripType().toString().equals("AVIA"))
                .collect(Collectors.toList());
    }

    public List<TripDto> getAllRailwayTripDto() {
        return tripRepository.findAll().stream()
                .map(m -> getLocalizedDTO().map(m))
                .filter(p->p.getTripType().toString().equals("RAILWAY"))
                .collect(Collectors.toList());
    }

    public List<TripDto> getAllBusTripDto() {
        return tripRepository.findAll().stream()
                .map(m -> getLocalizedDTO().map(m))
                .filter(p->p.getTripType().toString().equals("BUS"))
                .collect(Collectors.toList());
    }

    //public List<Trip> findAll(){ return tripRepository.findAll(); }

    public List<TripDto> getSearchedAviaTrip(String whereFrom, String whereTo, LocalDate departureDate){

        return tripRepository.findByFromWhereAndWhereToAndDepartureDate(whereFrom, whereTo, departureDate).stream()
                .map(m -> getLocalizedDTO().map(m))
                .filter(p->p.getTripType().toString().equals("AVIA"))
                .collect(Collectors.toList());
                //.orElseThrow(() -> new TripException("no trips:  " + whereFrom + "-" + whereTo));
    }

    public List<TripDto> getSearchedAviaTripWithArrivalDate(String whereFrom, String whereTo,
                                                            LocalDate departureDate, LocalDate arrivalDate){
        return tripRepository.findByFromWhereAndWhereToAndDepartureDateAndArrivalDate(whereFrom, whereTo,
                departureDate, arrivalDate).stream()
                .map(m -> getLocalizedDTO().map(m))
                .filter(p->p.getTripType().toString().equals("AVIA"))
                .collect(Collectors.toList());
    }

    public List<TripDto> getSearchedRailwayTrip(String whereFrom, String whereTo, LocalDate departureDate){

        return tripRepository.findByFromWhereAndWhereToAndDepartureDate(whereFrom, whereTo, departureDate).stream()
                .map(m -> getLocalizedDTO().map(m))
                .filter(p->p.getTripType().toString().equals("RAILWAY"))
                .collect(Collectors.toList());
    }

    public List<TripDto> getSearchedRailwayTripWithArrivalDate(String whereFrom, String whereTo,
                                                               LocalDate departureDate, LocalDate arrivalDate){
        return tripRepository.findByFromWhereAndWhereToAndDepartureDateAndArrivalDate(whereFrom, whereTo,
                departureDate, arrivalDate).stream()
                .map(m -> getLocalizedDTO().map(m))
                .filter(p->p.getTripType().toString().equals("RAILWAY"))
                .collect(Collectors.toList());
    }

    public List<TripDto> getSearchedBusTrip(String whereFrom, String whereTo, LocalDate departureDate){
        return tripRepository.findByFromWhereAndWhereToAndDepartureDate(whereFrom, whereTo, departureDate).stream()
                .map(m -> getLocalizedDTO().map(m))
                .filter(p->p.getTripType().toString().equals("BUS"))
                .collect(Collectors.toList());
    }

    public TripDto getTripDtoById(Long id) throws TripNotFoundException {
        return  tripRepository.findById(id).map(m -> getLocalizedDTO().map(m))
                .orElseThrow(() -> new TripNotFoundException("Trip id=" + id + " not found"));
    }

    public Trip getTripById(Long id) throws TripNotFoundException {
        return  tripRepository.findById(id)
                .orElseThrow(() -> new TripNotFoundException("Trip id=" + id + " not found"));
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW,
            rollbackFor = TripSaveException.class)
    public void createNewTrip(Trip trip) throws TripSaveException{
        try {
            tripRepository.save(trip);
        }catch (DataIntegrityViolationException e) {
            throw new TripSaveException("Can not trip with  id=" + trip.getId());
        }
    }

    public void editTrip(Trip trip){
        tripRepository.save(trip);
        log.info("editing trip");
    }

    public void deleteTripById(Long tripId) throws TripNotFoundException {
        Trip trip = tripRepository.findTripById(tripId).orElseThrow(()
                -> new TripNotFoundException("trip" + tripId + " not found"));
        tripRepository.delete(trip);
        log.info("deleting order");
    }


    public LocalizedDtoMapper<TripDto, Trip> getLocalizedDTO() {
        return trip -> TripDto.builder()
                .id(trip.getId())
                .tripType(trip.getTripType())
                .tripName(trip.getTripName())
                .fromWhere(trip.getFromWhere())
                .whereTo(trip.getWhereTo())
                .departureTime(trip.getDepartureTime())
                .arrivalTime(trip.getArrivalTime())
                .departureDate(trip.getDepartureDate())
                .arrivalDate(trip.getArrivalDate())
                .durationHours(trip.getDurationHours())
                .durationMinutes(trip.getDurationMinutes())
                .price(trip.getPrice())
                .classType(trip.getClassType())
                .placeAmount(trip.getPlaceAmount())
                .build();
    }
}
