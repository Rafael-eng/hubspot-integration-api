package hubspotintegration.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ContactProperties(@JsonProperty("email") String  email,
                                @JsonProperty("lastname")String lastName,
                                @JsonProperty("firstname")String firstName) {}
