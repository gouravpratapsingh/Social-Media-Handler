package social_media.social_media_handler.schedular.youtube;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import social_media.social_media_handler.repository.youtube.YouTubeAccountRepository;
import social_media.social_media_handler.service.youtube.YouTubeAnalyticsService;

@Component
@RequiredArgsConstructor
public class YouTubeAnalyticsScheduler {

    private final YouTubeAccountRepository accountRepository;
    private final YouTubeAnalyticsService analyticsService;

    @Scheduled(cron = "0 0 2 * * *")
    public void fetchDailyAnalytics() {
        accountRepository.findAll()
                .forEach(analyticsService::fetchAndStoreAnalytics);
    }
}
