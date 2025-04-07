package hubspotintegration.exception;

import org.springframework.http.HttpStatusCode;

public class HubSpotException extends RuntimeException {

    private final HttpStatusCode status;

    public HubSpotException(String message, String cause, HttpStatusCode status) {
        super(message.concat(cause));
        this.status = status;
    }

    public HttpStatusCode getStatus() {
        return status;
    }
}
