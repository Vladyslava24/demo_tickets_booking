package ua.kpi.tef.demo_ticket.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ua.kpi.tef.demo_ticket.entity.enums.TripType;
import ua.kpi.tef.demo_ticket.entity.enums.ClassType;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor

@Entity
@Table(name = "trip")
public class Trip {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false)
    private Long id;

    @Enumerated(EnumType.STRING)
    private TripType tripType;

    @Column(name = "trip_name")
    private String tripName;

    @Column(name = "from_where")
    private String fromWhere;

    @Column(name = "where_to")
    private String whereTo;

    @Column(name = "departure_time")
    private String departureTime;

    @Column(name = "arrival_time")
    private String arrivalTime;

    @Column(name = "departure_date")
    private LocalDate departureDate;

    @Column(name = "arrival_date")
    private LocalDate arrivalDate;

    @Column(name = "duration_hours")
    private int durationHours;

    @Column(name = "duration_minutes")
    private int durationMinutes;

    @Column(name = "price")
    private long price;

    @Enumerated(EnumType.STRING)
    private ClassType classType;

    @Column(name = "place_amount")
    private int placeAmount;

    @OneToMany(mappedBy = "trip")
    private List<RailwayTicket> tickets = new ArrayList<>();

    @OneToMany(mappedBy = "trip")
    private List<AviaTicket> aviaTickets = new ArrayList<>();

    @OneToMany(mappedBy = "trip")
    private List<BusTicket> busTickets = new ArrayList<>();
}
