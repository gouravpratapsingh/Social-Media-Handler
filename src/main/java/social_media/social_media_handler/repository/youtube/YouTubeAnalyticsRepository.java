package social_media.social_media_handler.repository.youtube;


import org.springframework.data.jpa.repository.JpaRepository;
import social_media.social_media_handler.entity.youtube.YouTubeAnalyticsData;

import java.time.LocalDate;
import java.util.List;

public interface YouTubeAnalyticsRepository
        extends JpaRepository<YouTubeAnalyticsData, Long> {

    List<YouTubeAnalyticsData> findByChannelIdOrderByAnalyticsDateAsc(String channelId);

    boolean existsByChannelIdAndAnalyticsDate(String channelId, LocalDate date);
}

