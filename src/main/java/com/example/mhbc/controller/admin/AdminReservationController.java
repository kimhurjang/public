package com.example.mhbc.controller.admin;

import com.example.mhbc.dto.ReservationDTO;
import com.example.mhbc.dto.ReservationSearchCondition;
import com.example.mhbc.entity.HallEntity;
import com.example.mhbc.repository.HallRepository;
import com.example.mhbc.repository.ReservationRepository;
import com.example.mhbc.service.admin.AdminReservationService;
import com.example.mhbc.util.Utility;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * AdminReservationController
 * - 관리자 예약 현황 컨트롤러
 */
@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/reservation")
public class AdminReservationController {

  private final AdminReservationService adminReservationService;
  private final ReservationRepository reservationRepository;
  private final HallRepository hallRepository;

  // 예약 리스트 출력
  @GetMapping("/list")
  public String reservationList(
    @RequestParam(value = "page", defaultValue = "1") int page,
    @RequestParam(required = false) String searchType,
    @RequestParam(required = false) String keyword,
    @RequestParam(required = false) String startDate,
    @RequestParam(required = false) String endDate,
    @RequestParam(required = false) List<String> status,
    @RequestParam(required = false) List<String> hallIdx,
    Model model
  ) {
    model.addAttribute("webtitle", "예약관리 | 예약현황");
    model.addAttribute("hallList", hallRepository.findAll()); //홀 리스트

    // 조건 없이 진입했는지 여부
    boolean isEmptySearch = (searchType == null && keyword == null &&
      startDate == null && endDate == null &&
      status == null && hallIdx == null);

    int itemsPerPage = 10;
    int groupSize = 5;
    String link = "/admin/reservation/list";
    Pageable pageable = PageRequest.of(page - 1, itemsPerPage, Sort.Direction.DESC, "idx");

    if (isEmptySearch) {
      // 전체 리스트 출력
      Page<ReservationDTO> paging = adminReservationService.findAll(pageable);
      int totalCount = (int) paging.getTotalElements();
      Utility.Pagination pagination = new Utility.Pagination(page, itemsPerPage, totalCount, groupSize, link);

      model.addAttribute("paging", paging);
      model.addAttribute("pagination", pagination);
      model.addAttribute("link", link);

      // 검색조건 비움
      model.addAttribute("searchType", null);
      model.addAttribute("keyword", null);
      model.addAttribute("startDate", null);
      model.addAttribute("endDate", null);
      model.addAttribute("status", Collections.emptyList());
      model.addAttribute("hallIdx", Collections.emptyList());

      return "admin/reservation/list";
    }

    // 검색 조건 있는 경우
    ReservationSearchCondition condition = new ReservationSearchCondition();
    condition.setSearchType(searchType);
    condition.setKeyword(keyword);
    condition.setStartDate(startDate);
    condition.setEndDate(endDate);
    condition.setStatus(status);
    condition.setHallIdx(hallIdx);

    Page<ReservationDTO> paging = adminReservationService.findByCondition(condition, pageable);
    int totalCount = (int) paging.getTotalElements();
    Utility.Pagination pagination = new Utility.Pagination(page, itemsPerPage, totalCount, groupSize, link);

    model.addAttribute("paging", paging);
    model.addAttribute("pagination", pagination);
    model.addAttribute("link", link);

    // 기존 조건 유지
    model.addAttribute("searchType", searchType);
    model.addAttribute("keyword", keyword);
    model.addAttribute("startDate", startDate);
    model.addAttribute("endDate", endDate);
    model.addAttribute("status", status != null ? status : Collections.emptyList());
    model.addAttribute("hallIdx", hallIdx != null ? hallIdx : Collections.emptyList());

    return "admin/reservation/list";
  }


  // 예약 상태 변경 저장 처리 (기존 방식)
  @PostMapping("/status")
  public String updateReservationStatus(@RequestParam(value = "statuses") List<String> statuses) {
    adminReservationService.updateStatuses(statuses);
    return "redirect:/admin/reservation/list";
  }

  // 예약 상태 변경 저장 처리 (Ajax 방식)
  @PostMapping("/status/ajax")
  @ResponseBody
  public ResponseEntity<String> updateStatusAjax(@RequestBody List<Map<String, String>> updates) {
    try {
      adminReservationService.updateStatusesByAjax(updates);
      return ResponseEntity.ok("상태가 성공적으로 변경되었습니다.");
    } catch (IllegalArgumentException e) {
      return ResponseEntity.badRequest().body(e.getMessage()); // ← 오직 메시지만 전달
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("알 수 없는 오류가 발생했습니다.");
    }
  }

  // 예약 상세보기
  @GetMapping("/view")
  public String reservationView(@RequestParam Long idx, Model model) {
    model.addAttribute("reservation", adminReservationService.findById(idx));
    return "admin/reservation/view";
  }
  /*
  @GetMapping("/edit")
  public String editReservation(@RequestParam Long idx, Model model) {
    ReservationDTO dto = adminReservationService.findById(idx);
    model.addAttribute("reservationDTO", dto);
    model.addAttribute("hallList", hallRepository.findAll());
    model.addAttribute("editMode", true); // 수정모드 플래그
    return "admin/reservation/form";
  }

  @PostMapping("/edit")
  public String editReservationForm(@RequestParam("idx") Long idx, Model model) {
    System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> eidt");
    //if (idx == null) throw new IllegalArgumentException("예약 번호가 없습니다.");
    ReservationDTO dto = adminReservationService.findById(idx);
    System.out.println(dto.toString());
    //model.addAttribute("reservation", dto);
    //model.addAttribute("hallList", hallRepository.findAll());
    return "admin/reservation/edit";
  } */

  /*
  // 📌 GET 요청으로 수정 페이지 열기
  @GetMapping("/edit")
  public String editReservationForm(@RequestParam Long idx, Model model) {
    ReservationDTO reservation = adminReservationService.findById(idx);
    if (reservation == null) {
      model.addAttribute("popupError", "해당 예약이 존재하지 않습니다.");
      return "redirect:/admin/reservation/list";
    }

    List<HallEntity> halls = hallRepository.findAll();
    int hallCapacity = 250;
    if (reservation.getHallIdx() != null) {
      HallEntity hall = halls.stream()
              .filter(h -> h.getIdx().equals(reservation.getHallIdx()))
              .findFirst()
              .orElse(null);
      if (hall != null) hallCapacity = hall.getCapacity();
    }

    model.addAttribute("reservation", reservation);
    model.addAttribute("halls", halls);
    model.addAttribute("hallCapacity", hallCapacity);

    return "admin/reservation/edit"; // 경로에 맞게 수정
  }

  // POST 요청으로 수정 데이터 저장
  @PostMapping("/edit")
  public String updateReservation(@ModelAttribute ReservationDTO dto,
                                  RedirectAttributes redirectAttributes,
                                  Model model) {
    try {
      adminReservationService.updateReservation(dto);
      return "redirect:/admin/reservation/view?idx=" + dto.getIdx();
    } catch (IllegalArgumentException e) {
      // 에러 메시지를 flash 속성으로 전달
      redirectAttributes.addFlashAttribute("popupError", e.getMessage());
      return "redirect:/admin/reservation/edit?idx=" + dto.getIdx();
    }
  }
*/
  // 📌 GET 요청으로 수정 페이지 열기
  @GetMapping("/edit")
  public String editReservationPage(@RequestParam Long idx, Model model) {
    ReservationDTO dto = adminReservationService.findById(idx);
    if (dto == null) {
      throw new IllegalArgumentException("예약 정보가 존재하지 않습니다.");
    }

    model.addAttribute("reservation", dto);
    model.addAttribute("halls", hallRepository.findAll()); // 홀 전체 리스트

    return "admin/reservation/edit"; // 수정 페이지로 이동
  }

  // 📌 POST 요청으로 수정 데이터 저장
  @PostMapping("/edit")
  public String updateReservation(@ModelAttribute ReservationDTO dto, RedirectAttributes redirectAttributes) {
    try {
      // 1. 서비스에서 업데이트 시도
      adminReservationService.updateReservation(dto);
      // 2. 성공 시 → 상세보기로 리다이렉트
      return "redirect:/admin/reservation/view?idx=" + dto.getIdx();
    } catch (IllegalArgumentException e) {
      // 3. 실패 시 → 에러 메시지를 flash에 담아 수정페이지로 리다이렉트
      redirectAttributes.addFlashAttribute("popupError", e.getMessage());
      return "redirect:/admin/reservation/edit?idx=" + dto.getIdx();
    }
  }

  // 관리자 메모 수정
  @PostMapping("/memoUpdate")
  public String updateAdminNote(@RequestParam("idx") Long idx,
                                @RequestParam("adminNote") String adminNote,
                                @ModelAttribute("loginId") String loginId) {

    adminReservationService.updateAdminNote(idx, adminNote, loginId);
    return "redirect:/admin/reservation/view?idx=" + idx;
  }

}
