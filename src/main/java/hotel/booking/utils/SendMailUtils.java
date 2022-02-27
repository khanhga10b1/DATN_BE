/**
 * 
 */
package hotel.booking.utils;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Map;

import javax.mail.internet.MimeMessage;

import hotel.booking.domain.SendMailDomain;
import hotel.booking.exception.CustomException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.context.Context;

@Component
public class SendMailUtils {

    private final Logger logger = LoggerFactory.getLogger(this.getClass().getName());

    @Autowired
    public JavaMailSender mailSender;
    @Autowired
    private SpringTemplateEngine templateEngine;
    
    @Value("${spring.mail.username}")
    private String mailFrom;

    public boolean sendMailWithTemplate(SendMailDomain mail, String templateFile, Map<String, Object> paramsInfo) {

        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                    StandardCharsets.UTF_8.name());

            Context context = new Context();
            context.setVariables(paramsInfo);
            helper.setSubject(mail.getSubject());
            helper.setFrom(mailFrom);
            helper.setTo(Arrays.copyOf(mail.getToEmail().toArray(), mail.getToEmail().toArray().length, String[].class));
//            if (StringUtils.isEmpty(mail.getCc())) {
//                helper.setCc(mail.getCc());
//            }
//            if (StringUtils.isEmpty(mail.getBcc())) {
//                helper.setBcc(mail.getBcc());
//            }
            String html = templateEngine.process(templateFile, context);
            helper.setText(html, true);
            mailSender.send(message);

        } catch (Exception ex) {
            logger.error(StringUtils.buildLog(ex.getMessage(), Thread.currentThread().getStackTrace()[1].getLineNumber()));
            logger.error(StringUtils.buildLog(Error.BAD_REQUEST, Thread.currentThread().getStackTrace()[1].getLineNumber()));
            throw new CustomException(Error.BAD_REQUEST.getMessage(),
                    Error.BAD_REQUEST.getCode(), HttpStatus.BAD_REQUEST);
        }
        return true;
    }
}
