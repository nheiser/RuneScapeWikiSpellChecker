package SpellCheck;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.logging.ErrorManager;

import org.languagetool.AnalyzedSentence;
import org.languagetool.JLanguageTool;
import org.languagetool.language.AmericanEnglish;
import org.languagetool.language.BritishEnglish;
import org.languagetool.rules.Rule;
import org.languagetool.rules.RuleMatch;
import org.languagetool.rules.spelling.SpellingCheckRule;
import org.languagetool.tools.ContextTools;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.Reporter;
import org.testng.annotations.*;

public class SpellCheckPages extends BaseTest{

	@DataProvider
	public Object[][] getRandomPages(){

		int length = 4;

		Object[][] o = new Object[length][1];
		driver.navigate().to("https://oldschool.runescape.wiki/");
		List<String> pages = new ArrayList<String>();

		for (int i = 0; i < length; i++) {

			driver.findElement(By.linkText("Random page")).click();
			pages.add(driver.getCurrentUrl());
			o[i][0] = driver.getCurrentUrl();
		}
		return o;

	}

	@AfterSuite
	public void offerSuggestions() throws IOException {

		Scanner scanner = new Scanner(System.in);

		for (Map.Entry<String, String> error: failedWords.entrySet()) {
			
			System.out.println("\nPotential error: " + error.getKey() +
					"\n" + error.getValue() + "\n"
					);

			System.out.println("Do you want to add word: " + error.getKey() + "\n");
			System.out.println("1: Yes");
			System.out.println("2: No");
			System.out.println("");

			String choice = scanner.nextLine();

			if (choice.equals("1")) {
				addWordToDictionary(error.getKey(), fileName);
			}

		}

		scanner.close();

	}


	@Test (dataProvider = "getRandomPages")
	public void spellCheck(String page) throws IOException {

		String fileName = "C:\\Users\\nheis\\eclipse-workspace\\RuneScapeWikiSpellChecker\\src\\main\\resources\\OSRS-Dictionary.txt";

		driver.navigate().to(page);

		String url = driver.getCurrentUrl();

		List<String> allLinkWords = getAllLinkWords(url);

		//addWordsToDictionary(allLinkWords);

		String rawText = driver.findElement(By.tagName("body")).getText();

		String cleanText = getCleanText(rawText);

		List<String> allWords = getWordsFromDictionary("C:\\Users\\nheis\\eclipse-workspace\\RuneScapeWikiSpellChecker\\src\\main\\resources\\OSRS-Dictionary.txt");

		//System.out.print(url + ": ");

		assert(checkSpelling(cleanText, fileName, allWords, allLinkWords));

	}

	public static String getSentence(String text, int startPos, int endPos) {

		//int start = match.getFromPos();
		//int end = match.getToPos();

		while(endPos < text.length() - 1) {
			if (text.charAt(endPos) == '.') {
				break;
			}
			endPos++;

		}
		while (startPos > 0) {

			if (text.charAt(startPos) == '.') {
				startPos++;
				break;
			}
			if (text.charAt(startPos) == '\n') {
				break;
			}
			startPos--;
		}

		return text.substring(startPos, endPos + 1).trim();
		//return text.substring(startPos, endPos).trim();

	}
	public static boolean checkSpelling(String text, String fileName, List<String> exceptions, List<String> linkExceptions) throws IOException {

		List<String> ignore = new ArrayList<String>();
		//ignore.add("OXFORD_SPELLING_Z_NOT_S");
		ignore.add("EN_GB_SIMPLE_REPLACE");//movie
		ignore.add("ENGLISH_WORD_REPEAT_BEGINNING_RULE");
		ignore.add("ON_IN_THE_MIDDLE");
		ignore.add("DROP_DOWN");
		ignore.add("SOME_OF_THE");
		ignore.add("COMMA_COMPOUND_SENTENCE");
		ignore.add("ADMIT_ENJOY_VB");
		ignore.add("APOSTROPHE_IN_DATES");
		ignore.add("COMMA_COMPOUND_SENTENCE_2");
		ignore.add("EVERY_NOW_AND_THEN");
		ignore.add("DASH_RULE");
		ignore.add("FILE_EXTENSIONS_CASE");
		ignore.add("DAMAGE_OF_TO");
		ignore.add("MISSING_COMMA_AFTER_INTRODUCTORY_PHRASE");
		ignore.add("ANYMORE");
		ignore.add("RETURN_BACK");
		ignore.add("OUTSIDE_OF");
		ignore.add("DESCEND_DOWN");
		ignore.add("LITTLE_BIT");
		ignore.add("ALL_OF_THE");
		ignore.add("CLICK_HYPHEN");
		//MISSING_HYPHEN 
		//CLOSE_SCRUTINY 
		//WITH_THE_EXCEPTION_OF 
		//IN_A_X_MANNER 
		//NUMEROUS_DIFFERENT 
		//CHILDISH_LANGUAGE 
		//ONE_OF_THE_ONLY 
		//SECOND_LARGEST_HYPHEN 
		//EXTREME_ADJECTIVES 
		//
		//
		//

		List<String> wordsToIgnore = exceptions;
		JLanguageTool langTool = new JLanguageTool(new BritishEnglish());
		langTool.disableRules(ignore);

		for (Rule rule : langTool.getAllActiveRules()) {

			if (rule instanceof SpellingCheckRule) {

				//((SpellingCheckRule)rule).acceptPhrases(wordsToIgnore);

				((SpellingCheckRule)rule).addIgnoreTokens(wordsToIgnore);
			}
		}

		List<RuleMatch> matches = langTool.check(text);
		AnalyzedSentence sentence;
		String word;
		ContextTools c = new ContextTools();
		boolean b = true;
		int count = 0;

		for (RuleMatch match: matches) {

			word = text.substring(match.getFromPos(), match.getToPos());

			//if word does not contain any numbers
			if (!word.matches(".*\\d.*")) {

				sentence = match.getSentence();
				
				if (!getWordsFromDictionary("C:\\Users\\nheis\\eclipse-workspace\\RuneScapeWikiSpellChecker\\src\\main\\resources\\OSRS-Dictionary.txt").contains(word)) {

					if (linkExceptions.contains(word)) {
						count++;
						addWordToDictionary(word, fileName);
					}
					else {

						if(match.getRule().getId().equals("OXFORD_SPELLING_Z_NOT_S") || match.getRule().getId().equals("MORFOLOGIK_RULE_EN_GB")) {
							failedWords.put(word, c.getPlainTextContext(match.getFromPos(), match.getToPos(), text));
						}

						b = false;

						System.out.println("\nPotential error " + word +   
								"\n" + sentence.getText() +
								"\nID: " + match.getRule().getId() + " = " + match.getMessage() + "\n"
								);

						String errorMessage = 
								"<br>Potential error: " + word +  
								"<br>" + sentence.getText() +
								"<br>ID: " + match.getRule().getId() + " = " + match.getMessage() + 
								"<br>";

						Reporter.log(errorMessage);

					}

				}

			}

		}

		System.out.println("Added " + count + " new words.");

		return b;
	}

}