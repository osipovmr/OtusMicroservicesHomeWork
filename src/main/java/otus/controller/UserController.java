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

@RestController
@RequestMapping(value = "/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserProfileService service;

    @GetMapping("/me")
    public ResponseEntity<?> getMyProfile(@RequestHeader Map<String, String> headers) {
        log.info(headers.toString());
        if (!headers.containsKey("x-userid")) {
            return new ResponseEntity<>("AUTHORIZATION IS REQUIRED", HttpStatus.UNAUTHORIZED);
        }
        UserProfile profile = service.findUserProfileById(Integer.valueOf(headers.get("x-userid")));
        UserProfileDto dto = getUserProfileDto(headers, profile);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @PutMapping("/me")
    public ResponseEntity<?> updateMyProfile(@RequestHeader Map<String, String> headers, @RequestBody UpdateDto dto) {
        log.info(headers.toString());
        if (!headers.containsKey("x-userid")) {
            return new ResponseEntity<>("AUTHORIZATION IS REQUIRED", HttpStatus.UNAUTHORIZED);
        }
        int id = Integer.parseInt(headers.get("x-userid"));
        UserProfile profile = service.updateUser(id, dto);
        return new ResponseEntity<>(profile, HttpStatus.OK);
    }

    private UserProfileDto getUserProfileDto(Map<String, String> headers, UserProfile profile) {
        return UserProfileDto.builder()
                .id(Integer.valueOf(headers.get("x-userid")))
                .login(headers.get("x-user"))
                .email(headers.get("x-email"))
                .firstName(headers.get("x-first-name"))
                .lastName(headers.get("x-last-name"))
                .avatar_uri(profile.getAvatarUri())
                .age(profile.getAge())
                .build();
    }
}
