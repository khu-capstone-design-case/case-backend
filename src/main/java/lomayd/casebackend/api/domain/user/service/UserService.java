package lomayd.casebackend.api.domain.user.service;

import lomayd.casebackend.api.domain.user.User;
import lomayd.casebackend.api.domain.user.dto.UserRequestDto;
import lomayd.casebackend.api.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UserRepository userRepository;

    public void joinUser(UserRequestDto.UserJoin data) {
        Optional<User> duplicateId = userRepository.findById(data.getId());
        Optional<User> duplicateName = userRepository.findByName(data.getName());

        if (duplicateId.isPresent())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "이미 존재하는 아이디입니다.");
        else {
            User user = User.builder()
                    .id(data.getId())
                    .password(data.getPassword())
                    .name(data.getName())
                    .build();

            userRepository.save(user);
        }
    }

    public Boolean loginUser(UserRequestDto.UserLogin data) {
        User user = userRepository.findById(data.getId()).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "존재하지 않는 아이디 입니다"));

        if (!user.getPassword().equals(data.getPassword())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "비밀번호가 일치하지 않습니다.");
        } else {
            return true;
        }
    }

    public void storeRefreshToken(User user, String refreshToken) {
        user.setRefreshToken(refreshToken);
        userRepository.save(user);
    }

    public User getUserById(String id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "해당하는 유저가 없습니다"));
    }

    public User getUserByRefreshToken(String refreshToken) {
        return userRepository.findByRefreshToken(refreshToken)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "해당하는 유저가 없습니다"));
    }

    public void logout(User user) {
        user.setRefreshToken(null);
        userRepository.save(user);
    }
}