package ua.kpi.tef.demo_ticket.service;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import ua.kpi.tef.demo_ticket.dto.UsersDTO;
import ua.kpi.tef.demo_ticket.entity.enums.RoleType;
import ua.kpi.tef.demo_ticket.entity.User;
import ua.kpi.tef.demo_ticket.repository.UserRepository;
import java.util.Optional;

import java.util.List;


@Slf4j
@Service
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


        private User createUser(User user) {
            return User.builder()
                    .firstName(user.getFirstName())
                    .lastName(user.getLastName())
                    .username(user.getUsername())
                    .email(user.getEmail())
                    .password(new BCryptPasswordEncoder().encode(user.getPassword()))
                    .balance(1000)
                    .role(RoleType.ROLE_USER)
                    .accountNonExpired(true)
                    .accountNonLocked(true)
                    .credentialsNonExpired(true)
                    .enabled(true)
                    .build();
        }

    /*@PostConstruct
    public void init(){
        if (!userRepository.findUserByUsername("user2").isPresent()){
            userRepository.save(User.builder()
                    .firstName("ghj")
                    .lastName("hjk")
                    .email("qwfg@dfgfghj")
                    .username("user2")
                    .balance(1000)
                .password(new BCryptPasswordEncoder().encode("user2"))
                .role(RoleType.ROLE_ADMIN)
                .accountNonExpired(true)
                .accountNonLocked(true)
                .credentialsNonExpired(true)
                .enabled(true).build());
        }
    }*/

    public User findUserById(Long id) {
        return userRepository
                .findUserById(id)
                .orElseThrow(() -> new UsernameNotFoundException("user with id " + id + " not found"));
    }



    @Override
    public UserDetails loadUserByUsername(@NonNull String username) throws UsernameNotFoundException {
        return userRepository.findUserByUsername(username).orElseThrow(()
                -> new UsernameNotFoundException("user " + username + " not found!"));
    }


    public UsersDTO getAllUsers() {
        //TODO checking for an empty user list
        return new UsersDTO(userRepository.findAll());
    }

    public User findById(Long id){
        return userRepository.getOne(id);
    }

    public List<User> findAll(){
        return userRepository.findAll();
    }

    public void updateUser(User user, String email, String lastName, String firstName, String username){
        user.setEmail(email);
        user.setLastName(lastName);
        user.setFirstName(firstName);
        user.setUsername(username);
        userRepository.save(user);
    }

    /*public User updateUser(User user){
        user.setFirstName(user.getFirstName());
        return user;
    }*/

    public void deleteById(Long id){
        userRepository.deleteById(id);
    }
}