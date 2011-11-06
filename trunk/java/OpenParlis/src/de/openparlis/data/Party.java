package de.openparlis.data;

public class Party {

	private String fullName;
	private String shortName;
	
	public Party(String fullName, String shortName) {
		this.fullName = fullName;
		this.shortName = shortName;
	}
	
	public String getFullName() {
		return fullName;
	}
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}
	public String getShortName() {
		return shortName;
	}
	public void setShortName(String shortName) {
		this.shortName = shortName;
	}
	
}
