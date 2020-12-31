package ua.kpi.tef.demo_ticket.controller.exception;

public class UserNotFoundException extends Exception {
    public UserNotFoundException(String message) {
        super(message);
    }
}
