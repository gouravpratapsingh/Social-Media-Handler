package social_media.social_media_handler.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import social_media.social_media_handler.entity.youtube.YouTubeAccount;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @Column(name = "user_id", nullable = false, updatable = false)
    private String id;

    @Column(name = "username", nullable = false)
    private String username;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    // 🔐 FORGOT PASSWORD
    private String resetToken;

    private LocalDateTime resetTokenExpiry;

    /**
     * One User → Many YouTube Accounts
     */
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    @JsonManagedReference
    private List<YouTubeAccount> youtubeAccounts = new ArrayList<>();

    /* ================= Helper methods ================= */

    public void addYouTubeAccount(YouTubeAccount account) {
        youtubeAccounts.add(account);
        account.setUser(this);
    }

    public void removeYouTubeAccount(YouTubeAccount account) {
        youtubeAccounts.remove(account);
        account.setUser(null);
    }
}
