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

	public List<String> getAllLinkWords(String url) {
		List<WebElement> links = driver.findElements(By.tagName("a"));
		List<String> words = new ArrayList<String>();

		//link: [King Black Dragon]
		//words: [King, Black, Dragon]

		for(WebElement link: links) {
			for(String s: link.getText().split(" ")) {
				words.add(s);//.toLowerCase());
			}
		}
		return words;
	}

	public static void addWordToDictionary(String word, String fileName) throws IOException {
		FileWriter filewriter = new FileWriter(fileName, true);
		//List<String> dictionaryWords = getWordsFromDictionary(fileName);
		//Set<String> words = new HashSet<String>();
		//words.addAll(dictionaryWords);

		filewriter.append(word);
		filewriter.append("\n");
		filewriter.flush();
		filewriter.close();
	}
	public void addWordsToDictionary(List<String> newWords) throws IOException {

		//driver.navigate().to(url);

		FileWriter filewriter = new FileWriter(fileName, true);
		List<String> dictionaryWords = getWordsFromDictionary(fileName);
		Set<String> words = new HashSet<String>();
		words.addAll(dictionaryWords);

		int count = 0;

		for (String word: newWords) {

			if (word.length() > 1) {
				word = cleanString(word);
			}				
			if (word.length() > 1 && word.matches(".*[a-zA-Z]+.*") && words.add(word)) {
				count++;
				filewriter.append(word);
				filewriter.append('\n');
				//System.out.print(word);
			}

		}
		System.out.println("Added " + count + " new words from: " + driver.getCurrentUrl());
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

	public static String getCleanText(String rawText) {

		String text = "";
		String line = "";

		while (rawText.indexOf('\n') != -1) {

			line = rawText.substring(0, rawText.indexOf('\n'));
			
			//only check text if it has a period and at least 4 words
			if (line.contains(".") && line.split(" ").length >= 4) {
				text = text + rawText.substring(0, rawText.indexOf('\n')) + "\n";
			}

			rawText = rawText.substring(rawText.indexOf('\n') + 1);
		}

		return text;
	}


	public static String cleanString(String s) {
		//DONE:
		//remove ()'s from beginning and end
		//remove []'s from beginning and end
		//remove {}'s from beginning and end
		//remove " from beginning and end
		//remove ,'s from beginning and end

		//TO_DO
		//remove .'s from beginning and end
		//
		char firstChar = s.charAt(0);

		if (firstChar == '(' || firstChar == ')' || firstChar == '[' || firstChar == ']' || firstChar == '{' || firstChar == '}' || firstChar == '\"' || firstChar == ',' || firstChar == '.') {
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
		if (s.contains(",")) {
			return s.substring(0, s.length() - 1);
		}

		return s;
	}


}
