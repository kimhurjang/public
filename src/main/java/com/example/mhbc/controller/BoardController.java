package com.example.mhbc.controller;

import com.example.mhbc.dto.BoardDTO;
import com.example.mhbc.dto.CommentsDTO;
import com.example.mhbc.dto.MemberDTO;
import com.example.mhbc.entity.*;
import com.example.mhbc.repository.*;
import com.example.mhbc.service.BoardService;
import com.example.mhbc.service.CommentsService;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

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
    private AttachmentRepository attachmentRepository;

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.setDisallowedFields("attachment");
    }


    /*갤러리*/
    @RequestMapping("/gallery")
    public String gallery() {
        System.out.println(">>>>>>>>>>gallery page<<<<<<<<<<");
        long boardType = 1;
        long groupIdx = 4;
        return "redirect:/board/gallery_page?board_type=" + boardType + "&group_idx=" + groupIdx;
    }
    @RequestMapping("/gallery_page")
    public String gallery_page(@RequestParam("board_type") long boardType,
                               @RequestParam("group_idx") long groupIdx,
                               Model model) {

        List<BoardEntity> boardList = boardService.getBoardListByGroupIdx(groupIdx);

        for (BoardEntity board : boardList) {
            AttachmentEntity attachment = attachmentRepository.findByBoard(board);
            board.setAttachment(attachment); // BoardEntity에 Attachment 필드 추가
        }
        model.addAttribute("boardList", boardList);


        model.addAttribute("boardType", boardType);
        model.addAttribute("groupIdx", groupIdx);
        model.addAttribute("boardList", boardList);

        return "board/gallery_page";
    }
    @RequestMapping("/gallery_view")
    public String gallery_iew(@RequestParam("idx") long idx,
                              @RequestParam("group_idx") long groupIdx,
                              Model model) {

        BoardEntity board = boardService.getBoardByIdx(idx);
        model.addAttribute("board", board);
        model.addAttribute("groupIdx", groupIdx);

        return "board/gallery_view";
    }
    @RequestMapping("/gallery_write")
    public String gallery_write(@RequestParam("group_idx") long groupIdx,
                                @RequestParam("board_type") long boardType,
                                Model model){

        List<BoardEntity> boardList = boardService.getBoardListByGroupIdx(groupIdx);

        model.addAttribute("boardType", boardType);
        model.addAttribute("groupIdx", groupIdx);
        model.addAttribute("boardList", boardList);

        return "board/gallery_write";
    }
    @PostMapping("/gallery_proc")
    public String gallery_proc(@ModelAttribute BoardEntity board,
                               @ModelAttribute AttachmentEntity attachmentEntity,
                               @RequestParam("file") MultipartFile file,
                               @RequestParam("group_idx") long groupIdx,
                               @RequestParam("board_type") long boardType,
                               RedirectAttributes redirectAttributes) {

        attachmentEntity.setCreatedAt(new Date());
        board.setCreatedAt(new Date());
        attachmentEntity.setBoard(board); // 첨부파일이 어떤 게시글에 속하는지 연결

        if (file != null && !file.isEmpty()) {
            try {
                // 업로드 디렉토리 설정
                String uploadDir = "D:/SpringProject/data";
                File directory = new File(uploadDir);
                if (!directory.exists()) {
                    directory.mkdirs();
                }

                // 고유 파일명 생성
                String uuidFileName = UUID.randomUUID().toString() + "." + file.getOriginalFilename();
                String savedPath = uploadDir + File.separator + uuidFileName;
                File destination = new File(savedPath);

                // 파일 저장
                file.transferTo(destination);

                // 엔티티에 저장할 정보 설정
                attachmentEntity.setFilePath("/data/" + uuidFileName); // DB에는 상대 경로 저장
                attachmentEntity.setFileName(file.getOriginalFilename()); // 원본 파일명 저장

                // 게시판 그룹 설정
                BoardGroupEntity group = new BoardGroupEntity();
                group.setGroupIdx(groupIdx);
                board.setGroup(group);

            } catch (IOException e) {
                e.printStackTrace();
                redirectAttributes.addFlashAttribute("error", "파일 업로드 실패");
                return "redirect:/board/gallery_page?board_type=" + boardType + "&group_idx=" + groupIdx;
            }
        }

        // 게시글과 파일 정보를 데이터베이스에 저장
        boardRepository.save(board);
        attachmentRepository.save(attachmentEntity);

        // 게시글 저장 및 리다이렉트 로직 추가되어야 함 (예: boardRepository.save(board), 등)
        return "redirect:/board/gallery_page?board_type=" + boardType + "&group_idx=" + groupIdx;
    }



    /*이벤트*/
    @RequestMapping("/event")
    public String event() {
        System.out.println(">>>>>>>>>>event page<<<<<<<<<<");
        long boardType = 1;
        long groupIdx = 3;
        return "redirect:/board/event_page?board_type="+boardType+"&group_idx="+groupIdx;
    }
    @RequestMapping("/event_page")
    public String event_Page(@RequestParam("board_type") long boardType,
                             @RequestParam("group_idx") long groupIdx,
                             Model model) {

        List<BoardEntity> boardList = boardService.getBoardListByGroupIdx(groupIdx);

        model.addAttribute("boardType", boardType);
        model.addAttribute("groupIdx", groupIdx);
        model.addAttribute("boardList", boardList);

        return "board/event_page";
    }
    @RequestMapping("/event_view")
    public String event_view(@RequestParam("idx") long idx,
                             @RequestParam("group_idx") long groupIdx,
                             Model model) {

        BoardEntity board = boardService.getBoardByIdx(idx);
        model.addAttribute("board", board);
        model.addAttribute("groupIdx", groupIdx);

        return "board/event_view";
    }
    @RequestMapping("/event_write")
    public String event_write(@RequestParam("group_idx") long groupIdx,
                              @RequestParam("board_type") long boardType,
                              Model model){

        List<BoardEntity> boardList = boardService.getBoardListByGroupIdx(groupIdx);

        model.addAttribute("boardList",boardList);
        model.addAttribute("groupIdx",groupIdx);
        model.addAttribute("boardType",boardType);

        return "board/event_write";
    }
    @PostMapping("/event_proc")
    public String event_proc(@ModelAttribute BoardEntity board,
                             @ModelAttribute AttachmentEntity attachmentEntity,
                             @RequestParam("file") MultipartFile file,
                             @RequestParam("group_idx") long groupIdx,
                             @RequestParam("board_type") long boardType,
                             RedirectAttributes redirectAttributes){
        attachmentEntity.setCreatedAt(new Date());
        board.setCreatedAt(new Date());
        attachmentEntity.setBoard(board); // 첨부파일이 어떤 게시글에 속하는지 연결

        if (file != null && !file.isEmpty()) {
            try {
                // 업로드 디렉토리 설정
                String uploadDir = "D:/SpringProject/data";
                File directory = new File(uploadDir);
                if (!directory.exists()) {
                    directory.mkdirs();
                }

                // 고유 파일명 생성
                String uuidFileName = UUID.randomUUID().toString() + "." + file.getOriginalFilename();
                String savedPath = uploadDir + File.separator + uuidFileName;
                File destination = new File(savedPath);

                // 파일 저장
                file.transferTo(destination);

                // 엔티티에 저장할 정보 설정
                attachmentEntity.setFilePath("/data/" + uuidFileName); // DB에는 상대 경로 저장
                attachmentEntity.setFileName(file.getOriginalFilename()); // 원본 파일명 저장

                // 게시판 그룹 설정
                BoardGroupEntity group = new BoardGroupEntity();
                group.setGroupIdx(groupIdx);
                board.setGroup(group);

            } catch (IOException e) {
                e.printStackTrace();
                redirectAttributes.addFlashAttribute("error", "파일 업로드 실패");
                return "redirect:/board/event_page?board_type=" + boardType + "&group_idx=" + groupIdx;
            }
        }

        // 게시글과 파일 정보를 데이터베이스에 저장
        boardRepository.save(board);
        attachmentRepository.save(attachmentEntity);

        // 게시글 저장 및 리다이렉트 로직 추가되어야 함 (예: boardRepository.save(board), 등)
        return "redirect:/board/gallery_page?board_type=" + boardType + "&group_idx=" + groupIdx;
    }




    /*자주 질문*/
    @RequestMapping("/oftenquestion")
    public String oftenquestion() {
        System.out.println(">>>>>>>>>>oftenquestion page<<<<<<<<<<");
        long boardType = 2;
        long groupIdx = 5;
        return "redirect:/board/oftenquestion_page?board_type="+boardType+"&group_idx="+groupIdx;
    }
    @RequestMapping("/oftenquestion_page")
    public String oftenquestion_page(@RequestParam("board_type") long boardType,
                                     @RequestParam("group_idx") long groupIdx,
                                     Model model) {

        List<BoardEntity> boardList = boardService.getBoardListByGroupIdx(groupIdx);

        model.addAttribute("boardType", boardType);
        model.addAttribute("groupIdx", groupIdx);
        model.addAttribute("boardList", boardList);

        return "board/oftenquestion_page";
    }
    @RequestMapping("/oftenquestion_view")
    public String oftenquestion_view(@RequestParam("idx") long idx,
                                     @RequestParam("title") String title,
                                     @RequestParam("group_idx") long groupIdx,
                                     Model model) {

        List<BoardEntity> boardList = boardService.getBoardListByTitle(title);

        model.addAttribute("boardList", boardList);
        model.addAttribute("groupIdx", groupIdx);
        model.addAttribute("idx", idx);
        model.addAttribute("title", title);

        return "board/oftenquestion_view";
    }


    /*1대1문의*/
    @RequestMapping("/personalquestion")
    public String personalquestion_page(Model model) {
        System.out.println(">>>>>>>>>>personalquestionpage page<<<<<<<<<<");

        long boardType = 2;
        long groupIdx = 6;

        return "redirect:/board/personalquestion_page?board_type="+boardType+"&group_idx="+groupIdx;
    }
    @RequestMapping("/personalquestion_page")
    public String personalquestion_page(@RequestParam("board_type") long boardType,
                                        @RequestParam("group_idx") long groupIdx,
                                        Model model) {

        List<BoardEntity> boardList = boardService.getBoardListByGroupIdx(groupIdx);
        String today = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        model.addAttribute("today", today);
        model.addAttribute("boardType", boardType);
        model.addAttribute("groupIdx", groupIdx);
        model.addAttribute("boardList", boardList);

        return "board/personalquestion_page";
    }
    @PostMapping("/pq_proc")
    public ResponseEntity<Map<String, String>> handleForm(@RequestParam("board_type") long boardType,
                                                          @RequestParam("group_idx") long groupIdx,
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

        long groupIdx = 1;
        long boardType = 0;

        return "redirect:/board/notice_page?board_type="+boardType+"&group_idx="+groupIdx;
    }
    @RequestMapping("/notice_page")
    public String notice_page(Model model,
                              @RequestParam("group_idx") long groupIdx,
                              @RequestParam("board_type") long boardType){
        System.out.println(">>>>>>>>>>noticepage page<<<<<<<<<<");

        List<BoardEntity> boardList = boardRepository.findBoardsByGroupIdx(groupIdx);

        model.addAttribute("boardList", boardList);
        model.addAttribute("groupIdx", groupIdx);
        model.addAttribute("boardType", boardType);
        return "/board/notice_page";
    }
    @RequestMapping("/notice_view")
    public String notice_view(Model model,
                              @RequestParam("group_idx") long groupIdx,
                              @RequestParam("board_type") long boardType,
                              @RequestParam("idx") long idx){
        System.out.println(">>>>>>>>>>noticeview page<<<<<<<<<<");

        BoardEntity board = boardRepository.findByIdx(idx);

        model.addAttribute("board", board);
        model.addAttribute("idx", idx);
        model.addAttribute("groupIdx", groupIdx);
        model.addAttribute("boardType", boardType);

        return"/board/notice_view";
    }



    /*커뮤니티*/
    @RequestMapping("/cmct")
    public String cmct(){

        long boardType = 0;
        long groupIdx = 2;
        return "redirect:/board/cmct_page?board_type="+boardType+"&group_idx="+groupIdx;
    }
    @RequestMapping("/cmct_page")
    public String cmct_page(@RequestParam("board_type") long boardType,
                            @RequestParam("group_idx") long groupIdx,
                            Model model){

        List<BoardEntity> boardList = boardRepository.findBoardsByGroupIdx(groupIdx);

        model.addAttribute("boardList", boardList);
        model.addAttribute("boardType", boardType);
        model.addAttribute("groupIdx", groupIdx);

        return"/board/cmct_page";
    }
    @RequestMapping("/cmct_view")
    public String cmct_view(@RequestParam("group_idx") long groupIdx,
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

        return "/board/cmct_view";
    }
    @RequestMapping("/cmct_write")
    public String cmct_write(Model model,
                             @RequestParam("group_idx")long groupIdx,
                             @RequestParam("board_type")long boardType){
        //로그인 구현 후 수정
        String today = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        model.addAttribute("boardType", boardType);
        model.addAttribute("groupIdx", groupIdx);
        model.addAttribute("today", today);
        return "board/cmct_write";
    }
    @PostMapping("/cmct_write_proc")
    public String cmct_write_proc(@ModelAttribute BoardEntity board,
                                  @RequestParam("attachment") MultipartFile attachment,
                                  @RequestParam("group_idx") long groupIdx,
                                  @RequestParam("board_type") long boardType,
                                  RedirectAttributes redirectAttributes,
                                  Model model) {

        try {
            boardService.saveBoardWithAttachment(board, attachment, groupIdx);
        } catch (IOException e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "파일 업로드 실패");
            return "redirect:/board/cmct_write";
        }

        model.addAttribute("groupIdx", groupIdx);
        model.addAttribute("boardType", boardType);

        return "redirect:/board/cmct_page?board_type=" + boardType + "&group_idx=" + groupIdx;
    }




    /*파일 다운로드*/
    @GetMapping("/file/download/{idx}")
    public ResponseEntity<Resource> filedownload(@PathVariable("idx") Long idx){

        AttachmentEntity attachment = attachmentRepository.findByIdx(idx);

        if (attachment.getFilePath() == null || attachment.getFilePath().isEmpty()) {
            throw new RuntimeException("파일 경로가 존재하지 않습니다: " + attachment);
        }

        Resource resource = new FileSystemResource(attachment.getFilePath());
        String encodedFilename;
        try {
            encodedFilename = URLEncoder.encode(attachment.getFileName(), "UTF-8").replaceAll("\\+", "%20");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("파일명 인코딩 중 오류 발생", e);
        }

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + encodedFilename + "\"")
                .body(resource);
    }



}