package com.example.mhbc.controller;

import com.example.mhbc.dto.BoardDTO;
import com.example.mhbc.dto.MemberDTO;
import com.example.mhbc.entity.BoardEntity;
import com.example.mhbc.entity.MemberEntity;
import com.example.mhbc.repository.MemberRepository;
import com.example.mhbc.service.BoardService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/board")
@AllArgsConstructor
public class BoardController {

    private final BoardService boardService;
    private MemberRepository memberRepository;

    /*갤러리*/
    @RequestMapping("/gallery")
    public String gallery() {
        System.out.println(">>>>>>>>>>gallery page<<<<<<<<<<");
        int boardType = 1;
        int groupIdx = 4;
        return "redirect:/board/gallerypage?board_type=" + boardType + "&group_idx=" + groupIdx;
    }
    @RequestMapping("/gallerypage")
    public String gallerypage(@RequestParam("board_type") int boardType,
                            @RequestParam("group_idx") int groupIdx,
                            Model model) {

        List<BoardEntity> boardList = boardService.getBoardListByGroupIdx(groupIdx);

        model.addAttribute("boardType", boardType);
        model.addAttribute("groupIdx", groupIdx);
        model.addAttribute("boardList", boardList);

        return "board/gallerypage";
    }
    @RequestMapping("/galleryview")
    public String galleryview(@RequestParam("idx") long idx,
                            @RequestParam("group_idx") int groupIdx,
                            Model model) {

        BoardEntity board = boardService.getBoardByIdx(idx);
        model.addAttribute("board", board);
        model.addAttribute("groupIdx", groupIdx);

        return "board/galleryview";
    }




    /*이벤트*/
    @RequestMapping("/event")
    public String event() {
        System.out.println(">>>>>>>>>>event page<<<<<<<<<<");
        int boardType = 1;
        int groupIdx = 3;
        return "redirect:/board/eventpage?board_type="+boardType+"&group_idx="+groupIdx;
    }
    @RequestMapping("/eventpage")
    public String eventPage(@RequestParam("board_type") int boardType,
                            @RequestParam("group_idx") int groupIdx,
                            Model model) {

        List<BoardEntity> boardList = boardService.getBoardListByGroupIdx(groupIdx);

        model.addAttribute("boardType", boardType);
        model.addAttribute("groupIdx", groupIdx);
        model.addAttribute("boardList", boardList);

        return "board/eventpage";
    }
    @RequestMapping("/eventview")
    public String eventview(@RequestParam("idx") long idx,
                            @RequestParam("group_idx") int groupIdx,
                            Model model) {

        BoardEntity board = boardService.getBoardByIdx(idx);
        model.addAttribute("board", board);
        model.addAttribute("groupIdx", groupIdx);

        return "board/eventview";
    }




    /*자주 질문*/
    @RequestMapping("/oftenquestion")
    public String oftenquestion() {
        System.out.println(">>>>>>>>>>oftenquestion page<<<<<<<<<<");
        int boardType = 2;
        int groupIdx = 5;
        return "redirect:/board/oftenquestionpage?board_type="+boardType+"&group_idx="+groupIdx;
    }
    @RequestMapping("/oftenquestionpage")
    public String oftenquestionpage(@RequestParam("board_type") int boardType,
                                    @RequestParam("group_idx") int groupIdx,
                                    Model model) {

        List<BoardEntity> boardList = boardService.getBoardListByGroupIdx(groupIdx);

        model.addAttribute("boardType", boardType);
        model.addAttribute("groupIdx", groupIdx);
        model.addAttribute("boardList", boardList);

        return "board/oftenquestionpage";
    }
    @RequestMapping("/oftenquestionview")
    public String oftenquestionview(@RequestParam("idx") long idx,
                                    @RequestParam("title") String title,
                                    @RequestParam("group_idx") int groupIdx,
                                    Model model) {

        List<BoardEntity> boardList = boardService.getBoardListByTitle(title);

        model.addAttribute("boardList", boardList);
        model.addAttribute("groupIdx", groupIdx);
        model.addAttribute("idx", idx);
        model.addAttribute("title", title);

        return "board/oftenquestionview";
    }


    /*1대1문의*/
    @RequestMapping("/personalquestionpage")
    public String personalquestionpage(Model model) {
        System.out.println(">>>>>>>>>>personalquestionpage page<<<<<<<<<<");

        BoardEntity board = new BoardEntity();

        model.addAttribute("board", board);

        return "/board/personalquestionpage";
    }
    @PostMapping("/pq_proc")
    public String handleForm(BoardDTO boardDTO , MemberDTO memberDTO) {

        Optional<MemberEntity> optionalMember = memberRepository.findByNameAndEmail(memberDTO.getName(), memberDTO.getEmail());
        System.out.println("제목: " + boardDTO.getTitle());
        System.out.println("내용: " + boardDTO.getContent());
        System.out.println("작성 날짜: " + boardDTO.getCreatedAt());

        // 처리 로직


        return "redirect:/thank-you";
    }



}
