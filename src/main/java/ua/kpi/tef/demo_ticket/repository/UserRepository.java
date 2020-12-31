package ua.kpi.tef.demo_ticket.repository;

import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ua.kpi.tef.demo_ticket.entity.User;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findUserByUsername(@NonNull String username);

    Optional<User> findUserById(Long id);

    @Query(nativeQuery = true, value ="update user set email = :email, last_name = :lastName, " +
            "username = :username where id = :id")
    Optional<User> updateUser(@Param("email") String email,
                              @Param("lastName") String lastName,
                              @Param("username") String userName,
                              @Param("id") Long userId);
}