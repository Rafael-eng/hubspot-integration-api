package hubspotintegration.service;

import hubspotintegration.HubSpotClient;
import hubspotintegration.exception.HubSpotException;
import hubspotintegration.response.TokenResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.lang.reflect.Field;
import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthenticationServiceTest {

    @Mock
    private HubSpotClient restResource;

    @InjectMocks
    private AuthenticationService authenticationService;

    @Test
    void deveRenovarTokenComRefreshToken_QuandoAccessTokenExpirado() throws Exception {
        TokenResponse response = mock(TokenResponse.class);
        when(response.accessToken()).thenReturn("token-renovado");
        when(response.expiresIn()).thenReturn(3600);
        when(restResource.refreshToken("refresh-token")).thenReturn(response);

        injectPrivateField(authenticationService, "refreshToken", "refresh-token");
        injectPrivateField(authenticationService, "accessToken", null);
        injectPrivateField(authenticationService, "expiryTime", Instant.now().minusSeconds(10));

        String token = authenticationService.getToken(null);

        assertEquals("token-renovado", token);
        verify(restResource).refreshToken("refresh-token");
    }

    @Test
    void deveRetornarMesmoToken_QuandoAindaValido() {
        TokenResponse response = mock(TokenResponse.class);
        when(response.accessToken()).thenReturn("token-atual");
        when(response.refreshToken()).thenReturn("refresh-token");
        when(response.expiresIn()).thenReturn(3600);

        when(restResource.requestAccessToken("auth-code")).thenReturn(response);

        String token1 = authenticationService.getToken("auth-code");

        String token2 = authenticationService.getToken(null);

        assertEquals(token1, token2);
        verify(restResource, times(1)).requestAccessToken("auth-code");
    }

    @Test
    void deveLancarExcecao_QuandoNaoHouverRefreshNemAuthorizationCode() {
        HubSpotException exception = assertThrows(
                HubSpotException.class,
                () -> authenticationService.getToken(null)
        );

        assertEquals("Erro ao obter ou renovar o token: Sem refresh token ou authorization code.", exception.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
    }

    @Test
    void deveLancarExcecao_QuandoRestFalha() {
        when(restResource.requestAccessToken("auth-code")).thenThrow(new RuntimeException("Falha externa"));

        HubSpotException exception = assertThrows(
                HubSpotException.class,
                () -> authenticationService.getToken("auth-code")
        );

        assertEquals("Erro ao obter ou renovar o token: Falha externa", exception.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
    }

    @Test
    void deveRetornarUrlDeAutorizacaoCorretamente() {
        injectPrivateField(authenticationService, "clientId", "123-client");
        injectPrivateField(authenticationService, "redirectUri", "https://meusistema.com/callback");
        injectPrivateField(authenticationService, "authUrl", "https://app.hubspot.com/oauth/authorize");
        injectPrivateField(authenticationService, "scope", "contacts");

        String url = authenticationService.redirectToAuthorizationUrl();

        String expected = "https://app.hubspot.com/oauth/authorize"
                + "?client_id=123-client"
                + "&redirect_uri=https://meusistema.com/callback"
                + "&scope=contacts"
                + "&response_type=code";

        assertEquals(expected, url);
    }


    private void injectPrivateField(Object target, String fieldName, Object value) {
        try {
            Field field = target.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(target, value);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao injetar o campo '" + fieldName + "'", e);
        }
    }
}

