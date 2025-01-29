package se.hedsec;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class RecipeFormatter {
    public static Recipe getRecipe(String recipe) throws IOException, InterruptedException {
        String apiKey = System.getenv("GEMINI_API_KEY");
        String requestBody = """
            {
                "contents": [{
                    "parts": [
                        {"text": "Kan du översätta till ett svenskt recept med EU metric, name / titeln på receptet kan förbli på engelska: %s"}
                    ]
                }],
                "generationConfig": {
                    "response_mime_type": "application/json",
                    "response_schema": {
                        "type": "OBJECT",
                        "properties": {
                            "name": {"type": "STRING"},
                            "ingredients": {
                                "type": "ARRAY",
                                "items": {"type": "STRING"}
                            },
                            "instructions": {
                                "type": "ARRAY",
                                "items": {"type": "STRING"}
                            }
                        }
                    }
                }
            }
            """.formatted(recipe);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash:generateContent?key=" + apiKey))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        var client = HttpClient.newHttpClient();
        var response = client.send(request, HttpResponse.BodyHandlers.ofString());
        String jsonResponse = response.body();
        return RecipeParser.parseRecipe(jsonResponse);
    }


    public class RecipeParser {
        public static Recipe parseRecipe(String jsonResponse) {
            try {
                ObjectMapper objectMapper = new ObjectMapper();
                JsonNode root = objectMapper.readValue(jsonResponse, JsonNode.class);
                String recipeJson = root.path("candidates")
                        .get(0)
                        .path("content")
                        .path("parts")
                        .get(0)
                        .path("text")
                        .asText();
                return objectMapper.readValue(recipeJson, Recipe.class);

            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
    }

}
