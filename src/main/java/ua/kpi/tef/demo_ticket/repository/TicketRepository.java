package ua.kpi.tef.demo_ticket.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.kpi.tef.demo_ticket.entity.RailwayTicket;
import ua.kpi.tef.demo_ticket.entity.Ticket;
import ua.kpi.tef.demo_ticket.entity.Trip;

import java.util.List;

@Repository
public interface TicketRepository extends JpaRepository<RailwayTicket, Long> {
    List<RailwayTicket> findTicketByUserId(Long userId);

    List<RailwayTicket> findTicketByTripId(Long ticketId);

    //List<Trip> findTripById(Long ticketId);
}
