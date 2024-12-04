package com.cpxHelper.myPatients.domain.controller;

import com.cpxHelper.myPatients.domain.dto.ChatRequestDto;
import com.cpxHelper.myPatients.domain.dto.ChatResponseDto;
import com.cpxHelper.myPatients.domain.entity.caseexam.CaseExam;
import com.cpxHelper.myPatients.domain.entity.chat.Chat;
import com.cpxHelper.myPatients.domain.entity.member.Member;
import com.cpxHelper.myPatients.domain.entity.member.MemberRole;
import com.cpxHelper.myPatients.domain.entity.patient.Patient;
import com.cpxHelper.myPatients.domain.service.CaseExamService;
import com.cpxHelper.myPatients.domain.service.ChatService;
import com.cpxHelper.myPatients.domain.service.OpenAIService;
import com.cpxHelper.myPatients.domain.utils.PromptLoader;
import com.cpxHelper.myPatients.domain.utils.PromptProcessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


import java.util.List;

@WebMvcTest(ChatController.class)
class ChatControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ChatService chatService;

    @MockBean
    private PromptLoader promptLoader;

    @MockBean
    private PromptProcessor promptProcessor;

    @MockBean
    private OpenAIService openAiService;

    @MockBean
    private CaseExamService caseExamService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void handleChat_shouldReturnChatResponseDto() throws Exception {
        // Mock PromptLoader
        Mockito.when(promptLoader.loadPromptTemplate()).thenReturn("This is a test template");

        // Mock PromptProcessor
        Mockito.when(promptProcessor.processTemplate(any(), any()))
                .thenReturn("Processed prompt");

        // Mock OpenAIService
        Mockito.when(openAiService.callGptApi(any()))
                .thenReturn("Mock GPT Response");

        // Mock Member and Patient
        Member mockMember = Member.builder()
                .email("test@example.com")
                .password("password")
                .memberRole(MemberRole.ROLE_USER)
                .build();

        Patient mockPatient = Patient.builder()
                .build();

        // Mock CaseExam
        CaseExam mockCaseExam = CaseExam.builder()
                .member(mockMember)
                .patient(mockPatient)
                .createdAt(LocalDateTime.now())
                .build();
        ReflectionTestUtils.setField(mockCaseExam, "id", 456L);

        // Mock Chat 리스트 생성
        List<Chat> chatHistory = List.of(
                Chat.builder()
                        .sender("USER")
                        .message("환자가 배가 아프다고 말했어요.")
                        .createdAt(LocalDateTime.now())
                        .build(),
                Chat.builder()
                        .sender("GPT")
                        .message("배가 아픈 원인은 다양합니다. 추가적인 정보가 필요합니다.")
                        .createdAt(LocalDateTime.now())
                        .build()
        );

        // Mock Service Calls
        Mockito.when(caseExamService.getCaseExamById(456L)).thenReturn(mockCaseExam);
        Mockito.when(chatService.processUserMessage(eq(mockCaseExam), eq("환자가 배가 아프다고 말했어요.")))
                .thenReturn("배가 아픈 원인은 다양합니다. 추가적인 정보가 필요합니다.");
        Mockito.when(chatService.getChatsByCaseExam(456L)).thenReturn(chatHistory);

        // Perform request
        ChatRequestDto requestDto = new ChatRequestDto();
        requestDto.setPatientId(123L);
        requestDto.setMessage("환자가 배가 아프다고 말했어요.");

        mockMvc.perform(post("/api/v1/chat/message")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.caseExamId").value(456L))
                .andExpect(jsonPath("$.messages[0].sender").value("USER"))
                .andExpect(jsonPath("$.messages[0].content").value("환자가 배가 아프다고 말했어요."))
                .andExpect(jsonPath("$.messages[1].sender").value("GPT"))
                .andExpect(jsonPath("$.messages[1].content").value("배가 아픈 원인은 다양합니다. 추가적인 정보가 필요합니다."));
    }
}