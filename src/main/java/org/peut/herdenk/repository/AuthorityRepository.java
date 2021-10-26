package org.peut.herdenk.repository;

import org.peut.herdenk.model.Authority;
import org.peut.herdenk.model.AuthorityKey;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorityRepository extends JpaRepository<Authority, AuthorityKey> {
}
