package social_media.social_media_handler.entity.youtube;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(name = "youtube_analytics")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class YouTubeAnalyticsData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String channelId;

    private LocalDate analyticsDate;

    private Long views;
    private Long likes;
    private Long comments;
    private Long subscribersGained;
    private Long subscribersLost;
}

