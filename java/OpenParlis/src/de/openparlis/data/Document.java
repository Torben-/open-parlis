package de.openparlis.data;

import java.util.ArrayList;
import java.util.Date;

public class Document {
	
	private DocumentId id;
	private ArrayList<DocumentRevision> revisions;
	
	public Document() {
		super();
		this.init();
	}

	public Document(DocumentId id) {
		super();
		this.init();
		this.id = id;
	}
	
	private void init() {
		this.revisions = new ArrayList<DocumentRevision>();
	}

	public void addRevision(DocumentRevision revision) {
		if (!revision.equals(getLatestRevision())) {
			this.revisions.add(revision);
		}
	}
	
	public DocumentRevision getLatestRevision() {
		try {
			// TODO optimize
			return revisions.get(revisions.size()-1);
		} catch (Exception ex) {
			return null;
		}
	}
	
	public String getHtml() {
		try {
			return getLatestRevision().getHtml();
		} catch (Exception ex) {
			return null;
		}
	}
	
	public String getSubject() {
		try {
			return getLatestRevision().getSubject();
		} catch (Exception ex) {
			return null;
		}
	}
	
	public String getContent() {
		try {
			return getLatestRevision().getContent();
		} catch (Exception ex) {
			return null;
		}
	}
	
	public Date getDocumentDate() {
		try {
			return getLatestRevision().getDocumentDate();
		} catch (Exception ex) {
			return null;
		}
	}
	
	public Date getFoundDate() {
		try {
			return getLatestRevision().getFoundDate();
		} catch (Exception ex) {
			return null;
		}
	}
	
	public Date getUpdateDate() {
		try {
			return getLatestRevision().getUpdateDate();
		} catch (Exception ex) {
			return null;
		}
	}
	
	public String toString() {
		String s = "";
		s += "\n=== ParlisDocument ===";
		s += "\n    ID: " + id;
		s += "\n### Revisions ###";
		for (DocumentRevision rev : this.revisions) {
			s += rev;
		}
		return s;
	}


}
