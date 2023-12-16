package org.taxiservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import org.taxiservice.model.User;

public interface UserRepository extends JpaRepository<User, Long> {

    User findByUsername(String username);

    User findByRegistrationKey(String key);

    User findByResetKey(String resetKey);
}
