package SpellCheck;

import java.io.IOException;

import org.openqa.selenium.By;
import org.testng.annotations.Test;

public class addLotsOfWordsToDictionary extends SpellCheckPages{

	@Test
	public void searchRandomPages() {

		driver.navigate().to("https://oldschool.runescape.wiki/");

		for (int i = 1; i <= 100; i++) {

			driver.findElement(By.linkText("Random page")).click();
			
			try {
				addWordsToDictionary(getAllLinkWords(driver.getCurrentUrl()));
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}


	}
}

