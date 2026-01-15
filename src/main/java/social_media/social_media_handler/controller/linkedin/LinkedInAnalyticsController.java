package social_media.social_media_handler.controller.linkedin;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import social_media.social_media_handler.entity.linkedin.LinkedInScheduledPost;
import social_media.social_media_handler.repository.linkedin.LinkedInScheduledPostRepository;
import social_media.social_media_handler.service.linkedin.LinkedInAnalyticsService;

@RestController
@RequestMapping("/linkedin/analytics")
@RequiredArgsConstructor
public class LinkedInAnalyticsController {

    private final LinkedInAnalyticsService analyticsService;
    private final LinkedInScheduledPostRepository postRepository;

    @GetMapping("/{postId}")
    public ResponseEntity<String> getAnalytics(@PathVariable String postId) {

        LinkedInScheduledPost post =
                postRepository.findById(postId)
                        .orElseThrow();

        return ResponseEntity.ok(
                analyticsService.fetchPostAnalytics(
                        post.getAccount().getAccessToken(),
                        post.getPlatformPostUrn()
                )
        );
    }
}

