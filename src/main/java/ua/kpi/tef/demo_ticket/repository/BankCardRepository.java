package ua.kpi.tef.demo_ticket.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.kpi.tef.demo_ticket.entity.BankCard;
import ua.kpi.tef.demo_ticket.entity.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface BankCardRepository  extends JpaRepository<BankCard, Long> {

    List<BankCard> findBankCardByUser(User user);

    Optional<BankCard> findBankCardByIdAndExpMonthAndExpYearAndCcv(Long id, Long expMonth, Long expYear, Long cvv);

}
