package ua.kpi.tef.demo_ticket.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ua.kpi.tef.demo_ticket.controller.exception.BankException;
import ua.kpi.tef.demo_ticket.controller.exception.CanNotPayException;
import ua.kpi.tef.demo_ticket.controller.exception.TicketBookingException;
import ua.kpi.tef.demo_ticket.dto.BankCardDto;
import ua.kpi.tef.demo_ticket.dto.TicketCheckDto;
import ua.kpi.tef.demo_ticket.entity.*;
import ua.kpi.tef.demo_ticket.entity.enums.TicketStatus;
import ua.kpi.tef.demo_ticket.mappers.LocalizedDtoMapper;
import ua.kpi.tef.demo_ticket.repository.BankCardRepository;
import ua.kpi.tef.demo_ticket.repository.TicketCheckRepository;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@PropertySource("classpath:constants.properties")
public class BankCardService {
    private final BankCardRepository bankCardRepository;
    private final UserService userService;
    private final TicketService ticketService;
    private final HotelBookingService hotelBookingService;
    private final TicketCheckRepository ticketCheckRepository;


    @Value("${constants.ACCOUNT.TO.SEND.MONEY.id}")
    private Long ACCOUNT_TO_SEND_MONEY_ID;

    @Value("${constants.ACCOUNT.TO.SEND.MONEY.expMonth}")
    private Long ACCOUNT_TO_SEND_MONEY_EXP_MONTH;

    @Value("${constants.ACCOUNT.TO.SEND.MONEY.expYear}")
    private Long ACCOUNT_TO_SEND_MONEY_EXP_YEAR;

    @Value("${constants.ACCOUNT.TO.SEND.MONEY.ccv}")
    private Long ACCOUNT_TO_SEND_MONEY_CCV;

    @Autowired
    public BankCardService(BankCardRepository bankCardRepository, UserService userService, TicketService ticketService, HotelBookingService hotelBookingService, TicketCheckRepository ticketCheckRepository) {
        this.bankCardRepository = bankCardRepository;
        this.userService = userService;
        this.ticketService = ticketService;
        this.hotelBookingService = hotelBookingService;
        this.ticketCheckRepository = ticketCheckRepository;
    }

    private BankCard findById(Long id) throws BankException {
        return bankCardRepository
                .findById(id)
                .orElseThrow(() -> new BankException("can not find bank card with id = " + id));
    }

    @Transactional
    public void deleteBankCardConnectionWithUser(Long bankId, Long userId) throws BankException {
        BankCard  bankCard = findBankCardById(bankId);
        User user = userService.findUserById(userId);
        bankCard.deleteUser(user);
    }

    public List<BankCardDto> getAllUserBankCards(User user) {
        return bankCardRepository
                .findBankCardByUser(user).stream()
                .map(m -> getLocalizedDTO().map(m))
                .collect(Collectors.toList());
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW,
            rollbackFor = BankException.class)
    public void saveBankCardDTO(BankCardDto bankCardDTO, Long userId) throws BankException {
        User user = userService.findUserById(userId);

        /*Optional<BankCard> optionalBankCard = bankCardRepository.findBankCardByIdAndExpMonthAndExpYearAndCcv(
                bankCardDTO.getId(), bankCardDTO.getExpMonth(), bankCardDTO.getExpYear(), bankCardDTO.getCcv()
        );*/
        /*BankCard bankCardToSave = optionalBankCard
                .orElseGet(() -> getLocalizedFromDTO().map(bankCardDTO));
        user.getCards().add(bankCardToSave);*/
        BankCard bankCardToSave = getLocalizedFromDTO().map(bankCardDTO);
        bankCardToSave.saveBankCard(user);
        //user.getCards().add(bankCardToSave);
        try {
            bankCardRepository.save(bankCardToSave);
        } catch (DataIntegrityViolationException e) {
            throw new BankException("Can not save bank card with  id=" + bankCardDTO.getId());
        }
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW,
            rollbackFor = BankException.class)
    public void saveBankCard(BankCard bankCard, Long userId) throws BankException {
        User user = userService.findUserById(userId);

        /*Optional<BankCard> optionalBankCard = bankCardRepository.findBankCardByIdAndExpMonthAndExpYearAndCcv(
                bankCardDTO.getId(), bankCardDTO.getExpMonth(), bankCardDTO.getExpYear(), bankCardDTO.getCcv()
        );*/
        /*BankCard bankCardToSave = optionalBankCard
                .orElseGet(() -> getLocalizedFromDTO().map(bankCardDTO));
        user.getCards().add(bankCardToSave);*/
        user.getCards().add(bankCard);
        try {
            bankCardRepository.save(bankCard);
        } catch (DataIntegrityViolationException e) {
            throw new BankException("Can not save bank card with  id=" + bankCard.getId());
        }
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW,
            rollbackFor = BankException.class)
    @PostConstruct
    public void createAccountToSendMoney() throws BankException {
        BankCard bankCard = BankCard.builder()
                .id(ACCOUNT_TO_SEND_MONEY_ID)
                .expMonth(ACCOUNT_TO_SEND_MONEY_EXP_MONTH)
                .expYear(ACCOUNT_TO_SEND_MONEY_EXP_YEAR)
                .balance(BigDecimal.ZERO)
                .ccv(ACCOUNT_TO_SEND_MONEY_CCV).build();
        try {
            bankCardRepository.save(bankCard);
        } catch (DataIntegrityViolationException e) {
            throw new BankException("Can not save bank card with  id=" + bankCard.getId());
        }
    }

    public void createBankCard() throws BankException {
        BankCard bankCard = BankCard.builder()
                .build();
        try {
            bankCardRepository.save(bankCard);
        } catch (DataIntegrityViolationException e) {
            throw new BankException("Can not save bank card with  id=" + bankCard.getId());
        }
    }

    public void updateBankCardDTO(BankCardDto bankCardDTO, Long bankCardId) throws BankException {
        BankCard bankCard = findBankCardById(bankCardId);
        bankCard.setBalance(bankCardDTO.getBalance());
        bankCardRepository.save(bankCard);
    }

    public void payForAviaOrder(TicketCheckDto ticketCheckDto) throws TicketBookingException, BankException, CanNotPayException {
        AviaTicket aviaTicket = ticketService.findAviaTicketById(ticketCheckDto.getOrderId());
        if (aviaTicket.getTicketStatus().equals(TicketStatus.PAID)){
            throw new BankException("order with  id=" + aviaTicket.getId() + " is already paid");
        }
        BankCard bankCard =  bankCardRepository.findById(ticketCheckDto.getBankCard())
                .orElseThrow(()->new BankException("no bank card with id=" + ticketCheckDto.getBankCard()));
        if (bankCard.getBalance().subtract(BigDecimal.valueOf(aviaTicket.getTicketPrice())).compareTo(BigDecimal.ZERO) < 0){
            throw  new CanNotPayException("no money to pay for order with id=" + aviaTicket.getId());
        }
        User user = userService.findUserById(ticketCheckDto.getUserId());
        TicketCheck orderCheck = TicketCheck.builder()
                .user(user)
                .bankCard(bankCard)
                .aviaTicket(aviaTicket)
                .build();
        BankCard bankCardToSend = bankCardRepository
                .findBankCardByIdAndExpMonthAndExpYearAndCcv(ACCOUNT_TO_SEND_MONEY_ID, ACCOUNT_TO_SEND_MONEY_EXP_MONTH,
                        ACCOUNT_TO_SEND_MONEY_EXP_YEAR, ACCOUNT_TO_SEND_MONEY_CCV)
                .orElseThrow(() ->new BankException("no bankCard to send money"));
        processAviaPaying(orderCheck, aviaTicket, bankCardToSend);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW,
            rollbackFor = {BankException.class})
    public void processAviaPaying(TicketCheck ticketCheck, AviaTicket aviaTicket, BankCard bankCard) throws BankException {
        BigDecimal moneyToPay = BigDecimal.valueOf(aviaTicket.getTicketPrice());

        sendMoney(ticketCheck.getBankCard().getId(), bankCard.getId(), moneyToPay);
        ticketCheck.setPrice(moneyToPay);
        ticketCheck.setCreationDate(LocalDate.now());
        aviaTicket.setCheck(ticketCheck);
        ticketCheck.getAviaTicket().setTicketStatus(TicketStatus.PAID);
        ticketCheckRepository.save(ticketCheck);
    }

    public void payForOrder(TicketCheckDto ticketCheckDto) throws TicketBookingException, BankException, CanNotPayException {
        RailwayTicket railwayTicket = ticketService.findRailwayTicketById(ticketCheckDto.getOrderId());
        if (railwayTicket.getTicketStatus().equals(TicketStatus.PAID)){
            throw new BankException("order with  id=" + railwayTicket.getId() + " is already paid");
        }
        BankCard bankCard =  bankCardRepository.findById(ticketCheckDto.getBankCard())
                .orElseThrow(()->new BankException("no bank card with id=" + ticketCheckDto.getBankCard()));
        if (bankCard.getBalance().subtract(BigDecimal.valueOf(railwayTicket.getTicketPrice())).compareTo(BigDecimal.ZERO) < 0){
            throw  new CanNotPayException("no money to pay for order with id=" + railwayTicket.getId());
        }
        User user = userService.findUserById(ticketCheckDto.getUserId());
        TicketCheck orderCheck = TicketCheck.builder()
                .user(user)
                .bankCard(bankCard)
                .ticket(railwayTicket)
                .build();
        BankCard bankCardToSend = bankCardRepository
                .findBankCardByIdAndExpMonthAndExpYearAndCcv(ACCOUNT_TO_SEND_MONEY_ID, ACCOUNT_TO_SEND_MONEY_EXP_MONTH,
                        ACCOUNT_TO_SEND_MONEY_EXP_YEAR, ACCOUNT_TO_SEND_MONEY_CCV)
                .orElseThrow(() ->new BankException("no bankCard to send money"));
        processPaying(orderCheck, railwayTicket, bankCardToSend);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW,
            rollbackFor = {BankException.class})
    public void processPaying(TicketCheck ticketCheck, RailwayTicket railwayTicket, BankCard bankCard) throws BankException {
        BigDecimal moneyToPay = BigDecimal.valueOf(railwayTicket.getTicketPrice());

        sendMoney(ticketCheck.getBankCard().getId(), bankCard.getId(), moneyToPay);
        ticketCheck.setPrice(moneyToPay);
        ticketCheck.setCreationDate(LocalDate.now());
        railwayTicket.setCheck(ticketCheck);
        ticketCheck.getTicket().setTicketStatus(TicketStatus.PAID);
        ticketCheckRepository.save(ticketCheck);
    }

    public void payForBusOrder(TicketCheckDto ticketCheckDto) throws TicketBookingException, BankException,
            CanNotPayException {
        BusTicket busTicket = ticketService.findBusTicketById(ticketCheckDto.getOrderId());
        if (busTicket.getTicketStatus().equals(TicketStatus.PAID)){
            throw new BankException("order with  id=" + busTicket.getId() + " is already paid");
        }
        BankCard bankCard =  bankCardRepository.findById(ticketCheckDto.getBankCard())
                .orElseThrow(()->new BankException("no bank card with id=" + ticketCheckDto.getBankCard()));
        if (bankCard.getBalance().subtract(BigDecimal.valueOf(busTicket.getTicketPrice())).compareTo(BigDecimal.ZERO) < 0){
            throw  new CanNotPayException("no money to pay for order with id=" + busTicket.getId());
        }
        User user = userService.findUserById(ticketCheckDto.getUserId());
        TicketCheck orderCheck = TicketCheck.builder()
                .user(user)
                .bankCard(bankCard)
                .busTicket(busTicket)
                .build();
        BankCard bankCardToSend = bankCardRepository
                .findBankCardByIdAndExpMonthAndExpYearAndCcv(ACCOUNT_TO_SEND_MONEY_ID, ACCOUNT_TO_SEND_MONEY_EXP_MONTH,
                        ACCOUNT_TO_SEND_MONEY_EXP_YEAR, ACCOUNT_TO_SEND_MONEY_CCV)
                .orElseThrow(() ->new BankException("no bankCard to send money"));
        processBusPaying(orderCheck, busTicket, bankCardToSend);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW,
            rollbackFor = {BankException.class})
    public void processBusPaying(TicketCheck ticketCheck, BusTicket busTicket, BankCard bankCard) throws BankException {
        BigDecimal moneyToPay = BigDecimal.valueOf(busTicket.getTicketPrice());

        sendMoney(ticketCheck.getBankCard().getId(), bankCard.getId(), moneyToPay);
        ticketCheck.setPrice(moneyToPay);
        ticketCheck.setCreationDate(LocalDate.now());
        busTicket.setCheck(ticketCheck);
        ticketCheck.getBusTicket().setTicketStatus(TicketStatus.PAID);
        ticketCheckRepository.save(ticketCheck);
    }

    public void payForHotelOrder(TicketCheckDto ticketCheckDto) throws TicketBookingException, BankException,
            CanNotPayException {
        HotelBooking hotelBooking = hotelBookingService.findHotelBookingById(ticketCheckDto.getOrderId());
        if (hotelBooking.getTicketStatus().equals(TicketStatus.PAID)){
            throw new BankException("order with  id=" + hotelBooking.getId() + " is already paid");
        }
        BankCard bankCard =  bankCardRepository.findById(ticketCheckDto.getBankCard())
                .orElseThrow(()->new BankException("no bank card with id=" + ticketCheckDto.getBankCard()));
        if (bankCard.getBalance().subtract(BigDecimal.valueOf(hotelBooking.getHotelPrice())).compareTo(BigDecimal.ZERO) < 0){
            throw  new CanNotPayException("no money to pay for order with id=" + hotelBooking.getId());
        }
        User user = userService.findUserById(ticketCheckDto.getUserId());
        TicketCheck orderCheck = TicketCheck.builder()
                .user(user)
                .bankCard(bankCard)
                .hotelBooking(hotelBooking)
                .build();
        BankCard bankCardToSend = bankCardRepository
                .findBankCardByIdAndExpMonthAndExpYearAndCcv(ACCOUNT_TO_SEND_MONEY_ID, ACCOUNT_TO_SEND_MONEY_EXP_MONTH,
                        ACCOUNT_TO_SEND_MONEY_EXP_YEAR, ACCOUNT_TO_SEND_MONEY_CCV)
                .orElseThrow(() ->new BankException("no bankCard to send money"));
        processHotelPaying(orderCheck, hotelBooking, bankCardToSend);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW,
            rollbackFor = {BankException.class})
    public void processHotelPaying(TicketCheck ticketCheck, HotelBooking hotelBooking, BankCard bankCard) throws
            BankException {
        BigDecimal moneyToPay = BigDecimal.valueOf(hotelBooking.getHotelPrice());

        sendMoney(ticketCheck.getBankCard().getId(), bankCard.getId(), moneyToPay);
        ticketCheck.setPrice(moneyToPay);
        ticketCheck.setCreationDate(LocalDate.now());
        hotelBooking.setCheck(ticketCheck);
        ticketCheck.getHotelBooking().setTicketStatus(TicketStatus.PAID);
        ticketCheckRepository.save(ticketCheck);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW,
            rollbackFor = BankException.class)
    public void sendMoney(Long fromAccountId, Long toAccountId, BigDecimal amount) throws BankException {
        replenishBankCard(fromAccountId, amount.negate());
        replenishBankCard(toAccountId, amount);
    }

    public void replenishBankCard(Long bankId, BigDecimal moneyToAdd) throws BankException {
        BankCard bankCard = findById(bankId);
        bankCard.setBalance(bankCard.getBalance().add(moneyToAdd));
        bankCardRepository.save(bankCard);
    }


    public BankCardDto findBankCardDtoById(Long id) throws BankException {
        return bankCardRepository
                .findById(id)
                .map(m -> getLocalizedDTO().map(m))
                .orElseThrow(() -> new BankException("no bank card with id=" + id));
    }

    public BankCard findBankCardById(Long id) throws BankException {
        return bankCardRepository
                .findById(id)
                .orElseThrow(() -> new BankException("no bank card with id=" + id));
    }

    public LocalizedDtoMapper<BankCardDto, BankCard> getLocalizedDTO() {
        return bankCard -> BankCardDto.builder()
                .id(bankCard.getId())
                .expMonth(bankCard.getExpMonth())
                .expYear(bankCard.getExpYear())
                .ccv(bankCard.getCcv())
                .balance(bankCard.getBalance())
                .build();
    }

    public LocalizedDtoMapper<BankCard, BankCardDto> getLocalizedFromDTO() {
        return bankCard -> BankCard.builder()
                .id(bankCard.getId())
                .expMonth(bankCard.getExpMonth())
                .expYear(bankCard.getExpYear())
                .ccv(bankCard.getCcv())
                .balance(bankCard.getBalance())
                .build();
    }
}
