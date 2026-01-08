package social_media.social_media_handler.entity.linkedin;

import jakarta.persistence.*;
import lombok.*;
import social_media.social_media_handler.entity.User;

import java.time.LocalDateTime;

@Entity
@Table(name = "linkedin_accounts")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LinkedInAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String linkedinUserId;

    @Column(length = 1000)
    private String accessToken;

    private LocalDateTime connectedAt;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}

