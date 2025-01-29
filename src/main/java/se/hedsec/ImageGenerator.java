package se.hedsec;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpClient;
import java.net.http.HttpResponse;

public class ImageGenerator {


    public static String generateImage(Recipe recipe) throws IOException, InterruptedException {

        /*
        curl https://api.cloudflare.com/client/v4/accounts/$CLOUDFLARE_ACCOUNT_ID/ai/run/@cf/lykon/dreamshaper-8-lcm  \
        -X POST  \
        -H "Authorization: Bearer $CLOUDFLARE_API_TOKEN"  \
        -d '{ "prompt": "cyberpunk cat" }'
         */
        String apiKey = System.getenv("CLOUDFLARE_API_KEY");
        String accountId = System.getenv("CLOUDFLARE_ACC_ID");
        String uri = "https://api.cloudflare.com/client/v4/accounts/"+accountId+ "/ai/run/@cf/lykon/dreamshaper-8-lcm";
        String requestBody = """
            {
                "prompt": "Realistic: %s"
            }
            """.formatted(recipe.getName());

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(uri))
                .header("Authorization", "Bearer " + apiKey)
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();
        var client = HttpClient.newHttpClient();
        var response = client.send(request, HttpResponse.BodyHandlers.ofString());
        return response.body();


    }

    //acc id: b75c08983b3edc7c6a7f59e4bdc846c9
    //key: VK927BJHwx4eTC7J5BSUIGMc1BrlQ-7qnuopvFtR
}
