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
		if (!revision.Equals(getLatestRevision())) {
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
