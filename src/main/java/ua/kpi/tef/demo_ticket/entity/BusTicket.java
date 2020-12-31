package ua.kpi.tef.demo_ticket.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ua.kpi.tef.demo_ticket.entity.enums.DocumentType;
import ua.kpi.tef.demo_ticket.entity.enums.TicketStatus;

import javax.persistence.*;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor

@Entity
@Table(name = "bus_ticket")
public class BusTicket {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne
    private Trip trip;

    @ManyToOne
    private User user;

    private int place;

    private String email;

    private String phoneNumber;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "birth_date")
    private LocalDate birthDate;

    @Enumerated(EnumType.STRING)
    private DocumentType documentType;

    @Column(name = "term_date")
    private LocalDate termDate;

    private long ticketPrice;

    @OneToOne(optional = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "check_id", referencedColumnName = "id", unique = true)
    private TicketCheck check;

    @Enumerated(EnumType.STRING)
    private TicketStatus ticketStatus;

    public BusTicket saveTicket(User user){
        this.setUser(user);
        user.getBusTickets().add(this);
        return this;
    }

    public BusTicket saveTicket(Trip trip){
        this.setTrip(trip);
        trip.getBusTickets().add(this);
        return this;
    }
}
