package com.example.mhbc.repository;

import com.example.mhbc.entity.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<MemberEntity, Long> {
    public MemberEntity findByUserid(String userid);
    Optional<MemberEntity> findByNameAndEmail(String name, String email);

}
