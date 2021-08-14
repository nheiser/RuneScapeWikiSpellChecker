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
import java.util.Scanner;
import java.util.Set;

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
import org.testng.annotations.Test;

public class SpellCheckPages extends BaseTest{

	@Test
	public void spellCheck() throws IOException {

		driver.navigate().to("https://oldschool.runescape.wiki/w/Another_Slice_of_H.A.M.");
		//driver.findElement(By.linkText("Random page")).click();

		String url = driver.getCurrentUrl();
		addWordsToDictionary(url);

		String rawText = driver.findElement(By.tagName("body")).getText();
		String text = "";
		String line = "";

		List<String> allWords = getWordsFromDictionary("C:\\Users\\nheis\\eclipse-workspace\\RuneScapeWikiSpellChecker\\src\\main\\resources\\OSRS-Dictionary.txt");
		//List<String> allWords2 = new ArrayList<String>();
		//allWords2.addAll(allWords);

		while (rawText.indexOf('\n') != -1) {

			line = rawText.substring(0, rawText.indexOf('\n'));
			if (line.contains(".")) {
				text = text + rawText.substring(0, rawText.indexOf('\n')) + "\n";
			}

			rawText = rawText.substring(rawText.indexOf('\n') + 1);
		}

		checkSpelling(text, allWords);

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
	public static boolean checkSpelling(String text, List<String> exceptions) throws IOException {

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
		//
		//
		//
		//

		//1. disable spell check
		//2. check vs allowed words
		//3. check vs prohibitied words
		//4. enable spell check


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
		String sentence;
		String word;
		ContextTools c = new ContextTools();
		boolean b = true;

		for (RuleMatch match: matches) {

			word = text.substring(match.getFromPos(), match.getToPos());
			sentence = c.getPlainTextContext(match.getFromPos(), match.getToPos(), text);

			if (!getWordsFromDictionary("C:\\Users\\nheis\\eclipse-workspace\\RuneScapeWikiSpellChecker\\src\\main\\resources\\OSRS-Dictionary.txt").contains(word)) {

				b = false;
				System.out.println("Potential error " +
						"<" + word + ">" +  
						"\n" + match.getSentence().getText() +
						"\nID: " + match.getRule().getId() + " = " + match.getMessage());
				System.out.println("Suggested correction(s): " +
						match.getSuggestedReplacements() + "\n");

			}

		}

		return b;
	}

}