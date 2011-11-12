package de.openparlis;

import java.net.URL;

import de.openparlis.data.Database;
import de.openparlis.data.Document;
import de.openparlis.data.DocumentId;
import de.openparlis.data.DocumentRevision;
import de.openparlis.data.DocumentType;
import de.openparlis.helper.HtmlDownloader;
import de.openparlis.helper.HtmlParser;
import de.openparlis.helper.ParlisHelper;


public class OpenParlisTester {

	private DocumentId docId;
	private Document doc;
	private String html;

	public void run() {
		testDownload();
		testCreateDocument();
		testSaveDB();
	}
	
	public void testDownload() {
		//docId = new DocumentId(DocumentType.M, 32, 2005);
		docId = new DocumentId(DocumentType.M, 32, 2011);
		//docId = new DocumentId(DocumentType.OF, 66, 9, 2011);
		
		URL url = ParlisHelper.createParlisDocumentUrl(docId);
		html = HtmlDownloader.getHtml(url);
		
	}
	
	public void testCreateDocument() {
		DocumentRevision rev = HtmlParser.createRevisionFromHtml(html);
		doc = new Document(docId);
		doc.addRevision(rev);
		System.out.println("success:" + doc);
	}
	
	public void testSaveDB() {
		Database.writeData(doc);
	}

}
