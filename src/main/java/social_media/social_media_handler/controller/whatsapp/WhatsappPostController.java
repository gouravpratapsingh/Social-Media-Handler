package social_media.social_media_handler.controller.whatsapp;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import social_media.social_media_handler.entity.whatsapp.WhatsAppScheduledPost;
import social_media.social_media_handler.repository.whatsapp.WhatsAppScheduledPostRepository;

@RestController
@RequestMapping("/api/posts")
public class WhatsappPostController {

    private final WhatsAppScheduledPostRepository repository;

    public WhatsappPostController(WhatsAppScheduledPostRepository repository) {
        this.repository = repository;
    }

    @PostMapping("/schedule")
    public WhatsAppScheduledPost scheduleNewPost(@RequestBody WhatsAppScheduledPost post) {
        post.setStatus("PENDING");
        return repository.save(post);
    }

    @GetMapping("/all")
    public List<WhatsAppScheduledPost> getAllPosts() {
        return repository.findAll();
    }
}