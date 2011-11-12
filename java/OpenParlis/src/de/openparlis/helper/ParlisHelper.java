package de.openparlis.helper;

import java.net.MalformedURLException;
import java.net.URL;

import de.openparlis.data.DocumentId;

public class ParlisHelper {
	
	static final int PARLIS_DOCS_PER_PAGE = 20;
	static final String PARLIS_BASE_URL = "http://stvv.frankfurt.de/PARLISLINK/";
	static final String PARLIS_DOCUMENT_URL = "DDW?W=DOK_NAME=%27{DOCUMENT_ID}%27";
	static final String PARLIS_DOCLIST_URL = "SDW?W%3DORDER+BY+DATUM/Descend%26M%3D{PAGE_NUM}%26R%3DY";

	static public URL createParlisDocumentUrl(DocumentId documentId) {
		try {
			String url = PARLIS_BASE_URL + PARLIS_DOCUMENT_URL;
			url = url.replaceAll("\\{DOCUMENT_ID\\}", documentId.toString());
			return new URL(url);
		} catch (MalformedURLException ex) {
			return null;
		}
	}
	
	static public URL createParlisDocListUrl(int page) {
		try {
			int pageNum = (PARLIS_DOCS_PER_PAGE * page) + 1;
			String url = PARLIS_BASE_URL + PARLIS_DOCLIST_URL;
			url = url.replaceAll("\\{PAGE_NUM\\}", Integer.toString(pageNum));
			return new URL(url);
		} catch (MalformedURLException ex) {
			return null;
		}
	}
	
}
