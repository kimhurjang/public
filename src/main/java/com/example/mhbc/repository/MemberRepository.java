package com.example.mhbc.repository;

import com.example.mhbc.entity.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<MemberEntity, Long> {
    boolean existsByUserid(String userid);

    Optional<MemberEntity> findByEmail(String email);

    Optional<MemberEntity> findByUserid(String userid);
}
