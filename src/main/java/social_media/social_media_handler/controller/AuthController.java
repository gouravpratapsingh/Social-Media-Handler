package social_media.social_media_handler.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import social_media.social_media_handler.dto.request.LoginRequest;
import social_media.social_media_handler.dto.request.SignupRequest;
import social_media.social_media_handler.dto.response.LoginResponse;
import social_media.social_media_handler.dto.response.SignupResponse;
import social_media.social_media_handler.services.AuthService;
import social_media.social_media_handler.util.JwtUtil;


@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;
    @Autowired
    private PasswordEncoder encoder;
    @Autowired
    private JwtUtil jwtUtils;

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
}