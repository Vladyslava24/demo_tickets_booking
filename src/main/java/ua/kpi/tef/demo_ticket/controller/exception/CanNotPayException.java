package ua.kpi.tef.demo_ticket.controller.exception;

public class CanNotPayException extends Exception {
    public CanNotPayException(String message) {
        super(message);
    }
}
