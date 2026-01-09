package social_media.social_media_handler.schedular.pinterest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import social_media.social_media_handler.entity.pinterest.PinterestScheduledPost;
import social_media.social_media_handler.repository.pinterest.PinterestScheduledPostRepository;
import social_media.social_media_handler.service.pinterest.PinterestService;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class PinterestPostScheduler {

    @Autowired
    private PinterestScheduledPostRepository repository;

    @Autowired
    private PinterestService pinterestService;

    @Scheduled(cron = "0 * * * * *")
    public void processScheduledPins() {
        // Current time ke hisab se PENDING posts nikalna
        List<PinterestScheduledPost> pendingPosts = repository.findByStatusAndScheduledTimeBefore("PENDING", LocalDateTime.now());

        for (PinterestScheduledPost post : pendingPosts) {
            try {
                // Ensure kijiye PinterestService mein createPin method 5 parameters leta hai
                boolean isSuccess = pinterestService.createPin(
                        post.getTitle(),
                        post.getDescription(),
                        post.getBoardId(),
                        post.getImageUrl(),
                        post.getDestinationLink()
                );

                if (isSuccess) {
                    post.setStatus("PUBLISHED");
                } else {
                    post.setStatus("FAILED");
                    post.setErrorMessage("API call failed.");
                }
            } catch (Exception e) {
                post.setStatus("FAILED");
                post.setErrorMessage(e.getMessage());
            }
            repository.save(post);
        }
    }
}