package social_media.social_media_handler.controller.linkedin;


import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication; // Added import
import social_media.social_media_handler.entity.User; // Added import
import social_media.social_media_handler.service.linkedin.LinkedInAuthService;
import jakarta.servlet.http.HttpServletResponse; // Added import
import java.io.IOException; // Added import

@RestController
@RequestMapping("/oauth/linkedin")
@RequiredArgsConstructor
public class LinkedInOAuthController {

    private final LinkedInAuthService linkedInAuthService;

    /**
     * STEP 1: Redirect user to LinkedIn authorization page
     */
    @GetMapping("/connect")
    public ResponseEntity<String> connect(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        String userId = user.getId();
        String authUrl = linkedInAuthService.getAuthorizationUrl(userId);
        return ResponseEntity.ok(authUrl);
    }

    /**
     * STEP 2: LinkedIn redirects here after user consent
     * NOTE: userId is passed temporarily for testing
     */

    @GetMapping("/callback")
    public void callback(@RequestParam("code") String code, @RequestParam("state") String userId, HttpServletResponse response) throws IOException {
        linkedInAuthService.handleCallback(code, userId);
        String htmlContent = "<!DOCTYPE html><html><head><title>LinkedIn Auth Success</title></head><body>" +
                             "<script type='text/javascript'>" +
                             "window.opener.postMessage('linkedin-auth-success', 'http://localhost:8081');" +
                             "window.close();" +
                             "</script></body></html>";
        response.setContentType("text/html");
        response.getWriter().write(htmlContent);
    }
}

