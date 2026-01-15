package social_media.social_media_handler.repository.youtube;
import org.springframework.data.mongodb.repository.MongoRepository;
import social_media.social_media_handler.entity.youtube.YouTubeAnalyticsData;
import java.time.LocalDate;
import java.util.List;
public interface YouTubeAnalyticsRepository extends MongoRepository<YouTubeAnalyticsData, Long> {
List<YouTubeAnalyticsData> findByChannelIdOrderByAnalyticsDateAsc(String channelId);
boolean existsByChannelIdAndAnalyticsDate(String channelId, LocalDate date);
}