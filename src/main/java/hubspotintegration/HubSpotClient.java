package hubspotintegration;

import hubspotintegration.exception.HubSpotException;
import hubspotintegration.response.TokenResponse;
import hubspotintegration.utils.RateLimitUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientResponseException;

import java.util.function.Consumer;

@Component
public class HubSpotClient {

    private static final Logger log = LoggerFactory.getLogger(HubSpotClient.class);

    @Value("${hubspot.api.url}")
    private String apiUrl;

    @Value("${hubspot.token.url}")
    private String tokenUrl;

    @Value("${hubspot.redirect.uri}")
    private String redirectUri;

    @Value("${hubspot.client.id}")
    private String clientId;

    @Value("${hubspot.client.secret}")
    private String clientSecret;

    @Autowired
    private RestClient restClient;
    public <T, B> T post(String uri, B body, Class<T> responseType, String token) {
        RateLimitUtils.validateRateLimit();

        try {
            return
                    restClient.post()
                            .uri(apiUrl.concat(uri))
                            .header("Authorization", "Bearer " + token)
                            .body(body)
                            .retrieve()
                            .body(responseType);
        } catch (HttpClientErrorException ex) {
            throw new HubSpotException("Erro ao comunicar com API externa", ex.getResponseBodyAsString(), ex.getStatusCode());
        } catch (RestClientResponseException ex) {
            log.error("Erro de resposta: {}", ex.getResponseBodyAsString());
            throw new HubSpotException("Erro de resposta da API externa", ex.getResponseBodyAsString(), ex.getStatusCode());
        } catch (Exception ex) {
            log.error("Erro inesperado ao comunicar com API externa", ex);
            throw new HubSpotException("Erro inesperado", ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public TokenResponse requestAccessToken(String code) {
        return sendAccountTokenRequest("authorization_code", form -> {
            form.add("code", code);
            form.add("redirect_uri", redirectUri);
        });
    }

    public TokenResponse refreshToken(String refreshToken) {
        return sendAccountTokenRequest("refresh_token", form -> {
            form.add("refresh_token", refreshToken);
        });
    }

    private TokenResponse sendAccountTokenRequest(String grantType, Consumer<MultiValueMap<String, String>> paramsBuilder) {
        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();
        form.add("grant_type", grantType);
        form.add("client_id", clientId);
        form.add("client_secret", clientSecret);

        paramsBuilder.accept(form);

        return restClient
                .post()
                .uri(tokenUrl)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(form)
                .retrieve()
                .body(TokenResponse.class);
    }

}