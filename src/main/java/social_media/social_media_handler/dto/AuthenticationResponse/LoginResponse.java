package social_media.social_media_handler.dto.AuthenticationResponse;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponse{
    private String token;
    private String email;
    private String message;
}