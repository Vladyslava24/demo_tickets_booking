package ua.kpi.tef.demo_ticket.dto;

import lombok.*;
import ua.kpi.tef.demo_ticket.entity.TicketCheck;
import ua.kpi.tef.demo_ticket.entity.Trip;
import ua.kpi.tef.demo_ticket.entity.enums.DocumentType;
import ua.kpi.tef.demo_ticket.entity.enums.TicketStatus;

import java.time.LocalDate;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class BusTicketDto {
    private Long id;

    private Trip tripDto;

    //private User user;

    private int place;

    private String email;

    private String phoneNumber;

    private String firstName;

    private String lastName;

    private LocalDate birthDate;

    private DocumentType documentType;

    private long ticketPrice;

    private TicketCheck check;

    private TicketStatus ticketStatus = TicketStatus.AVAILABLE;
}
