package ua.kpi.tef.demo_ticket.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ua.kpi.tef.demo_ticket.controller.exception.TicketBookingException;
import ua.kpi.tef.demo_ticket.controller.exception.TicketCheckException;
import ua.kpi.tef.demo_ticket.dto.*;
import ua.kpi.tef.demo_ticket.entity.TicketCheck;
import ua.kpi.tef.demo_ticket.entity.User;
import ua.kpi.tef.demo_ticket.entity.enums.TicketStatus;
import ua.kpi.tef.demo_ticket.mappers.LocalizedDtoMapper;
import ua.kpi.tef.demo_ticket.repository.TicketCheckRepository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class TicketCheckService {
    private final TicketCheckRepository ticketCheckRepository;
    private final TicketService ticketService;
    private final HotelBookingService hotelBookingService;
    private final UserService userService;

    @Autowired
    public TicketCheckService(TicketCheckRepository ticketCheckRepository, TicketService ticketService, HotelBookingService hotelBookingService, UserService userService) {
        this.ticketCheckRepository = ticketCheckRepository;
        this.ticketService = ticketService;
        this.hotelBookingService = hotelBookingService;
        this.userService = userService;
    }

    public List<TicketCheckDto> showAllChecks() {
        List<TicketCheck> ticketChecks = new ArrayList<>();
        ticketCheckRepository.findAll()
                .forEach(ticketChecks::add);
        return ticketChecks.stream()
                .map(m -> getLocalizedDTO().map(m))
                .collect(Collectors.toList());
    }

    public TicketCheckDto showCheckById(Long checkId) throws TicketCheckException {
        return ticketCheckRepository
                .findById(checkId)
                .map(m -> getLocalizedDTO().map(m))
                .orElseThrow(()->new TicketCheckException("no check with id=" + checkId));
    }

    public List<TicketCheckDto> showAviaChecksByUser(Long userId) {
        return ticketCheckRepository
                .findAllByUser_Id(userId).stream()
                .map(m -> getLocalizedAviaDTO().map(m))
                .collect(Collectors.toList());
    }

    public List<TicketCheckDto> showRailwayChecksByUser(Long userId) {
        return ticketCheckRepository
                .findAllByUser_Id(userId).stream()
                .map(m -> getLocalizedDTO().map(m))
                .collect(Collectors.toList());
    }

    public TicketCheckDto createCheckDto(Long ticketDtoId, BankCardDto bankCardDto, Long userId)
            throws TicketBookingException {
        TicketDto ticketDto = ticketService.getTicketDtoById(ticketDtoId);
        User userDto = userService.findUserById(userId);
        return TicketCheckDto.builder()
                .orderId(ticketDtoId)
                .bankCard(bankCardDto.getId())
                .price(BigDecimal.valueOf(ticketDto.getTicketPrice()))
                .userId(userDto.getId())
                .ticketStatus(TicketStatus.NOT_PAID).build();
    }

    public TicketCheckDto createAviaCheckDto(Long ticketDtoId, BankCardDto bankCardDto, Long userId)
            throws TicketBookingException {
        AviaTicketDto ticketDto = ticketService.getAviaTicketDtoById(ticketDtoId);
        User userDto = userService.findUserById(userId);
        return TicketCheckDto.builder()
                .orderId(ticketDtoId)
                .bankCard(bankCardDto.getId())
                .price(BigDecimal.valueOf(ticketDto.getTicketPrice()))
                .userId(userDto.getId())
                .ticketStatus(TicketStatus.NOT_PAID).build();
    }

    public TicketCheckDto createBusCheckDto(Long ticketDtoId, BankCardDto bankCardDto, Long userId)
            throws TicketBookingException {
        BusTicketDto ticketDto = ticketService.getBusTicketDtoById(ticketDtoId);
        User userDto = userService.findUserById(userId);
        return TicketCheckDto.builder()
                .orderId(ticketDtoId)
                .bankCard(bankCardDto.getId())
                .price(BigDecimal.valueOf(ticketDto.getTicketPrice()))
                .userId(userDto.getId())
                .ticketStatus(TicketStatus.NOT_PAID).build();
    }

    public TicketCheckDto createHotelCheckDto(Long ticketDtoId, BankCardDto bankCardDto, Long userId)
            throws TicketBookingException {
        HotelBookingDto hotelBookingDto = hotelBookingService.getHotelBookingDtoById(ticketDtoId);
        User userDto = userService.findUserById(userId);
        return TicketCheckDto.builder()
                .orderId(ticketDtoId)
                .bankCard(bankCardDto.getId())
                .price(BigDecimal.valueOf(hotelBookingDto.getHotelPrice()))
                .userId(userDto.getId())
                .ticketStatus(TicketStatus.NOT_PAID).build();
    }


    public LocalizedDtoMapper<TicketCheckDto, TicketCheck> getLocalizedAviaDTO() {
        return ticketCheck -> TicketCheckDto.builder()
                .id(ticketCheck.getId())
                .orderId(ticketCheck.getAviaTicket().getId())
                .price(ticketCheck.getPrice())
                .ticketStatus(ticketCheck.getTicketStatus())
                .userId(ticketCheck.getUser().getId())
                .bankCard(ticketCheck.getBankCard().getId())
                .creationDate(ticketCheck.getCreationDate())
                .build();
    }

    public LocalizedDtoMapper<TicketCheckDto, TicketCheck> getLocalizedDTO() {
        return ticketCheck -> TicketCheckDto.builder()
                .id(ticketCheck.getId())
                .orderId(ticketCheck.getTicket().getId())
                .price(ticketCheck.getPrice())
                .ticketStatus(ticketCheck.getTicketStatus())
                .userId(ticketCheck.getUser().getId())
                .bankCard(ticketCheck.getBankCard().getId())
                .creationDate(ticketCheck.getCreationDate())
                .build();
    }

}
