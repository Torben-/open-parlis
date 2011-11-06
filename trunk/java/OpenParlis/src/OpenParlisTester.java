import helper.HtmlDownloader;
import helper.HtmlParser;
import helper.ParlisHelper;

import java.net.URL;

import data.Database;
import data.Document;
import data.DocumentId;
import data.DocumentRevision;
import data.DocumentType;


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
		
		URL url = ParlisHelper.createParlisUrl(docId);
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
