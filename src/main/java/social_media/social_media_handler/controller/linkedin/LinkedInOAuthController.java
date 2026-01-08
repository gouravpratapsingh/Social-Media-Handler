package social_media.social_media_handler.controller.linkedin;


import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import social_media.social_media_handler.service.linkedin.LinkedInAuthService;

@RestController
@RequestMapping("/oauth/linkedin")
@RequiredArgsConstructor
public class LinkedInOAuthController {

    private final LinkedInAuthService linkedInAuthService;

    /**
     * STEP 1: Redirect user to LinkedIn authorization page
     */
    @GetMapping("/connect")
    public ResponseEntity<String> connect() {
        String authUrl = linkedInAuthService.getAuthorizationUrl();
        return ResponseEntity.ok(authUrl);
    }

    /**
     * STEP 2: LinkedIn redirects here after user consent
     * NOTE: userId is passed temporarily for testing
     */

    @GetMapping("/callback")
    public ResponseEntity<String> callback(@RequestParam("code") String code, @RequestParam("state") String userId) {
        linkedInAuthService.handleCallback(code, userId);
        return ResponseEntity.ok("LinkedIn connected successfully");
    }
}

