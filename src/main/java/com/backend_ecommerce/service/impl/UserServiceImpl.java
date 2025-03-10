package com.backend_ecommerce.service.impl;

import com.backend_ecommerce.domain.AccountStatus;
import com.backend_ecommerce.dto.UserPrincipal;
import com.backend_ecommerce.exception.UserException;
import com.backend_ecommerce.model.Avatar;
import com.backend_ecommerce.model.User;
import com.backend_ecommerce.repository.AvatarRepository;
import com.backend_ecommerce.repository.UserRepository;
import com.backend_ecommerce.request.UpdatePasswordRequest;
import com.backend_ecommerce.request.UpdateUserRequest;
import com.backend_ecommerce.response.ProfileUserResponse;
import com.backend_ecommerce.service.CloudinaryService;
import com.backend_ecommerce.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final AvatarRepository avatarRepository;
    private final PasswordEncoder passwordEncoder;
    private final CloudinaryService cloudinaryService;


    @Override
    public String uploadAvatar(MultipartFile file) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();

        String source = cloudinaryService.uploadImage(file);

        Avatar avatar = new Avatar();
        avatar.setUser(userPrincipal.user());
        avatar.setSource(source);
        avatar.setCreatedAt(LocalDateTime.now());

        avatarRepository.save(avatar);

        return source;
    }

    @Override
    public String updateUserHandler(UpdateUserRequest req) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();

        User user = userRepository.findById(userPrincipal.user().getId()).orElseThrow(() -> new UserException("User does not exist"));

        if(userRepository.existsByMobileAndIdNot(req.getMobile(), user.getId()))
            throw new UserException("Mobile phone number is already exist with another user");

        user.setFullName(req.getFullName());
        user.setMobile(req.getMobile());

        try {
            userRepository.save(user);
            return "Update successfully";
        } catch (Exception e) {
            throw new UserException("Something went wrong...");
        }
    }

    @Override
    public String updatePasswordHandler(UpdatePasswordRequest req) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();

        User existUser = userRepository.findById(userPrincipal.user().getId()).orElseThrow(() -> new UserException("User does not exist"));


        if(!passwordEncoder.matches(req.getOldPassword(), existUser.getPassword()))
            throw new UserException("Old password does not match");

        if(!req.getNewPassword().equals(req.getRePassword()))
            throw new UserException("New password does not match");

        existUser.setPassword(passwordEncoder.encode(req.getNewPassword()));

        userRepository.save(existUser);

        return "Password changed";
    }

    @Override
    public ProfileUserResponse getProfileHandler() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();

        User user = userRepository.findById(userPrincipal.user().getId()).orElseThrow(
                () -> new UserException("User does not exist"));

        return ProfileUserResponse.mapFromUser(user);
    }

    @Override
    public String deleteUserById(Long id) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        if(userPrincipal.user().getId().equals(id)) {
            throw new UserException("Can't delete yourself");
        }

        User user = userRepository.findById(id).orElseThrow(() -> new UserException("User does not exist"));
        try {
            user.setAccountStatus(AccountStatus.CLOSED);
            userRepository.save(user);
            return "User deleted";
        } catch (Exception e) {
            throw new UserException("Something went wrong");
        }
    }

    @Override
    public String unlockUser(Long id) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        if(userPrincipal.user().getId().equals(id)) {
            throw new UserException("Can't unlock yourself");
        }

        User user = userRepository.findById(id).orElseThrow(() -> new UserException("User does not exist"));

        try {
            user.setAccountStatus(AccountStatus.ACTIVE);
            userRepository.save(user);
            return "User unlocked";
        } catch (Exception e) {
            throw new UserException("Something went wrong");
        }
    }

}
