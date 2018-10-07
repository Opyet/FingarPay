package com.fingarpay.helper;

public class StaffDetailsInfo {
	private int Id;
	private String Surname;
	private String StaffCode;
	private String OtherNames;
	private String Mobile;
	private String Discipline;
	private String DateCreated;
	private int IsAdmin;
	private String Email;
	
	
	/**
	 * @return the id
	 */
	public int getId() {
		return Id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(int id) {
		Id = id;
	}
	/**
	 * @return the surname
	 */
	public String getSurname() {
		return Surname;
	}
	/**
	 * @param surname the surname to set
	 */
	public void setSurname(String surname) {
		Surname = surname;
	}
	/**
	 * @return the staffCode
	 */
	public String getStaffCode() {
		return StaffCode;
	}
	/**
	 * @param staffCode the staffCode to set
	 */
	public void setStaffCode(String staffCode) {
		StaffCode = staffCode;
	}
	/**
	 * @return the otherNames
	 */
	public String getOtherNames() {
		return OtherNames;
	}
	/**
	 * @param otherNames the otherNames to set
	 */
	public void setOtherNames(String otherNames) {
		OtherNames = otherNames;
	}
	/**
	 * @return the discipline
	 */
	public String getDiscipline() {
		return Discipline;
	}
	/**
	 * @param discipline the discipline to set
	 */
	public void setDiscipline(String discipline) {
		Discipline = discipline;
	}
	/**
	 * @return the dateCreated
	 */
	public String getDateCreated() {
		return DateCreated;
	}
	/**
	 * @param dateCreated the dateCreated to set
	 */
	public void setDateCreated(String dateCreated) {
		DateCreated = dateCreated;
	}
	/**
	 * @return the isAdmin
	 */
	public int getIsAdmin() {
		return IsAdmin;
	}
	/**
	 * @param isAdmin the isAdmin to set
	 */
	public void setIsAdmin(int isAdmin) {
		IsAdmin = isAdmin;
	}
	public String getMobile() {
		return Mobile;
	}
	public void setMobile(String mobile) {
		Mobile = mobile;
	}
	public String getEmail() {
		return Email;
	}
	public void setEmail(String email) {
		Email = email;
	}
	
}
