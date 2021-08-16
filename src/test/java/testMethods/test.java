package testMethods;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.languagetool.AnalyzedSentence;
import org.languagetool.JLanguageTool;
import org.languagetool.language.BritishEnglish;
import org.languagetool.rules.RuleMatch;
import org.languagetool.tools.ContextTools;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.testng.Reporter;
import org.testng.annotations.*;
import SpellCheck.BaseTest;
import SpellCheck.SpellCheckPages;
import junit.framework.Assert;

public class test {

	@Test
	public void getTextFromPage() {
		
		System.setProperty("webdriver.chrome.driver","C:\\Users\\nheis\\eclipse-workspace\\RuneScapeWikiSpellChecker\\src\\main\\resources\\drivers\\chromedriver.exe");
		WebDriver driver = new ChromeDriver();
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
	
		driver.navigate().to("https://oldschool.runescape.wiki/w/Dragon_Slayer_II");
		
		String text = driver.findElement(By.tagName("body")).getText();
		System.out.println(BaseTest.getCleanText(text));
	}
	
	@Test (dataProvider="sampleSentences")
	public void testGetSentence(String sentence, String expectedOutput) {

		ContextTools c = new ContextTools();
		String text = "Helllo world.";
		//String sentence = c.getPlainTextContext(0, 5, text);

		String errorMessage = 
				"Potential error " +
				"<" + "Helllo" + ">" +  
				"<br>" + "Helllo world." +
				"<br>ID: " + "101" + " = " + "mispelled" + 
				"<br>Suggested correction(s): " +
				"Hello" + "<br>";
		
		Reporter.log(errorMessage, true);
		
		Assert.assertTrue(expectedOutput.equals(SpellCheckPages.getSentence(sentence, 6, 7)));

	}

	@Test (dataProvider = "sampleStrings")
	public void testCheckSpelling(String sentence, String g) throws IOException {

		List<String> exceptions = BaseTest.getWordsFromDictionary("C:\\Users\\nheis\\eclipse-workspace\\RuneScapeWikiSpellChecker\\src\\test\\resources\\Test-Dictionary.txt");//new ArrayList<String>();
		
		Assert.assertTrue(SpellCheckPages.checkSpelling(sentence, exceptions, exceptions));
	}

	@Test (dataProvider = "sampleStrings")
	public void testCleanString(String s, String expectedOutput) {

		System.out.println("In Test");
		Assert.assertTrue(BaseTest.cleanString(s).equals(expectedOutput));

	}


	@DataProvider
	public Object[][] sampleStrings(){
		System.out.println("In Data Provider");
		return new Object[][] {{"Zulrah's", "Potion"}, {"Zulrah'", "Potion"},
			{"Zulrah", "Potion"}, {"Zulra", "Potion"},
			//{",Potionx", "Potion"}, {"Potionx,", "Potion"},
			//{"[Potionx", "Potion"}, {"Potionx[", "Potion"},
			//{"]Potionx", "Potion"}, {"Potionx]", "Potion"},
			//{"{Potionx", "Potion"}, {"Potionx{", "Potion"},
			//{"}Potionx", "Potion"}, {"Potionx}", "Potion"},
			//{"\"Potionx", "Potion"}, {"Potionx\"", "Potion"},
			//{"Potionx(1)", "Potion(1)"}, {"Potionx[1]", "Potion[1]"},
			//{"(Potionx", "Potion"}, {"Potionx(", "Potion"},
			//{".Potionx", "Potion"}, {"Potionx", "Potion"}
			/*{"duelling", true}, {"dueling", false},
			{"jewellery", true}, {"jewelry", false},
			{"centre", true}, {"center", false},
			{"grey", true}, {"gray", false},
			{"favour", true}, {"favor", false},
			{"artefact", true}, {"artifact", false},
			{"defence", true}, {"defense", false},
			{"offence", true}, {"offense", false},
			{"tradeable", true}, {"tradable", false},
			{"fulfil", true}, {"fulfill", false},
			{"travelled", true}, {"traveled", false},
			{"rumour", true}, {"rumor", false}
			//{""}, {""}
			 * */
			 
		};
	}

	@DataProvider
	public Object[][] sampleOSRSWords(){

		///////////////////////TO_DO////////////////////
		//REMOVE ENDING 'S AND '
		//
		//
		////////////////////////////////////////////////


		//can create 's and '
		//cannot create part before 's
		//all words in ' are their own word => Mort'ton => Mort, ton in dict
		//all words in - are their own word => Anti-dragon => Anti, Dragon in dict
		//cannot create singular from plural
		//cannot create plural from singular
		//does not recognize words like free.to.play.

		return new Object[][] {{"Bloodveld"}, {"Bloodvelds"},
			{"Mort'ton"}, {"Mort"}, {"ton"},
			{"Zulrah"}, {"Zulrah's"},
			{"Heroes"}, {"Heroes'"},
			{"spell"}, {"book"},
			{"spell/book"}, {"spell-book"},
			{"free"}, {"free.to.play."}
		};

	}



	@DataProvider
	public Object[][] sampleBritishWords(){		

		//British / American
		//not working:
		//capitalize
		//tradable
		////////////////

		return new Object[][] {{"armour", true}, {"armor", false},
			{"behaviour", true}, {"behavior", false},
			{"colour", true}, {"color", false},
			{"honour", true}, {"honor", false},
			{"humour", true}, {"humor", false},
			{"humorous", true}, {"humourous", false},
			{"capitalise", true}, {"capitalize", false},
			{"analyse", true}, {"analyze", false},
			{"dialogue", true}, {"dialog", false},
			{"duelling", true}, {"dueling", false},
			{"jewellery", true}, {"jewelry", false},
			{"centre", true}, {"center", false},
			{"grey", true}, {"gray", false},
			{"favour", true}, {"favor", false},
			{"artefact", true}, {"artifact", false},
			{"defence", true}, {"defense", false},
			{"offence", true}, {"offense", false},
			{"tradeable", true}, {"tradable", false},
			{"fulfil", true}, {"fulfill", false},
			{"travelled", true}, {"traveled", false},
			{"rumour", true}, {"rumor", false}
			//{""}, {""}
		};

	}

	@DataProvider
	public Object[][] sampleSentences(){		

		return new Object[][] {{"Helllo world.", "Helllo world."}, 
			{"Helllo world", "Helllo world"},
			{"Hi. Helllo world.", "Helllo world."}};

	}

}
