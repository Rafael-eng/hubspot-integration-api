package hubspotintegration.service;

import hubspotintegration.HubSpotClient;
import hubspotintegration.request.ContactRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class ContactService {

    private static final Logger log  = LoggerFactory.getLogger(ContactService.class);

    @Value("${hubspot.api.endpoint.create-contact}")
    private String endpointCreateContact;

    @Autowired
    private AuthenticationService service;

    @Autowired
    private HubSpotClient restResource;

    public String createContact(ContactRequest contactRequest) {
        String contactResponse = restResource.post(endpointCreateContact, contactRequest, String.class, service.getToken(null));
        log.info("response: {}", contactResponse);
        return contactResponse;
    }
}
