package org.peut.herdenk.repository;

import org.peut.herdenk.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}

