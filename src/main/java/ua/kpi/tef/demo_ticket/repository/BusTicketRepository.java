package ua.kpi.tef.demo_ticket.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.kpi.tef.demo_ticket.entity.BusTicket;

import java.util.List;

@Repository
public interface BusTicketRepository extends JpaRepository<BusTicket, Long> {
    List<BusTicket> findBusTicketByUserId(Long userId);

    List<BusTicket> findBusTicketByTripId(Long ticketId);
}
