package ua.kpi.tef.demo_ticket.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.kpi.tef.demo_ticket.entity.TicketCheck;

import java.util.List;

@Repository
public interface TicketCheckRepository  extends JpaRepository<TicketCheck, Long> {
    List<TicketCheck> findAllByUser_Id(Long id);
}

