package ua.kpi.tef.demo_ticket.controller.exception;

public class HotelNotFoundException extends Exception {

    public HotelNotFoundException(String message) {
        super(message);
    }
}
