package com.example.mhbc.controller;

import com.example.mhbc.entity.BoardEntity;
import com.example.mhbc.entity.BoardGroupEntity;
import com.example.mhbc.repository.AttachmentRepository;
import com.example.mhbc.repository.BoardGroupRepository;
import com.example.mhbc.repository.BoardRepository;
import com.example.mhbc.repository.CommentsRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/board")
@AllArgsConstructor
public class BoardController {

    private AttachmentRepository attachmentRepository;
    private BoardGroupRepository boardGroupRepository;
    private BoardRepository boardRepository;
    private CommentsRepository commentsRepository;

    @RequestMapping("/gallery")
    public String gallery(){
        System.out.println(">>>>>>>>>>gallery page<<<<<<<<<<");
        return "board/gallery";
    }
    @RequestMapping("/event")
    public String event(){
        System.out.println(">>>>>>>>>>event page<<<<<<<<<<");
        BoardGroupEntity bg = new BoardGroupEntity();
        int boardType = 1;
        int groupIdx = 3;

        return "redirect:/board/eventpage?board_type=" + boardType + "&group_idx=" + groupIdx;
    }
    // '/board/eventpage' 요청을 처리하는 메소드
    @RequestMapping("/eventpage")
    public String eventPage(@RequestParam("board_type") int boardType,
                            @RequestParam("group_idx") int groupIdx,
                            Model model) {

        List<BoardEntity> boardList = boardRepository.findBoardsByGroupIdx(groupIdx);

        // model에 데이터를 추가하여 뷰에 전달
        model.addAttribute("boardType", boardType);
        model.addAttribute("groupIdx", groupIdx);
        model.addAttribute("boardList", boardList);

        return "board/eventpage";  // eventpage.html을 반환
    }
    @RequestMapping("/eventview")
    public String eventview(@RequestParam("idx") long idx,
                            @RequestParam("group_idx") int groupIdx,
                            Model model){
        System.out.println(">>>>>>>>>>eventView page<<<<<<<<<<");

        BoardEntity board = boardRepository.findByIdx(idx);

        int viewCnt = board.getViewCnt();
        board.setViewCnt(viewCnt + 1); // ← 여기서 board 객체에 다시 넣고
        boardRepository.save(board);  // ← DB에 저장해야 실제 반영됨
        System.out.println("-----------------------보드"+board.toString());

        model.addAttribute("board", board);
        model.addAttribute("groupIdx", groupIdx);

        return "board/eventview";
    }


}
