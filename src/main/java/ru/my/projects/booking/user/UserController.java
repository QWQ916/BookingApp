package ru.my.projects.booking.user;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/0/users")
public class UserController {
    private final UserService service;

    public UserController(UserService service){
        this.service = service;
    }

    @GetMapping
    public List<UserDTO> getUsers(@RequestParam(required = false, name = "fullName") String fullName, @RequestParam(required = false) String email){
        return service.getAll(fullName, email).stream().map(UserDTO::transfer).toList();
    }

    @GetMapping("/{id}")
    public UserDTO getUser(@PathVariable Long id){
        return UserDTO.transfer(service.getOne(id));
    }

    @PostMapping
    public ResponseEntity<UserDTO> createUser(@RequestBody UserCreateDTO rq){
        return ResponseEntity.status(HttpStatus.CREATED).body(UserDTO.transfer(service.create(rq.fullName(), rq.email(), rq.birthDate())));
    }

    @PutMapping("/{id}")
    public UserDTO putUser(@PathVariable Long id, @RequestBody UserCreateDTO rq){
        return UserDTO.transfer(service.put(id, rq.fullName(), rq.email(), rq.birthDate(), rq.balance()));
    }

    @PatchMapping("/{id}")
    public UserDTO patchUser(@PathVariable Long id, @RequestBody UserPatchDTO rq){
        return UserDTO.transfer(service.patch(id, rq.fullName(), rq.email(), rq.birthDate(), rq.balance()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id){
        service.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
