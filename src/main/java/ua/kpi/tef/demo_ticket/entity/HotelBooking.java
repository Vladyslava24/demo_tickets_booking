package ua.kpi.tef.demo_ticket.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ua.kpi.tef.demo_ticket.entity.enums.ApartmentType;
import ua.kpi.tef.demo_ticket.entity.enums.TicketStatus;

import javax.persistence.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor

@Entity
@Table(name = "hotel_booking")
public class HotelBooking {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne
    private Hotel hotel;

    @ManyToOne
    private User user;

    private int apartmentNumber;

    private String email;

    private String phoneNumber;

    @Enumerated(EnumType.STRING)
    private ApartmentType apartmentType;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    private String citizenship;

    private long hotelPrice;

    @OneToOne(optional = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "check_id", referencedColumnName = "id", unique = true)
    private TicketCheck check;

    @Enumerated(EnumType.STRING)
    private TicketStatus ticketStatus;

    public HotelBooking saveHotel(User user){
        this.setUser(user);
        user.getHotelBookings().add(this);
        return this;
    }

    public HotelBooking saveHotel(Hotel hotel){
        this.setHotel(hotel);
        hotel.getHotels().add(this);
        return this;
    }
}
