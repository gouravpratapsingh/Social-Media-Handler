package social_media.social_media_handler.service.twitter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import social_media.social_media_handler.config.TwitterConfig;
import social_media.social_media_handler.entity.twitter.TwitterPost;

@Service
public class TwitterService {

    @Autowired
    private TwitterConfig twitterConfig;

    private final RestTemplate restTemplate = new RestTemplate();

    // ✅ UPDATED: Now requires the User's Access Token
    public String postTweet(String accessToken, String text) {
        
        HttpHeaders headers = new HttpHeaders();
        // Use the token passed in, NOT the global config one
        headers.setBearerAuth(accessToken); 
        headers.setContentType(MediaType.APPLICATION_JSON);

        String body = "{ \"text\": \"" + text + "\" }";

        HttpEntity<String> request = new HttpEntity<>(body, headers);

        try {
            ResponseEntity<String> response = restTemplate.postForEntity(
                    "https://api.twitter.com/2/tweets",
                    request,
                    String.class
            );
            return response.getBody();
        } catch (Exception e) {
            // Log error or throw it to be caught by scheduler
            throw new RuntimeException("Failed to post to Twitter: " + e.getMessage());
        }
    }

    // 2️⃣ For SCHEDULER (scheduled tweet)
    public String postTweet(TwitterPost post) {

        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(twitterConfig.getBearerToken());
        headers.setContentType(MediaType.APPLICATION_JSON);

        String body = "{ \"text\": \"" + post.getContent() + "\" }";

        HttpEntity<String> request = new HttpEntity<>(body, headers);

        ResponseEntity<String> response =
                restTemplate.postForEntity(
                        "https://api.twitter.com/2/tweets",
                        request,
                        String.class
                );

        return response.getBody();
    }
}
