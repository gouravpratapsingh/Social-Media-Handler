package social_media.social_media_handler.schedular.twitter;

import social_media.social_media_handler.entity.twitter.TwitterPost;
import social_media.social_media_handler.repository.twitter.TwitterPostRepository;
import social_media.social_media_handler.service.TwitterService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class TwitterPostScheduler {

    private final TwitterPostRepository repository;
    private final TwitterService twitterService;

    public TwitterPostScheduler(
            TwitterPostRepository repository,
            TwitterService twitterService) {
        this.repository = repository;
        this.twitterService = twitterService;
    }

    @Scheduled(fixedRate = 60000) // every 1 minute
    public void postScheduledTweets() {

        List<TwitterPost> posts =
                repository.findByStatusAndScheduledTimeBefore(
                        "PENDING",
                        LocalDateTime.now()
                );

        for (TwitterPost post : posts) {
            // ✅ CORRECT CALL
            twitterService.postTweet(post);
        }
    }
}
