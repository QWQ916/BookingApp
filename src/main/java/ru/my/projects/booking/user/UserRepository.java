package ru.my.projects.booking.user;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {

    List<User> findUsersByFullNameContainsIgnoreCase(String fullName);

    List<User> findUsersByEmailContainsIgnoreCase(String email);

    List<User> findUsersByFullNameContainsIgnoreCaseAndEmailContainsIgnoreCase(String fullName, String email);
}
