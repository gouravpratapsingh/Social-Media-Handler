package social_media.social_media_handler.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import social_media.social_media_handler.entity.ScheduledPost;
import social_media.social_media_handler.repository.ScheduledPostRepository;

@RestController
@RequestMapping("/api/posts")
public class PostController {

    private final ScheduledPostRepository repository;

    public PostController(ScheduledPostRepository repository) {
        this.repository = repository;
    }

    @PostMapping("/schedule")
    public ScheduledPost scheduleNewPost(@RequestBody ScheduledPost post) {
        post.setStatus("PENDING");
        return repository.save(post);
    }

    @GetMapping("/all")
    public List<ScheduledPost> getAllPosts() {
        return repository.findAll();
    }
}