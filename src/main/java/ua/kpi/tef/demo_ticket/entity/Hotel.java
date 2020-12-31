package ua.kpi.tef.demo_ticket.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ua.kpi.tef.demo_ticket.entity.enums.TripType;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor

@Entity
@Table(name = "hotel")
public class Hotel {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false)
    private Long id;

    @Enumerated(EnumType.STRING)
    private TripType tripType;

    @Column(name = "hotel_name")
    private String hotelName;

    @Column(name = "city")
    private String city;

    @Column(name = "departure_date")
    private LocalDate departureDate;

    @Column(name = "arrival_date")
    private LocalDate arrivalDate;

    @Column(name = "apartment_type")
    private String apartmentType;

    @Column(name = "address")
    private String address;

    @Column(name = "food_type")
    private String foodType;

    @Column(name = "apartment_amount")
    private int apartmentAmount;

    @Column(name = "price")
    private long price;


    @OneToMany(mappedBy = "hotel")
    private List<HotelBooking> hotels = new ArrayList<>();
}
