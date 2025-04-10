package com.backend_ecommerce.response;

import com.backend_ecommerce.domain.AccountStatus;
import com.backend_ecommerce.model.Avatar;
import com.backend_ecommerce.model.User;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.Optional;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AllUserResponse {
    private Long id;
    private String avatar;
    private String name;
    private String mobile;
    private String email;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private LocalDateTime registrationDate;
    private AccountStatus status;

    public static AllUserResponse mapFrom(User user) {

        String avatarUrl = Optional.ofNullable(user.getAvatar()).flatMap(avatars -> avatars.stream()
                        .max(Comparator.comparing(Avatar::getCreatedAt))
                        .map(Avatar::getSource))
                .orElse("/default_avatar");

        return AllUserResponse
                .builder()
                .id(user.getId())
                .avatar(avatarUrl)
                .email(user.getEmail())
                .name(user.getFullName())
                .mobile(user.getMobile())
                .registrationDate(user.getCreatedAt())
                .status(user.getAccountStatus())
                .build();
    }
}
