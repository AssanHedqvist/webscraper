package se.hedsec;

import com.microsoft.playwright.*;

import java.io.IOException;
import java.util.List;

public class TiktokScraper {

    public List<String> getVideoLinks(String username){
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
    public String getRecipe(String videoLink){
        try (Playwright playwright = Playwright.create()) {
            Browser browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(true));
            BrowserContext context = browser.newContext();
            Page page = context.newPage();

            page.navigate(videoLink);

            // Wait for the description element to load
            Locator descriptionLocator = page.locator("[data-e2e='browse-video-desc']");
            descriptionLocator.waitFor();

            // Get the text content of the description
            String videoDescription = descriptionLocator.innerText();

            browser.close();
            return videoDescription;
        }
    }

    public static void main(String[] args) throws IOException, InterruptedException {


        TiktokScraper scraper = new TiktokScraper();
        List<String> videoLinks = scraper.getVideoLinks("jalalsamfit");
        String recipe = scraper.getRecipe(videoLinks.getFirst());
        System.out.println(AiRecipeFormatting.getRecipe(recipe));
    }
}
