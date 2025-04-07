package hubspotintegration.exception;

import hubspotintegration.response.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(HubSpotException.class)
    public ResponseEntity<ErrorResponse> handleHubSpotException(HubSpotException ex) {
        return buildResponseEntity(ex.getStatus(), ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleHubSpotException(Exception ex) {
        return buildResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
    }

    private ResponseEntity<ErrorResponse> buildResponseEntity(HttpStatusCode status, String message) {
        return ResponseEntity.status(status).body(new ErrorResponse(status.value(), message));
    }

}
