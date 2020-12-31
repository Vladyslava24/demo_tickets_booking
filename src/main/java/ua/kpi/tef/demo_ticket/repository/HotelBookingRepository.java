package ua.kpi.tef.demo_ticket.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.kpi.tef.demo_ticket.entity.HotelBooking;

import java.util.List;

@Repository
public interface HotelBookingRepository extends JpaRepository<HotelBooking, Long> {

    List<HotelBooking> findHotelBookingByUserId(Long userId);

    List<HotelBooking> findHotelBookingByHotelId(Long hotelBookingId);
}
