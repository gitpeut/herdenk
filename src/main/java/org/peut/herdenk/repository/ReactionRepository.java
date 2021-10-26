package org.peut.herdenk.repository;

import org.peut.herdenk.model.Reaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReactionRepository extends JpaRepository<Reaction, Long> {
}