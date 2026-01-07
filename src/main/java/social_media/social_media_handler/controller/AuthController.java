package social_media.social_media_handler.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import social_media.social_media_handler.dto.auth.*;
import social_media.social_media_handler.service.AuthService;
import social_media.social_media_handler.util.JwtUtil;


@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private AuthService authService;
    @Autowired
    private PasswordEncoder encoder;
    @Autowired
    private JwtUtil jwtUtils;

    // ===============================
    // LOGIN AND SIGNUP
    // ===============================

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@RequestBody SignupRequest request) {
        SignupResponse response=authService.signupUser(request);
        if (response.getEmail()==null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
        else{
            return ResponseEntity.ok(response);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        LoginResponse response = authService.loginUser(request);
        return ResponseEntity.ok(response);
    }

    // ===============================
    // FORGOT PASSWORD
    // ===============================

    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(@RequestBody ForgotPasswordRequest request) {
        authService.forgotPassword(request.getEmail());
        return ResponseEntity.ok("Password reset email sent");
    }

    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@RequestBody ResetPasswordRequest request){
        authService.resetPassword(request.getToken(), request.getNewPassword());
        return ResponseEntity.ok("Password reset successful");
    }
}