package social_media.social_media_handler.controller.youtube;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import social_media.social_media_handler.service.youtube.YouTubeAuthService;

import java.io.IOException;

@RestController
@RequestMapping("/oauth/youtube")
@RequiredArgsConstructor
public class YouTubeOAuthController {

    private  YouTubeAuthService authService;

    // STEP 1: Redirect user to Google OAuth
    @GetMapping("/connect")
    public void connect(HttpServletResponse response) throws IOException {
        String authUrl = authService.getAuthorizationUrl();
        response.sendRedirect(authUrl);
    }

    // STEP 2: Google redirects back here
    @GetMapping("/callback")
    public void callback(
            @RequestParam("code") String code,
            @RequestParam(value = "state", required = false) String state,
            HttpServletResponse response
    ) throws IOException {

        // 🔥 DEMO-SAFE: skip user binding for now
        authService.handleCallback(code, email);
        // 🔥 Redirect back to FRONTEND (not backend)
        response.sendRedirect(
            "http://127.0.0.1:5500/src/main/resources/static/frontend/main.html?youtube=connected"
        );
    }
}