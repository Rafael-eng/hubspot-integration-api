package hubspotintegration.request;

public record ContactRequest(ContactProperties properties) {
    public ContactRequest(String email, String lastName, String firstName) {
        this(new ContactProperties(email, lastName, firstName));
    }
}
