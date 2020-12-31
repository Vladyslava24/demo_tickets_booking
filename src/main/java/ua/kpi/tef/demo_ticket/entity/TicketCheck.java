package ua.kpi.tef.demo_ticket.entity;

import lombok.*;
import ua.kpi.tef.demo_ticket.entity.enums.TicketStatus;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder

@Entity
@Table(name = "ticket_check")
public class TicketCheck {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false)
    private Long id;

    @OneToOne(mappedBy = "check")
    private RailwayTicket ticket;

    @OneToOne(mappedBy = "check")
    private AviaTicket aviaTicket;

    @OneToOne(mappedBy = "check")
    private HotelBooking hotelBooking;

    @OneToOne(mappedBy = "check")
    private BusTicket busTicket;

    private BigDecimal price;

    @Column(name = "status")
    @Enumerated(value = EnumType.STRING)
    private TicketStatus ticketStatus;

    @ManyToOne
    private User user;

    @ManyToOne
    private BankCard bankCard;

    private LocalDate creationDate ;

}
