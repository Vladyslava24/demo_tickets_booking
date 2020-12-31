package ua.kpi.tef.demo_ticket.dto;

import lombok.*;
import ua.kpi.tef.demo_ticket.entity.enums.TicketStatus;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class TicketCheckDto {
    private Long id;

    private Long orderId;

    private BigDecimal price;

    private TicketStatus ticketStatus;

    private Long userId;

    private Long bankCard;

    private LocalDate creationDate;
}
