package com.cpxHelper.myPatients.domain.controller;

import com.cpxHelper.myPatients.domain.dto.ChecklistResponseDto;
import com.cpxHelper.myPatients.domain.entity.chat.Chat;
import com.cpxHelper.myPatients.domain.entity.checklist.ChecklistItem;
import com.cpxHelper.myPatients.domain.entity.checklist.ChecklistStatus;
import com.cpxHelper.myPatients.domain.entity.caseexam.CaseExam;
import com.cpxHelper.myPatients.domain.entity.patient.Patient;
import com.cpxHelper.myPatients.domain.repository.ChecklistItemRepository;
import com.cpxHelper.myPatients.domain.repository.CaseExamChecklistRepository;
import com.cpxHelper.myPatients.domain.service.ChecklistService;
import com.cpxHelper.myPatients.domain.service.ChatService;
import com.cpxHelper.myPatients.domain.service.CaseExamService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ChecklistController.class)
class ChecklistControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ChatService chatService;

    @MockBean
    private CaseExamService caseExamService;

    @MockBean
    private ChecklistService checklistService;

    @MockBean
    private ChecklistItemRepository checklistItemRepository;

    @MockBean
    private CaseExamChecklistRepository caseExamChecklistRepository;

    @Test
    void testGetChecklist() throws Exception {
        // Mock 데이터 생성
        Long caseExamId = 1L;
        CaseExam caseExam = mock(CaseExam.class);
        Patient patient = mock(Patient.class);
        List<Chat> chatHistory = List.of(
                new Chat(1L, caseExam, "USER", "GPT", "Hello", LocalDateTime.now()),
                new Chat(2L, caseExam, "GPT", "USER", "Hi", LocalDateTime.now())
        );
        List<ChecklistItem> checklistItems = List.of(
                new ChecklistItem("병력청취", "복통 발생 시기를 확인하였는가", true),
                new ChecklistItem("병력청취", "복통 지속 시간을 확인하였는가", true)
        );

        // Mock 서비스 동작 정의
        when(caseExamService.getCaseExamById(caseExamId)).thenReturn(caseExam);
        when(caseExam.getPatient()).thenReturn(patient);
        when(patient.getChiefComplaints().getId()).thenReturn(1L);
        when(chatService.getChatsByCaseExam(caseExamId)).thenReturn(chatHistory);
        when(checklistItemRepository.findActiveItemsByChiefComplaintsId(1L)).thenReturn(checklistItems);
        when(checklistService.evaluateChecklist(chatHistory, checklistItems))
                .thenReturn(Map.of(1L, 1, 2L, 0));

        // 엔드포인트 호출 및 검증
        mockMvc.perform(get("api/checklist/{caseExamId}", caseExamId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        // 서비스 호출 검증
        verify(caseExamService, times(1)).getCaseExamById(caseExamId);
        verify(chatService, times(1)).getChatsByCaseExam(caseExamId);
        verify(checklistItemRepository, times(1)).findActiveItemsByChiefComplaintsId(1L);
        verify(checklistService, times(1)).evaluateChecklist(chatHistory, checklistItems);
    }
}
