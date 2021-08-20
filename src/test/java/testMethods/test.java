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
		
		System.setProperty("webdriver.chrome.driver","src/main/resources/drivers/chromedriver.exe");
		WebDriver driver = new ChromeDriver();
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
	
		driver.navigate().to("https://oldschool.runescape.wiki/'24-carat' sword");
		
		String text = driver.findElement(By.tagName("body")).getText();
		System.out.println(driver.getTitle().substring(0, driver.getTitle().indexOf(" - OSRS Wiki")));	
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

}
