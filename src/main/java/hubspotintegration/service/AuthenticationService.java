package hubspotintegration.service;

import hubspotintegration.HubSpotClient;
import hubspotintegration.exception.HubSpotException;
import hubspotintegration.response.TokenResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.Instant;

@Service
public class AuthenticationService {

    private static final Logger log = LoggerFactory.getLogger(AuthenticationService.class);
    private static final long EXPIRY_BUFFER_SECONDS = 60;

    @Value("${hubspot.client.id}")
    private String clientId;
    @Value("${hubspot.redirect.uri}")
    private String redirectUri;
    @Value("${hubspot.auth.url}")
    private String authUrl;
    @Value("${hubspot.scope}")
    private String scope;

    private String accessToken;
    private Instant expiryTime;
    private String refreshToken;;

    @Autowired
    private HubSpotClient restResource;

    public String redirectToAuthorizationUrl() {
        return UriComponentsBuilder.fromHttpUrl(authUrl)
                .queryParam("client_id", clientId)
                .queryParam("redirect_uri", redirectUri)
                .queryParam("scope", scope)
                .queryParam("response_type", "code")
                .build()
                .encode()
                .toUriString();
    }

    public synchronized String getToken(String authorizationCode) {
        try {
            if (accessToken == null || Instant.now().isAfter(expiryTime)) {
                log.info("Renovando ou obtendo novo token...");

                TokenResponse response;

                if (refreshToken == null && authorizationCode != null) {
                    response = restResource.requestAccessToken(authorizationCode);
                    this.refreshToken = response.refreshToken();
                } else if (refreshToken != null) {
                    response = restResource.refreshToken(refreshToken);
                } else {
                    throw new IllegalStateException("Sem refresh token ou authorization code.");
                }

                this.accessToken = response.accessToken();
                this.expiryTime = Instant.now().plusSeconds(response.expiresIn() - EXPIRY_BUFFER_SECONDS);
                return accessToken;

            } else {
               log.info("Token ainda válido.");
                return accessToken;
            }
        } catch (Exception e) {
            throw new HubSpotException("Erro ao obter ou renovar o token: ", e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
