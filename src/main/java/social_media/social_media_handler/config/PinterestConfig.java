package social_media.social_media_handler.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PinterestConfig {

    @Value("${pinterest.api.base-url}")
    private String baseUrl;

    @Value("${pinterest.access.token}")
    private String accessToken;

    public String getBaseUrl() {
        return baseUrl;
    }

    public String getAccessToken() {
        return accessToken;
    }
}

