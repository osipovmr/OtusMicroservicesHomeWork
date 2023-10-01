package otus.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import otus.model.dto.UserDto;
import otus.model.dto.UserInfoDto;
import otus.model.dto.UserUpdateDto;
import service.UserService;

@RestController("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/{userId}")
    public ResponseEntity<UserInfoDto> getUserById(@PathVariable int userId) {
        return ResponseEntity.ok(userService.getUserById(userId));
    }

    @PostMapping("/")
    public ResponseEntity<Void> createUser(@Valid @RequestBody UserDto dto) {
        userService.createUser(dto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUserById(@PathVariable int userId) {
        userService.deleteUserById(userId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/{userId}")
    public ResponseEntity<Void> updateUserById(@PathVariable int userId, @Valid @RequestBody UserUpdateDto dto) {
        userService.updateUser(userId, dto);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
