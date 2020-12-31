package ua.kpi.tef.demo_ticket.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ua.kpi.tef.demo_ticket.controller.exception.TicketBookingException;
import ua.kpi.tef.demo_ticket.controller.exception.TicketCreateException;
import ua.kpi.tef.demo_ticket.controller.exception.TripNotFoundException;
import ua.kpi.tef.demo_ticket.controller.exception.UserNotFoundException;
import ua.kpi.tef.demo_ticket.dto.TicketDto;
import ua.kpi.tef.demo_ticket.dto.TripDto;
import ua.kpi.tef.demo_ticket.entity.*;
import ua.kpi.tef.demo_ticket.service.TicketService;
import ua.kpi.tef.demo_ticket.service.TripService;

import javax.persistence.Access;
import javax.validation.Valid;

@Slf4j
@Controller
public class BookingController {
    private final TripService tripService;
    private final TicketService ticketService;

    @Autowired
    public BookingController(TripService tripService, TicketService ticketService) {
        this.tripService = tripService;
        this.ticketService = ticketService;
    }

    @PostMapping("/find_avia_trip{id}")
    public String createAviaTicket(Model model, @Valid @ModelAttribute("newTicket") AviaTicket newTicket,
                               @ModelAttribute("trip") TripDto trip,
                               BindingResult bindingResult,  @AuthenticationPrincipal User user)
            throws TicketCreateException, TicketBookingException, TripNotFoundException, UserNotFoundException {
        if (bindingResult.hasErrors()){
            return "avia_booking";
        }
        if(user != null){
            ticketService.bookAviaTicketAuthorized(newTicket, trip, user);
        } else {
            ticketService.bookAviaTicketUnauthorized(newTicket, trip);
        }
        Long ticketId = newTicket.getId();
        //model.addAttribute("ticketId", newTicket.getId());
        //ticketId = newTicket.getId();
        return "redirect:/pay_avia{id}";
    }


    @PostMapping("/find_one_railway_trip{id}")
    public String createTicket(Model model, @Valid @ModelAttribute("newTicket") RailwayTicket newTicket,
                               @ModelAttribute("trip") TripDto trip,
                               BindingResult bindingResult,  @AuthenticationPrincipal User user)
            throws TicketBookingException, TripNotFoundException, UserNotFoundException {

        if (bindingResult.hasErrors()){
            return "railway_ticket_booking";
        }
        if(user != null){
            ticketService.bookRailwayTicketAuthorized(newTicket, trip, user);
        } else {
            ticketService.bookRailwayTicketUnauthorized(newTicket, trip);
        }
        return "redirect:/pay{id}";
    }

    @PostMapping("/find_bus_trip{id}")
    public String createBusTicket(Model model, @Valid @ModelAttribute("newTicket") BusTicket newTicket,
                               @ModelAttribute("trip") TripDto trip,
                               BindingResult bindingResult,  @AuthenticationPrincipal User user)
            throws TicketBookingException, TripNotFoundException, UserNotFoundException {

        if (bindingResult.hasErrors()){
            return "bus_booking";
        }
        if(user != null){
            ticketService.bookBusTicketAuthorized(newTicket, trip, user);
        } else {
            ticketService.bookBusTicketUnauthorized(newTicket, trip);
        }
        //model.addAttribute("bus_id", newTicket.getId());
        return "redirect:/pay_bus{id}";
    }

    @ExceptionHandler(TripNotFoundException.class)
    public String handleTripNotFoundException(Model model) {
        log.error("TripNotFoundException Exception");
        model.addAttribute("error", true);
        return "redirect:/";
    }

    @ExceptionHandler(TicketCreateException.class)
    public String handleTicketCreateException(Model model) {
        log.error("TicketCreateException Exception");
        model.addAttribute("error", true);
        return "redirect:/";
    }
}
