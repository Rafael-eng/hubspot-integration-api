package hubspotintegration;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource(properties = {
		"HUBSPOT_CLIENT_SECRET=123",
		"HUBSPOT_CLIENT_ID=123"
})class HubSpotIntegrationApplicationTests {

	@Test
	void contextLoads() {
	}

}
