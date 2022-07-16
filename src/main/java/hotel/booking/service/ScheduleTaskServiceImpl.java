package hotel.booking.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ScheduleTaskServiceImpl implements ScheduleTaskService {
    private final Logger logger = LoggerFactory.getLogger(this.getClass().getName());

    @Value("${api-url}")
    private String apiUrl;
    @Value("${web-react-url}")
    private String webUrl;


    @Override
//     @Scheduled(fixedRate = 900000)
    public void scheduleTaskWithFixedRate() {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
        headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
        HttpEntity<String> httpEntity = new HttpEntity<>(headers);
        RestTemplate restTemplate = new RestTemplate();
        try {
            ResponseEntity<String> responseEntity = restTemplate.exchange(apiUrl+"/test", HttpMethod.GET, httpEntity, String.class);
            logger.info(responseEntity.getBody());
            restTemplate.exchange(webUrl, HttpMethod.GET,httpEntity, String.class);
        } catch (Exception exception) {
            logger.info("call k dc");
        }
    }
}
