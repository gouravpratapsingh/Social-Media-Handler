package social_media.social_media_handler.schedular;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import social_media.social_media_handler.entity.User;
import social_media.social_media_handler.service.email.EmailService;
import social_media.social_media_handler.util.EmailTemplateUtil;


@Component
@RequiredArgsConstructor
public class PostStatusNotifier {

    private final EmailService emailService;

    public void notifyPostSuccess(User user, String platform, String time) {
        emailService.sendEmail(
                user.getEmail(),
                "✅ Post Published Successfully",
                EmailTemplateUtil.postSuccess(
                        user.getUsername(),
                        platform,
                        time
                )
        );
    }

    public void notifyPostFailure(User user, String platform, String reason) {
        emailService.sendEmail(
                user.getEmail(),
                "❌ Post Failed",
                EmailTemplateUtil.postFailure(
                        user.getUsername(),
                        platform,
                        reason
                )
        );
    }
}

