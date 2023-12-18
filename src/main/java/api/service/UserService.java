package api.service;

import api.dto.UserDto;
import api.entity.User;
import api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final UserRepository userRepository;

    public List<User> showUsers() {
        return userRepository.findAll();
    }

    public Optional<User> showById(int id) {
        return userRepository.findById(id);
    }

    public Optional<User> showByPhone(String phone) {
        return userRepository.findUserByPhone(phone);
    }

    @Transactional
    public void save (UserDto dto) {
        User user = convertUserDto(dto);
        userRepository.save(user);
    }

    @Transactional
    public void delete(int id) {
        userRepository.deleteById(id);
    }

    @Transactional
    public void update(int id, UserDto dto) {
        User user = convertUserDto(dto);
        user.setId(id);
        userRepository.save(user);
    }

    private static User convertUserDto(UserDto userDto) {
        User user = new User();

        user.setEmail(userDto.getEmail());
        user.setName(userDto.getName());
        user.setSurname(userDto.getSurname());
        user.setPatronymic(userDto.getPatronymic());
        user.setPhone(userDto.getPhone());

        return user;
    }

}
