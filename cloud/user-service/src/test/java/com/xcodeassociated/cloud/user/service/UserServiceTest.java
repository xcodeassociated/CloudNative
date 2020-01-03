package com.xcodeassociated.cloud.user.service;

import com.xcodeassociated.cloud.user.dto.UserQueryRequestDto;
import com.xcodeassociated.cloud.user.dto.UserQueryResponseDto;
import com.xcodeassociated.cloud.user.model.User;
import com.xcodeassociated.cloud.user.model.UserRole;
import com.xcodeassociated.cloud.user.repository.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;

@ActiveProfiles("test")
@DataJpaTest
@RunWith(SpringRunner.class)
public class UserServiceTest {

    @Autowired
    private UserRepository userRepository;
    private UserService userService = null;
    private List<User> testUsers = new ArrayList<>();

    @Before
    public void setUp() {
        this.userService = new UserService(this.userRepository);

        final User admin = new User(null, "admin", "password", UserRole.ROLE_ADMIN);
        final User user = new User(null, "user", "password", UserRole.ROLE_USER);
        this.testUsers.addAll(List.of(admin, user));

        this.userRepository.saveAll(this.testUsers);
    }

    @Test
    public void dbSize_Test() {
        assertEquals(2, this.userRepository.findAll().size());
    }

    @Test
    public void dbData_Test() {
        assertEquals(this.testUsers, this.userRepository.findAll());
    }

    @Test
    public void getUserByUsernameAndPassword_AdminRole_Test() {
        // given
        String username = "admin", password = "password";
        UserQueryRequestDto dto = new UserQueryRequestDto(username, password);

        // when
        Optional<UserQueryResponseDto> responseDto = this.userService.getUserByData(dto);

        // then
        assertTrue(responseDto.isPresent());
        assertEquals(dto.getUsername(), responseDto.get().getUsername());
        assertEquals(dto.getPassword(), responseDto.get().getPassword());
        assertEquals(UserRole.ROLE_ADMIN, responseDto.get().getRoles()
            .stream().findFirst().get());
    }

    @Test
    public void getUserByUsernameAndPassword_UserRole_Test() {
        // given
        String username = "user", password = "password";
        UserQueryRequestDto dto = new UserQueryRequestDto(username, password);

        // when
        Optional<UserQueryResponseDto> responseDto = this.userService.getUserByData(dto);

        // then
        assertTrue(responseDto.isPresent());
        assertEquals(dto.getUsername(), responseDto.get().getUsername());
        assertEquals(dto.getPassword(), responseDto.get().getPassword());
        assertEquals(UserRole.ROLE_USER, responseDto.get().getRoles()
            .stream().findFirst().get());
    }

    @Test
    public void getUserByUsernameAndPassword_UserRoleIncorrect_Test() {
        // given
        String username = "user", password = "incorrect_password";
        UserQueryRequestDto dto = new UserQueryRequestDto(username, password);

        // when
        Optional<UserQueryResponseDto> responseDto = this.userService.getUserByData(dto);

        // then
        assertFalse(responseDto.isPresent());
    }

}
