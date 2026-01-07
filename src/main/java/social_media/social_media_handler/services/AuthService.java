package social_media.social_media_handler.services;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import social_media.social_media_handler.dto.AuthenticationRequest.LoginRequest;
import social_media.social_media_handler.dto.AuthenticationRequest.SignupRequest;
import social_media.social_media_handler.dto.AuthenticationResponse.LoginResponse;
import social_media.social_media_handler.dto.AuthenticationResponse.SignupResponse;
import social_media.social_media_handler.entity.User;
import social_media.social_media_handler.exception.ResourceNotFoundException;
import social_media.social_media_handler.repository.UserRepository;
import social_media.social_media_handler.util.JwtUtil;

@Service
public class AuthService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
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

        return new LoginResponse(jwt, user.getEmail(), "Login Successfully");
    }

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
}