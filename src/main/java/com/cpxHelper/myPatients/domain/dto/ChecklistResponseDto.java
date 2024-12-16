package com.cpxHelper.myPatients.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChecklistResponseDto {

    private List<CheckListCategoryDto> categories; // 카테고리 리스트

    // 체크리스트의 카테고리별 항목과 시행 여부를 위한 DTO
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CheckListCategoryDto {
        private String category; // 카테고리 이름
        private Map<String, Boolean> items; // 항목 이름과 시행 여부 (key: 항목, value: 시행 여부)
    }
}
