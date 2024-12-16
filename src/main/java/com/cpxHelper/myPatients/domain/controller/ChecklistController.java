package com.cpxHelper.myPatients.domain.controller;

import com.cpxHelper.myPatients.domain.dto.ChecklistResponseDto;
import com.cpxHelper.myPatients.domain.entity.caseexam.CaseExam;
import com.cpxHelper.myPatients.domain.entity.chat.Chat;
import com.cpxHelper.myPatients.domain.entity.checklist.CaseExamChecklist;
import com.cpxHelper.myPatients.domain.entity.checklist.ChecklistItem;
import com.cpxHelper.myPatients.domain.entity.checklist.ChecklistStatus;
import com.cpxHelper.myPatients.domain.entity.patient.Patient;
import com.cpxHelper.myPatients.domain.repository.CaseExamChecklistRepository;
import com.cpxHelper.myPatients.domain.repository.ChecklistItemRepository;
import com.cpxHelper.myPatients.domain.service.CaseExamService;
import com.cpxHelper.myPatients.domain.service.ChatService;
import com.cpxHelper.myPatients.domain.service.ChecklistService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/checklist")
@RequiredArgsConstructor
public class ChecklistController {
    private final ChatService chatService;
    private final CaseExamService caseExamService;
    private final ChecklistService checklistService;
    private final ChecklistItemRepository checklistItemRepository;
    private final CaseExamChecklistRepository caseExamChecklistRepository;

    // 체크리스트 받아오기
    @GetMapping("/{caseExamId}")
    public ResponseEntity<ChecklistResponseDto> getChecklist(@PathVariable Long caseExamId) {
        // 시험 채팅 내역 가져오기
        List<Chat> chatHistory = chatService.getChatsByCaseExam(caseExamId);

        // CaseExam에서 Patient 및 ChiefComplaints 가져오기
        CaseExam caseExam = caseExamService.getCaseExamById(caseExamId);
        Patient patient = caseExam.getPatient();
        Long chiefComplaintsId = patient.getChiefComplaints().getId();

        // 체크리스트 불러오기
        List<ChecklistItem> checklistItems = checklistItemRepository.findActiveItemsByChiefComplaintsId(chiefComplaintsId);

        // GPT를 이용해 수행 여부 평가
        Map<Long, Integer> checklistEvaluation = checklistService.evaluateChecklist(chatHistory, checklistItems);

        // CaseExamChecklist에 저장
        checklistItems.forEach(item -> {
            Integer evaluation = checklistEvaluation.getOrDefault(item.getId(), -1); // 기본값 -1
            ChecklistStatus status;

            if (evaluation == 1) {
                status = ChecklistStatus.COMPLETE;
            } else if (evaluation == 0) {
                status = ChecklistStatus.PENDING;
            } else { // -1의 경우
                status = ChecklistStatus.NOT_APPLICABLE;
            }

            CaseExamChecklist caseExamChecklist = CaseExamChecklist.builder()
                    .caseExam(caseExam)
                    .checklistItem(item)
                    .status(status)
                    .updatedAt(LocalDateTime.now())
                    .build();

            caseExamChecklistRepository.save(caseExamChecklist);
        });

        // 수행 여부 결과 DTO로 변환
        List<ChecklistResponseDto.CheckListCategoryDto> categoryDtos = checklistItems.stream()
                .collect(Collectors.groupingBy(ChecklistItem::getCategory))
                .entrySet().stream()
                .map(entry -> ChecklistResponseDto.CheckListCategoryDto.builder()
                        .category(entry.getKey())
                        .items(entry.getValue().stream()
                                .collect(Collectors.toMap(
                                        ChecklistItem::getContent,
                                        item -> checklistEvaluation.getOrDefault(item.getId(), 0) == 1 // Integer -> Boolean 변환
                                )))
                        .build())
                .toList();

        ChecklistResponseDto responseDto = ChecklistResponseDto.builder()
                .categories(categoryDtos)
                .build();

        return ResponseEntity.ok(responseDto);
    }
}
