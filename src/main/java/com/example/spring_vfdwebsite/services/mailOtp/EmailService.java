package com.example.spring_vfdwebsite.services.mailOtp;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.example.spring_vfdwebsite.exceptions.HttpException;

import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {
    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    public void sendOtpEmail(String toEmail, String otp, String fullName) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(fromEmail, "Volleyball Federation Da Nang ");
            helper.setTo(toEmail);
            helper.setSubject("Mã xác thực OTP - Volleyball Federation Da Nang ");

            String htmlContent = buildOtpEmailTemplate(otp, fullName, "đăng ký");
            helper.setText(htmlContent, true);

            mailSender.send(message);
            log.info("Registration OTP email sent successfully to: {}", toEmail);
        } catch (Exception e) {
            log.error("Failed to send registration OTP email to: {}", toEmail, e);
            throw new HttpException("Failed to send verification email", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public void sendChangePasswordOtpEmail(String toEmail, String otp, String fullName) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(fromEmail, "Volleyball Federation Da Nang ");
            helper.setTo(toEmail);
            helper.setSubject("Mã xác thực thay đổi mật khẩu - Volleyball Federation Da Nang ");

            String htmlContent = buildChangePasswordOtpTemplate(otp, fullName);
            helper.setText(htmlContent, true);

            mailSender.send(message);
            log.info("Change password OTP email sent successfully to: {}", toEmail);
        } catch (Exception e) {
            log.error("Failed to send change password OTP email to: {}", toEmail, e);
            throw new HttpException("Failed to send change password email", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public void sendForgotPasswordOtpEmail(String toEmail, String otp, String fullName) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(fromEmail, "Volleyball Federation Da Nang ");
            helper.setTo(toEmail);
            helper.setSubject("Khôi phục mật khẩu - Volleyball Federation Da Nang ");

            String htmlContent = buildForgotPasswordOtpTemplate(otp, fullName);
            helper.setText(htmlContent, true);

            mailSender.send(message);
            log.info("Forgot password OTP email sent successfully to: {}", toEmail);
        } catch (Exception e) {
            log.error("Failed to send forgot password OTP email to: {}", toEmail, e);
            throw new HttpException("Failed to send forgot password email", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // ================== GENERAL EMAIL ==================
    public void sendEmail(String toEmail, String subject, String htmlContent) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(fromEmail, "Volleyball Federation Da Nang ");
            helper.setTo(toEmail);
            helper.setSubject(subject);
            helper.setText(htmlContent, true);

            mailSender.send(message);
            log.info("Email sent successfully to: {}", toEmail);
        } catch (Exception e) {
            log.error("Failed to send email to: {}", toEmail, e);
            throw new HttpException("Failed to send email", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Respond email submit registration form tournament
    public void sendRespondSubmitRegistrationFormEmail(String toEmail, String fullName, String teamName, String phoneNumber, String registrationUnit, Integer numberAthletes, String fileUrl, String tournamentName, String status) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(fromEmail, "Volleyball Federation Da Nang ");
            helper.setTo(toEmail);
            helper.setSubject("Phản hồi đăng ký tham gia giải đấu - Volleyball Federation Da Nang ");

            String htmlContent = buildTournamentRegistrationEmail(fullName, teamName, toEmail, phoneNumber, registrationUnit, numberAthletes, fileUrl, tournamentName, status);
            helper.setText(htmlContent, true);

            mailSender.send(message);
            log.info("Respond submit registration form email sent successfully to: {}", toEmail);
        } catch (Exception e) {
            log.error("Failed to send respond submit registration form email to: {}", toEmail, e);
            throw new HttpException("Failed to send respond submit registration form email", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Registration approval response email
    public void sendRegistrationApprovalResponseEmail(String toEmail, String fullName, String teamName, String registrationUnit, String tournamentName, String status, String adminNote) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(fromEmail, "Volleyball Federation Da Nang ");
            helper.setTo(toEmail);
            helper.setSubject("Phản hồi phê duyệt đăng ký - Volleyball Federation Da Nang ");

            String htmlContent = buildTournamentApprovalEmail(fullName, teamName, registrationUnit, tournamentName, status, adminNote);
            helper.setText(htmlContent, true);

            mailSender.send(message);
            log.info("Registration approval response email sent successfully to: {}", toEmail);
        } catch (Exception e) {
            log.error("Failed to send registration approval response email to: {}", toEmail, e);
            throw new HttpException("Failed to send registration approval response email", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private String buildOtpEmailTemplate(String otp, String fullName, String purpose) {
        String template = """
                <!DOCTYPE html>
                <html>
                <head>
                    <meta charset="UTF-8">
                    <title>Xác thực OTP</title>
                    <style>
                        body { font-family: Arial, sans-serif; margin: 0; padding: 20px; background-color: #f5f5f5; }
                        .container { max-width: 600px; margin: 0 auto; background-color: white; padding: 30px; border-radius: 10px; box-shadow: 0 2px 10px rgba(0,0,0,0.1); }
                        .header { text-align: center; margin-bottom: 30px; }
                        .otp-code { background-color: #f8f9fa; padding: 20px; text-align: center; border-radius: 8px; margin: 20px 0; }
                        .otp-number { font-size: 32px; font-weight: bold; color: #28a745; letter-spacing: 5px; }
                        .footer { margin-top: 30px; padding-top: 20px; border-top: 1px solid #eee; color: #666; font-size: 14px; }
                        .warning { background-color: #fff3cd; padding: 15px; border-radius: 5px; color: #856404; margin: 15px 0; }
                    </style>
                </head>
                <body>
                    <div class="container">
                        <div class="header">
                            <h1 style="color: #28a745;">Volleyball Federation Da Nang </h1>
                            <h2>Xác thực tài khoản của bạn</h2>
                        </div>

                        <p>Xin chào <strong>%1$s</strong>,</p>

                        <p>Cảm ơn bạn đã đăng ký tài khoản tại Volleyball Federation Da Nang . Để hoàn tất quá trình %2$s, vui lòng nhập mã OTP bên dưới:</p>

                        <div class="otp-code">
                            <p style="margin: 0; color: #666;">Mã xác thực OTP của bạn là:</p>
                            <div class="otp-number">%3$s</div>
                        </div>

                        <div class="warning">
                            <strong>Lưu ý quan trọng:</strong>
                            <ul style="margin: 10px 0; padding-left: 20px;">
                                <li>Mã OTP này có hiệu lực trong <strong>5 phút</strong></li>
                                <li>Không chia sẻ mã này với bất kỳ ai</li>
                                <li>Nếu bạn không yêu cầu %2$s, vui lòng bỏ qua email này</li>
                            </ul>
                        </div>

                        <p>Nếu bạn gặp bất kỳ vấn đề gì, vui lòng liên hệ với chúng tôi qua email hỗ trợ.</p>

                        <div class="footer">
                            <p>Trân trọng,<br>Volleyball Federation Da Nang</p>
                            <p style="margin-top: 15px;"><small>Email này được gửi tự động, vui lòng không trả lời.</small></p>
                        </div>
                    </div>
                </body>
                </html>
                """;
        return String.format(template, fullName, purpose, otp);
    }

    private String buildChangePasswordOtpTemplate(String otp, String fullName) {
        String template = """
                <!DOCTYPE html>
                <html>
                <head>
                    <meta charset="UTF-8">
                    <title>Thay đổi mật khẩu</title>
                    <style>
                        body { font-family: Arial, sans-serif; margin: 0; padding: 20px; background-color: #f5f5f5; }
                        .container { max-width: 600px; margin: 0 auto; background-color: white; padding: 30px; border-radius: 10px; box-shadow: 0 2px 10px rgba(0,0,0,0.1); }
                        .header { text-align: center; margin-bottom: 30px; }
                        .otp-code { background-color: #f8f9fa; padding: 20px; text-align: center; border-radius: 8px; margin: 20px 0; }
                        .otp-number { font-size: 32px; font-weight: bold; color: #dc3545; letter-spacing: 5px; }
                        .footer { margin-top: 30px; padding-top: 20px; border-top: 1px solid #eee; color: #666; font-size: 14px; }
                        .warning { background-color: #f8d7da; padding: 15px; border-radius: 5px; color: #721c24; margin: 15px 0; }
                    </style>
                </head>
                <body>
                    <div class="container">
                        <div class="header">
                            <h1 style="color: #dc3545;">🔐 Volleyball Federation Da Nang </h1>
                            <h2>Thay đổi mật khẩu</h2>
                        </div>

                        <p>Xin chào <strong>%1$s</strong>,</p>

                        <p>Chúng tôi nhận được yêu cầu thay đổi mật khẩu cho tài khoản của bạn. Để xác nhận thay đổi, vui lòng nhập mã OTP bên dưới:</p>

                        <div class="otp-code">
                            <p style="margin: 0; color: #666;">Mã xác thực OTP của bạn là:</p>
                            <div class="otp-number">%2$s</div>
                        </div>

                        <div class="warning">
                            <strong>⚠️ Lưu ý bảo mật:</strong>
                            <ul style="margin: 10px 0; padding-left: 20px;">
                                <li>Mã OTP này có hiệu lực trong <strong>5 phút</strong></li>
                                <li>Tuyệt đối không chia sẻ mã này với bất kỳ ai</li>
                                <li>Nếu bạn không yêu cầu thay đổi mật khẩu, vui lòng bỏ qua email này và kiểm tra bảo mật tài khoản</li>
                                <li>Sau khi thay đổi mật khẩu thành công, bạn sẽ cần đăng nhập lại</li>
                            </ul>
                        </div>

                        <p>Nếu bạn gặp bất kỳ vấn đề gì, vui lòng liên hệ với chúng tôi ngay lập tức qua email hỗ trợ.</p>

                        <div class="footer">
                            <p>Trân trọng,<br>Volleyball Federation Da Nang</p>
                            <p style="margin-top: 15px;"><small>Email này được gửi tự động, vui lòng không trả lời.</small></p>
                        </div>
                    </div>
                </body>
                </html>
                """;
        return String.format(template, fullName, otp);
    }

    private String buildForgotPasswordOtpTemplate(String otp, String fullName) {
        String template = """
                <!DOCTYPE html>
                <html>
                <head>
                    <meta charset="UTF-8">
                    <title>Khôi phục mật khẩu</title>
                    <style>
                        body { font-family: Arial, sans-serif; margin: 0; padding: 20px; background-color: #f5f5f5; }
                        .container { max-width: 600px; margin: 0 auto; background-color: white; padding: 30px; border-radius: 10px; box-shadow: 0 2px 10px rgba(0,0,0,0.1); }
                        .header { text-align: center; margin-bottom: 30px; }
                        .otp-code { background-color: #f8f9fa; padding: 20px; text-align: center; border-radius: 8px; margin: 20px 0; }
                        .otp-number { font-size: 32px; font-weight: bold; color: #fd7e14; letter-spacing: 5px; }
                        .footer { margin-top: 30px; padding-top: 20px; border-top: 1px solid #eee; color: #666; font-size: 14px; }
                        .warning { background-color: #fff3cd; padding: 15px; border-radius: 5px; color: #856404; margin: 15px 0; }
                    </style>
                </head>
                <body>
                    <div class="container">
                        <div class="header">
                            <h1 style="color: #fd7e14;">🔑 Volleyball Federation Da Nang </h1>
                            <h2>Khôi phục mật khẩu</h2>
                        </div>

                        <p>Xin chào <strong>%1$s</strong>,</p>

                        <p>Chúng tôi nhận được yêu cầu khôi phục mật khẩu cho tài khoản của bạn. Để tiếp tục quá trình đặt lại mật khẩu, vui lòng nhập mã OTP bên dưới:</p>

                        <div class="otp-code">
                            <p style="margin: 0; color: #666;">Mã xác thực OTP của bạn là:</p>
                            <div class="otp-number">%2$s</div>
                        </div>

                        <div class="warning">
                            <strong>🔒 Hướng dẫn khôi phục:</strong>
                            <ul style="margin: 10px 0; padding-left: 20px;">
                                <li>Mã OTP này có hiệu lực trong <strong>5 phút</strong></li>
                                <li>Sau khi xác thực OTP, bạn sẽ được yêu cầu nhập mật khẩu mới</li>
                                <li>Tuyệt đối không chia sẻ mã này với bất kỳ ai</li>
                                <li>Nếu bạn không yêu cầu khôi phục mật khẩu, vui lòng bỏ qua email này và kiểm tra bảo mật tài khoản</li>
                            </ul>
                        </div>

                        <p><strong>Lưu ý:</strong> Sau khi đặt lại mật khẩu thành công, tất cả các phiên đăng nhập hiện tại sẽ bị hủy và bạn cần đăng nhập lại.</p>

                        <p>Nếu bạn gặp bất kỳ vấn đề gì, vui lòng liên hệ với chúng tôi ngay lập tức qua email hỗ trợ.</p>

                        <div class="footer">
                            <p>Trân trọng,<br>Volleyball Federation Da Nang</p>
                            <p style="margin-top: 15px;"><small>Email này được gửi tự động, vui lòng không trả lời.</small></p>
                        </div>
                    </div>
                </body>
                </html>
                """;
        return String.format(template, fullName, otp);
    }

    // Respond email submit registration form tournament
    public String buildTournamentRegistrationEmail(
            String fullName,
            String teamName,
            String email,
            String phoneNumber,
            String registrationUnit,
            Integer numberAthletes,
            String fileUrl,
            String tournamentName,
            String status) {

        String statusColor;
        String statusLabel;

        switch (status.toLowerCase()) {
            case "accepted":
                statusColor = "#28a745";
                statusLabel = "Đã được chấp nhận";
                break;
            case "rejected":
                statusColor = "#dc3545";
                statusLabel = "Bị từ chối";
                break;
            default:
                statusColor = "#007bff";
                statusLabel = "Đang chờ phê duyệt";
        }
        String template = """
                <!DOCTYPE html>
                <html>
                <head>
                    <meta charset="UTF-8">
                    <title>Xác nhận đăng ký giải đấu</title>
                    <style>
                        body { font-family: Arial, sans-serif; background-color: #f5f5f5; margin: 0; padding: 20px; }
                        .container { max-width: 600px; margin: 0 auto; background-color: #ffffff; padding: 30px;
                                     border-radius: 10px; box-shadow: 0 2px 10px rgba(0,0,0,0.1); }
                        .header { text-align: center; margin-bottom: 20px; color: #007bff; }
                        .label { font-weight: bold; color: #333; }
                        .value { color: #000; }
                        .highlight { color: #28a745; font-weight: bold; }
                        .footer { margin-top: 30px; color: #555; font-size: 14px; text-align: center; }
                        .file-link { margin-top: 10px; }
                    </style>
                </head>
                <body>
                    <div class="container">
                        <div class="header">
                            <h2>Xác nhận đăng ký giải đấu</h2>
                        </div>
                        <p>Xin chào <strong>%1$s</strong></p>
                        <p>Chúng tôi đã nhận được đăng ký tham gia giải:</p>

                        <p class="highlight">%8$s</p>

                        <p>Dưới đây là thông tin đăng ký của bạn:</p>

                        <p><span class="label">Tên đội:</span> <span class="value">%2$s</span></p>
                        <p><span class="label">Email liên hệ:</span> <span class="value">%3$s</span></p>
                        <p><span class="label">Số điện thoại:</span> <span class="value">%4$s</span></p>
                        <p><span class="label">Đơn vị đăng ký:</span> <span class="value">%5$s</span></p>
                        <p><span class="label">Số lượng vận động viên:</span> <span class="value">%6$s</span></p>
                        <p><span class="label">Tình trạng đơn:</span>
                            <span class="value" style="color:%10$s; font-weight:bold">%9$s</span>
                        </p>

                        <p class="file-link">
                            <span class="label">File thông tin đội:</span>
                            <a href='%7$s' target="_blank">Xem tại đây</a>
                        </p>

                        <p>Chúng tôi sẽ xem xét và phản hồi kết quả phê duyệt sớm nhất có thể.</p>

                        <p class="footer">Trân trọng,<br>Volleyball Federation Da Nang</p>
                    </div>
                </body>
                </html>
                """;
        return String.format(template, fullName, teamName, email, phoneNumber, registrationUnit,
                String.valueOf(numberAthletes), fileUrl, tournamentName, statusLabel, statusColor);
    }

    // Registration approval response email
    public String buildTournamentApprovalEmail(
            String fullName,
            String teamName,
            String registrationUnit,
            String tournamentName,
            String status,
            String adminNote // ghi chú từ admin (tùy chọn)
    ) {

        String statusColor;
        String statusLabel;
        String statusMessage;

        switch (status.toLowerCase()) {
            case "accepted":
                statusColor = "#28a745";
                statusLabel = "Đã được phê duyệt";
                statusMessage = "Chúc mừng! Đơn đăng ký của bạn đã được phê duyệt. Vui lòng chuẩn bị đội hình tham gia giải theo đúng lịch trình.";
                break;

            case "rejected":
                statusColor = "#dc3545";
                statusLabel = "Bị từ chối";
                statusMessage = "Rất tiếc! Đơn đăng ký của bạn chưa được chấp nhận. Bạn có thể xem ghi chú để biết thêm chi tiết.";
                break;

            default:
                statusColor = "#007bff";
                statusLabel = "Không xác định";
                statusMessage = "";
        }

        if (adminNote == null || adminNote.trim().isEmpty()) {
            adminNote = "Không có ghi chú.";
        }

        String template = """
                <!DOCTYPE html>
                <html>
                <head>
                    <meta charset="UTF-8">
                    <title>Kết quả phê duyệt đăng ký giải đấu</title>
                    <style>
                        body { font-family: Arial, sans-serif; background-color: #f5f5f5; margin: 0; padding: 20px; }
                        .container { max-width: 600px; margin: 0 auto; background-color: #ffffff; padding: 30px;
                                     border-radius: 10px; box-shadow: 0 2px 10px rgba(0,0,0,0.1); }
                        .header { text-align: center; margin-bottom: 20px; color: #007bff; }
                        .label { font-weight: bold; color: #333; }
                        .value { color: #000; }
                        .footer { margin-top: 30px; color: #555; font-size: 14px; text-align: center; }
                    </style>
                </head>
                <body>
                    <div class="container">
                        <div class="header">
                            <h2>Kết quả phê duyệt đăng ký</h2>
                        </div>

                        <p>Xin chào <strong>%1$s</strong>,</p>

                        <p>Đơn đăng ký tham gia giải đấu <strong>%4$s</strong> của đội <strong>%2$s</strong> thuộc đơn vị <strong>%3$s</strong> đã có kết quả.</p>

                        <p><span class="label">Trạng thái:</span>
                            <span class="value" style="color:%6$s; font-weight:bold">%5$s</span>
                        </p>

                        <p>%7$s</p>
                        <p><span class="label">Ghi chú từ ban tổ chức:</span></p>
                        <p>%8$s</p>

                        <p class="footer">Trân trọng,<br>Volleyball Federation Da Nang</p>
                    </div>
                </body>
                </html>
                """;

        return String.format(
                template,
                fullName,
                teamName,
                registrationUnit,
                tournamentName,
                statusLabel,
                statusColor,
                statusMessage,
                adminNote);
    }
}
