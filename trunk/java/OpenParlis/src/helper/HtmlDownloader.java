package helper;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

public class HtmlDownloader {
	
	static public String getHtml(URL url) {
		String html = "";
		String inputLine = "";
		try {
			InputStream is = url.openStream();
			InputStreamReader isr = new InputStreamReader(is);
			BufferedReader br = new BufferedReader(isr);
			while ((inputLine = br.readLine()) != null) {
				html += inputLine + "\n";
			}
			return html;
		} catch (Exception ex) {
			return null;
		}
	}

}
