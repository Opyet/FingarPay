package com.fingarpay.helper;
public class TransferFundsInfo {
	private int Id;
	private int PortalId ;
	private String TransactionCode ;
    private String SenderUsername ;
    private String SenderFullName;
    private String ReceiverAccountNo ;
    private String ReceiverAccountName ;
    private String ReceiverBankName ;
    private String ReceiverBankCode ;
    public double  Amount ;
    private String TransactionDetails ;
    private String TransactionStatus ;
    public int TransactionSwitchId ;
    private String TransactionSwitchReference ;
    private String TransactionIPAddress ;
    public double TransactionChargedFees ;
    public double MerchantCommission ;
    private String TransactionReceiptNo ;
    public String IsSuccess ;


    public String DateCreated ;
    public int CreatedBy ;
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
	 * @return the portalId
	 */
	public int getPortalId() {
		return PortalId;
	}
	/**
	 * @param portalId the portalId to set
	 */
	public void setPortalId(int portalId) {
		PortalId = portalId;
	}
	/**
	 * @return the transactionCode
	 */
	public String getTransactionCode() {
		return TransactionCode;
	}
	/**
	 * @param transactionCode the transactionCode to set
	 */
	public void setTransactionCode(String transactionCode) {
		TransactionCode = transactionCode;
	}
	/**
	 * @return the senderUsername
	 */
	public String getSenderUsername() {
		return SenderUsername;
	}
	/**
	 * @param senderUsername the senderUsername to set
	 */
	public void setSenderUsername(String senderUsername) {
		SenderUsername = senderUsername;
	}
	/**
	 * @return the receiverAccountNo
	 */
	public String getReceiverAccountNo() {
		return ReceiverAccountNo;
	}
	/**
	 * @param receiverAccountNo the receiverAccountNo to set
	 */
	public void setReceiverAccountNo(String receiverAccountNo) {
		ReceiverAccountNo = receiverAccountNo;
	}
	/**
	 * @return the receiverAccountName
	 */
	public String getReceiverAccountName() {
		return ReceiverAccountName;
	}
	/**
	 * @param receiverAccountName the receiverAccountName to set
	 */
	public void setReceiverAccountName(String receiverAccountName) {
		ReceiverAccountName = receiverAccountName;
	}
	/**
	 * @return the receiverBankName
	 */
	public String getReceiverBankName() {
		return ReceiverBankName;
	}
	/**
	 * @param receiverBankName the receiverBankName to set
	 */
	public void setReceiverBankName(String receiverBankName) {
		ReceiverBankName = receiverBankName;
	}
	/**
	 * @return the receiverBankCode
	 */
	public String getReceiverBankCode() {
		return ReceiverBankCode;
	}
	/**
	 * @param receiverBankCode the receiverBankCode to set
	 */
	public void setReceiverBankCode(String receiverBankCode) {
		ReceiverBankCode = receiverBankCode;
	}
	/**
	 * @return the amount
	 */
	public double getAmount() {
		return Amount;
	}
	/**
	 * @param amount the amount to set
	 */
	public void setAmount(double amount) {
		Amount = amount;
	}
	/**
	 * @return the transactionDetails
	 */
	public String getTransactionDetails() {
		return TransactionDetails;
	}
	/**
	 * @param transactionDetails the transactionDetails to set
	 */
	public void setTransactionDetails(String transactionDetails) {
		TransactionDetails = transactionDetails;
	}
	/**
	 * @return the transactionStatus
	 */
	public String getTransactionStatus() {
		return TransactionStatus;
	}
	/**
	 * @param transactionStatus the transactionStatus to set
	 */
	public void setTransactionStatus(String transactionStatus) {
		TransactionStatus = transactionStatus;
	}
	/**
	 * @return the transactionSwitchId
	 */
	public int getTransactionSwitchId() {
		return TransactionSwitchId;
	}
	/**
	 * @param transactionSwitchId the transactionSwitchId to set
	 */
	public void setTransactionSwitchId(int transactionSwitchId) {
		TransactionSwitchId = transactionSwitchId;
	}
	/**
	 * @return the transactionSwitchReference
	 */
	public String getTransactionSwitchReference() {
		return TransactionSwitchReference;
	}
	/**
	 * @param transactionSwitchReference the transactionSwitchReference to set
	 */
	public void setTransactionSwitchReference(String transactionSwitchReference) {
		TransactionSwitchReference = transactionSwitchReference;
	}
	/**
	 * @return the transactionIPAddress
	 */
	public String getTransactionIPAddress() {
		return TransactionIPAddress;
	}
	/**
	 * @param transactionIPAddress the transactionIPAddress to set
	 */
	public void setTransactionIPAddress(String transactionIPAddress) {
		TransactionIPAddress = transactionIPAddress;
	}
	/**
	 * @return the transactionChargedFees
	 */
	public double getTransactionChargedFees() {
		return TransactionChargedFees;
	}
	/**
	 * @param transactionChargedFees the transactionChargedFees to set
	 */
	public void setTransactionChargedFees(double transactionChargedFees) {
		TransactionChargedFees = transactionChargedFees;
	}
	/**
	 * @return the merchantCommission
	 */
	public double getMerchantCommission() {
		return MerchantCommission;
	}
	/**
	 * @param merchantCommission the merchantCommission to set
	 */
	public void setMerchantCommission(double merchantCommission) {
		MerchantCommission = merchantCommission;
	}
	/**
	 * @return the transactionReceiptNo
	 */
	public String getTransactionReceiptNo() {
		return TransactionReceiptNo;
	}
	/**
	 * @param transactionReceiptNo the transactionReceiptNo to set
	 */
	public void setTransactionReceiptNo(String transactionReceiptNo) {
		TransactionReceiptNo = transactionReceiptNo;
	}
	/**
	 * @return the isSuccess
	 */
	public String isIsSuccess() {
		return IsSuccess;
	}
	/**
	 * @param isSuccess the isSuccess to set
	 */
	public void setIsSuccess(String isSuccess) {
		IsSuccess = isSuccess;
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
	 * @return the createdBy
	 */
	public int getCreatedBy() {
		return CreatedBy;
	}
	/**
	 * @param createdBy the createdBy to set
	 */
	public void setCreatedBy(int createdBy) {
		CreatedBy = createdBy;
	}
	public String getSenderFullName() {
		return SenderFullName;
	}
	public void setSenderFullName(String senderFullName) {
		SenderFullName = senderFullName;
	}

    
}
