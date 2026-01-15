package social_media.social_media_handler.controller.youtube;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import social_media.social_media_handler.repository.youtube.YouTubeAnalyticsRepository;
@RestController
@RequestMapping("/youtube/analytics")
@RequiredArgsConstructor
public class YouTubeAnalyticsController {
    private final YouTubeAnalyticsRepository repository;
    @GetMapping("/{channelId}")
    public Object getAnalytics(@PathVariable String channelId) {
        return repository.findByChannelIdOrderByAnalyticsDateAsc(channelId);
    }
}