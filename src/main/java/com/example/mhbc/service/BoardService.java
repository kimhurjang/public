package com.example.mhbc.service;

import com.example.mhbc.entity.BoardEntity;
import com.example.mhbc.repository.BoardRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class BoardService {

    private final BoardRepository boardRepository;

    public BoardService(BoardRepository boardRepository) {
        this.boardRepository = boardRepository;
    }

    // 게시글 조회 및 조회수 증가
    @Transactional
    public BoardEntity getBoardByIdx(long idx) {
        BoardEntity board = boardRepository.findByIdx(idx);

        int viewCnt =  0;

        if(board.getViewCnt() != null){
            viewCnt = board.getViewCnt();
        }

        if (board != null) {
            board.setViewCnt(viewCnt + 1);
            boardRepository.save(board);  // 조회수 증가 반영
        }
        return board;
    }

    // 그룹별 게시글 목록 조회
    public List<BoardEntity> getBoardListByGroupIdx(int groupIdx) {

        return boardRepository.findBoardsByGroupIdx(groupIdx);
    }

        //같은 제목의 게시물 조회(자주 묻는 질문)
        public List<BoardEntity> getBoardListByTitle(String title){
            return boardRepository.findByTitle(title);
        }
}
