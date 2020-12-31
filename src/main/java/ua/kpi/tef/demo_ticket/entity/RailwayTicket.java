package ua.kpi.tef.demo_ticket.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ua.kpi.tef.demo_ticket.entity.enums.PassengerType;
import ua.kpi.tef.demo_ticket.entity.enums.TicketStatus;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor

@Entity
@Table(name = "railway_ticket")
public class RailwayTicket {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne
    private Trip trip;

    @ManyToOne
    private User user;

    private int carriage;

    private int place;

    private String email;

    private String phoneNumber;

    @Enumerated(EnumType.STRING)
    private PassengerType passengerType;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "birth_date")
    private LocalDate birthDate;

    @Column(name = "student_card_number")
    private String studentCardNumber;

    @OneToMany(mappedBy = "ticket")
    private List<AdditionalServices> additionalServices = new ArrayList<>();

    @Column(nullable = false)
    private long ticketPrice;

    @OneToOne(optional = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "check_id", referencedColumnName = "id", unique = true)
    private TicketCheck check;

    @Enumerated(EnumType.STRING)
    private TicketStatus ticketStatus;

    public RailwayTicket saveTicket(User user){
        this.setUser(user);
        user.getTickets().add(this);
        return this;
    }

    public RailwayTicket saveTicket(Trip trip){
        this.setTrip(trip);
        trip.getTickets().add(this);
        return this;
    }
}
