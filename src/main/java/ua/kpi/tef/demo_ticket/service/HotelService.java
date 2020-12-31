package ua.kpi.tef.demo_ticket.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ua.kpi.tef.demo_ticket.controller.exception.HotelNotFoundException;
import ua.kpi.tef.demo_ticket.controller.exception.HotelSaveException;
import ua.kpi.tef.demo_ticket.dto.HotelDto;
import ua.kpi.tef.demo_ticket.entity.Hotel;
import ua.kpi.tef.demo_ticket.mappers.LocalizedDtoMapper;
import ua.kpi.tef.demo_ticket.repository.HotelRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class HotelService {
    private final HotelRepository hotelRepository;

    public HotelService(HotelRepository hotelRepository) {
        this.hotelRepository = hotelRepository;
    }

    public List<HotelDto> getAllHotelDto() {
        return hotelRepository.findAll().stream()
                .map(m -> getLocalizedDTO().map(m))
                .collect(Collectors.toList());
    }

    public List<HotelDto> getSearchedHotels(String city, LocalDate departureDate, LocalDate arrivalDate,
                                            String apartmentType){
        return hotelRepository.findByAllParameters(city, departureDate, arrivalDate, apartmentType).stream()
                .map(m -> getLocalizedDTO().map(m))
                .filter(p->p.getTripType().toString().equals("HOTEL"))
                .collect(Collectors.toList());
    }

    public HotelDto getHotelDtoById(Long id) throws HotelNotFoundException {
        return  hotelRepository.findById(id).map(m -> getLocalizedDTO().map(m))
                .orElseThrow(() -> new HotelNotFoundException("Trip id=" + id + " not found"));
    }

    public Hotel getHotelById(Long id) throws HotelNotFoundException {
        return  hotelRepository.findById(id)
                .orElseThrow(() -> new HotelNotFoundException("Trip id=" + id + " not found"));
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW,
            rollbackFor = HotelSaveException.class)
    public void createNewHotel(Hotel hotel) throws HotelSaveException{
        try {
            hotelRepository.save(hotel);
        }catch (DataIntegrityViolationException e) {
            throw new HotelSaveException("Can not hotel with  id=" + hotel.getId());
        }
    }

    public void editHotel(Hotel hotel) {
        hotelRepository.save(hotel);
        log.info("editing hotel");
    }

    public Hotel findHotelById(Long hotelId) throws HotelNotFoundException {
        return hotelRepository.findHotelById(hotelId).orElseThrow(()
                -> new HotelNotFoundException("hotel " + hotelId + " not found"));
    }

    public void deleteHotelById(Long hotelId) throws HotelNotFoundException {
        Hotel hotel = hotelRepository.findHotelById(hotelId).orElseThrow(()
                -> new HotelNotFoundException("hotel " + hotelId + " not found"));
        hotelRepository.delete(hotel);
        log.info("deleting hotel");
    }




    public LocalizedDtoMapper<HotelDto, Hotel> getLocalizedDTO() {
        return hotel -> HotelDto.builder()
                .id(hotel.getId())
                .tripType(hotel.getTripType())
                .city(hotel.getCity())
                .hotelName(hotel.getHotelName())
                .departureDate(hotel.getDepartureDate())
                .arrivalDate(hotel.getArrivalDate())
                .apartmentType(hotel.getApartmentType())
                .address(hotel.getAddress())
                .foodType(hotel.getFoodType())
                .price(hotel.getPrice())
                .apartmentAmount(hotel.getApartmentAmount())
                .build();
    }
}
