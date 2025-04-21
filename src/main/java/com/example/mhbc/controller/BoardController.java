package com.example.mhbc.controller;

import com.example.mhbc.dto.BoardDTO;
import com.example.mhbc.dto.CommentsDTO;
import com.example.mhbc.dto.MemberDTO;
import com.example.mhbc.entity.BoardEntity;
import com.example.mhbc.entity.BoardGroupEntity;
import com.example.mhbc.entity.CommentsEntity;
import com.example.mhbc.entity.MemberEntity;
import com.example.mhbc.repository.BoardGroupRepository;
import com.example.mhbc.repository.BoardRepository;
import com.example.mhbc.repository.CommentsRepository;
import com.example.mhbc.repository.MemberRepository;
import com.example.mhbc.service.BoardService;
import com.example.mhbc.service.CommentsService;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
@RequestMapping("/board")
@AllArgsConstructor
public class BoardController {

    private final BoardService boardService;
    private CommentsService commentsService;
    private MemberRepository memberRepository;
    private BoardGroupRepository boardGroupRepository;
    private BoardRepository boardRepository;
    private CommentsRepository commentsRepository;

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
    @RequestMapping("/personalquestion")
    public String personalquestionpage(Model model) {
        System.out.println(">>>>>>>>>>personalquestionpage page<<<<<<<<<<");

        int boardType = 2;
        int groupIdx = 6;

        return "redirect:/board/personalquestionpage?board_type="+boardType+"&group_idx="+groupIdx;
    }
    @RequestMapping("/personalquestionpage")
    public String personalquestionpage(@RequestParam("board_type") int boardType,
                                        @RequestParam("group_idx") int groupIdx,
                                         Model model) {

        List<BoardEntity> boardList = boardService.getBoardListByGroupIdx(groupIdx);
        String today = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        model.addAttribute("today", today);
        model.addAttribute("boardType", boardType);
        model.addAttribute("groupIdx", groupIdx);
        model.addAttribute("boardList", boardList);

        return "board/personalquestionpage";
    }
    @PostMapping("/pq_proc")
    public ResponseEntity<Map<String, String>> handleForm(@RequestParam("board_type") int boardType,
                                                          @RequestParam("group_idx") int groupIdx,
                                                          @ModelAttribute BoardDTO boardDTO,
                                                          @ModelAttribute MemberDTO memberDTO) {
        Map<String, String> response = new HashMap<>();
        try {
            // 서비스 호출
            boardService.processBoardForm(groupIdx, boardDTO, memberDTO);

            // 응답 데이터 준비
            response.put("message", "질문을 성공적으로 보냈습니다!");
            return ResponseEntity.ok(response);  // 성공적인 응답 반환
        } catch (IllegalArgumentException e) {
            // 예외 처리
            response.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        } catch (Exception e) {
            // 기타 예외 처리
            response.put("error", "서버에 문제가 발생했습니다. 다시 시도해주세요.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }


    /*공지사항*/
    @RequestMapping("/notice")
    public String notice(){
        System.out.println(">>>>>>>>>>notice page<<<<<<<<<<");

        int groupIdx = 1;
        int boardType = 0;

        return "redirect:/board/noticepage?board_type="+boardType+"&group_idx="+groupIdx;
    }
    @RequestMapping("/noticepage")
    public String noticepage(Model model,
                             @RequestParam("group_idx") int groupIdx,
                             @RequestParam("board_type") int boardType){
        System.out.println(">>>>>>>>>>noticepage page<<<<<<<<<<");

        List<BoardEntity> boardList = boardRepository.findBoardsByGroupIdx(groupIdx);

        model.addAttribute("boardList", boardList);
        model.addAttribute("groupIdx", groupIdx);
        model.addAttribute("boardType", boardType);
        return "/board/noticepage";
    }
    @RequestMapping("/noticeview")
    public String noticeview(Model model,
                             @RequestParam("group_idx") int groupIdx,
                             @RequestParam("board_type") int boardType,
                             @RequestParam("idx") int idx){
        System.out.println(">>>>>>>>>>noticeview page<<<<<<<<<<");

        BoardEntity board = boardRepository.findByIdx(idx);

        model.addAttribute("board", board);
        model.addAttribute("idx", idx);
        model.addAttribute("groupIdx", groupIdx);
        model.addAttribute("boardType", boardType);

        return"/board/noticeview";
    }



    /*커뮤니티*/
    @RequestMapping("/cmct")
    public String cmct(){

        int boardType = 0;
        int groupIdx = 2;
        return "redirect:/board/cmctpage?board_type="+boardType+"&group_idx="+groupIdx;
    }
    @RequestMapping("/cmctpage")
    public String cmctpage(@RequestParam("board_type") int boardType,
                           @RequestParam("group_idx") int groupIdx,
                           Model model){

        List<BoardEntity> boardList = boardRepository.findBoardsByGroupIdx(groupIdx);

        model.addAttribute("boardList", boardList);
        model.addAttribute("boardType", boardType);
        model.addAttribute("groupIdx", groupIdx);

        return"/board/cmctpage";
    }
    @RequestMapping("/cmctview")
    public String cmctview(@RequestParam("group_idx") int groupIdx,
                           @RequestParam("idx") long idx,
                           @RequestParam("member") long memberIdx,
                            Model model){

        MemberEntity member = memberRepository.findById(memberIdx).orElse(null);
        BoardEntity board = boardRepository.findByIdx(idx);
        List<CommentsEntity> commentsList = commentsRepository.findByBoard_idx(idx);

        model.addAttribute("commentsList", commentsList);
        model.addAttribute("board", board);
        model.addAttribute("member", member);
        model.addAttribute("groupIdx", groupIdx);

        return "/board/cmctview";
    }
    @PostMapping("/cmctproc")
    public String cmctproc(@ModelAttribute CommentsDTO dto, HttpSession session){
        Long memberIdx = (Long) session.getAttribute("memberIdx");
        if (memberIdx == null) {
            return "redirect:/member/login"; // 로그인 안 되어 있으면 로그인 페이지로
        }

        commentsService.saveComment(dto, memberIdx);

        return "redirect:/board/view/" + dto.getBoardIdx(); // 댓글 달린 글로 리다이렉트
    }


}


