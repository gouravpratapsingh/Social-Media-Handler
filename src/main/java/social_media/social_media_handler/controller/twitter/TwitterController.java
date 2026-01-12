package social_media.social_media_handler.controller.twitter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import social_media.social_media_handler.dto.twitter.TwitterPostRequest;
import social_media.social_media_handler.service.twitter.TwitterService;

@RestController
@RequestMapping("/api/twitter")
@CrossOrigin(origins = "*")
public class TwitterController {

    @Autowired
    private TwitterService twitterService;

    @PostMapping("/tweet")
    public ResponseEntity<String> postTweet(
            @RequestBody TwitterPostRequest request) {

        String response =
                twitterService.postTweet(request.getText());

        return ResponseEntity.ok(response);
    }
}
