package se.hedsec;

import com.microsoft.playwright.*;

import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Iterator;

public class TiktokScraper {

    public static List<String> getVideoUrls(String username) {
        try (Playwright playwright = Playwright.create()) {

            Browser browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(true));
            Page page = browser.newPage();
            page.navigate("https://www.tiktok.com/@" + username);

            //a href containing video link
            page.waitForSelector("a.css-1mdo0pl-AVideoContainer");

            List<String> videoLinks =
                    (List<String>) page.locator("a.css-1mdo0pl-AVideoContainer").evaluateAll(
                            "elements => elements.map(element => element.href)");

            browser.close();
            return videoLinks;
        }
    }

    public static RecipePreFormatting fetchTiktokRecipe(String videoLink) {
        try (Playwright playwright = Playwright.create()) {

            Browser browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(true));
            BrowserContext context = browser.newContext();
            Page page = context.newPage();

            page.navigate(videoLink);

            Locator videoDescription = page.locator("[data-e2e='browse-video-desc']");
            videoDescription.waitFor();
            String videoDescRecipe = videoDescription.innerText();

            Locator videoDate = page.locator("span[data-e2e='browser-nickname'] span:last-of-type");
            Date recipeDate = Date.valueOf(LocalDate.now());;

            /*
            if (videoDate.innerText().length() < "yyyy-mm-dd".length() && !videoDate.innerText().isEmpty()) {
                StringBuilder sb = new StringBuilder(Integer.toString(LocalDate.now().getYear()));
                sb.append('-').append(videoDate.innerText());
                recipeDate = Date.valueOf(sb.toString());

            } else if (videoDate.innerText() == null) {
                recipeDate = Date.valueOf(LocalDate.now());

            } else
                recipeDate = Date.valueOf(videoDate.innerText());

             */

            browser.close();
            RecipePreFormatting recipe = new RecipePreFormatting();
            recipe.setRecipe(videoDescRecipe);
            recipe.setDate(recipeDate);
            return recipe;
        }
    }
    public static void main(String[] args) throws IOException, InterruptedException, SQLException {
        String username = "jalalsamfit";
        List<String> videoUrls = TiktokScraper.getVideoUrls(username);

        // Use an iterator to safely remove elements
        Iterator<String> iterator = videoUrls.iterator();
        while (iterator.hasNext()) {
            String url = iterator.next();
            if (RecipeDB.checkIfRecipeExists(url)) {
                iterator.remove();
            }
        }

        for (String url : videoUrls) {
            RecipePreFormatting tiktokRecipe = TiktokScraper.fetchTiktokRecipe(url);
            Recipe result = RecipeFormatter.getRecipe(tiktokRecipe.getRecipe());

            result.setDate(tiktokRecipe.getDate());
            result.setAuthor(username);
            result.setVideoUrl(url);
            String recipeImage = ImageGenerator.generateImage(result);
            result.setImage(recipeImage.getBytes());
            RecipeDB.addRecipe(result);
        }
    }
}
