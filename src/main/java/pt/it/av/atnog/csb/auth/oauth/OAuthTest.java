package pt.it.av.atnog.csb.auth.oauth;

import java.util.Scanner;

import org.scribe.builder.ServiceBuilder;
import org.scribe.builder.api.CsbApi;
import org.scribe.model.OAuthRequest;
import org.scribe.model.Response;
import org.scribe.model.Token;
import org.scribe.model.Verb;
import org.scribe.model.Verifier;
import org.scribe.oauth.OAuthService;

/**
 * @author <a href="mailto:cgoncalves@av.it.pt">Carlos Gon&ccedil;alves</a>
 */
public class OAuthTest {

	static final String RequestURL = "http://localhost:8080/csb/oauth/requestToken";
	static final String AccessURL = "http://localhost:8080/csb/oauth/accessToken";
	static final String ProtectedURL = "http://localhost:8080/csb/rest/users";

	private static final String PROTECTED_RESOURCE_URL = "http://localhost:8080/csb/rest/users/hello";

	public static void main(String[] args) throws Exception {
		// Replace these with your own api key and secret
		String apiKey = "9cnkiWf4XmzNVQZTEem1rJv0odSMYYikNQBuCzqsHWnMget3txlnfKpQw1QlEOxat7zbHPJoDXZHJCMFTUoRxcnyQ0YrazzL9ZwWAmezFMRkSVQ7JwRAovaa";
		String apiSecret = "KBUxy8vlBCej6VxpcJOb4PlmDN4U94O5cEYUJcpm6kqdhMiaiWMHFzu3wHvixE2jnRhoyfkr7mDBiORTn2GuCtF3YX38SN7AiQTKaUxcEwQ5kVrmSBtCdMuL";
		OAuthService service = new ServiceBuilder().provider(CsbApi.class).apiKey(apiKey).apiSecret(apiSecret).build();
		Scanner in = new Scanner(System.in);

		System.out.println("=== Csb's OAuth Workflow ===");
		System.out.println();

		// Obtain the Request Token
		System.out.println("Fetching the Request Token...");
		Token requestToken = service.getRequestToken();
		System.out.println("Got the Request Token!");
		System.out.println();

		System.out.println("Now go and authorize Scribe here:");
		String authorizationUrl = service.getAuthorizationUrl(requestToken);
		System.out.println(authorizationUrl);
		System.out.println("And paste the verifier here");
		System.out.print(">>");
		Verifier verifier = new Verifier(in.nextLine());
		System.out.println();

		// Trade the Request Token and Verifier for the Access Token
		System.out.println("Trading the Request Token for an Access Token...");
		Token accessToken = service.getAccessToken(requestToken, verifier);
		System.out.println("Got the Access Token!");
		System.out.println("(if your curious it looks like this: " + accessToken + " )");
		System.out.println();

		// Now let's go and ask for a protected resource!
		System.out.println("Now we're going to access a protected resource...");
		OAuthRequest request = new OAuthRequest(Verb.GET, PROTECTED_RESOURCE_URL);
		service.signRequest(accessToken, request);
		Response response = request.send();
		System.out.println("Got it! Lets see what we found...");
		System.out.println();
		System.out.println(response.getBody());

		System.out.println();
		System.out.println("Thats it man! Go and build something awesome with Scribe! :)");
	}
}
