package getWikiPages;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.Scanner;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.google.gson.JsonParser;


public class getOSRSWikiPages {

	public static void main(String[] args) {

		Scanner scanner;
		//JSONObject extra = new JSONObject();
		String apcontinue = "";
		
		
		try {

			while (true) {

				URL url = new URL("https://oldschool.runescape.wiki/api.php?action=query&list=allpages&aplimit=500" + apcontinue + "&format=json");

				HttpURLConnection conn = (HttpURLConnection) url.openConnection();
				conn.setRequestMethod("GET");
				conn.connect();

				//Getting the response code
				int responsecode = conn.getResponseCode();

				if (responsecode != 200) {
					throw new RuntimeException("HttpResponseCode: " + responsecode);
				} else {

					String inline = "";
					scanner = new Scanner(url.openStream());

					//Write all the JSON data into a string using a scanner
					while (scanner.hasNext()) {
						inline += scanner.nextLine();
					}

					//System.out.println(inline);

					//Using the JSON simple library parse the string into a json object
					JSONParser parse = new JSONParser();

					JSONObject base = (JSONObject) parse.parse(inline);

					//System.out.println(base);

					JSONObject extra = new JSONObject();
					
					extra = (JSONObject) base.get("continue");
					
					apcontinue = "&apcontinue=" + (String) extra.get("apcontinue");
					
					JSONObject pages = (JSONObject) base.get("query");
					
					//System.out.println(apcontinue);

					JSONArray arr = (JSONArray) pages.get("allpages");

					for (int i = 0; i < arr.size(); i++) {

						JSONObject page = (JSONObject) arr.get(i);
						//System.out.println(page.get("title"));

					}
					
				}
				//System.out.println(apcontinue);
				scanner.close();

			}
			

		} catch(Exception e) {

			e.printStackTrace();

		}

	}

}

