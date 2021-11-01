package org.peut.herdenk.repository;

import org.peut.herdenk.model.Authority;
import org.peut.herdenk.model.AuthorityKey;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AuthorityRepository extends JpaRepository<Authority, AuthorityKey> {
    List<Authority> findAllByUserId(Long userId );
    List<Authority> findAllByGraveId( Long graveId );

    @Query("SELECT a FROM Authority a WHERE a.graveId = ?1 AND a.authority = 'OWNER'")
    Optional<List<Authority>> findOwnerByGraveId( Long graveId);

    @Query("SELECT a FROM Authority a WHERE a.userId = ?1 OR a.authority = 'PUBLIC'")
    List<Authority> findAccessibleByUserId( Long userId );

    @Query("SELECT p FROM Authority p WHERE p.graveId = ?1 AND p.userId = 0")
    Optional<Authority>  findPubliclyAccessibleByGraveId( Long graveId );

    @Query("SELECT a FROM Authority a WHERE a.userId = ?1 AND a.graveId = ?2")
    Optional<Authority> findGraveAccessibleByUserId( Long userId, Long graveId );



}
