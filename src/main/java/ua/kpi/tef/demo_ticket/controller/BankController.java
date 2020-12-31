package ua.kpi.tef.demo_ticket.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import ua.kpi.tef.demo_ticket.controller.exception.BankException;
import ua.kpi.tef.demo_ticket.controller.exception.CanNotPayException;
import ua.kpi.tef.demo_ticket.controller.exception.TicketBookingException;
import ua.kpi.tef.demo_ticket.dto.BankCardDto;
import ua.kpi.tef.demo_ticket.dto.TicketCheckDto;
import ua.kpi.tef.demo_ticket.entity.*;
import ua.kpi.tef.demo_ticket.service.BankCardService;
import ua.kpi.tef.demo_ticket.service.HotelBookingService;
import ua.kpi.tef.demo_ticket.service.TicketCheckService;
import ua.kpi.tef.demo_ticket.service.TicketService;

@Slf4j
@Controller
public class BankController {
    private final TicketService orderService;
    private final HotelBookingService hotelBookingService;
    private final BankCardService bankCardService;
    private final TicketCheckService orderCheckService;

    @Autowired
    public BankController(TicketService orderService, HotelBookingService hotelBookingService, BankCardService bankCardService, TicketCheckService orderCheckService) {
        this.orderService = orderService;
        this.hotelBookingService = hotelBookingService;
        this.bankCardService = bankCardService;
        this.orderCheckService = orderCheckService;
    }

    @GetMapping(value = "/pay_avia{id}")
    public String payParticularAviaShipmentView(@PathVariable Long id, Model model, @AuthenticationPrincipal User user,
                                                @ModelAttribute("checkDto") TicketCheckDto orderCheckDto,
                                                @ModelAttribute("bankCard") BankCardDto bankCardDto)
            throws TicketBookingException {
        AviaTicket aviaTicket = orderService.findAviaTicketById(id);
        //model.addAttribute("ticketId", id);
        model.addAttribute("ticket", aviaTicket);
        model.addAttribute("bankDTO", BankCardDto.builder().build());
        //model.addAttribute("ticketCheck", orderCheckService.createCheckDto(ticketId, bankCardDto, user.getId()));
        //model.addAttribute("bankCards", bankCardService.getAllUserBankCards(user));
        return "avia_payment";
    }


    @PostMapping(value = "/pay_avia{id}")
    public String payAviaShipment(@PathVariable Long id,  @ModelAttribute("bankDTO") BankCardDto bankCardDto,
                                  @ModelAttribute("checkDto") TicketCheckDto ticketCheckDto,
                                  @AuthenticationPrincipal User user)
            throws TicketBookingException, BankException, CanNotPayException {
        ticketCheckDto.setUserId(user.getId());
        ticketCheckDto.setOrderId(id);
        bankCardService.saveBankCardDTO(bankCardDto, user.getId());
        ticketCheckDto.setBankCard(bankCardDto.getId());
        orderCheckService.createAviaCheckDto(id, bankCardDto, user.getId());
        bankCardService.payForAviaOrder(ticketCheckDto);
        log.info("ticket paying");
        return "success_payment";
    }


    @GetMapping(value = "/pay{id}")
    public String payParticularShipmentView(@PathVariable Long id, Model model, @AuthenticationPrincipal User user,
                                            @ModelAttribute("checkDto") TicketCheckDto orderCheckDto,
                                            @ModelAttribute("bankCard") BankCardDto bankCardDto)
            throws TicketBookingException {

        RailwayTicket railwayTicket = orderService.findRailwayTicketById(id);
        model.addAttribute("ticket", railwayTicket);
        model.addAttribute("bankDTO", BankCardDto.builder().build());
        return "pay_for_ticket";
    }


    @PostMapping(value = "/pay{id}")
    public String payShipment(@PathVariable Long id,  @ModelAttribute("bankDTO") BankCardDto bankCardDto,
                              @ModelAttribute("checkDto") TicketCheckDto ticketCheckDto,
                              @AuthenticationPrincipal User user)
            throws TicketBookingException, BankException, CanNotPayException {
        ticketCheckDto.setUserId(user.getId());
        ticketCheckDto.setOrderId(id);
        bankCardService.saveBankCardDTO(bankCardDto, user.getId());
        ticketCheckDto.setBankCard(bankCardDto.getId());
        orderCheckService.createCheckDto(id, bankCardDto, user.getId());
        bankCardService.payForOrder(ticketCheckDto);
        log.info("ticket paying");
        return "success_payment";
    }

    @GetMapping(value = "/pay_bus{id}")
    public String payParticularBusShipmentView(Model model, @PathVariable Long id, @AuthenticationPrincipal User user,
                                                @ModelAttribute("newTicket") BusTicket newTicket,
                                                @ModelAttribute("checkDto") TicketCheckDto orderCheckDto,
                                                @ModelAttribute("bankCard") BankCardDto bankCardDto)
            throws TicketBookingException {

        BusTicket busTicket = orderService.findBusTicketById(id);
        model.addAttribute("ticket", busTicket);
        model.addAttribute("bankDTO", BankCardDto.builder().build());
        return "bus_payment";
    }

    @PostMapping(value = "/pay_bus{id}")
    public String payBusShipment(@PathVariable Long id, @ModelAttribute("newTicket") BusTicket newTicket,
                                 @ModelAttribute("bankDTO") BankCardDto bankCardDto,
                                 @ModelAttribute("checkDto") TicketCheckDto ticketCheckDto,
                                 @AuthenticationPrincipal User user)
            throws TicketBookingException, BankException, CanNotPayException {
        ticketCheckDto.setUserId(user.getId());
        ticketCheckDto.setOrderId(id);
        bankCardService.saveBankCardDTO(bankCardDto, user.getId());
        ticketCheckDto.setBankCard(bankCardDto.getId());
        orderCheckService.createBusCheckDto(id, bankCardDto, user.getId());
        bankCardService.payForBusOrder(ticketCheckDto);
        log.info("ticket paying");
        return "success_payment";
    }

    @GetMapping(value = "/pay_hotel{id}")
    public String payParticularHotelShipmentView(@PathVariable Long id, Model model, @AuthenticationPrincipal User user,
                                               @ModelAttribute("checkDto") TicketCheckDto orderCheckDto,
                                               @ModelAttribute("bankCard") BankCardDto bankCardDto)
            throws TicketBookingException {
        HotelBooking hotelBooking = hotelBookingService.findHotelBookingById(id);
        model.addAttribute("hotel", hotelBooking);
        model.addAttribute("bankDTO", BankCardDto.builder().build());
        return "hotel_payment";
    }

    @PostMapping(value = "/pay_hotel{id}")
    public String payHotelShipment(@PathVariable Long id,  @ModelAttribute("bankDTO") BankCardDto bankCardDto,
                                   @ModelAttribute("checkDto") TicketCheckDto ticketCheckDto,
                                   @AuthenticationPrincipal User user)
            throws TicketBookingException, BankException, CanNotPayException {
        ticketCheckDto.setUserId(user.getId());
        ticketCheckDto.setOrderId(id);
        bankCardService.saveBankCardDTO(bankCardDto, user.getId());
        ticketCheckDto.setBankCard(bankCardDto.getId());
        orderCheckService.createHotelCheckDto(id, bankCardDto, user.getId());
        bankCardService.payForHotelOrder(ticketCheckDto);
        log.info("hotel paying");
        return "success_payment";
    }
}