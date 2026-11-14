package ru.my.projects.booking.user;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.my.projects.booking.common.exception.ResourceNotFoundException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepo;

    public UserService(UserRepository userRepo){
        this.userRepo = userRepo;
    }

    @Transactional(readOnly = true)
    public List<User> getAll(String fullName, String email){
        if (fullName != null && email != null){
            return userRepo.findUsersByFullNameContainsIgnoreCaseAndEmailContainsIgnoreCase(fullName.trim(), email.trim());
        }
        else if (fullName != null){
            return userRepo.findUsersByFullNameContainsIgnoreCase(fullName.trim());
        }
        else if (email != null){
            return userRepo.findUsersByEmailContainsIgnoreCase(email.trim());
        }
        else{
            return userRepo.findAll();
        }
    }

    @Transactional(readOnly = true)
    public User getOne(Long id){
        return userRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Пользователя с такими данными нет в базе!"));
    }

    @Transactional
    public User create(String fullname, String email, LocalDate birthDate){
        User newUser =  new User(fullname, email, birthDate);
        userRepo.save(newUser);
        return newUser;
    }

    @Transactional
    public User put(Long id, String fullname, String email, LocalDate birthDate, BigDecimal balance){
        User user = userRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Пользователя с такими данными нет в базе!"));

        user.setEmail(email);
        user.setFullName(fullname);
        user.setBirthDate(birthDate);
        user.setBalance(balance);

        userRepo.save(user);
        return user;
    }

    @Transactional
    public User patch(Long id, String fullname, String email, LocalDate birthDate, BigDecimal balance){
        User user = userRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Пользователя с такими данными нет в базе!"));

        if (email != null) user.setEmail(email);
        if (fullname != null) user.setFullName(fullname);
        if (birthDate != null) user.setBirthDate(birthDate);
        if (balance != null) user.setBalance(balance);

        userRepo.save(user);
        return user;
    }

    @Transactional
    public void delete(Long id){
        User user = userRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Пользователя с такими данными нет в базе!"));
        userRepo.delete(user);
    }
}
