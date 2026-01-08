package social_media.social_media_handler.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import social_media.social_media_handler.dto.auth.LoginRequest;
import social_media.social_media_handler.dto.auth.SignupRequest;
import social_media.social_media_handler.dto.auth.LoginResponse;
import social_media.social_media_handler.dto.auth.SignupResponse;
import social_media.social_media_handler.entity.User;
import social_media.social_media_handler.exception.ResourceNotFoundException;
import social_media.social_media_handler.repository.UserRepository;
import social_media.social_media_handler.service.email.EmailService;
import social_media.social_media_handler.util.AuthEmailTemplateUtil;
import social_media.social_media_handler.util.JwtUtil;
import social_media.social_media_handler.util.PasswordEmailTemplateUtil;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class AuthService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtUtil jwtUtils;
    @Autowired
    private EmailService emailService;

    /**
     * logic for User Signup
     */
    public SignupResponse signupUser(SignupRequest signupRequest) {
        // 1. Check if user exists
        if (userRepository.findByEmail(signupRequest.getEmail()).isPresent()) {
            return new SignupResponse("Email already exists!", null, null, false);
        }

        // 2. Create and Save User

        // Generate random number (4 digits)
        int randomNum = (int)(Math.random() * 10000000); // ensures 1000–9999
        String customId = signupRequest.getUsername()+ "_" + randomNum;

        // Ensure uniqueness by checking DB
        while (userRepository.existsById(customId)) {
            randomNum = (int)(Math.random() * 10000000);
            customId = signupRequest.getUsername()+ "_" + randomNum;
        }

        User user = User.builder()
                .id(customId)
                .username(signupRequest.getUsername())
                .email(signupRequest.getEmail())
                .password(passwordEncoder.encode(signupRequest.getPassword()))
                .build();
        User savedUser = userRepository.save(user);

        // 3. Generate JWT immediately (Auto-Login)
        String token = jwtUtils.generateToken(savedUser.getEmail());

        // ✅ EMAIL ON SIGNUP
        emailService.sendEmail(
                user.getEmail(),
                "🎉 Welcome to Social Media Handler",
                AuthEmailTemplateUtil.signupSuccess(user.getUsername())
        );

        // 4. Return AuthenticationResponse with token
        return new SignupResponse(
                "Account created! Logging you in...",
                savedUser.getEmail(),
                token,
                true
        );
    }

    /**
     * logic for User Login
     */
    public LoginResponse loginUser(LoginRequest request) {
        // Find user
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        // Check password
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }

        // Set security context (optional, since we're using JWT)
        Authentication authentication = new UsernamePasswordAuthenticationToken(user.getEmail(), null, null);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        //Generate JWT
        String jwt = jwtUtils.generateToken(request.getEmail());

        // ✅ EMAIL ON LOGIN
        emailService.sendEmail(
                user.getEmail(),
                "🔐 New Login Detected",
                AuthEmailTemplateUtil.loginAlert(user.getUsername())
        );

        return new LoginResponse(jwt, user.getEmail(), "Login Successfully");
    }

    @SuppressWarnings("NullableProblems")
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));
        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getEmail())
                .password(user.getPassword())
                .roles("USER") // or get from user if you have roles
                .build();
    }


    // ===============================
    // FORGOT PASSWORD
    // ===============================

    public void forgotPassword(String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        String token = UUID.randomUUID().toString();

        user.setResetToken(token);
        user.setResetTokenExpiry(LocalDateTime.now().plusMinutes(15));

        userRepository.save(user);

        String resetLink = "http://localhost:3000/reset-password?token=" + token;

        emailService.sendEmail( user.getEmail(), "🔐 Reset Your Password",
                PasswordEmailTemplateUtil.resetPassword(user.getUsername(), resetLink));
    }

    public void resetPassword(String token, String newPassword) {

        User user = userRepository.findByResetToken(token)
                .orElseThrow(() -> new RuntimeException("Invalid token"));

        if (user.getResetTokenExpiry().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Token expired");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        user.setResetToken(null);
        user.setResetTokenExpiry(null);

        userRepository.save(user);
    }
}