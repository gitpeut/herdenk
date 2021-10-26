package org.peut.herdenk.repository;

import org.peut.herdenk.model.Grave;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GraveRepository extends JpaRepository<Grave, Long> {
}
