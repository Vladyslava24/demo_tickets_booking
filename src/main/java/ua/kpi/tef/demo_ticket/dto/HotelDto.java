package ua.kpi.tef.demo_ticket.dto;

import lombok.*;
import ua.kpi.tef.demo_ticket.entity.enums.TripType;

import java.time.LocalDate;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class HotelDto {
    private Long id;

    private TripType tripType;

    private String hotelName;

    private String city;

    private LocalDate departureDate;

    private LocalDate arrivalDate;

    private String apartmentType;

    private String address;

    private String foodType;

    private long price;

    private int apartmentAmount;
}
