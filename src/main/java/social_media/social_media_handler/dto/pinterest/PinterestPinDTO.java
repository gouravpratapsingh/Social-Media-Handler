package social_media.social_media_handler.dto.pinterest;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class PinterestPinDTO {
    private String title;
    private String description;
    private String boardId;
    private String imageUrl;
    private String destinationLink;
    private LocalDateTime scheduledTime; // User kab post karna chahta hai
}