package social_media.social_media_handler.controller.pinterest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import social_media.social_media_handler.dto.pinterest.PinterestPinDTO;
import social_media.social_media_handler.entity.pinterest.PinterestScheduledPost;
import social_media.social_media_handler.repository.pinterest.PinterestScheduledPostRepository;
import social_media.social_media_handler.service.pinterest.PinterestService;

@RestController
@RequestMapping("/api/pinterest")
public class PinterestController {

    @Autowired
    private PinterestService pinterestService;

    @Autowired
    private PinterestScheduledPostRepository repository;

    @GetMapping("/account")
    public String getPinterestAccount() {
        return pinterestService.getUserAccount();
    }

    // Naya Endpoint: Post schedule karne ke liye
    @PostMapping("/schedule")
    public String schedulePin(@RequestBody PinterestPinDTO dto) {
        PinterestScheduledPost post = new PinterestScheduledPost();
        post.setTitle(dto.getTitle());
        post.setDescription(dto.getDescription());
        post.setBoardId(dto.getBoardId());
        post.setImageUrl(dto.getImageUrl());
        post.setDestinationLink(dto.getDestinationLink());
        post.setScheduledTime(dto.getScheduledTime());
        post.setStatus("PENDING");

        repository.save(post);
        return "Pin scheduled successfully for " + dto.getScheduledTime();
    }
}