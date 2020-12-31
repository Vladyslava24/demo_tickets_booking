package ua.kpi.tef.demo_ticket.controller.exception;

public class TripNotFoundException extends Exception {
    public TripNotFoundException(String message) {
        super(message);
    }
}
