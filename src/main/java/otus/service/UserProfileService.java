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

@Service
@RequiredArgsConstructor
@Slf4j
public class UserProfileService {

    private final UserProfileRepository repository;

    public UserProfile updateUser(int id, UpdateDto dto) {
        UserProfile profile;
        Optional<UserProfile> optionalProfile = findUserProfileById(id);
        if (optionalProfile.isPresent()) {
            profile = optionalProfile.get();
            profile.setAvatarUri(dto.getAvatarUri());
            profile.setAge(dto.getAge());
        } else {
            profile = UserProfile.builder()
                    .id(id)
                    .avatarUri(dto.getAvatarUri())
                    .age(dto.getAge())
                    .build();
        }
        log.info("UserProfile {} has been updated.", profile);
        return repository.save(profile);
    }

    private Optional<UserProfile> findUserProfileById(Integer id) {
        return repository.findById(id);
    }

    public UserProfileDto getUserInfo(Map<String, String> headers) {
        UserProfileDto dto = getProfileByHeaders(headers);
        Optional<UserProfile> optionalProfile = findUserProfileById(Integer.valueOf(headers.get("x-userid")));
        if (optionalProfile.isPresent()) {
            dto.setAvatarUri(optionalProfile.get().getAvatarUri());
            dto.setAge(optionalProfile.get().getAge());
        }
        return dto;
    }

    private UserProfileDto getProfileByHeaders(Map<String, String> headers) {
        return UserProfileDto.builder()
                .id(Integer.valueOf(headers.get("x-userid")))
                .login(headers.get("x-user"))
                .email(headers.get("x-email"))
                .firstName(headers.get("x-first-name"))
                .lastName(headers.get("x-last-name"))
                .build();
    }
}
