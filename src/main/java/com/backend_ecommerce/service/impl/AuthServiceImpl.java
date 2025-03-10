package com.backend_ecommerce.service.impl;

import com.backend_ecommerce.config.JwtProvider;
import com.backend_ecommerce.domain.AccountStatus;
import com.backend_ecommerce.domain.ROLE_NAME;
import com.backend_ecommerce.domain.TypeCode;
import com.backend_ecommerce.dto.UserPrincipal;
import com.backend_ecommerce.exception.CartException;
import com.backend_ecommerce.exception.UserException;
import com.backend_ecommerce.model.Avatar;
import com.backend_ecommerce.model.User;
import com.backend_ecommerce.model.VerificationCode;
import com.backend_ecommerce.repository.UserRepository;
import com.backend_ecommerce.request.*;
import com.backend_ecommerce.response.AuthResponse;
import com.backend_ecommerce.service.AuthService;
import com.backend_ecommerce.service.CartService;
import com.backend_ecommerce.service.EmailService;
import com.backend_ecommerce.service.VerificationCodeService;
import com.backend_ecommerce.validation.AuthValidation;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepo;
    private final AuthValidation authValidation;
    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;
    private final VerificationCodeService verificationCodeService;
    private final EmailService emailService;
    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;
    private final CartService cartService;


    //Sign up request
    @Override
    public AuthResponse signupHandler(SignupRequest req) {
        //Validate
        authValidation.validateSignUpRequest(req);
        User user = User.builder()
                .fullName(req.getFullName())
                .email(req.getEmail())
                .password(passwordEncoder.encode(req.getPassword()))
                .role(ROLE_NAME.USER)
                .build();

        User newUser;
        try {
            newUser = userRepo.save(user);
            Long cartId = cartService.createCart(newUser);
            if(cartId == null)
                throw new CartException("Cannot create cart");
        } catch (UserException e) {
            throw new UserException("Sign up failed");
        }

        UserPrincipal userPrincipal = new UserPrincipal(newUser);

        Authentication authentication = new UsernamePasswordAuthenticationToken(userPrincipal, null, userPrincipal.getAuthorities());

        return getUserDetails(authentication);
    }

    //Login with otp request
    @Override
    public AuthResponse signingHandler(SigningWithOtpRequest req) {

        Authentication authentication = authenticate(req.getEmail());

        verificationCodeService.findVerificationCode(req.getEmail(), req.getOtp(), TypeCode.SIGNIN);

        return getUserDetails(authentication);
    }

    //Authenticate with email
    private Authentication authenticate(String email) {

        UserDetails userDetails = userDetailsService.loadUserByUsername(email);

            return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }

    //Login with email and password
    @Override
    public AuthResponse signingHandler(SigningWithPasswordRequest req) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(req.getEmail(), req.getPassword())
            );

            return getUserDetails(authentication);
        }
        catch (BadCredentialsException e) {
            throw new BadCredentialsException("Bad Credentials!");
        }
        catch (Exception e) {
            throw new DisabledException("Your account is banned!");
        }
    }

    //AuthResponse t authentication
    private AuthResponse getUserDetails(Authentication authentication) {

        String jwtToken = jwtProvider.generateToken(authentication);

        UserPrincipal user = (UserPrincipal) authentication.getPrincipal();

        String avatarUrl = Optional.ofNullable(user.user().getAvatar()).flatMap(avatars -> avatars.stream()
                        .max(Comparator.comparing(Avatar::getCreatedAt))
                        .map(Avatar::getSource))
                .orElse(null);

        return AuthResponse
                .builder()
                .fullName(user.user().getFullName())
                .avatar(avatarUrl)
                .jwt(jwtToken)
                .role(user.user().getRole())
                .build();
    }

    //Generate signing otp code
    @Override
    public String generateSigningOtpCode(SentOtpRequest req) throws MessagingException {

        if(!userRepo.existsByEmail(req.getEmail()))
            throw new UsernameNotFoundException("User not found with email: " + req.getEmail());

        if(!userRepo.existsByEmailAndAccountStatus(req.getEmail(), AccountStatus.ACTIVE))
            throw new DisabledException("Your account has been banned.");

        VerificationCode verificationCode = verificationCodeService.generateCode(req.getEmail(), TypeCode.SIGNIN);

        String subject = "Huy shop signing otp";
        String text = "your signing otp is - ";

        try {
            emailService.sendVerificationOtpEmail(req.getEmail(), verificationCode.getOtp(), subject, text);
            return "Sent OTP successfully";
        } catch (MessagingException e) {
            throw new MessagingException("Failed to send verification OTP email");
        }

    }

    @Override
    public String generateResetPasswordOtpCode(SentOtpRequest req) throws MessagingException {

        if(!userRepo.existsByEmail(req.getEmail()))
            throw new UsernameNotFoundException("User not found with email: " + req.getEmail());

        if(!userRepo.existsByEmailAndAccountStatus(req.getEmail(), AccountStatus.ACTIVE))
            throw new DisabledException("Your account has been banned.");

        VerificationCode verificationCode = verificationCodeService.generateCode(req.getEmail(), TypeCode.RESETPASSWORD);

        String subject = "Huy shop reset password otp";
        String text = "your reset password otp is - ";

        try {
            emailService.sendVerificationOtpEmail(req.getEmail(), verificationCode.getOtp(), subject, text);
            return "Sent OTP successfully";
        } catch (MessagingException e) {
            throw new MessagingException("Failed to send verification OTP email");
        }
    }

    @Override
    public String updatePasswordWithOtp(UpdatePasswordWithOtpRequest req) {

        if(!req.getNewPassword().equals(req.getConfirmPassword())) {
            throw new UserException("Passwords do not match");
        }

        User existUser = userRepo.findByEmail(req.getEmail()).orElseThrow(
                () -> new UserException("User does not exist with email: " + req.getEmail()));

        if(!userRepo.existsByEmailAndAccountStatus(req.getEmail(), AccountStatus.ACTIVE))
            throw new DisabledException("Your account has been banned.");

        verificationCodeService.findVerificationCode(req.getEmail(), req.getOtp(), TypeCode.RESETPASSWORD);

        existUser.setPassword(passwordEncoder.encode(req.getNewPassword()));
        try {
            userRepo.save(existUser);
            return "Password changed successfully";
        } catch (Exception e) {
            throw new UserException("Failed to update password");
        }
    }
}
