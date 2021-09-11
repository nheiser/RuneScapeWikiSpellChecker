package SpellCheck;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.languagetool.rules.RuleMatch;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.testng.annotations.*;

public class BaseTest {

	WebDriver driver;
	Actions action;
	String path;
	String baseUrl;
	Map<String, String> failedWords = new HashMap<String, String>();
	
	@BeforeTest
	@Parameters("wiki")
	public void initialize(String wiki) {
	
		System.setProperty("webdriver.chrome.driver","src/main/resources/drivers/chromedriver.exe");

		if (wiki.equalsIgnoreCase("OSRS")) {
			path = "src/main/resources/OSRS";
			baseUrl = "https://oldschool.runescape.wiki/w/";
		}
		
		driver = new ChromeDriver();
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		action = new Actions(driver);
	}

	@AfterTest
	public void teardown() {
		driver.quit();
	}

	public List<String> getAllLinkWords(String url) {
		List<WebElement> links = driver.findElements(By.tagName("a"));
		List<String> words = new ArrayList<String>();

		for(WebElement link: links) {
			for(String s: link.getText().split(" ")) {
				words.add(s);
			}
		}
		
		for (String s: driver.getTitle().substring(0, driver.getTitle().indexOf(" - OSRS Wiki")).split(" ")){
			words.add(s);
		}
				
		return words;
	}

	public void addWordToDictionary(String word, String path) throws IOException {
		FileWriter filewriter = new FileWriter(path, true);

		filewriter.append(word);
		filewriter.append("\n");
		filewriter.flush();
		filewriter.close();
	}

	public List<String> getWordsFromDictionary(String path) throws FileNotFoundException{
		File file = new File(path);
		Scanner scanner = new Scanner(file);

		Set<String> allWords = new HashSet<String>();

		while (scanner.hasNextLine()) {
			allWords.add(scanner.nextLine());
		}
		scanner.close();

		List<String> list = new ArrayList<String>();
		list.addAll(allWords);		

		return list;

	}

	public String getCleanText(WebElement body) {
		
		String rawText = body.getText();
		
		rawText = rawText.replace(body.findElement(By.className("infobox")).getText(), "");
		rawText = rawText.replace(body.findElement(By.className("musicplayer")).getText(), "");
		rawText = rawText.replace(body.findElement(By.id("catlinks")).getText(), "");
		
		for (WebElement element: body.findElements(By.tagName("table"))) {
			rawText = rawText.replace(element.getText(), "");
		}
		for (WebElement element: body.findElements(By.className("references"))) {
			rawText = rawText.replace(element.getText(), "");
		}
		
		for (WebElement element: body.findElements(By.className("chat-options"))) {
			rawText = rawText.replace(element.getText(), "(#" + element.getText().substring(2));	
		}
		
		rawText = rawText.replaceAll("[\n]+", "\n\n");
		
		return rawText;
		
	}

}
