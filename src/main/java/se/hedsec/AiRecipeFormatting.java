package se.hedsec;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class AiRecipeFormatting {
    public static String getRecipe(String recipe) throws IOException, InterruptedException {
        // Retrieve API key from environment variables
        String apiKey = System.getenv("OPEN_AI_KEY");
        var body = """
                {
                  "contents": [{
                    "parts":[{"text": "Kan du skriva detta recept på svenska med EU metric?"}]
                    }]
                   }""".formatted(recipe);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash:generateContent?key=" + apiKey))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .build();

        var client = HttpClient.newHttpClient();
        var response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println(response.body());
        return response.body();
    }
}
