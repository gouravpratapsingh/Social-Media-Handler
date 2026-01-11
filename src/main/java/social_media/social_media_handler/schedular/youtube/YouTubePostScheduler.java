package social_media.social_media_handler.schedular.youtube;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import social_media.social_media_handler.entity.PostStatus;
import social_media.social_media_handler.entity.youtube.YouTubeScheduledPost;
import social_media.social_media_handler.entity.youtube.YouTubeAccount;
import social_media.social_media_handler.repository.youtube.YouTubeScheduledPostRepository;
import social_media.social_media_handler.repository.youtube.YouTubeAccountRepository;
import social_media.social_media_handler.schedular.PostStatusNotifier;
import social_media.social_media_handler.service.youtube.YouTubeUploadService;

import java.io.File;
import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class YouTubePostScheduler {

    private final YouTubeScheduledPostRepository postRepository;
    private final YouTubeAccountRepository accountRepository;
    private final YouTubeUploadService uploadService;
    private final PostStatusNotifier postStatusNotifier; // ✅ ADD THIS

    @Scheduled(cron = "0 * * * * *") // every minute
    public void autoPublish() {

        List<YouTubeScheduledPost> posts =
                postRepository.findByPlatformAndStatusAndScheduledAtLessThanEqual(
                        "YOUTUBE",
                        PostStatus.PENDING,
                        LocalDateTime.now()
                );

        for (YouTubeScheduledPost post : posts) {

            YouTubeAccount account = null;
            try {
                post.setStatus(PostStatus.PROCESSING);
                postRepository.save(post);

                account = accountRepository.findAll().get(0);

                uploadService.uploadVideo(
                        account.getAccessToken(),
                        new File(post.getMediaPath()),
                        post.getTitle(),
                        post.getDescription(),
                        "public"
                );

                post.setStatus(PostStatus.POSTED);
                post.setPostedAt(LocalDateTime.now());
                postRepository.save(post);

                // ✅ EMAIL ON SUCCESS
                postStatusNotifier.notifyPostSuccess(
                        account.getUser(),
                        "YouTube",
                        post.getPostedAt().toString()
                );


            } catch (Exception e) {

                post.setStatus(PostStatus.FAILED);
                post.setFailureReason(e.getMessage());
                postRepository.save(post);

                log.error("YouTube post failed", e);

                // ❌ EMAIL ON FAILURE
                if (account != null) {
                    postStatusNotifier.notifyPostFailure(
                            account.getUser(),
                            "YouTube",
                            e.getMessage()
                    );
                }
            }
        }
    }
}
