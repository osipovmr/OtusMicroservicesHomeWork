package otus.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import otus.model.dto.UpdateDto;
import otus.model.dto.UserProfileDto;
import otus.model.entity.UserProfile;
import otus.repository.UserProfileRepository;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserProfileService {

    private final UserProfileRepository repository;

    public UserProfile updateUser(UUID uuid, UpdateDto dto) {
        UserProfile profile;
        Optional<UserProfile> optionalProfile = findUserProfileById(uuid);
        if (optionalProfile.isPresent()) {
            profile = optionalProfile.get();
            profile.setAvatarUri(dto.getAvatarUri());
            profile.setAge(dto.getAge());
        } else {
            profile = UserProfile.builder()
                    .userUUID(uuid)
                    .avatarUri(dto.getAvatarUri())
                    .age(dto.getAge())
                    .build();
        }
        log.info("UserProfile {} has been updated.", profile);
        return repository.save(profile);
    }

    private Optional<UserProfile> findUserProfileById(UUID uuid) {
        return repository.findById(uuid);
    }

    public UserProfileDto getUserInfo(Map<String, String> headers) {
        UserProfileDto dto = getProfileByHeaders(headers);
        Optional<UserProfile> optionalProfile = findUserProfileById(UUID.fromString(headers.get("x-userid")));
        if (optionalProfile.isPresent()) {
            dto.setAvatarUri(optionalProfile.get().getAvatarUri());
            dto.setAge(optionalProfile.get().getAge());
        }
        return dto;
    }

    private UserProfileDto getProfileByHeaders(Map<String, String> headers) {
        return UserProfileDto.builder()
                .userUUID(UUID.fromString(headers.get("x-userid")))
                .login(headers.get("x-user"))
                .email(headers.get("x-email"))
                .firstName(headers.get("x-first-name"))
                .lastName(headers.get("x-last-name"))
                .build();
    }
}
