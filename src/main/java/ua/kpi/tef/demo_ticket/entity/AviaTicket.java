package ua.kpi.tef.demo_ticket.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ua.kpi.tef.demo_ticket.entity.enums.TicketStatus;
import ua.kpi.tef.demo_ticket.entity.enums.Sex;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor

@Entity
@Table(name = "avia_ticket")
public class AviaTicket {
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
    private Sex sex;

    private String citizenship;

    private String documentNumber;

    @Column(name = "term_date")
    private LocalDate termDate;

    @OneToMany(mappedBy = "ticket")
    private List<AdditionalServices> additionalServices = new ArrayList<>();

    @Column(nullable = false)
    private long ticketPrice;

    @OneToOne(optional = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "check_id", referencedColumnName = "id", unique = true)
    private TicketCheck check;

    @Enumerated(EnumType.STRING)
    private TicketStatus ticketStatus;

    public AviaTicket saveTicket(User user){
        this.setUser(user);
        user.getAviaTickets().add(this);
        return this;
    }

    public AviaTicket saveTicket(Trip trip){
        this.setTrip(trip);
        trip.getAviaTickets().add(this);
        return this;
    }
}
