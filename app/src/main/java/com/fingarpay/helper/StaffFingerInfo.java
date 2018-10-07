package com.fingarpay.helper;

public class StaffFingerInfo {

	private int Id;
	private byte[] RFThumb ;
	private byte[] RFIndex ;
	private byte[] LFThumb ;
	private byte[] LFIndex ;
	private int  StaffId ;
	private String StaffCode;
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
	 * @return the email
	 */
	public String getEmail() {
		return Email;
	}
	/**
	 * @param email the email to set
	 */
	public void setEmail(String email) {
		Email = email;
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
	private String Surname;
	private String Email;
	private String OtherNames;
	
	private String DateCreated;
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
	 * @return the rFThumb
	 */
	public byte[] getRFThumb() {
		return RFThumb;
	}
	/**
	 * @param rFThumb the rFThumb to set
	 */
	public void setRFThumb(byte[] rFThumb) {
		RFThumb = rFThumb;
	}
	/**
	 * @return the rFIndex
	 */
	public byte[] getRFIndex() {
		return RFIndex;
	}
	/**
	 * @param rFIndex the rFIndex to set
	 */
	public void setRFIndex(byte[] rFIndex) {
		RFIndex = rFIndex;
	}
	/**
	 * @return the lFThumb
	 */
	public byte[] getLFThumb() {
		return LFThumb;
	}
	/**
	 * @param lFThumb the lFThumb to set
	 */
	public void setLFThumb(byte[] lFThumb) {
		LFThumb = lFThumb;
	}
	/**
	 * @return the lFIndex
	 */
	public byte[] getLFIndex() {
		return LFIndex;
	}
	/**
	 * @param lFIndex the lFIndex to set
	 */
	public void setLFIndex(byte[] lFIndex) {
		LFIndex = lFIndex;
	}
	/**
	 * @return the staffId
	 */
	public int getStaffId() {
		return StaffId;
	}
	/**
	 * @param staffId the staffId to set
	 */
	public void setStaffId(int staffId) {
		StaffId = staffId;
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

	
	
}
