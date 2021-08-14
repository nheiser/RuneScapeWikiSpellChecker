package SpellCheck;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.testng.annotations.*;

public class BaseTest {

	WebDriver driver;
	Actions action;
	String fileName = "C:\\Users\\nheis\\eclipse-workspace\\RuneScapeWikiSpellChecker\\src\\main\\resources\\OSRS-Dictionary.txt";

	@BeforeTest
	public void initialize() {
		System.setProperty("webdriver.chrome.driver","C:\\Users\\nheis\\eclipse-workspace\\RuneScapeWikiSpellChecker\\src\\main\\resources\\drivers\\chromedriver.exe");
		driver = new ChromeDriver();
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		action = new Actions(driver);
	}

	@AfterTest
	public void teardown() {
		driver.quit();
	}
	
	public void getAllLinks(String url) {
		
		
	}
	
	

	public void addWordsToDictionary(String url) throws IOException {

		driver.navigate().to(url);

		FileWriter filewriter = new FileWriter(fileName, true);
		List<String> allWords = getWordsFromDictionary(fileName);
		Set<String> words = new HashSet<String>();
		words.addAll(allWords);
		
		
		List<WebElement> links = driver.findElements(By.tagName("a"));
		int count = 0;
		String[] temp;

		for (WebElement link: links) {

			temp = link.getText().toLowerCase().split(" ");

			for (String s: temp) {
				
				if (s.length() > 1) {
					s = cleanString(s);
				}
				
				if (s.length() > 1 && s.matches(".*[a-zA-Z]+.*") && words.add(s)) {
					count++;
					filewriter.append(s);
					filewriter.append('\n');
				}
			}


		}
		System.out.println("Added " + count + " new words from: " + url);
		filewriter.flush();
		filewriter.close();
	}

	public static List<String> getWordsFromDictionary(String path) throws FileNotFoundException{
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

	public static String cleanString(String s) {
		//remove ,'s and .'s
		if (s.charAt(0) == '(' || s.charAt(0) == '\"' || s.charAt(0) == '[') {
			s = s.substring(1);
		}
		if (s.contains("(") && s.contains(")")) {
			return s;
		}
		if (s.contains(")")) {
			return s.substring(0, s.length() - 1);
		}
		if (s.contains("[") && s.contains("]")) {
			return s;
		}
		if (s.contains("]")) {
			return s.substring(0, s.length() - 1);
		}
		if (s.contains("\"")) {
			return s.substring(0, s.length() - 1);
		}
	
		return s;
	}


}
