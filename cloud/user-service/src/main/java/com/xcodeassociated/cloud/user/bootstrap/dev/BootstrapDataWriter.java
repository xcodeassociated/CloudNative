package com.xcodeassociated.cloud.user.bootstrap.dev;

import com.xcodeassociated.cloud.user.model.User;
import com.xcodeassociated.cloud.user.model.UserRole;
import com.xcodeassociated.cloud.user.repository.UserRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Log4j2
@Component
@Profile({"dev", "local-dev"})
public class BootstrapDataWriter implements ApplicationRunner {
    private final UserRepository userRepository;

    public BootstrapDataWriter(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void run(ApplicationArguments args) {
        log.info("DataWriter Bean Context Started");
        List<User> users = Arrays.asList(
            new User(null, "admin", "dQNjUIMorJb8Ubj2+wVGYp6eAeYkdekqAcnYp+aRq5w=", UserRole.ROLE_ADMIN),
            new User(null, "user", "cBrlgyL2GI2GINuLUUwgojITuIufFycpLG4490dhGtY=", UserRole.ROLE_USER));

        this.userRepository.deleteAll();
        this.userRepository.saveAll(users);
        this.userRepository.findAll().forEach(log::debug);
    }
}
