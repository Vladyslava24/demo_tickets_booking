package ua.kpi.tef.demo_ticket.dto;

import lombok.*;
import ua.kpi.tef.demo_ticket.entity.enums.ClassType;
import ua.kpi.tef.demo_ticket.entity.enums.TripType;

import java.time.LocalDate;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class TripDto {
    private Long id;

    private TripType tripType;

    private String tripName;

    private String fromWhere;

    private String whereTo;

    private String departureTime;

    private String arrivalTime;

    private LocalDate departureDate;

    private LocalDate arrivalDate;

    private int durationHours;

    private int durationMinutes;

    private long price;

    private ClassType classType;

    private int placeAmount;
}
