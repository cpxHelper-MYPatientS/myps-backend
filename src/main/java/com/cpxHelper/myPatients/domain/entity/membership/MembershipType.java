package com.cpxHelper.myPatients.domain.entity.membership;

import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
public enum MembershipType {
    FREE(0, 0),
    PAID(10000, 10);

    private final int price;        // 멤버십 가격
    private final int mockExamLimit; // 모의시험 이용 가능 횟수

    MembershipType(int price, int mockExamLimit) {
        this.price = price;
        this.mockExamLimit = mockExamLimit;
    }

    public static MembershipType fromPrice(int price) {
        for (MembershipType type : values()) {
            if (type.price == price) {
                return type;
            }
        }
        throw new IllegalArgumentException("No matching MembershipType for price: " + price);
    }
}
