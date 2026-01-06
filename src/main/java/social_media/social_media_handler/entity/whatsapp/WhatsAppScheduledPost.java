package social_media.social_media_handler.entity.whatsapp;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "scheduled_posts")
public class WhatsAppScheduledPost {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String platform; // "WHATSAPP", "INSTAGRAM", "FACEBOOK", "LINKEDIN", "X" [cite: 144]
    private String recipient; // Phone number or Social Media ID
    
    @Column(columnDefinition = "TEXT")
    private String content; // Post ya message ka content [cite: 44]
    
    private LocalDateTime scheduledTime; // Kab post publish karni hai [cite: 46]
    private String status; // "PENDING", "SENT", "FAILED", "DRAFT" [cite: 47, 94]

    // 1. Default Constructor (JPA ke liye zaroori hai)
    public WhatsAppScheduledPost() {
    }

    // 2. Parameterized Constructor (Data save karne ke liye easy rehta hai)
    public WhatsAppScheduledPost(String platform, String recipient, String content, LocalDateTime scheduledTime, String status) {
        this.platform = platform;
        this.recipient = recipient;
        this.content = content;
        this.scheduledTime = scheduledTime;
        this.status = status;
    }

    // 3. Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public String getRecipient() {
        return recipient;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getScheduledTime() {
        return scheduledTime;
    }

    public void setScheduledTime(LocalDateTime scheduledTime) {
        this.scheduledTime = scheduledTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}