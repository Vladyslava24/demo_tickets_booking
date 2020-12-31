package ua.kpi.tef.demo_ticket.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ua.kpi.tef.demo_ticket.controller.exception.TicketBookingException;
import ua.kpi.tef.demo_ticket.controller.exception.TripNotFoundException;
import ua.kpi.tef.demo_ticket.dto.*;
import ua.kpi.tef.demo_ticket.entity.AviaTicket;
import ua.kpi.tef.demo_ticket.entity.Hotel;
import ua.kpi.tef.demo_ticket.entity.Trip;
import ua.kpi.tef.demo_ticket.entity.enums.RoleType;
import ua.kpi.tef.demo_ticket.entity.User;
import ua.kpi.tef.demo_ticket.service.*;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Controller
public class PageController {
    private final UserService userService;
    private final TicketService ticketService;
    private final HotelBookingService hotelBookingService;
    private final TicketCheckService ticketCheckService;
    private final TripService tripService;

    @Autowired
    public PageController(UserService userService, TicketService ticketService, HotelBookingService hotelBookingService, TicketCheckService ticketCheckService, TripService tripService) {
        this.userService = userService;
        this.ticketService = ticketService;
        this.hotelBookingService = hotelBookingService;
        this.ticketCheckService = ticketCheckService;
        this.tripService = tripService;
    }

    /*@RequestMapping("/")
    public String getMainPage(Model model){
        //model.addAttribute("editions", editionService.getAllEditions());
        return "index.html";
    }*/

    @RequestMapping("/cab")
    public String getUserPage(Model model, @AuthenticationPrincipal User user) throws TripNotFoundException, TicketBookingException {
        List<Trip> aviaTrips = ticketService.findAllUserAviaTickets(user.getId()).stream()
                .map(AviaTicketDto::getTripDto)
                .collect(Collectors.toList());
        List<Trip> railwayTrips = ticketService.findAllUserTickets(user.getId()).stream()
                .map(TicketDto::getTripDto)
                .collect(Collectors.toList());
        List<Trip> busTrips = ticketService.findAllUserBusTickets(user.getId()).stream()
                .map(BusTicketDto::getTripDto)
                .collect(Collectors.toList());
        List<Hotel> hotels = hotelBookingService.findAllUserHotelBookings(user.getId()).stream()
                .map(HotelBookingDto::getHotelDto)
                .collect(Collectors.toList());

        model.addAttribute("user", user);
        model.addAttribute("avia_trips", aviaTrips);
        model.addAttribute("railway_trips", railwayTrips);
        model.addAttribute("bus_trips", busTrips);
        model.addAttribute("hotels", hotels);
        /*model.addAttribute("email", user.getEmail());
        model.addAttribute("lastName", user.getLastName());
        model.addAttribute("id", user.getId());
        model.addAttribute("username", user.getUsername());*/
        //model.addAttribute("trips", trips);

        model.addAttribute("avia_ticket", ticketService.findAllUserAviaTickets(user.getId()));
        model.addAttribute("bus_ticket", ticketService.findAllUserBusTickets(user.getId()));
        model.addAttribute("railway_ticket", ticketService.findAllUserTickets(user.getId()));
        model.addAttribute("hotel", hotelBookingService.findAllUserHotelBookings(user.getId()));
        //model.addAttribute("avia_checks", ticketCheckService.showAviaChecksByUser(user.getId()));
//        model.addAttribute("ticket_checks", ticketCheckService.showRailwayChecksByUser(user.getId()));
        return "cab";
    }

    @PostMapping("/cab_update")
    public String updateUser(Model model, @RequestParam("email") String  email,
                             @RequestParam("lastName") String lastName,
                             @RequestParam("firstName") String firstName,
                             @RequestParam("username") String username,
                             @AuthenticationPrincipal User user){
        userService.updateUser(user, email, lastName, firstName, username);
        return "redirect:/cab";
    }

    public TripDto getById(User user) throws TicketBookingException, TripNotFoundException {
        return tripService.getTripDtoById(ticketService.getTicketDtoById(user.getId()).getTripDto().getId());
    }

    @RequestMapping("/login")
    public String getLogin(@RequestParam(value = "error", required = false) String error,
                           @RequestParam(value = "logout", required = false) String logout,
                           Model model) {
        model.addAttribute("error", error != null);
        model.addAttribute("logout", logout != null);
        return "login.html";
    }


    @GetMapping("/users")
    public String findAll(Model model){
        List<User> users = userService.findAll();
        model.addAttribute("users", users);
        return "user-list";
    }

    @GetMapping("/user-create")
    public String createUserForm(User user){
        return "user-create";
    }

    /*@PostMapping("/user-create")
    public String createUser(User user){
        userService.saveUser(user);
        return "redirect:/users";
    }*/

    @GetMapping("user-delete/{id}")
    public String deleteUser(@PathVariable("id") Long id){
        userService.deleteById(id);
        return "redirect:/users";
    }

    @GetMapping("/user-update/{id}")
    public String updateUserForm(@PathVariable("id") Long id, Model model){
        User user = userService.findById(id);
        model.addAttribute("user", user);
        return "user-update";
    }

   /* @PostMapping("/user-update")
    public String updateUser(User user){
        userService.saveUser(user);
        return "redirect:/users";
    }*/


    @RequestMapping("/registration")
    public String regForm(){
        return "registration.html";
    }

    public RoleType role(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        return user.getRole();
    }

    public String userName(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        return user.getUsername();
    }

    @ExceptionHandler(TicketBookingException.class)
    public String handleTicketNotFoundException(Model model) {
        log.error("TicketBookingException Exception");
        model.addAttribute("error", true);
        return "redirect:/";
    }

    @ExceptionHandler(TripNotFoundException.class)
    public String handleTripNotFoundException(Model model) {
        log.error("TripNotFoundException Exception");
        model.addAttribute("error", true);
        return "redirect:/";
    }
}