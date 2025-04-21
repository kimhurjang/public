package com.example.mhbc.repository;

import com.example.mhbc.entity.CommentsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentsRepository extends JpaRepository<CommentsEntity, Long> {
    public List<CommentsEntity> findByBoard_idx (long boardIdx);
}
