package ua.kpi.tef.demo_ticket.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import ua.kpi.tef.demo_ticket.controller.exception.TicketBookingException;
import ua.kpi.tef.demo_ticket.controller.exception.TicketCreateException;
import ua.kpi.tef.demo_ticket.controller.exception.TripNotFoundException;
import ua.kpi.tef.demo_ticket.controller.exception.UserNotFoundException;
import ua.kpi.tef.demo_ticket.dto.HotelDto;
import ua.kpi.tef.demo_ticket.entity.*;
import ua.kpi.tef.demo_ticket.service.HotelBookingService;

import javax.validation.Valid;

@Slf4j
@Controller
public class HotelBookingController {
    private final HotelBookingService hotelBookingService;

    public HotelBookingController(HotelBookingService hotelBookingService) {
        this.hotelBookingService = hotelBookingService;
    }

    @PostMapping("/find_hotel{id}")
    public String createHotelBooking(Model model, @Valid @ModelAttribute("newBooking") HotelBooking newBooking,
                                   @ModelAttribute("hotel") HotelDto hotel,
                                   BindingResult bindingResult, @AuthenticationPrincipal User user)
            throws TicketCreateException, TicketBookingException, TripNotFoundException, UserNotFoundException {
        if (bindingResult.hasErrors()){
            return "hotel_booking";
        }
        if(user != null){
            hotelBookingService.bookHotelBookingAuthorized(newBooking, hotel, user);
        } else {
            hotelBookingService.bookHotelBookingUnauthorized(newBooking, hotel);
        }
        //ticketId = newTicket.getId();
        return "redirect:/pay_hotel{id}";
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
