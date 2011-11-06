package de.openparlis.data;

import java.util.ArrayList;

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
		this.revisions.add(revision);
	}
	
	public DocumentRevision getLastRevision() {
		return revisions.get(revisions.size()-1);
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
