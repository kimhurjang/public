package com.example.mhbc.controller;

import com.example.mhbc.dto.CommentsDTO;
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
@AllArgsConstructor
public class MainController {

    private AttachmentRepository attachmentRepository;
    private BoardGroupRepository boardGroupRepository;
    private BoardRepository boardRepository;
    private CommentsRepository commentsRepository;

    @RequestMapping({"/" , "/home"})
    public String index(Model model){
        System.out.println(">>>>>>>>>>index page<<<<<<<<<<");
        model.addAttribute("title", "만화방초");//페이지별 타이틀 설정.(디폴트값==기본 제목)
        return "index";
    }

    @RequestMapping("/admin")
    public String admin(){
        System.out.println(">>>>>>>>>>admin page<<<<<<<<<<");
        return "admin";
    }
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

        return "redirect:board/eventpage?board_type=" + boardType + "&group_idx=" + groupIdx;
    }
    // '/board/eventpage' 요청을 처리하는 메소드
    @RequestMapping("/board/eventpage")
    public String eventPage(@RequestParam("board_type") int boardType,
                            @RequestParam("group_idx") int groupIdx,
                            Model model) {
        List<BoardEntity> boardList = boardRepository.findBoardsByGroupIdx(groupIdx);
        System.out.println("----------------------board"+boardList);

        // model에 데이터를 추가하여 뷰에 전달
        model.addAttribute("boardType", boardType);
        model.addAttribute("groupIdx", groupIdx);
        model.addAttribute("boardList", boardList);

        return "board/eventpage";  // eventpage.html을 반환
    }




}
