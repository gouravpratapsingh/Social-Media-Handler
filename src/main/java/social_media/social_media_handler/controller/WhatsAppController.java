package social_media.social_media_handler.controller;

import social_media.social_media_handler.dto.request.WhatsAppMessageRequest;
import social_media.social_media_handler.services.WhatsAppService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/whatsapp")
public class WhatsAppController {

    private final WhatsAppService service;

    public WhatsAppController(WhatsAppService service) {
        this.service = service;
    }

    @PostMapping("/send")
    public String send(@RequestBody WhatsAppMessageRequest request) {
        return service.sendMessage(
                request.getTo(),
                request.getMessage()
        );
    }
}
