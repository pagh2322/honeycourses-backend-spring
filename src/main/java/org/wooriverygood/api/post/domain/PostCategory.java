package org.wooriverygood.api.post.domain;

import lombok.Getter;

@Getter
public enum PostCategory {
    FREE("자유"), QUESTION("질문"), TRADE("중고거래"), OFFER("구인");

    private final String value;

    PostCategory(String value) {
        this.value = value;
    }

    public boolean equalsTo(String value) {
        return this.value.equals(value);
    }

    public static PostCategory parse(String value) {
        return switch (value) {
            case "자유" -> PostCategory.FREE;
            case "질문" -> PostCategory.QUESTION;
            case "중고거래" -> PostCategory.TRADE;
            default -> PostCategory.OFFER;
        };
    }
}