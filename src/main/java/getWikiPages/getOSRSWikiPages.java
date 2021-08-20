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

public class getOSRSWikiPages {

	public static void main(String[] args) throws IOException {

		Scanner scanner;
		String gapcontinue = "";
		int i=1;
		List<String> list = new ArrayList<String>();
		try {

			FileWriter file = new FileWriter("src/main/resources/OSRS/wikiPages.txt");
			BufferedWriter bf = new BufferedWriter(file);

			while (i==1) {
				i++;
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
						list.add(page.get("title").toString());
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
				scanner.close();

			}

			Set<String> hs = new HashSet<String>();

			java.util.Collections.sort(list);
			for (String s: list) {
				if (!hs.add(s)) {
					System.out.println("Duplicate item: " + s);
					break;

				}
				bf.write(s);
				bf.write("\n");
			}
			System.out.println(list.size());

			bf.close();

		} catch(Exception e) {

			e.printStackTrace();

		}

	}

}

