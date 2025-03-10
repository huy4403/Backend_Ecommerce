package com.backend_ecommerce.response;

import com.backend_ecommerce.model.Avatar;
import com.backend_ecommerce.model.User;
import lombok.*;

import java.util.Comparator;
import java.util.Optional;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ProfileUserResponse {
    private String fullName;
    private String email;
    private String mobile;
    private String avatar;

    public static ProfileUserResponse mapFromUser(User user) {

        String avatarUrl = Optional.ofNullable(user.getAvatar()).flatMap(avatars -> avatars.stream()
                        .max(Comparator.comparing(Avatar::getCreatedAt))
                        .map(Avatar::getSource))
                .orElse(null);

        return ProfileUserResponse.builder()
                .fullName(user.getFullName())
                .email(user.getEmail())
                .mobile(user.getMobile())
                .avatar(avatarUrl)
                .build();
    }
}
