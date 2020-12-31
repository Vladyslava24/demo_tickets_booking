package ua.kpi.tef.demo_ticket.entity;


import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "bank_card",
        uniqueConstraints = {@UniqueConstraint(columnNames = {"id"})})
public class BankCard {

    @Id
    @EqualsAndHashCode.Include
    private Long id;

    private Long expMonth;

    private Long expYear;

    private Long ccv;

    private BigDecimal balance = BigDecimal.ZERO;

    @ManyToOne
    private User user;

    @OneToMany(mappedBy = "bankCard")
    private List<TicketCheck> ticketChecks = new ArrayList<>();

    @PreRemove
    public void deleteBankCard(){
        user.getCards().remove(this);
        ticketChecks.forEach(b -> b.setBankCard(null));
    }

    public BankCard saveBankCard(User user){
        this.setUser(user);
        user.getCards().add(this);
        return this;
    }

    public void deleteUser(User user){
        user.getCards().remove(this);
    }

}
