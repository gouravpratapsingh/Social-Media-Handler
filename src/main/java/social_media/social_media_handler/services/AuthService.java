package social_media.social_media_handler.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import social_media.social_media_handler.dto.SignupRequest;
import social_media.social_media_handler.dto.SignupResponse;
import social_media.social_media_handler.model.User;
import social_media.social_media_handler.repository.UserRepository;

@Service
public class AuthService{
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private

    public SignupResponse signup(SignupRequest request){
        if (userRepository.existsByEmail(request.getEmail())){
            throw new RuntimeException("Email already exists.");
        }

        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());

    }
}
