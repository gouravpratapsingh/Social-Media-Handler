package social_media.social_media_handler.controller.whatsapp;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import social_media.social_media_handler.service.whatsapp.WhatsAppAuthService;

import java.io.IOException;

@RestController
@RequestMapping("/oauth/whatsapp")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class WhatsAppOAuthController {

    private final WhatsAppAuthService authService;

    // STEP 1: Frontend asks for Facebook/WhatsApp Login URL
    @GetMapping("/connect")
    public ResponseEntity<String> connect(@RequestParam String userId) {
        String url = authService.getAuthorizationUrl(userId);
        return ResponseEntity.ok(url);
    }

    // STEP 2: Facebook redirects back here
    @GetMapping("/callback")
    public void callback(
            @RequestParam("code") String code,
            @RequestParam("state") String userId,
            HttpServletResponse response
    ) throws IOException {
        
        authService.handleCallback(code, userId);

        // Redirect back to frontend dashboard
        response.sendRedirect("http://127.0.0.1:5500/src/main/resources/static/frontend/main.html?status=whatsapp_connected");
    }
}