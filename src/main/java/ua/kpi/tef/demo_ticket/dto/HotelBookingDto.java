package ua.kpi.tef.demo_ticket.dto;

import lombok.*;
import ua.kpi.tef.demo_ticket.entity.Hotel;
import ua.kpi.tef.demo_ticket.entity.TicketCheck;
import ua.kpi.tef.demo_ticket.entity.enums.ApartmentType;
import ua.kpi.tef.demo_ticket.entity.enums.TicketStatus;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class HotelBookingDto {
    private Long id;

    private Hotel hotelDto;

    //private User user;

    private int apartmentNumber;

    private String email;

    private String phoneNumber;

    private ApartmentType apartmentType;

    private String firstName;

    private String lastName;

    private String citizenship;

    private long hotelPrice;

    private TicketCheck check;

    private TicketStatus ticketStatus = TicketStatus.AVAILABLE;

}
