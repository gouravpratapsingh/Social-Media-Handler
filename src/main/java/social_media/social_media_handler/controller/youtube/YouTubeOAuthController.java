package social_media.social_media_handler.controller.youtube;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import social_media.social_media_handler.entity.youtube.YouTubeAccount;
import social_media.social_media_handler.service.youtube.YouTubeAuthService;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

@RestController
@RequestMapping("/oauth/youtube")
@RequiredArgsConstructor
public class YouTubeOAuthController {

    private final YouTubeAuthService youTubeAuthService;

    /**
     * Frontend will call this with ?email=...
     */
    @GetMapping("/connect")
    public void connect(@RequestParam("email") String email, HttpServletResponse response) throws Exception {
        String url = youTubeAuthService.getAuthorizationUrl(email);
        response.sendRedirect(url);
    }

    /**
     * Google redirects here
     */
    @GetMapping("/callback")
    public void callback(@RequestParam String code, @RequestParam String state,   /* this is the email we passed */
            HttpServletResponse response
    ) throws Exception {
        try {
            youTubeAuthService.handleCallback(code, state);
            response.sendRedirect("/frontend/oauth-success.html");
        } catch (Exception e) {
            response.sendRedirect("/frontend/oauth-error.html?msg=" + URLEncoder.encode(e.getMessage(), StandardCharsets.UTF_8));
        }
    }

    @GetMapping("/channels")
    public List<YouTubeAccount> channels(@RequestParam String email){
        return youTubeAuthService.getAllChannels(email);
    }
}