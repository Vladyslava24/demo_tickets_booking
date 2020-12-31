package ua.kpi.tef.demo_ticket.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import ua.kpi.tef.demo_ticket.dto.UsersDTO;
import ua.kpi.tef.demo_ticket.entity.enums.RoleType;
import ua.kpi.tef.demo_ticket.entity.User;
import ua.kpi.tef.demo_ticket.service.RegistrationFormService;
import ua.kpi.tef.demo_ticket.service.UserService;

@Slf4j
@RestController
@RequestMapping(value = "/")
public class RegistrationFormController {
    private final RegistrationFormService registrationFormService;
    private final UserService userService;

    @Autowired
    public RegistrationFormController(RegistrationFormService registrationFormService,
                                      UserService userService) {
        this.registrationFormService = registrationFormService;
        this.userService = userService;
    }

    @ResponseStatus( HttpStatus.CREATED)
    @PostMapping(value = "reg")
    public void regFormController(User user) {
        registrationFormService.saveNewUser(User.builder()
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .username(user.getUsername())
                .password(new BCryptPasswordEncoder().encode(user.getPassword()))
                .role(RoleType.ROLE_USER)
                .enabled(true)
                .credentialsNonExpired(true)
                .accountNonLocked(true)
                .accountNonExpired(true)
                .build());
        log.info("{}", user);
    }

    @RequestMapping(value = "user", method = RequestMethod.GET)
    public UsersDTO getAllUser(){
        //log.info("{}",userService.getAllUsers());
        return userService.getAllUsers();
    }
}
