package SpellCheck;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

import org.languagetool.JLanguageTool;
import org.languagetool.language.BritishEnglish;
import org.languagetool.rules.Rule;
import org.languagetool.rules.RuleMatch;
import org.languagetool.rules.spelling.SpellingCheckRule;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.annotations.Test;

public class SpellCheckPages extends BaseTest{

	@Test
	public void spellCheck() throws IOException {

		driver.navigate().to("https://oldschool.runescape.wiki/w/Another_Slice_of_H.A.M.");
		//driver.findElement(By.linkText("Random page")).click();

		addWordsToDict(driver.getCurrentUrl());

		String rawText = driver.findElement(By.tagName("body")).getText();
		String text = "";
		String line = "";

		Set<String> allWords = getWordsFromDict();
		List<String> allWords2 = new ArrayList<String>();
		allWords2.addAll(allWords);

		while (rawText.indexOf('\n') != -1) {

			line = rawText.substring(0, rawText.indexOf('\n'));
			if (line.contains(".")) {
				text = text + rawText.substring(0, rawText.indexOf('\n')) + "\n";
			}

			rawText = rawText.substring(rawText.indexOf('\n') + 1);
		}

		checkSpelling(text, allWords2);

	}

	public String getSentence(String text, RuleMatch match) {

		int start = match.getFromPos();
		int end = match.getToPos();

		while(end < text.length()) {
			if (text.charAt(end) == '.') {
				break;
			}
			end++;

		}
		while (start >= 0) {

			if (text.charAt(start) == '.') {
				start++;
				break;
			}
			if (text.charAt(start) == '\n') {
				break;
			}
			start--;
		}

		return text.substring(start, end + 1).trim();

	}
	public void checkSpelling(String text, List<String> exceptions) throws IOException {

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

		JLanguageTool langTool = new JLanguageTool(new BritishEnglish());
		langTool.disableRules(ignore);

		for (Rule rule : langTool.getAllActiveRules()) {

			if (rule instanceof SpellingCheckRule) {
				List<String> wordsToIgnore = exceptions;
				((SpellingCheckRule)rule).addIgnoreTokens(wordsToIgnore);
			}
		}

		List<RuleMatch> matches = langTool.check(text);

		for (RuleMatch match : matches) {

			System.out.println("Potential error " +
					"<" + text.substring(match.getFromPos(), match.getToPos()) + ">" +  
					"\n" + getSentence(text, match) +
					"\nID: " + match.getRule().getId() + " = " + match.getMessage());
			System.out.println("Suggested correction(s): " +
					match.getSuggestedReplacements() + "\n");
		}

	}

}