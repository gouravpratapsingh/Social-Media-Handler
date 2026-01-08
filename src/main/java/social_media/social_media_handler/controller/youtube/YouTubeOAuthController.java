package social_media.social_media_handler.controller.youtube;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import social_media.social_media_handler.entity.User;
import social_media.social_media_handler.service.youtube.YouTubeAuthService;

@RestController
@RequestMapping("/oauth/youtube")
@RequiredArgsConstructor
public class YouTubeOAuthController {

    private final YouTubeAuthService authService;

    @GetMapping("/connect")
    public void connect(HttpServletResponse response) throws Exception {
        response.sendRedirect(authService.getAuthorizationUrl());
    }

    @GetMapping("/callback")
    public String callback(@RequestParam String code) throws Exception {
        User user = getLoggedInUser();
        authService.handleCallback(code, user);
        return "YouTube Connected Successfully";
    }

    private User getLoggedInUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return (User) auth.getPrincipal();
    }
}
