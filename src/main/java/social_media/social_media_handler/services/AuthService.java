package social_media.social_media_handler.services;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import social_media.social_media_handler.dto.request.LoginRequest;
import social_media.social_media_handler.dto.request.SignupRequest;
import social_media.social_media_handler.dto.response.LoginResponse;
import social_media.social_media_handler.dto.response.SignupResponse;
import social_media.social_media_handler.entity.User;
import social_media.social_media_handler.exception.ResourceNotFoundException;
import social_media.social_media_handler.repository.UserRepository;
import social_media.social_media_handler.util.JwtUtil;

@Service
public class AuthService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtUtil jwtUtils;

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

        // 4. Return response with token
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

        //Authenticate credentials
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(),request.getPassword()));

        // Set security context
        SecurityContextHolder.getContext().setAuthentication(authentication);

        //Generate JWT
        String jwt = jwtUtils.generateToken(request.getEmail());

        //Return token and role details
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(()-> new ResourceNotFoundException("User Not Found"));

        return new LoginResponse(jwt, user.getEmail(), "Login Successfully");
    }
}