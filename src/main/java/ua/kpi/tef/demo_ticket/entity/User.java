package ua.kpi.tef.demo_ticket.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import ua.kpi.tef.demo_ticket.entity.enums.RoleType;

import javax.persistence.*;
import java.util.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor

@Entity
@Table( name="user",
        uniqueConstraints={@UniqueConstraint(columnNames={"email"})})
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false)
    Long id;
    @Column(name = "first_name", nullable = false)
    private String firstName;
    @Column(name = "last_name", nullable = false)
    private String lastName;
    @Column(nullable = false)
    private String email;
    @Column(nullable = false)
    private String username;
    @Column(name = "password", nullable = false)
    private String password;
    @Column
    @Enumerated(EnumType.STRING)
    private RoleType role;
    @Column(name = "balance", nullable = false)
    private long balance;

    @OneToMany(mappedBy = "user")
    private List<AviaTicket> aviaTickets = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<RailwayTicket> tickets = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<BusTicket> busTickets = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<HotelBooking> hotelBookings = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<TicketCheck> ticketChecks = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private Set<BankCard> cards = new HashSet<>();

    private boolean accountNonExpired;
    private boolean accountNonLocked;
    private boolean credentialsNonExpired;
    private boolean enabled;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<RoleType> list = new ArrayList<>();
        list.add(role);
        return list;
    }
}