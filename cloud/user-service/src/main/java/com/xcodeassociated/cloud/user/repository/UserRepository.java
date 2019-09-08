package com.xcodeassociated.cloud.user.repository;

import com.xcodeassociated.cloud.user.model.User;
import com.xcodeassociated.cloud.user.model.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    User findUserByUsernameAndPassword(String username, String password);

}
