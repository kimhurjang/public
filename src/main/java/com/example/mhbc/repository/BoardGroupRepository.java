package com.example.mhbc.repository;

import com.example.mhbc.entity.BoardEntity;
import com.example.mhbc.entity.BoardGroupEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BoardGroupRepository extends JpaRepository<BoardGroupEntity, Long> {


    //Page<BoardEntity> findByIdx
}
