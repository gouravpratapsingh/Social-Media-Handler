package social_media.social_media_handler.services;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import social_media.social_media_handler.entity.ScheduledPost;
import social_media.social_media_handler.repository.ScheduledPostRepository;

@Service
public class PostSchedulerService {

    private final ScheduledPostRepository repository;
    private final WhatsAppService whatsappService;

    public PostSchedulerService(ScheduledPostRepository repository, WhatsAppService whatsappService) {
        this.repository = repository;
        this.whatsappService = whatsappService;
    }

    @Scheduled(fixedRate = 60000)
    public void runAutomation() {
        List<ScheduledPost> pendingList = repository.findByStatusAndScheduledTimeBefore("PENDING", LocalDateTime.now());

        for (ScheduledPost post : pendingList) {
            try {
                if ("WHATSAPP".equalsIgnoreCase(post.getPlatform())) {
                    whatsappService.sendMessage(post.getRecipient(), post.getContent());
                }
                // Agar post successfully chali gayi
                post.setStatus("SENT"); 
            } catch (Exception e) {
                post.setStatus("FAILED");
            }
            repository.save(post);
        }
    }
}