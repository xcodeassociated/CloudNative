package com.xcodeassociated.cloud.user.service;

import com.xcodeassociated.cloud.user.dto.UserQueryRequestDto;
import com.xcodeassociated.cloud.user.dto.UserQueryResponseDto;
import com.xcodeassociated.cloud.user.model.User;
import com.xcodeassociated.cloud.user.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Optional<UserQueryResponseDto> getUserByData(UserQueryRequestDto request) {
        Optional<User> user = Optional.ofNullable(this.getUserByUsernameAndPassword(request));
        if (user.isPresent()) {
            return Optional.of(this.userQueryResponseDtoBuilder(user.get()));
        } else {
            return Optional.empty();
        }
    }

    private User getUserByUsernameAndPassword(UserQueryRequestDto request) {
        return this.userRepository.findUserByUsernameAndPassword(request.getUsername(), request.getPassword());
    }

    private UserQueryResponseDto userQueryResponseDtoBuilder(User user) {
        return new UserQueryResponseDto(user.getUsername(), user.getPassword(), user.getRole());
    }
}
