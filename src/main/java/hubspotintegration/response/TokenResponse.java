package hubspotintegration.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public record TokenResponse(
        @JsonProperty("token_type") String tokenType,
        @JsonProperty("refresh_token") String refreshToken,
        @JsonProperty("expires_in") int expiresIn,
        @JsonProperty("access_token") String accessToken
) {
    public TokenResponse(String accessToken, String refreshToken, int expiresIn) {
        this("bearer", refreshToken, expiresIn, accessToken);
    }
}
