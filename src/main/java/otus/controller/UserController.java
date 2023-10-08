package otus.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import otus.model.dto.UserDto;
import otus.model.dto.UserInfoDto;
import otus.model.dto.UserUpdateDto;
import otus.service.UserService;

@RestController
@RequestMapping(value = "/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/{id}")
    public ResponseEntity<UserInfoDto> getUserById(@PathVariable("id") int userId) {
        return ResponseEntity.ok(userService.getUserById(userId));
    }

    @PostMapping("/add")
    public ResponseEntity<Void> createUser(@Valid @RequestBody UserDto dto) {
        userService.createUser(dto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUserById(@PathVariable("id") int userId) {
        userService.deleteUserById(userId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateUserById(@PathVariable("id") int userId, @Valid @RequestBody UserUpdateDto dto) {
        userService.updateUser(userId, dto);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
