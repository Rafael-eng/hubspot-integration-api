package hubspotintegration.request;

public record ContactRequest(ContactProperties properties) {
    public ContactRequest(String email, String lastname, String firstname) {
        this(new ContactProperties(email, lastname, firstname));
    }
}
