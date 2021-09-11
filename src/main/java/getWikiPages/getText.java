package getWikiPages;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.jsoup.Jsoup;
import org.jsoup.safety.Safelist;
import org.jsoup.safety.Whitelist;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Actions;

public class getText {

	public static void main(String[] args) throws InterruptedException, AWTException {
		
		//System.setProperty("webdriver.gecko.driver","C:\\Users\\nheis\\Downloads\\geckodriver-v0.29.1-win64\\geckodriver.exe");
		//WebDriver driver = new FirefoxDriver();
		System.setProperty("webdriver.chrome.driver","C:\\Users\\nheis\\Downloads\\chromedriver_win32(2)\\chromedriver.exe");
		WebDriver driver = new ChromeDriver();
		
		Robot robot = new Robot();
		
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		
		driver.navigate().to("https://oldschool.runescape.wiki/w/Alchemical_Hydra");
		//driver.navigate().to("https://oldschool.runescape.wiki/w/Alchemical_Hydra");
		WebElement body = driver.findElement(By.id("content"));
		String text = body.getText();
		
		
		//class=mw-header
		//System.out.println(text);
		text = text.replace(body.findElement(By.className("infobox")).getText(), "");
		text = text.replace(body.findElement(By.className("musicplayer")).getText(), "");
		text = text.replace(body.findElement(By.id("catlinks")).getText(), "");
		//tbody
		for (WebElement element: body.findElements(By.tagName("table"))) {
			text = text.replace(element.getText(), "");
		}
		for (WebElement element: body.findElements(By.className("references"))) {
			text = text.replace(element.getText(), "");
		}
		
		
		/*for (WebElement element: body.findElements(By.tagName("tbody"))) {
			String s = "";
			for (WebElement subElement: element.findElements(By.tagName("td"))) {
				s += subElement.getText() + "\n";
			}
			
			if (!s.equals("") && text.contains(element.getText())) {
				text = text.replace(element.getText().trim(), s);
			}
			
		}*/
		
		//remove chat bubbles
		for (WebElement element: body.findElements(By.className("chat-options"))) {
			text = text.replace(element.getText(), "(#" + element.getText().substring(2));	
		}
		
		text = text.replaceAll("[\n]+", "\n\n");
		
		System.out.println(text);
		
		driver.close();
	}

}
