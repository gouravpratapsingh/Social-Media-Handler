package social_media.social_media_handler.dto.twitter;

import org.springframework.stereotype.Service;

@Service
public class TwitterPostRequest {

    private String text;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
