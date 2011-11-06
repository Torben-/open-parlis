package de.openparlis.data;

public class DocumentId {
	
	private DocumentType type;
	private int number;
	private int district;
	private int year;
	
	public DocumentId(DocumentType type, int number, int year) {
		super();
		this.type = type;
		this.number = number;
		this.district = 0;
		this.year = year;
	}

	public DocumentId(DocumentType type, int number, int district, int year) {
		super();
		this.type = type;
		this.number = number;
		this.district = district;
		this.year = year;
	}

	public String toString() {
		String s;
		if (district>0) {
			s = type + "_" + number + "-" + district + "_" + year;
		} else {
			s = type + "_" + number + "_" + year;
		}
		return s;
	}
	
	//=====================================================================
	// Getters and Setters BEGIN
	//=====================================================================

	public DocumentType getType() {
		return type;
	}

	public void setType(DocumentType type) {
		this.type = type;
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public int getDistrict() {
		return district;
	}

	public void setDistrict(int district) {
		this.district = district;
	}	
	
	//=====================================================================
	// Getters and Setters END
	//=====================================================================

}
