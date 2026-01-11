package social_media.social_media_handler.service.youtube;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeTokenRequest;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.Channel;
import com.google.api.services.youtube.model.ChannelListResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import social_media.social_media_handler.config.YouTubeConfig;
import social_media.social_media_handler.entity.User;
import social_media.social_media_handler.entity.youtube.YouTubeAccount;
import social_media.social_media_handler.repository.UserRepository;
import social_media.social_media_handler.repository.youtube.YouTubeAccountRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class YouTubeAuthService {

    private final YouTubeConfig config;
    private final YouTubeAccountRepository youtubeAccountRepository;
    private final UserRepository userRepository;

    /**
     * Step 1: Generate Google OAuth URL
     */
    public String getAuthorizationUrl(String email) {
        return "https://accounts.google.com/o/oauth2/v2/auth"
                + "?client_id=" + config.getClientId()
                + "&redirect_uri=" + config.getRedirectUri()
                + "&response_type=code"
                + "&access_type=offline"
                + "&prompt=consent"
                + "&state=" + email   // 🔥 PASS USER ID HERE
                + "&scope="
                + "https://www.googleapis.com/auth/youtube.upload "
                + "https://www.googleapis.com/auth/youtube.readonly "
                + "https://www.googleapis.com/auth/yt-analytics.readonly";
    }


    /**
     * Step 2: Handle OAuth callback
     */
    public void handleCallback(String code, String email) throws Exception {

        // Fetch logged-in user
        User user = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));

        // Exchange code for access token
        GoogleTokenResponse tokenResponse = new GoogleAuthorizationCodeTokenRequest(
                        GoogleNetHttpTransport.newTrustedTransport(),
                        GsonFactory.getDefaultInstance(),
                        "https://oauth2.googleapis.com/token",
                        config.getClientId(),
                        config.getClientSecret(),
                        code,
                        config.getRedirectUri()
                ).execute();

        // Use token to get channel info from YouTube API
        Credential credential = new GoogleCredential().setAccessToken(tokenResponse.getAccessToken());
        YouTube youtube = new YouTube.Builder(
                GoogleNetHttpTransport.newTrustedTransport(),
                GsonFactory.getDefaultInstance(),
                credential
        ).setApplicationName("social-media-handler").build();

        YouTube.Channels.List request = youtube.channels().list("snippet");
        ChannelListResponse response = request.setMine(true).execute();
        
        if (response.getItems().isEmpty()) {
            throw new RuntimeException("No YouTube channel found for this account.");
        }

        Channel channel = response.getItems().get(0);
        String channelId = channel.getId();
        String channelName = channel.getSnippet().getTitle();

        // Fetch or create YouTube account based on channelId
        YouTubeAccount account = youtubeAccountRepository.findByChannelIdAndUser_Id(channelId, user.getId())
                .orElse(new YouTubeAccount());
        account.setUser(user);
        account.setChannelId(channelId);
        account.setChannelName(channelName);
        account.setAccessToken(tokenResponse.getAccessToken());

        // Only set refresh token if it's provided in the response
        if (tokenResponse.getRefreshToken() != null) {
            account.setRefreshToken(tokenResponse.getRefreshToken());
        }
        account.setTokenExpiry(
                LocalDateTime.now().plusSeconds(tokenResponse.getExpiresInSeconds())
        );
        youtubeAccountRepository.save(account);
    }

    public List<YouTubeAccount> getAllChannels(String email){
        User user = userRepository.findByEmail(email).orElseThrow(()-> new RuntimeException("user not found"));
        return youtubeAccountRepository.findAllByUser_Id(user.getId());
    }
}
