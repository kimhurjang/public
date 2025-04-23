package com.example.mhbc.repository;

import com.example.mhbc.entity.AttachmentEntity;
import com.example.mhbc.entity.BoardEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AttachmentRepository extends JpaRepository<AttachmentEntity, Long> {
    public AttachmentEntity findByBoard (BoardEntity Board);

    public AttachmentEntity findByIdx(long idx);
}
