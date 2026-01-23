package social_media.social_media_handler.controller.pinterest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
@GetMapping("/login")
    public ResponseEntity<String> login() {
        String authUrl = pinterestService.getPinterestAuthUrl();
        return ResponseEntity.ok(authUrl);
    }

    @GetMapping("/callback")
    public ResponseEntity<String> callback(@RequestParam("code") String code) {
        return ResponseEntity.ok("Received Code from Pinterest: " + code);
    }
@PostMapping("/schedule")
public ResponseEntity<String> schedulePin(@RequestBody PinterestPinDTO dto) {
    PinterestScheduledPost post = new PinterestScheduledPost();
    
    // Agar @Data kaam nahi kar raha, toh check karein spelling sahi hai
    post.setTitle(dto.getTitle());
    post.setDescription(dto.getDescription());
    post.setBoardId(dto.getBoardId());
    post.setImageUrl(dto.getImageUrl());
    post.setDestinationLink(dto.getDestinationLink());
    post.setScheduledTime(dto.getScheduledTime());
    
    // Yahan status set karein
    post.setStatus("PENDING"); 
    
    repository.save(post);
    return ResponseEntity.ok("Pin scheduled successfully for " + dto.getScheduledTime());
}}
