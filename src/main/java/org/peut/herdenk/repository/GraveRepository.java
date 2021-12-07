package org.peut.herdenk.repository;

import org.peut.herdenk.model.Grave;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GraveRepository extends JpaRepository<Grave, Long> {
    Optional<Grave> findGraveByOccupantFullName(String occupantFullName);
}
