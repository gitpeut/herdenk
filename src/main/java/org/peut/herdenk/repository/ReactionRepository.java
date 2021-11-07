package org.peut.herdenk.repository;

import org.peut.herdenk.model.Reaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReactionRepository extends JpaRepository<Reaction, Long> {
    List<Reaction> findAllByGraveId( Long graveId);
    List<Reaction> findAllByUserId( Long userId);
    List<Reaction> findReactionsByGraveIdAndType( Long graveId, String type);

}