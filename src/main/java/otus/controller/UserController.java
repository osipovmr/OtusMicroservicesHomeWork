package otus.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import otus.model.dto.UpdateDto;
import otus.model.dto.UserProfileDto;
import otus.model.entity.UserProfile;
import otus.service.UserProfileService;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping(value = "/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserProfileService service;

    @GetMapping("/me")
    public ResponseEntity<?> getMyProfile(@RequestHeader Map<String, String> headers) {
        if (!headers.containsKey("x-userid")) {
            return new ResponseEntity<>("AUTHORIZATION IS REQUIRED", HttpStatus.UNAUTHORIZED);
        }
        UserProfileDto dto = service.getUserInfo(headers);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @PutMapping("/me")
    public ResponseEntity<?> updateMyProfile(@RequestHeader Map<String, String> headers, @RequestBody UpdateDto dto) {
        log.info(headers.toString());
        if (!headers.containsKey("x-userid")) {
            return new ResponseEntity<>("AUTHORIZATION IS REQUIRED", HttpStatus.UNAUTHORIZED);
        }
        UUID uuid = UUID.fromString(headers.get("x-userid"));
        UserProfile profile = service.updateUser(uuid, dto);
        return new ResponseEntity<>(profile, HttpStatus.OK);
    }
}
