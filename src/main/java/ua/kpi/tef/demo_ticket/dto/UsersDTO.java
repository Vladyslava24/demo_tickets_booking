package ua.kpi.tef.demo_ticket.dto;

import lombok.*;
import ua.kpi.tef.demo_ticket.entity.User;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class UsersDTO {
    private List<User> users;
}
