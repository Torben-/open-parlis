package helper;

import java.net.MalformedURLException;
import java.net.URL;

import data.DocumentId;

public class ParlisHelper {
	
	static final String PARLIS_DOCUMENT_URL = "http://stvv.frankfurt.de/PARLISLINK/DDW?W=DOK_NAME=%27{DOCUMENT_ID}%27";

	static public URL createParlisUrl(DocumentId documentId) {
		try {
			String url = PARLIS_DOCUMENT_URL;
			url = url.replaceAll("\\{DOCUMENT_ID\\}", documentId.toString());
			return new URL(url);
		} catch (MalformedURLException ex) {
			return null;
		}
	}
	
}
