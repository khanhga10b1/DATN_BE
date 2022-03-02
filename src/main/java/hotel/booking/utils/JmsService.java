package hotel.booking.utils;

import hotel.booking.domain.SendMailDomain;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class JmsService {

    @Autowired
    private SendMailUtils sendMailUtils;

    @JmsListener(destination = "sendMail", containerFactory = "myFactory")
    public void sendMail(SendMailDomain domain) {
        sendMailUtils.sendMailWithTemplate(domain);
    }
}
