package social_media.social_media_handler.schedular.youtube;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import social_media.social_media_handler.entity.youtube.PostStatus;
import social_media.social_media_handler.entity.youtube.ScheduledPost;
import social_media.social_media_handler.entity.youtube.YouTubeAccount;
import social_media.social_media_handler.repository.youtube.ScheduledPostRepository;
import social_media.social_media_handler.repository.youtube.YouTubeAccountRepository;
import social_media.social_media_handler.services.youtube.YouTubeUploadService;

import java.io.File;
import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class YouTubePostScheduler {

    private final ScheduledPostRepository postRepository;
    private final YouTubeAccountRepository accountRepository;
    private final YouTubeUploadService uploadService;

    @Scheduled(cron = "0 * * * * *") // every minute
    public void autoPublish() {

        List<ScheduledPost> posts =
                postRepository.findByPlatformAndStatusAndScheduledAtLessThanEqual(
                        "YOUTUBE",
                        PostStatus.PENDING,
                        LocalDateTime.now()
                );

        for (ScheduledPost post : posts) {
            try {
                post.setStatus(PostStatus.PROCESSING);
                postRepository.save(post);

                YouTubeAccount account = accountRepository.findAll().get(0);

                uploadService.uploadVideo(
                        account.getAccessToken(),
                        new File(post.getMediaPath()),
                        post.getTitle(),
                        post.getDescription(),
                        "public"
                );

                post.setStatus(PostStatus.POSTED);
                post.setPostedAt(LocalDateTime.now());

            } catch (Exception e) {
                post.setStatus(PostStatus.FAILED);
                post.setFailureReason(e.getMessage());
                log.error("YouTube post failed", e);
            }
            postRepository.save(post);
        }
    }
}

