package ua.kpi.tef.demo_ticket.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ua.kpi.tef.demo_ticket.controller.exception.TicketBookingException;
import ua.kpi.tef.demo_ticket.controller.exception.TripNotFoundException;
import ua.kpi.tef.demo_ticket.controller.exception.UserNotFoundException;
import ua.kpi.tef.demo_ticket.dto.AviaTicketDto;
import ua.kpi.tef.demo_ticket.dto.BusTicketDto;
import ua.kpi.tef.demo_ticket.dto.TicketDto;
import ua.kpi.tef.demo_ticket.dto.TripDto;
import ua.kpi.tef.demo_ticket.entity.*;
import ua.kpi.tef.demo_ticket.entity.enums.PassengerType;
import ua.kpi.tef.demo_ticket.entity.enums.TicketStatus;
import ua.kpi.tef.demo_ticket.mappers.LocalizedDtoMapper;
import ua.kpi.tef.demo_ticket.repository.*;

import java.util.List;
import java.util.stream.Collectors;


@Slf4j
@Service
public class TicketService {
    private final TicketRepository ticketRepository;
    private final AviaTicketRepository aviaTicketRepository;
    private final BusTicketRepository busTicketRepository;
    private final UserRepository userRepository;
    private final TripRepository tripRepository;

    public TicketService(TicketRepository ticketRepository, AviaTicketRepository aviaTicketRepository, BusTicketRepository busTicketRepository, UserRepository userRepository, TripRepository tripRepository) {
        this.ticketRepository = ticketRepository;
        this.aviaTicketRepository = aviaTicketRepository;
        this.busTicketRepository = busTicketRepository;
        this.userRepository = userRepository;
        this.tripRepository = tripRepository;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW,
            rollbackFor = TicketBookingException.class)
    public void bookAviaTicketUnauthorized(AviaTicket ticket, TripDto trip) throws TicketBookingException,
            TripNotFoundException{
        Trip tripToSave = tripRepository.findById(trip.getId())
                .orElseThrow(()->new TripNotFoundException("no trip with id=" + trip.getId()));
         ticket.setTicketPrice(tripToSave.getPrice());
         ticket.saveTicket(tripToSave);
         ticket.setTicketStatus(TicketStatus.RESERVED);
         aviaTicketRepository.save(ticket);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW,
            rollbackFor = TicketBookingException.class)
    public void bookAviaTicketAuthorized(AviaTicket ticket, TripDto trip, User user) throws TicketBookingException,
            TripNotFoundException, UserNotFoundException {
        User userToSave = userRepository.findById(user.getId())
                .orElseThrow(()->new UserNotFoundException("no user with id=" + user.getId()));
        Trip tripToSave = tripRepository.findById(trip.getId())
                .orElseThrow(()->new TripNotFoundException("no trip with id=" + trip.getId()));
        ticket.saveTicket(tripToSave);
        if(userToSave != null) {
            ticket.saveTicket(userToSave);
        }
        ticket.setTicketPrice(tripToSave.getPrice());
        ticket.setTicketStatus(TicketStatus.RESERVED);
        aviaTicketRepository.save(ticket);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW,
            rollbackFor = TicketBookingException.class)
    public void bookRailwayTicketUnauthorized(RailwayTicket ticket, TripDto trip) throws TicketBookingException, TripNotFoundException{
        Trip tripToSave = tripRepository.findById(trip.getId())
                .orElseThrow(()->new TripNotFoundException("no trip with id=" + trip.getId()));
        //Order orderToSave = orderMapper.orderDtoToOrder(orderDTO);
        //try {
            //ticket.setTrip(tripService.getTripById(ticket.getTrip().getId()));
            //ticket.setCarriage(ticket.getCarriage());
            //ticket.setPlace(ticket.getPlace());
            //ticket.getEmail();
            //ticket.setPhoneNumber("6890-090990");
            //ticket.getFirstName();
            //ticket.getLastName();
            if(ticket.getBirthDate() != null) {
                ticket.setPassengerType(PassengerType.CHILD);
            } else  if(ticket.getStudentCardNumber() != null) {
                ticket.setPassengerType(PassengerType.STUDENT);
            }else {
                ticket.setPassengerType(PassengerType.ADULT);
            }
            ticket.setTicketPrice(tripToSave.getPrice());
            ticket.saveTicket(tripToSave);
            //ticket.setTicketPrice(tripService.getTripById(ticket.getTrip().getId()).getPrice());
            ticket.setTicketStatus(TicketStatus.RESERVED);
            ticketRepository.save(ticket);
        /*} catch (TripNotFoundException e) {
            throw new TicketBookingException("Can not book ticket with id = " + ticket.getId());
        }*/
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW,
            rollbackFor = TicketBookingException.class)
    public void bookRailwayTicketAuthorized(RailwayTicket ticket, TripDto trip, User user) throws TripNotFoundException,
            UserNotFoundException {
        User userToSave = userRepository.findById(user.getId())
                .orElseThrow(()->new UserNotFoundException("no user with id=" + user.getId()));
        Trip tripToSave = tripRepository.findById(trip.getId())
                .orElseThrow(()->new TripNotFoundException("no trip with id=" + trip.getId()));
        if(ticket.getBirthDate() != null) {
            ticket.setPassengerType(PassengerType.CHILD);
        } else  if(ticket.getStudentCardNumber() != null) {
            ticket.setPassengerType(PassengerType.STUDENT);
        }else {
            ticket.setPassengerType(PassengerType.ADULT);
        }
        ticket.saveTicket(tripToSave);
        if(userToSave != null) {
            ticket.saveTicket(userToSave);
        }
        ticket.setTicketPrice(tripToSave.getPrice());
        ticket.setTicketStatus(TicketStatus.RESERVED);
        ticketRepository.save(ticket);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW,
            rollbackFor = TicketBookingException.class)
    public void bookBusTicketUnauthorized(BusTicket ticket, TripDto trip) throws TicketBookingException, TripNotFoundException{
        Trip tripToSave = tripRepository.findById(trip.getId())
                .orElseThrow(()->new TripNotFoundException("no trip with id=" + trip.getId()));
        ticket.setTicketPrice(tripToSave.getPrice());
        ticket.saveTicket(tripToSave);
        ticket.setTicketStatus(TicketStatus.RESERVED);
        busTicketRepository.save(ticket);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW,
            rollbackFor = TicketBookingException.class)
    public void bookBusTicketAuthorized(BusTicket ticket, TripDto trip, User user) throws TicketBookingException,
            TripNotFoundException, UserNotFoundException {
        User userToSave = userRepository.findById(user.getId())
                .orElseThrow(()->new UserNotFoundException("no user with id=" + user.getId()));
        Trip tripToSave = tripRepository.findById(trip.getId())
                .orElseThrow(()->new TripNotFoundException("no trip with id=" + trip.getId()));
        ticket.saveTicket(tripToSave);
        if(userToSave != null) {
            ticket.saveTicket(userToSave);
        }
        ticket.setTicketPrice(tripToSave.getPrice());
        ticket.setTicketStatus(TicketStatus.RESERVED);
        busTicketRepository.save(ticket);
    }

    public List<AviaTicketDto> findAllUserAviaTickets(Long userId) {
        return aviaTicketRepository.findAviaTicketByUserId(userId).stream()
                .map(m -> getLocalizedDTOAvia().map(m))
                //.filter(o -> !o.getStatus().equals(Status.ARCHIVED))
                .collect(Collectors.toList());
    }

    public List<TicketDto> findAllUserTickets(Long userId) {
        return ticketRepository.findTicketByUserId(userId).stream()
                .map(m -> getLocalizedDTO().map(m))
                //.filter(o -> !o.getStatus().equals(Status.ARCHIVED))
                .collect(Collectors.toList());
    }

    public List<BusTicketDto> findAllUserBusTickets(Long userId) {
        return busTicketRepository.findBusTicketByUserId(userId).stream()
                .map(m -> getLocalizedDTOBus().map(m))
                //.filter(o -> !o.getStatus().equals(Status.ARCHIVED))
                .collect(Collectors.toList());
    }


    public List<RailwayTicket> findAllTripTickets(Long tripId) {
        return ticketRepository.findTicketByTripId(tripId).stream()
                .collect(Collectors.toList());
    }

    public RailwayTicket getRailwayTripById(Long id) throws TripNotFoundException {
        return  ticketRepository.findById(id)
                .orElseThrow(() -> new TripNotFoundException("Trip id=" + id + " not found"));
    }

    public RailwayTicket findRailwayTicketById(Long orderId) throws TicketBookingException {
        return ticketRepository.findById(orderId)
                .orElseThrow(() -> new TicketBookingException("order " + orderId + " not found"));
    }

    public AviaTicket findAviaTicketById(Long orderId) throws TicketBookingException {
        return aviaTicketRepository.findById(orderId)
                .orElseThrow(() -> new TicketBookingException("order " + orderId + " not found"));
    }

    public BusTicket findBusTicketById(Long orderId) throws TicketBookingException {
        return busTicketRepository.findById(orderId)
                .orElseThrow(() -> new TicketBookingException("order " + orderId + " not found"));
    }


    public TicketDto getTicketDtoById(Long id) throws TicketBookingException {
        return ticketRepository.findById(id)
                .map(m -> getLocalizedDTO().map(m))
                .orElseThrow(() -> new TicketBookingException("order id=" + id + " not found"));
    }

    public AviaTicketDto getAviaTicketDtoById(Long id) throws TicketBookingException {
        return aviaTicketRepository.findById(id)
                .map(m -> getLocalizedDTOAvia().map(m))
                .orElseThrow(() -> new TicketBookingException("order id=" + id + " not found"));
    }

    public BusTicketDto getBusTicketDtoById(Long id) throws TicketBookingException {
        return busTicketRepository.findById(id)
                .map(m -> getLocalizedDTOBus().map(m))
                .orElseThrow(() -> new TicketBookingException("order id=" + id + " not found"));
    }


    public LocalizedDtoMapper<AviaTicketDto, AviaTicket> getLocalizedDTOAvia() {
        return aviaTicket -> AviaTicketDto.builder()
                .id(aviaTicket.getId())
                .tripDto(aviaTicket.getTrip())
                .place(aviaTicket.getPlace())
                .email(aviaTicket.getEmail())
                .phoneNumber(aviaTicket.getPhoneNumber())
                .firstName(aviaTicket.getFirstName())
                .lastName(aviaTicket.getLastName())
                .birthDate(aviaTicket.getBirthDate())
                .sex(aviaTicket.getSex())
                .citizenship(aviaTicket.getCitizenship())
                .documentNumber(aviaTicket.getDocumentNumber())
                .ticketPrice(aviaTicket.getTicketPrice())
                .check(aviaTicket.getCheck())
                .ticketStatus(aviaTicket.getTicketStatus())
                .build();
    }

    public LocalizedDtoMapper<TicketDto, RailwayTicket> getLocalizedDTO() {
        return ticket -> TicketDto.builder()
                .id(ticket.getId())
                .tripDto(ticket.getTrip())
                .carriage(ticket.getCarriage())
                .place(ticket.getPlace())
                .email(ticket.getEmail())
                .phoneNumber(ticket.getPhoneNumber())
                .passengerType(ticket.getPassengerType())
                .firstName(ticket.getFirstName())
                .lastName(ticket.getLastName())
                .birthDate(ticket.getBirthDate())
                .studentCardNumber(ticket.getStudentCardNumber())
                .ticketPrice(ticket.getTicketPrice())
                .check(ticket.getCheck())
                .ticketStatus(ticket.getTicketStatus())
                .build();
    }

    public LocalizedDtoMapper<BusTicketDto, BusTicket> getLocalizedDTOBus() {
        return busTicket -> BusTicketDto.builder()
                .id(busTicket.getId())
                .tripDto(busTicket.getTrip())
                .place(busTicket.getPlace())
                .email(busTicket.getEmail())
                .phoneNumber(busTicket.getPhoneNumber())
                .firstName(busTicket.getFirstName())
                .lastName(busTicket.getLastName())
                .birthDate(busTicket.getBirthDate())
                .documentType(busTicket.getDocumentType())
                .ticketPrice(busTicket.getTicketPrice())
                .check(busTicket.getCheck())
                .ticketStatus(busTicket.getTicketStatus())
                .build();
    }
}
