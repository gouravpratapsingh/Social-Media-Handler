package social_media.social_media_handler.controller;

import social_media.social_media_handler.dto.TwitterPostRequest;
import social_media.social_media_handler.service.TwitterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
