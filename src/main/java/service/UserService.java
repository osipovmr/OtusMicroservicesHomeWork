package service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import otus.exception.UserNotFoundException;
import otus.model.dto.UserDto;
import otus.model.dto.UserInfoDto;
import otus.model.dto.UserUpdateDto;
import otus.model.entity.User;
import otus.repository.UserRepository;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository repository;
    private final ModelMapper mapper;

    public UserInfoDto getUserById(int id) {
        User user = findUserById(id);
        return mapper.map(user, UserInfoDto.class);
    }

    public void createUser(UserDto dto) {
        User user = mapper.map(dto, User.class);
        repository.save(user);
    }

    public void deleteUserById(int userId) {
        repository.deleteById(userId);
    }

    public void updateUser(int userId, UserUpdateDto dto) {
        User user = findUserById(userId);
        updateFromDto(dto, user);
        repository.save(user);
    }

    private User findUserById(int id) {
        return repository.findById(id).orElseThrow(() ->
                new UserNotFoundException(String.format("Usr with id %s does not exist", id)));
    }

    private void updateFromDto(UserUpdateDto dto, User user) {
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setPhone(dto.getPhone());
        user.setEmail(dto.getEmail());
    }
}
