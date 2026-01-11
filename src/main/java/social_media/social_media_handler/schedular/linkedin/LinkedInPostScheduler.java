package social_media.social_media_handler.schedular.linkedin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import social_media.social_media_handler.entity.linkedin.LinkedInAccount;
import social_media.social_media_handler.entity.linkedin.LinkedInScheduledPost;
import social_media.social_media_handler.entity.PostStatus;
import social_media.social_media_handler.repository.linkedin.LinkedInScheduledPostRepository;
import social_media.social_media_handler.service.email.EmailService;
import social_media.social_media_handler.service.linkedin.LinkedInMediaService;
import social_media.social_media_handler.service.linkedin.LinkedInPostService;

import java.io.File;
import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class LinkedInPostScheduler {

    private final LinkedInScheduledPostRepository postRepository;
    private final LinkedInMediaService mediaService;
    private final LinkedInPostService postService;
    private final EmailService emailService;

    @Scheduled(cron = "0 * * * * *")
    public void publishPosts() {

        List<LinkedInScheduledPost> posts =
                postRepository.findByStatusAndScheduledAtLessThanEqual(
                        PostStatus.PENDING,
                        LocalDateTime.now()
                );

        for (LinkedInScheduledPost post : posts) {

            LinkedInAccount account = post.getAccount();

            try {
                String ownerUrn =
                        "urn:li:person:" + account.getLinkedinUserId();

                String assetUrn =
                        mediaService.uploadMedia(
                                account.getAccessToken(),
                                ownerUrn,
                                new File(post.getMediaPath()),
                                "urn:li:digitalmediaRecipe:feedshare-image"
                        );

                String postUrn =
                        postService.postWithMedia(
                                account.getAccessToken(),
                                ownerUrn,
                                post.getContent(),
                                assetUrn,
                                "IMAGE"
                        );

                post.setPlatformPostUrn(postUrn);
                post.setStatus(PostStatus.POSTED);
                post.setPostedAt(LocalDateTime.now());

                emailService.sendEmail(
                        account.getUser().getEmail(),
                        "LinkedIn Post Published",
                        "Your LinkedIn post was published successfully."
                );

            } catch (Exception e) {
                post.setStatus(PostStatus.FAILED);
                post.setFailureReason(e.getMessage());

                emailService.sendEmail(
                        account.getUser().getEmail(),
                        "LinkedIn Post Failed",
                        e.getMessage()
                );
            }

            postRepository.save(post);
        }
    }
}

