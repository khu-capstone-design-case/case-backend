package lomayd.casebackend.api.domain.user.service;

import lomayd.casebackend.api.domain.user.User;
import lomayd.casebackend.api.domain.user.dto.UserRequestDto;
import lomayd.casebackend.api.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public void joinUser(UserRequestDto.UserJoin data) {
        Optional<User> duplicateId = userRepository.findById(data.getId());
        Optional<User> duplicateName = userRepository.findByName(data.getName());

        if (duplicateId.isPresent())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "이미 존재하는 아이디입니다.");
        else if (duplicateName.isPresent())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "이미 존재하는 이름입니다.");
        else {
            User user = User.builder()
                    .id(data.getId())
                    .password(data.getPassword())
                    .name(data.getName())
                    .build();

            userRepository.save(user);
        }
    }


}
