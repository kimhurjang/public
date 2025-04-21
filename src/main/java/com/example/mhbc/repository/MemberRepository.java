package com.example.mhbc.repository;

import com.example.mhbc.entity.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<MemberEntity, Long> {
    public MemberEntity findByUserid(String userid);

    @Query("SELECT m FROM MemberEntity m WHERE LOWER(m.name) = LOWER(:name) AND LOWER(m.email) = LOWER(:email)")
    public Optional<MemberEntity> findByNameAndEmail(@Param("name") String name, @Param("email") String email);

    public Optional<MemberEntity> findByEmail(String email);


}
