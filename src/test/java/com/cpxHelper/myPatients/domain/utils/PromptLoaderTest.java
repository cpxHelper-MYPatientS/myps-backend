package com.cpxHelper.myPatients.domain.utils;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@TestPropertySource(properties = {
        "prompt.base-path=prompts/"
})
class PromptLoaderTest {

    @Autowired
    private PromptLoader promptLoader;

    @Test
    void testLoadPromptTemplate_case() {
        // "case" 프롬프트 파일 로드
        String casePrompt = promptLoader.loadPromptTemplate("case");
        assertNotNull(casePrompt, "Case prompt should not be null");
        assertTrue(casePrompt.contains("This is a sample case prompt."), "Case prompt should contain expected text");
        System.out.println("Case Prompt: \n" + casePrompt);
    }

    @Test
    void testLoadPromptTemplate_checklist() {
        // "checklist" 프롬프트 파일 로드
        String checklistPrompt = promptLoader.loadPromptTemplate("checklist");
        assertNotNull(checklistPrompt, "Checklist prompt should not be null");
        assertTrue(checklistPrompt.contains("This is a sample checklist prompt."), "Checklist prompt should contain expected text");
        System.out.println("Checklist Prompt: \n" + checklistPrompt);
    }
}
