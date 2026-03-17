package org.example.unicare_backend.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    @Override
    public void sendOtpEmail(String to, String otp) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("UniCare <" + fromEmail + ">");
        message.setTo(to);
        message.setSubject("UniCare - Mã xác thực tài khoản");
        message.setText("Chào bạn,\n\nMã xác thực của bạn là: " + otp + "\n\nVui lòng nhập mã này để kích hoạt tài khoản. Mã có hiệu lực trong 5 phút.\n\nTrân trọng,\nUniCare Team");
        mailSender.send(message);
    }

    @Override
    public void sendNewPassword(String to, String newPassword) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("UniCare <" + fromEmail + ">");
        message.setTo(to);
        message.setSubject("UniCare - Mật khẩu mới");
        message.setText("Chào bạn,\n\nMật khẩu mới của bạn là: " + newPassword + "\n\nVui lòng đăng nhập và đổi mật khẩu ngay lập tức để bảo mật tài khoản.\n\nTrân trọng,\nUniCare Team");
        mailSender.send(message);
    }
}
