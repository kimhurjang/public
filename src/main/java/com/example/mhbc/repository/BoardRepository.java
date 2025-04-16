package com.example.mhbc.repository;

import com.example.mhbc.entity.BoardEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BoardRepository extends JpaRepository<BoardEntity, Long> {
    public BoardEntity findByGroup_GroupIdx(int groupIdx);
    @Query("SELECT b FROM BoardEntity b WHERE b.group.groupIdx = :groupIdx")
    List<BoardEntity> findBoardsByGroupIdx(@Param("groupIdx") int groupIdx);
}
