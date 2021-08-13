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
	public void testGetSentence(String sentence, String expectedOutput) throws IOException {
		
		Assert.assertTrue(expectedOutput.equals(SpellCheckPages.getSentence(sentence, 6, 7)));

	}
	
	
	
	
	@DataProvider
	public Object[][] sampleSentences(){		
		
		return new Object[][] {{"Helllo world.", "Helllo world."}, 
			{"Helllo world", "Helllo world"},
			{"Hi. Helllo world.", "Helllo world."}};
		
	}
	
}
