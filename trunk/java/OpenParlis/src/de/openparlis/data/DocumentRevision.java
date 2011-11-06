package de.openparlis.data;

import java.util.Date;

public class DocumentRevision {

	private String html;
	private String subject;
	private String content;
	private Date documentDate;
	private Date foundDate;
	private Date updateDate;
	
	public String toString() {
		String s = "";
		s += "\n=== ParlisDocumentRevision ===";
		s += "\n    Subject:      " + subject;
		s += "\n    Content:      " + content;
		s += "\n    DocumentDate: " + documentDate;
		s += "\n    FoundDate:    " + foundDate;
		s += "\n    UpdateDate:   " + updateDate;
		s += "\n    HTML:         [" + html.length() + " Chars]";
		return s;
	}
	
	//=====================================================================
	// Getters and Setters BEGIN
	//=====================================================================

	public String getHtml() {
		return html;
	}
	
	public void setHtml(String html) {
		this.html = html;
	}
	
	public String getSubject() {
		return subject;
	}
	
	public void setSubject(String subject) {
		this.subject = subject;
	}	
	
	public Date getDocumentDate() {
		return documentDate;
	}
	
	public void setDocumentDate(Date documentDate) {
		this.documentDate = documentDate;
	}
	
	public String getContent() {
		return content;
	}
	
	public void setContent(String content) {
		this.content = content;
	}
	
	public Date getFoundDate() {
		return foundDate;
	}
	
	public void setFoundDate(Date foundDate) {
		this.foundDate = foundDate;
	}
	
	public Date getUpdateDate() {
		return updateDate;
	}
	
	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	//=====================================================================
	// Getters and Setters END
	//=====================================================================

}
