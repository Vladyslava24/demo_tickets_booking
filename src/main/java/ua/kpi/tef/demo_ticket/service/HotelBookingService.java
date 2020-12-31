package ua.kpi.tef.demo_ticket.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ua.kpi.tef.demo_ticket.controller.exception.TicketBookingException;
import ua.kpi.tef.demo_ticket.controller.exception.TripNotFoundException;
import ua.kpi.tef.demo_ticket.controller.exception.UserNotFoundException;
import ua.kpi.tef.demo_ticket.dto.*;
import ua.kpi.tef.demo_ticket.entity.*;
import ua.kpi.tef.demo_ticket.entity.enums.TicketStatus;
import ua.kpi.tef.demo_ticket.mappers.LocalizedDtoMapper;
import ua.kpi.tef.demo_ticket.repository.*;

import java.util.List;
import java.util.stream.Collectors;


@Slf4j
@Service
public class HotelBookingService {
    private final HotelBookingRepository hotelBookingRepository;
    private final UserRepository userRepository;
    private final HotelRepository hotelRepository;

    public HotelBookingService(HotelBookingRepository hotelBookingRepository, UserRepository userRepository, HotelRepository hotelRepository) {
        this.hotelBookingRepository = hotelBookingRepository;
        this.userRepository = userRepository;
        this.hotelRepository = hotelRepository;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW,
            rollbackFor = TicketBookingException.class)
    public void bookHotelBookingUnauthorized(HotelBooking booking, HotelDto hotel) throws TicketBookingException, TripNotFoundException{
        Hotel hotelToSave = hotelRepository.findById(hotel.getId())
                .orElseThrow(()->new TripNotFoundException("no trip with id=" + hotel.getId()));
        booking.setHotelPrice(hotelToSave.getPrice());
        booking.saveHotel(hotelToSave);
        booking.setTicketStatus(TicketStatus.RESERVED);
        hotelBookingRepository.save(booking);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW,
            rollbackFor = TicketBookingException.class)
    public void bookHotelBookingAuthorized(HotelBooking booking, HotelDto hotel, User user) throws TicketBookingException,
            TripNotFoundException, UserNotFoundException {
        User userToSave = userRepository.findById(user.getId())
                .orElseThrow(()->new UserNotFoundException("no user with id=" + user.getId()));
        Hotel hotelToSave = hotelRepository.findById(hotel.getId())
                .orElseThrow(()->new TripNotFoundException("no trip with id=" + hotel.getId()));
        booking.saveHotel(hotelToSave);
        if(userToSave != null) {
            booking.saveHotel(userToSave);
        }
        booking.setHotelPrice(hotelToSave.getPrice());
        booking.setTicketStatus(TicketStatus.RESERVED);
        hotelBookingRepository.save(booking);
    }

    public List<HotelBookingDto> findAllUserHotelBookings(Long userId) {
        return hotelBookingRepository.findHotelBookingByUserId(userId).stream()
                .map(m -> getLocalizedDTO().map(m))
                .collect(Collectors.toList());
    }


    public List<HotelBooking> findAllHotelBookings(Long tripId) {
        return hotelBookingRepository.findHotelBookingByHotelId(tripId).stream()
                .collect(Collectors.toList());
    }

    public HotelBooking findHotelBookingById(Long orderId) throws TicketBookingException {
        return hotelBookingRepository.findById(orderId)
                .orElseThrow(() -> new TicketBookingException("order " + orderId + " not found"));
    }


    public HotelBookingDto getHotelBookingDtoById(Long id) throws TicketBookingException {
        return hotelBookingRepository.findById(id)
                .map(m -> getLocalizedDTO().map(m))
                .orElseThrow(() -> new TicketBookingException("order id=" + id + " not found"));
    }

    public LocalizedDtoMapper<HotelBookingDto, HotelBooking> getLocalizedDTO() {
        return hotelBooking -> HotelBookingDto.builder()
                .id(hotelBooking.getId())
                .hotelDto(hotelBooking.getHotel())
                .apartmentNumber(hotelBooking.getApartmentNumber())
                .email(hotelBooking.getEmail())
                .phoneNumber(hotelBooking.getPhoneNumber())
                .firstName(hotelBooking.getFirstName())
                .lastName(hotelBooking.getLastName())
                .apartmentType(hotelBooking.getApartmentType())
                .citizenship(hotelBooking.getCitizenship())
                .hotelPrice(hotelBooking.getHotelPrice())
                .check(hotelBooking.getCheck())
                .ticketStatus(hotelBooking.getTicketStatus())
                .build();
    }
}
