package com.xcodeassociated.cloud.user.service;

import com.xcodeassociated.cloud.user.dto.UserQueryDto;
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

    public Optional<UserQueryDto> getUserByData(UserQueryDto request) {
        Optional<User> user = Optional.ofNullable(this.getUserByUsernameAndPasswordAndRole(request));
        if (user.isPresent()) {
            return Optional.of(this.dtoBuilder(user.get()));
        } else {
            return Optional.empty();
        }
    }

    private User getUserByUsernameAndPasswordAndRole(UserQueryDto request) {
        return this.userRepository.findUserByUsernameAndPasswordAndRole
            (request.getUsername(), request.getPassword(), request.getRole());
    }

    private UserQueryDto dtoBuilder(User user) {
        return new UserQueryDto(user.getUsername(), user.getPassword(), user.getRole());
    }
}
