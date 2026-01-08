package social_media.social_media_handler.service.linkedin;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class LinkedInAnalyticsService {

    private final RestTemplate restTemplate = new RestTemplate();

    public String fetchPostAnalytics(
            String accessToken,
            String postUrn
    ) {

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);

        return restTemplate.exchange(
                "https://api.linkedin.com/v2/socialActions/" + postUrn,
                HttpMethod.GET,
                new HttpEntity<>(headers),
                String.class
        ).getBody();
    }
}

