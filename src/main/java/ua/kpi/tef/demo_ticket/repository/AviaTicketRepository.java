package ua.kpi.tef.demo_ticket.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.kpi.tef.demo_ticket.entity.AviaTicket;

import java.util.List;

@Repository
public interface AviaTicketRepository extends JpaRepository<AviaTicket, Long> {
    List<AviaTicket> findAviaTicketByUserId(Long userId);

    List<AviaTicket> findAviaTicketByTripId(Long ticketId);
}
