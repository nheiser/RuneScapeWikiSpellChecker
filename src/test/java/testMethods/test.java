package testMethods;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.testng.annotations.*;
import SpellCheck.BaseTest;
import SpellCheck.SpellCheckPages;
import junit.framework.Assert;

public class test {

	@Test (dataProvider="sampleSentences")
	public void testGetSentence(String sentence, String expectedOutput) {
		
		Assert.assertTrue(expectedOutput.equals(SpellCheckPages.getSentence(sentence, 6, 7)));

	}
	
	@Test (dataProvider = "sampleBritishWords")
	public void testCheckSpelling(String sentence, boolean expectedOutput) throws IOException {
		
		List<String> exceptions = BaseTest.getWordsFromDictionary("C:\\Users\\nheis\\eclipse-workspace\\RuneScapeWikiSpellChecker\\src\\test\\resources\\Test-Dictionary.txt");//new ArrayList<String>();
		
		Assert.assertEquals(expectedOutput, SpellCheckPages.checkSpelling(sentence, exceptions));
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
		//all words in ' are their own word => Mort'ton => Mort, ton
		//all words in - are their own word => Anti-dragon => Anti, Dragon
		//
		
		return new Object[][] {{"Bloodveld", true}, {"Bloodveld's", true},
			{"Mort'ton", true}, {"Mort", false},
			{"Free-to-play", true}, {"Ton", false},
			{"Zulrah", true}, {"Zulrah's", true},
			{"Anti-dragon", true}, {"Capitalize", false},
			{"spellbook", true}, {"Capitalise", true},
			{"esfefewf", false}, {"w4wt4wt", false}
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
