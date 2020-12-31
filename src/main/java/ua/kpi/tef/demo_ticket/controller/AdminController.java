package ua.kpi.tef.demo_ticket.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ua.kpi.tef.demo_ticket.controller.exception.*;
import ua.kpi.tef.demo_ticket.dto.HotelDto;
import ua.kpi.tef.demo_ticket.dto.TripDto;
import ua.kpi.tef.demo_ticket.entity.Hotel;
import ua.kpi.tef.demo_ticket.entity.Trip;
import ua.kpi.tef.demo_ticket.service.HotelService;
import ua.kpi.tef.demo_ticket.service.TripService;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.List;

@Slf4j
@PreAuthorize("hasAuthority('ROLE_ADMIN')")
@RequestMapping("/admin/")
@Controller
public class AdminController {
    private final TripService tripService;
    private final HotelService hotelService;

    @Autowired
    public AdminController(TripService tripService, HotelService hotelService) {
        this.tripService = tripService;
        this.hotelService = hotelService;
    }


    @GetMapping("avia")
    public String getAdminAviaPage(Model model){
        List<TripDto> trips = tripService.getAllAviaTripDto();
        model.addAttribute("trips", trips);
        model.addAttribute("departure", LocalDate.now());
        model.addAttribute("arrival", LocalDate.now());
        model.addAttribute("newTrip", Trip.builder().build());
        return "admin/admin_avia";
    }

    @GetMapping("railway")
    public String getAdminRailwayPage(Model model){
        List<TripDto> trips = tripService.getAllRailwayTripDto();
        model.addAttribute("trips", trips);
        model.addAttribute("departure", LocalDate.now());
        model.addAttribute("arrival", LocalDate.now());
        model.addAttribute("newTrip", Trip.builder().build());
        return "admin/admin_railway";
    }

    @GetMapping("bus")
    public String getAdminBusPage(Model model){
        List<TripDto> trips = tripService.getAllBusTripDto();
        model.addAttribute("trips", trips);
        model.addAttribute("departure", LocalDate.now());
        model.addAttribute("arrival", LocalDate.now());
        model.addAttribute("newTrip", Trip.builder().build());
        return "admin/admin_bus";
    }

    @GetMapping("hotel")
    public String getAdminHotelPage(Model model){
        List<HotelDto> hotels = hotelService.getAllHotelDto();
        model.addAttribute("hotels", hotels);
        model.addAttribute("departure", LocalDate.now());
        model.addAttribute("arrival", LocalDate.now());
        model.addAttribute("newHotel", Hotel.builder().build());
        return "admin/admin_hotel";
    }

    @PostMapping("save_trip")
    public String saveNewTrip(Model model, @Valid @ModelAttribute("newTrip") Trip trip,
                              @RequestParam("departure") String departureDate,
                              @RequestParam("arrival") String arrivalDate,
                              BindingResult bindingResult) throws TripSaveException {
        LocalDate departure = LocalDate.parse(departureDate);
        LocalDate arrival = LocalDate.parse(arrivalDate);
        trip.setDepartureDate(departure);
        trip.setArrivalDate(arrival);
        tripService.createNewTrip(trip);
        return "redirect:/admin/avia";
    }

    @PostMapping("save_hotel")
    public String saveNewHotel(Model model, @Valid @ModelAttribute("newHotel") Hotel hotel,
                              @RequestParam("departure") String departureDate,
                              @RequestParam("arrival") String arrivalDate) throws HotelSaveException {
        LocalDate departure = LocalDate.parse(departureDate);
        LocalDate arrival = LocalDate.parse(arrivalDate);
        hotel.setDepartureDate(departure);
        hotel.setArrivalDate(arrival);
        hotelService.createNewHotel(hotel);
        return "redirect:/admin/hotel";
    }

    @GetMapping("edit_trip{id}")
    public String editTrip(Model model, @PathVariable Long id) throws TripNotFoundException {
        Trip trip = tripService.getTripById(id);
        model.addAttribute("editTrip", trip);
        model.addAttribute("tripId", id);
        return "admin/edit_trip";
    }

    @PostMapping("edit_trip{id}")
    public String editTrip(Model model,  @PathVariable Long id,
                               @Valid @ModelAttribute("editTrip") Trip trip,
                               @RequestParam("departure") String departureDate,
                               @RequestParam("arrival") String arrivalDate,
                               BindingResult bindingResult){
        LocalDate departure = LocalDate.parse(departureDate);
        LocalDate arrival = LocalDate.parse(arrivalDate);
        trip.setDepartureDate(departure);
        trip.setArrivalDate(arrival);
        tripService.editTrip(trip);
        return "redirect:/admin/avia";
    }

    @GetMapping("edit_hotel{id}")
    public String editHotel(Model model, @PathVariable Long id) throws HotelNotFoundException {
        Hotel hotel = hotelService.findHotelById(id);
        model.addAttribute("editHotel", hotel);
        model.addAttribute("hotelId", id);
        return "admin/edit_hotel";
    }

    @PostMapping("edit_hotel{id}")
    public String editNewHotel(Model model,  @PathVariable Long id,
                               @Valid @ModelAttribute("editHotel") Hotel hotel,
                               @RequestParam("departure") String departureDate,
                               @RequestParam("arrival") String arrivalDate) {
        LocalDate departure = LocalDate.parse(departureDate);
        LocalDate arrival = LocalDate.parse(arrivalDate);
        hotel.setDepartureDate(departure);
        hotel.setArrivalDate(arrival);
        hotelService.editHotel(hotel);
        return "redirect:/admin/hotel";
    }

    @GetMapping("delete_trip{id}")
    public String deleteTrip(@PathVariable Long id) throws TripNotFoundException {
        tripService.deleteTripById(id);
        return "redirect:/admin/avia";
    }

    @GetMapping("delete_hotel{id}")
    public String deleteHotel(@PathVariable Long id) throws HotelNotFoundException {
        hotelService.deleteHotelById(id);
        return "redirect:/admin/hotel";
    }


    @ExceptionHandler(TripSaveException.class)
    public String handleTripSaveException(Model model) {
        log.error("TripSaveException Exception");
        model.addAttribute("error", true);
        return "redirect:/admin/avia";
    }

    @ExceptionHandler(TripNotFoundException.class)
    public String handleTripNotFoundException(Model model) {
        log.error("TripTripNotFoundException Exception");
        model.addAttribute("error", true);
        return "redirect:/admin/avia";
    }
}
