package social_media.social_media_handler.controller.youtube;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import social_media.social_media_handler.service.youtube.YouTubeAuthService;

@RestController
@RequestMapping("/oauth/youtube")
@RequiredArgsConstructor
public class YouTubeOAuthController{

    private final YouTubeAuthService authService;

    @GetMapping("/connect")
    public void connect(HttpServletResponse response) throws Exception {
        response.sendRedirect(authService.getAuthorizationUrl());
    }

    @GetMapping("/callback")
    public void callback(
            @RequestParam String code, Authentication authentication, HttpServletResponse response
    ) throws Exception {
        String email = authentication.getName(); // SAFE
        authService.handleCallback(code, email);
        response.sendRedirect("http://localhost:8081/frontend/oauth-success.html");
    }

}
