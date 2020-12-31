package ua.kpi.tef.demo_ticket.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ua.kpi.tef.demo_ticket.controller.exception.HotelNotFoundException;
import ua.kpi.tef.demo_ticket.controller.exception.TripNotFoundException;
import ua.kpi.tef.demo_ticket.dto.HotelDto;
import ua.kpi.tef.demo_ticket.dto.TripDto;
import ua.kpi.tef.demo_ticket.entity.AviaTicket;
import ua.kpi.tef.demo_ticket.entity.BusTicket;
import ua.kpi.tef.demo_ticket.entity.HotelBooking;
import ua.kpi.tef.demo_ticket.entity.Ticket;
import ua.kpi.tef.demo_ticket.service.HotelService;
import ua.kpi.tef.demo_ticket.service.TicketService;
import ua.kpi.tef.demo_ticket.service.TripService;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Controller
public class TripController {
    private final TripService tripService;
    private final HotelService hotelService;
    private final TicketService ticketService;

    @Autowired
    public TripController(TripService tripService, HotelService hotelService, TicketService ticketService) {
        this.tripService = tripService;
        this.hotelService = hotelService;
        this.ticketService = ticketService;
    }



    /*@RequestMapping("/avia")
    public String getAviaPage(Model model){
        List<Trip> trips = tripService.findAll();
        model.addAttribute("trips", trips);
        return "avia_trip.html";
    }*/

    /*@RequestMapping("/railway")
    public String getRailwayPage(Model model){
        List<Trip> trips = tripService.findAll();
        model.addAttribute("trips", trips);
        return "railway_trip.html";
    }*/
    /*@InitBinder
    public void setAllowedFields(WebDataBinder webDataBinder){
        webDataBinder.setDisallowedFields("departureDate");

    }*/

    @GetMapping("/")
    public String createTripView(Model model) {
        List<TripDto> tripDto = tripService.getAllTripDto();
        List<String> tripsFrom = tripDto.stream()
                .map(TripDto::getFromWhere)
                .collect(Collectors.toList());
        List<String> tripsTo = tripDto.stream()
                .map(TripDto::getWhereTo)
                .collect(Collectors.toList());
        List<LocalDate> departureDate = tripDto.stream()
                .map(TripDto::getDepartureDate)
                .collect(Collectors.toList());
        List<LocalDate> arrivalDate = tripDto.stream()
                .map(TripDto::getArrivalDate)
                .collect(Collectors.toList());
        List<HotelDto> hotelDto = hotelService.getAllHotelDto();
        List<String> city = hotelDto.stream()
                .map(HotelDto::getCity)
                .collect(Collectors.toList());
        List<LocalDate> departure = hotelDto.stream()
                .map(HotelDto::getDepartureDate)
                .collect(Collectors.toList());
        List<LocalDate> arrival = hotelDto.stream()
                .map(HotelDto::getArrivalDate)
                .collect(Collectors.toList());
        List<String> apartmentType = hotelDto.stream()
                .map(HotelDto::getApartmentType)
                .collect(Collectors.toList());
        model.addAttribute("tripsFrom", tripsFrom);
        model.addAttribute("tripsTo", tripsTo);
        model.addAttribute("departureDate", departureDate);
        model.addAttribute("arrivalDate", arrivalDate);
        model.addAttribute("city", city);
        model.addAttribute("departure", departure);
        model.addAttribute("arrival", arrival);
        model.addAttribute("apartment_type", apartmentType);
        return "index";
    }

    /*@GetMapping("/avia_trip/{tripsFrom}/{tripsTo}")
    public String findTripPage(@PathVariable String tripsFrom, @PathVariable String tripsTo) {
        try {
            tripService.getSearchedTrip(tripsFrom, tripsTo);
        } catch (TripException e) {
            e.printStackTrace();
        }
        //if (bindingResult.hasErrors()){
        //  return "/";
        //}
        //tripService.getAllTripDto();
        //orderService.createOrder(newOrder, user);
        return "avia_trip";
    }*/

    /*@GetMapping("/find_railway_trips")
    public String findRailwayTripPage(){
        return "railway_trip";
    }*/

    @PostMapping("/find_avia_trips")
    public String findAviaTrip(Model model, @RequestParam("tripsFrom") String  tripsFrom,
                               @RequestParam("tripsTo") String tripsTo,
                               @RequestParam("departureDate") String departureDate,
                               @RequestParam(value = "arrivalDate",required = false) String arrivalDate) {

        LocalDate departure = LocalDate.parse(departureDate);
        List<TripDto> trips;
        if(!arrivalDate.isEmpty()) {
            LocalDate arrival = LocalDate.parse(arrivalDate);
            trips = tripService.getSearchedAviaTripWithArrivalDate(tripsFrom, tripsTo, departure, arrival);
        }else {
            trips = tripService.getSearchedAviaTrip(tripsFrom, tripsTo, departure);
        }
        if(trips.size() == 0){
            return "not_found_trips";
        }
        model.addAttribute("trip_from", tripsFrom);
        model.addAttribute("trip_to", tripsTo);
        model.addAttribute("departure_date", departureDate);
        model.addAttribute("trips", trips);
        return "avia_trip";
    }

    @PostMapping("/find_railway_trips")
    public String postFindRailwayTrip(Model model, @RequestParam("tripsFrom") String  tripsFrom,
                                      @RequestParam("tripsTo") String tripsTo,
                                      @RequestParam("departureDate") String departureDate,
                                      @RequestParam(required = false) String arrivalDate) {
        LocalDate departure = LocalDate.parse(departureDate);
        List<TripDto> trips;
        if(!arrivalDate.isEmpty()){
            LocalDate arrival = LocalDate.parse(arrivalDate);
            trips = tripService.getSearchedRailwayTripWithArrivalDate(tripsFrom, tripsTo, departure, arrival);
        } else {
            trips = tripService.getSearchedRailwayTrip(tripsFrom, tripsTo, departure);
        }
        if(trips.size() == 0){
            return "not_found_trips";
        }
        model.addAttribute("trip_from", tripsFrom);
        model.addAttribute("trip_to", tripsTo);
        model.addAttribute("departure_date", departureDate);
        model.addAttribute("trips", trips);
        return "railway_trip";
    }

    @PostMapping("/find_bus_trips")
    public String postFindBusTrip(Model model, @RequestParam("tripsFrom") String  tripsFrom,
                                      @RequestParam("tripsTo") String tripsTo,
                                      @RequestParam("departureDate") String departureDate) {
        LocalDate departure = LocalDate.parse(departureDate);
        List<TripDto> trips = tripService.getSearchedBusTrip(tripsFrom, tripsTo, departure);
        if(trips.size() == 0){
            return "not_found_trips";
        }
        model.addAttribute("trip_from", tripsFrom);
        model.addAttribute("trip_to", tripsTo);
        model.addAttribute("departure_date", departureDate);
        model.addAttribute("trips", trips);
        return "bus_trip";
    }

    @PostMapping("/find_hotels")
    public String findHotels(Model model, @RequestParam("city") String  city,
                               @RequestParam("departure") String departureDate,
                               @RequestParam("arrival") String arrivalDate,
                               @RequestParam("apartment_type") String apartmentType) {
        LocalDate departure = LocalDate.parse(departureDate);
        LocalDate arrival = LocalDate.parse(arrivalDate);
        List<HotelDto> hotels = hotelService.getSearchedHotels(city, departure, arrival, apartmentType);
        if(hotels.size() == 0){
            return "not_found_trips";
        }
        model.addAttribute("city", city);
        model.addAttribute("departure", departure);
        model.addAttribute("arrival", arrival);
        model.addAttribute("hotels", hotels);
        return "hotel";
    }


    @GetMapping("/find_avia_trip{id}")
    public String findAviaTrip(@PathVariable Long id, Model model,
                               @ModelAttribute("newTicket") AviaTicket newTicket) throws TripNotFoundException {

        TripDto trip =  tripService.getTripDtoById(id);
        model.addAttribute("trip_from", trip.getFromWhere());
        model.addAttribute("trip_to", trip.getWhereTo());
        model.addAttribute("departure_date", trip.getDepartureDate());
        model.addAttribute("trip", trip);
        model.addAttribute("newTicket", AviaTicket.builder().build());
        model.addAttribute("ticketId", newTicket.getId());
        return "avia_booking";
    }

    @GetMapping("/find_railway_trip{id}")
    public String findRailwayTrip(@PathVariable Long id,
                                   Model model) throws TripNotFoundException {
        TripDto trip =  tripService.getTripDtoById(id);
        model.addAttribute("trip", trip);
        return "railway_ticket_booking";
    }

    @GetMapping("/find_bus_trip{id}")
    public String findBusTrip(@PathVariable Long id,
                               Model model) throws TripNotFoundException {
        TripDto trip =  tripService.getTripDtoById(id);
        model.addAttribute("trip_from", trip.getFromWhere());
        model.addAttribute("trip_to", trip.getWhereTo());
        model.addAttribute("departure_date", trip.getDepartureDate());
        model.addAttribute("trip", trip);
        model.addAttribute("newTicket", BusTicket.builder().build());
        return "bus_booking";
    }

    @GetMapping("/find_one_railway_trip{id}")
    public String findOneRailwayTrip(@PathVariable Long id,
                                  Model model) throws TripNotFoundException {

        TripDto trip =  tripService.getTripDtoById(id);
        model.addAttribute("trip", trip);
        model.addAttribute("newTicket", Ticket.builder().build());
        return "railway_ticket_booking";
    }

    @GetMapping("/find_hotel{id}")
    public String findHotel(@PathVariable Long id,
                               Model model) throws HotelNotFoundException {
        HotelDto hotel =  hotelService.getHotelDtoById(id);
        model.addAttribute("city", hotel.getCity());
        model.addAttribute("departure", hotel.getDepartureDate());
        model.addAttribute("arrival", hotel.getArrivalDate());
        model.addAttribute("hotelName", hotel.getHotelName());
        model.addAttribute("hotel", hotel);
        model.addAttribute("newBooking", HotelBooking.builder().build());
        return "hotel_booking";
    }

    @ExceptionHandler(TripNotFoundException.class)
    public String handleOrderNotFoundException(Model model) {
        log.error("TripNotFoundException Exception");
        model.addAttribute("error", true);
        return "redirect:/";
    }
}
