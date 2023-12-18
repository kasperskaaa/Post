package api.controller;

import api.dto.UserDto;
import api.entity.User;
import api.service.UserService;
import api.utils.exceptions.EntityNotFoundException;
import api.utils.exceptions.PhoneAlreadyUseException;
import api.utils.validations.BindingResultParser;
import api.utils.validations.CreateValidation;
import api.utils.validations.UpdateValidation;
import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:9091")
public class UserController {

    private final UserService userService;

    @GetMapping
    public ResponseEntity<?> index() {
        return new ResponseEntity<>(userService.showUsers(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> show(@PathVariable int id) {
       User user = checkUser(id);
       return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @GetMapping("/by_phone")
    public ResponseEntity<?> showByPhone(@RequestParam String phone) {
        Optional<User> user = userService.showByPhone(phone);
        if (user.isEmpty()) {
            throw new EntityNotFoundException("User not found");
        }

        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> create(@Validated(CreateValidation.class) @RequestBody UserDto dto, BindingResult bindingResult) {
        checkValidation(bindingResult);
        checkPhone(dto.getPhone());
        userService.save(dto);
        return new ResponseEntity<>("User " + dto.getSurname() + " " + dto.getName() + " was created",HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable int id) {
        User user = checkUser(id);
        userService.delete(id);
        return new ResponseEntity<>("User " + user.getSurname() + " " + user.getName() + " was deleted", HttpStatus.OK);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable int id, @Validated(UpdateValidation.class) @RequestBody UserDto dto,
                                    BindingResult bindingResult) {
        User pachedUser = checkUser(id);
        checkValidation(bindingResult);
        checkPhone(dto.getPhone());

        dto.setEmail(dto.getEmail() == null ? pachedUser.getEmail() : dto.getEmail());
        dto.setName(dto.getName() == null ? pachedUser.getName() : dto.getName());
        dto.setSurname(dto.getSurname() == null ? pachedUser.getSurname() : dto.getSurname());
        dto.setPatronymic(dto.getPatronymic() == null ? pachedUser.getPatronymic() : dto.getPatronymic());
        dto.setPhone(dto.getPhone() == null ? pachedUser.getPhone() : dto.getPhone());

        userService.update(id, dto);
        return new ResponseEntity<>("User " + dto.getSurname() + " " + dto.getName() + " was updated", HttpStatus.OK);
    }

    private User checkUser(int id) {
        Optional<User> user = userService.showById(id);
        if (user.isEmpty()) {
            throw new EntityNotFoundException("User not found");
        }

        return user.get();
    }

    private void checkValidation (BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new ValidationException(BindingResultParser.parse(bindingResult));
        }
    }

    private void checkPhone(String phone) {
        if (phone != null && userService.showByPhone(phone).isPresent()) {
            throw new PhoneAlreadyUseException();
        }
    }

}
