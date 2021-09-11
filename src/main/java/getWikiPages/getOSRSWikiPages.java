package getWikiPages;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.languagetool.JLanguageTool;
import org.languagetool.language.BritishEnglish;
import org.languagetool.rules.RuleMatch;

public class getOSRSWikiPages {

	public static void main(String[] args) throws IOException {

		Scanner scanner;
		String gapcontinue = "";
		List<String> titles = new ArrayList<String>();
		Set<String> hs = new HashSet<String>();
		
		try {

			FileWriter wikiPages = new FileWriter("src/main/resources/OSRS/wikiPages.txt");
			FileWriter allowedWords = new FileWriter("src/main/resources/OSRS/allowedWords.txt");
			BufferedWriter bf = new BufferedWriter(wikiPages);

			while (true) {
				//URL url = new URL("https://oldschool.runescape.wiki/api.php?action=query&list=allpages&aplimit=500" + apcontinue + "&format=json");
				URL url = new URL("https://oldschool.runescape.wiki/api.php?action=query&generator=allpages&gapfilterredir=nonredirects&gaplimit=500" + gapcontinue + "&format=json");
				HttpURLConnection conn = (HttpURLConnection) url.openConnection();
				conn.setRequestMethod("GET");
				conn.connect();

				int responsecode = conn.getResponseCode();

				if (responsecode != 200) {
					throw new RuntimeException("HttpResponseCode: " + responsecode);
				} else {

					String inline = "";
					scanner = new Scanner(url.openStream());

					while (scanner.hasNext()) {
						inline += scanner.nextLine();
					}

					JSONParser parse = new JSONParser();
					JSONObject base = (JSONObject) parse.parse(inline);

					JSONObject pages = (JSONObject) base.get("query");
					JSONObject arr = (JSONObject) pages.get("pages");

					for (Object id: arr.keySet()) {
						JSONObject page = (JSONObject) arr.get(id);
						//System.out.println(titles.toString());
						titles.add(page.get("title").toString());
						//System.out.println(titles.toString());
					}

					try {

						JSONObject extra = new JSONObject();
						extra = (JSONObject) base.get("continue");
						gapcontinue = "&gapcontinue=" + (String) extra.get("gapcontinue");

					} catch(Exception e) {
						break;
					}
					
				}
				System.out.println(gapcontinue);
				

			}
			
			scanner.close();
			java.util.Collections.sort(titles);
			
			for (String title: titles) {
				bf.write(title);
				bf.write("\n");
			}
			bf.flush();
			
			wikiPages.close();
			bf = new BufferedWriter(allowedWords);
			
			for (String title: titles) {
				title = title.toLowerCase();
				if (!title.matches(".*[a-zA-Z][(].*")) {
					title = title.replaceAll("[()]", "");
				}
				else {
					title = title.replaceAll("[(]", " ");
					title = title.replaceAll("[)]", "");
				}
				title = title.replaceAll("[/.]", " ");
				title = title.replaceAll("[,?!]", "");
				
				for(String word: title.split("[ ]+")) {
					
					if (word.contains("\'s") && word.indexOf("\'s") == word.length() - 2) {
						word = word.substring(0, word.length() - 2);
					}
					if (word.indexOf("\'") == 0){
						word = word.substring(1);
					}
					if (word.contains("\'") && word.indexOf("\'") == word.length() - 1) {
						word = word.substring(0, word.length() - 1);
					}
					if (word.length() > 1) {
						hs.add(Character.toUpperCase(word.charAt(0)) + word.substring(1));
					}
				}
				
			}
			
			JLanguageTool langTool = new JLanguageTool(new BritishEnglish());
			//langTool.getUnknownWords();
			List<String> sortedWords = new ArrayList<String>();
			
			for (String s: hs) {
				List<RuleMatch> matches = langTool.check(s);
				if (matches.size() > 0) {
					sortedWords.add(s);
				}
			}
			
			java.util.Collections.sort(sortedWords);
			
			for(String word: sortedWords) {
				bf.write(word);
				bf.write("\n");
			}

			bf.close();
			allowedWords.close();

		} catch(Exception e) {

			e.printStackTrace();

		}

	}

}

