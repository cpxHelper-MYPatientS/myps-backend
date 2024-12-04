package com.cpxHelper.myPatients.domain.utils;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@TestPropertySource(properties = {
        "prompt.path=classpath:prompts/case_prompt.txt"
})
class PromptLoaderTest {

    @Autowired
    private PromptLoader promptLoader;

    @Test
    void testLoadPromptTemplate() {
        String prompt = promptLoader.loadPromptTemplate();
        assertNotNull(prompt);
        System.out.println(prompt);
    }
}