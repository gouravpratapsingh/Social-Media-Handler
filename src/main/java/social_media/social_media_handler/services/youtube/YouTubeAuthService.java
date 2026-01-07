package social_media.social_media_handler.services.youtube;

import com.google.api.client.googleapis.auth.oauth2.*;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import social_media.social_media_handler.config.YouTubeConfig;
import social_media.social_media_handler.entity.User;
import social_media.social_media_handler.entity.youtube.YouTubeAccount;
import social_media.social_media_handler.repository.youtube.YouTubeAccountRepository;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class YouTubeAuthService {

    private final YouTubeConfig config;
    private final YouTubeAccountRepository repository;

    public String getAuthorizationUrl() {
        return "https://accounts.google.com/o/oauth2/v2/auth"
                + "?client_id=" + config.clientId
                + "&redirect_uri=" + config.redirectUri
                + "&response_type=code"
                + "&scope=https://www.googleapis.com/auth/youtube.upload "
                + "https://www.googleapis.com/auth/youtube.readonly "
                + "https://www.googleapis.com/auth/yt-analytics.readonly";
    }

    public void handleCallback(String code, User user) throws Exception {

        GoogleTokenResponse tokenResponse =
                new GoogleAuthorizationCodeTokenRequest(
                        GoogleNetHttpTransport.newTrustedTransport(),
                        GsonFactory.getDefaultInstance(),
                        "https://oauth2.googleapis.com/token",
                        config.clientId,
                        config.clientSecret,
                        code,
                        config.redirectUri
                ).execute();

        YouTubeAccount account = repository
                .findByUser_Id(user.getId())
                .orElse(new YouTubeAccount());

        account.setAccessToken(tokenResponse.getAccessToken());
        account.setRefreshToken(tokenResponse.getRefreshToken());
        account.setTokenExpiry(LocalDateTime.now()
                .plusSeconds(tokenResponse.getExpiresInSeconds()));
        account.setUser(user);

        repository.save(account);
    }
}
