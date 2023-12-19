package com.example.board.dto;

import com.example.board.domain.UserAccount;

import java.time.LocalDateTime;

public record UserAccountDto(
        String userId,
        String userPassword,
        String email,
        String nickname,
        String memo,
        LocalDateTime createdAt,
        String createdBy,
        LocalDateTime modifiedAt,
        String modifiedBy) {

    /* 저장을 할때, modified_at/by , created_at/by 는 자동으로 입력 될 값. */
    public static UserAccountDto of( String userId,
                                    String userPassword,
                                    String email,
                                    String nickname,
                                    String memo){
        return new UserAccountDto(userId, userPassword, email, nickname, memo, null, null, null, null);
    }

    public static UserAccountDto of(String userId,
                                    String userPassword,
                                    String email,
                                    String nickname,
                                    String memo,
                                    LocalDateTime createdAt,
                                    String createdBy,
                                    LocalDateTime modifiedAt,
                                    String modifiedBy){
        return new UserAccountDto(userId, userPassword, email, nickname, memo, createdAt, createdBy, modifiedAt, modifiedBy);
    }

    /* Entity를 Dto로 변경
    * Dto만 Article의 존재를 알면 된다. Dto의 변경은 Article 에 영향을 안 끼침.
    * Article 의 변경은 Dto에 영향을 끼침.
    * */
    public static UserAccountDto from(UserAccount entity){
        return new UserAccountDto(entity.getUserId(),
                                  entity.getUserPassword(),
                                  entity.getEmail(),
                                  entity.getNickname(),
                                  entity.getMemo(),
                                  entity.getCreatedAt(),
                                  entity.getCreatedBy(),
                                  entity.getModifiedAt(),
                                  entity.getModifiedBy());
    }

    public UserAccount toEntity(){

        return UserAccount.of(userId, userPassword, email, nickname, memo);
    }
}
