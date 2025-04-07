package hubspotintegration.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/webhook/hubspot")
public class WebhookController {

    private static final Logger log  = LoggerFactory.getLogger(WebhookController.class);

    @PostMapping("/contact")
    public ResponseEntity<Void> onContactCreated(@RequestBody String payload) {
        log.info("Recebido contato criado do HubSpot: {}", payload);
        return ResponseEntity.ok().build();
    }
}
