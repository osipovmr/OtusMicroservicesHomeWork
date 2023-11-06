package otus.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import otus.exception.EntityNotFoundException;
import otus.model.dto.UpdateDto;
import otus.model.entity.UserProfile;
import otus.repository.UserProfileRepository;


@Service
@RequiredArgsConstructor
@Slf4j
public class UserProfileService {

    private final UserProfileRepository repository;

    public UserProfile updateUser(int id, UpdateDto dto) {
        UserProfile profile;
        try {
            profile = findUserProfileById(id);
            profile.setAvatarUri(dto.getAvatarUri());
            profile.setAge(dto.getAge());
        } catch (EntityNotFoundException e) {
            profile = UserProfile.builder()
                    .id(id)
                    .avatarUri(dto.getAvatarUri())
                    .age(dto.getAge())
                    .build();
        }
        log.info("UserProfile {} has been updated.", profile);
        return repository.save(profile);
    }

    public UserProfile findUserProfileById(Integer id) {
        return repository.findById(id).orElseThrow(() ->
                new EntityNotFoundException(String.format("UserProfile with id %s does not exist.", id)));
    }
}
