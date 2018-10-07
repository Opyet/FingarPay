package com.fingarpay.Data;
import android.util.Log;

import com.fingarpay.helper.BVNSearchInfo;
import com.fingarpay.helper.BanksInfo;
import com.fingarpay.helper.TransferFundsInfo;
import com.fingarpay.helper.UsersInfo;
import com.fingarpay.helper.UtilityHelper;

import org.json.JSONArray;
import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;



public class DataAccessMethods {
	String tag=this.getClass().getName();




	public ArrayList<BanksInfo> LoadBanks(){
		try {

			SoapObject request = new SoapObject(UtilityHelper.NAMESPACE, UtilityHelper.METHOD_NAME_BanksList);
			SoapSerializationEnvelope envelope =
					new SoapSerializationEnvelope(SoapEnvelope.VER11);
			envelope. dotNet = true;
			envelope.setOutputSoapObject(request);
			HttpTransportSE androidHttpTransport = new HttpTransportSE(UtilityHelper.SERVER_URL,60000);
			androidHttpTransport.setXmlVersionTag("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
			androidHttpTransport.call(UtilityHelper.NAMESPACE + UtilityHelper.METHOD_NAME_BanksList, envelope);

			try {


				//int count=ks.getPropertyCount();
				//String str1=ks.getProperty(0).toString();
				// String str2=ks.getProperty(1).toString();
				//System.out.print(str1 + ">>" + str2);
				if (envelope.getResponse() instanceof SoapFault) {
					String str= ((SoapFault) envelope.bodyIn).faultstring;
					Log.i("Error Fees", str);

				} else {
					KvmSerializable ks = (KvmSerializable)envelope.getResponse();
					// Object ks = (Object)envelope.getResponse();

					//ArrayList<FeedItem> lstrfid=new ArrayList<FeedItem>();
					// anyType{ResultCourseSummaryInfo=anyType{PortalId=0; StudentId=0; TotalTest=0;
					//TotalExams=0; Pos_Name=CNDDS; };

					if (ks==null) {
						return null;
					}
					//RegistratioNumber=kpt/casss/10/2095; AmountDue=24,500.00; AmountPaid=24,500.00; Balance=0.00; Level=100; SessioName=2010/2011; }
					int cnt=ks.getPropertyCount();
					Log.i("Counter", String.valueOf(cnt));
					ArrayList<BanksInfo> addSchProg=new ArrayList<BanksInfo>();
					for(int i=0;i<ks.getPropertyCount();i++)
					{
						BanksInfo spInfo=new BanksInfo();
						SoapObject rs4=(SoapObject)  ks.getProperty(i);
						spInfo.setBankName((rs4.getProperty("BankName").toString()));
						spInfo.setBankCode((rs4.getProperty("BankCBNCode").toString()));
						addSchProg.add(spInfo);

					}
					return addSchProg;
					// Toast.makeText(getApplicationContext(),resultString.toString(), Toast.LENGTH_LONG).show();
				}

			}
			catch (Exception e) {

				Log.e(tag, e.getMessage() + e.getStackTrace());
				return null;
			}


		} catch (Exception e) {

			Log.e(tag, e.getMessage() + e.getStackTrace());
		}

		return null;
	}



	public String VerifyAccountNumber(String account_number, String bank_code)
	{

		try {
			SoapObject request = new SoapObject(UtilityHelper.NAMESPACE, UtilityHelper.METHOD_NAME_VerifyAccountNumber);
			//String RegNo, int CampusGistId, Boolean isLiked
			request.addProperty("account_number", account_number);
			request.addProperty("bank_code", bank_code);

			SoapSerializationEnvelope envelope =
					new SoapSerializationEnvelope(SoapEnvelope.VER11);
			envelope. dotNet = true;
			envelope.setOutputSoapObject(request);
			HttpTransportSE androidHttpTransport = new HttpTransportSE(UtilityHelper.SERVER_URL,120000);
			androidHttpTransport.setXmlVersionTag("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
			androidHttpTransport.call(UtilityHelper.NAMESPACE + UtilityHelper.METHOD_NAME_VerifyAccountNumber, envelope);

			SoapPrimitive ks = (SoapPrimitive)envelope.getResponse();

			// KvmSerializable ks = (KvmSerializable)envelope.getResponse();

			return ks.toString();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return null;

	}

	public String VerifyCardDetails(String card_no, String cvv, String expiry_month, String expiry_year
			,String cardName,String mobileNo)
	{

		try {
			SoapObject request = new SoapObject(UtilityHelper.NAMESPACE, UtilityHelper.METHOD_NAME_VerifyCard);
			//String RegNo, int CampusGistId, Boolean isLiked
			request.addProperty("cardNo", card_no);
			request.addProperty("cardcvv", cvv);
			request.addProperty("expiryMonth", expiry_month);
			request.addProperty("expiryYear", expiry_year);
			request.addProperty("cardName", cardName);
			request.addProperty("mobileNo", mobileNo);

			SoapSerializationEnvelope envelope =
					new SoapSerializationEnvelope(SoapEnvelope.VER11);
			envelope. dotNet = true;
			envelope.setOutputSoapObject(request);
			HttpTransportSE androidHttpTransport = new HttpTransportSE(UtilityHelper.SERVER_URL,120000);
			androidHttpTransport.setXmlVersionTag("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
			androidHttpTransport.call(UtilityHelper.NAMESPACE + UtilityHelper.METHOD_NAME_VerifyCard, envelope);

			SoapPrimitive ks = (SoapPrimitive)envelope.getResponse();

			// KvmSerializable ks = (KvmSerializable)envelope.getResponse();

			return ks.toString();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return null;

	}

	public String GetTotalChargeable(String amount)
	{

		try {
			SoapObject request = new SoapObject(UtilityHelper.NAMESPACE, UtilityHelper.METHOD_NAME_GetTotalChargeable);
			//String RegNo, int CampusGistId, Boolean isLiked
			request.addProperty("amount", amount);

			SoapSerializationEnvelope envelope =
					new SoapSerializationEnvelope(SoapEnvelope.VER11);
			envelope. dotNet = true;
			envelope.setOutputSoapObject(request);
			HttpTransportSE androidHttpTransport = new HttpTransportSE(UtilityHelper.SERVER_URL);
			androidHttpTransport.setXmlVersionTag("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
			androidHttpTransport.call(UtilityHelper.NAMESPACE + UtilityHelper.METHOD_NAME_GetTotalChargeable, envelope);

			SoapPrimitive ks = (SoapPrimitive)envelope.getResponse();

			// KvmSerializable ks = (KvmSerializable)envelope.getResponse();

			return ks.toString();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return null;

	}

	public String TransferFromCardToAccountNumber(String username,String LastName,String ONames,String SenderEmail,String SenderMobile, String transactionDetails, String amount,
												  String cardNo, String cardcvv, String expiryMonth, String expiryYear, String receipientAccountNumber,
												  String receipientBankCode, String bankName, String redirectUrl, String MobileOrWeb)
	{
		try {
			SoapObject request = new SoapObject(UtilityHelper.NAMESPACE, UtilityHelper.METHOD_NAME_TransferFromCardToAccountNumber);
			//String RegNo, int CampusGistId, Boolean isLiked
			request.addProperty("username", username);
			request.addProperty("LastName", LastName);
			request.addProperty("ONames", ONames);
			request.addProperty("SenderEmail", SenderEmail);
			request.addProperty("SenderMobile", SenderMobile);
			request.addProperty("transactionDetails", transactionDetails);
			request.addProperty("amount", amount);
			request.addProperty("cardNo", cardNo);
			request.addProperty("cardcvv", cardcvv);
			request.addProperty("expiryMonth", expiryMonth);
			request.addProperty("expiryYear", expiryYear);
			request.addProperty("receipientAccountNumber", receipientAccountNumber);
			request.addProperty("receipientBankCode", receipientBankCode);
			request.addProperty("bankName", bankName);
			request.addProperty("redirectUrl", redirectUrl);
			request.addProperty("MobileOrWeb", MobileOrWeb);

			SoapSerializationEnvelope envelope =
					new SoapSerializationEnvelope(SoapEnvelope.VER11);
			envelope. dotNet = true;
			envelope.setOutputSoapObject(request);
			HttpTransportSE androidHttpTransport = new HttpTransportSE(UtilityHelper.SERVER_URL,80000);
			androidHttpTransport.setXmlVersionTag("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
			androidHttpTransport.call(UtilityHelper.NAMESPACE + UtilityHelper.METHOD_NAME_TransferFromCardToAccountNumber, envelope);

			SoapPrimitive ks = (SoapPrimitive)envelope.getResponse();

			// KvmSerializable ks = (KvmSerializable)envelope.getResponse();
			if (ks !=null) {
				return ks.toString();
			}

		}
		catch (Exception e) {
			Log.e(receipientAccountNumber, e.getMessage() + e.getStackTrace());
		}
		return null;

	}



	public ArrayList<TransferFundsInfo> TransactionByUsernameList(String Username){
		try {

			SoapObject request = new SoapObject(UtilityHelper.NAMESPACE, UtilityHelper.METHOD_NAME_TransactionByUsernameList);
			request.addProperty("Username", Username);

			SoapSerializationEnvelope envelope =
					new SoapSerializationEnvelope(SoapEnvelope.VER11);
			envelope. dotNet = true;
			envelope.setOutputSoapObject(request);
			HttpTransportSE androidHttpTransport = new HttpTransportSE(UtilityHelper.SERVER_URL,60000);
			androidHttpTransport.setXmlVersionTag("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
			androidHttpTransport.call(UtilityHelper.NAMESPACE + UtilityHelper.METHOD_NAME_TransactionByUsernameList, envelope);

			try {


				//int count=ks.getPropertyCount();
				//String str1=ks.getProperty(0).toString();
				// String str2=ks.getProperty(1).toString();
				//System.out.print(str1 + ">>" + str2);
				if (envelope.getResponse() instanceof SoapFault) {
					String str= ((SoapFault) envelope.bodyIn).faultstring;
					Log.i("Error Fees", str);

				} else {
					KvmSerializable ks = (KvmSerializable)envelope.getResponse();
					// Object ks = (Object)envelope.getResponse();

					//ArrayList<FeedItem> lstrfid=new ArrayList<FeedItem>();
					// anyType{ResultCourseSummaryInfo=anyType{PortalId=0; StudentId=0; TotalTest=0;
					//TotalExams=0; Pos_Name=CNDDS; };

					if (ks==null) {
						return null;
					}
					//RegistratioNumber=kpt/casss/10/2095; AmountDue=24,500.00; AmountPaid=24,500.00; Balance=0.00; Level=100; SessioName=2010/2011; }
					int cnt=ks.getPropertyCount();
					Log.i("Counter", String.valueOf(cnt));
					ArrayList<TransferFundsInfo> addSchProg=new ArrayList<TransferFundsInfo>();
					for(int i=0;i<ks.getPropertyCount();i++)
					{
						TransferFundsInfo spInfo=new TransferFundsInfo();
						SoapObject rs4=(SoapObject)  ks.getProperty(i);
						spInfo.setAmount(Double.valueOf(rs4.getProperty("Amount").toString()));
						spInfo.setIsSuccess((rs4.getProperty("IsSuccess").toString()));
						spInfo.setSenderUsername((rs4.getProperty("SenderUsername").toString()));
						spInfo.setSenderFullName((rs4.getProperty("SenderFullName").toString()));
						spInfo.setReceiverAccountName((rs4.getProperty("ReceiverAccountName").toString()));
						spInfo.setReceiverAccountNo((rs4.getProperty("ReceiverAccountNo").toString()));
						spInfo.setTransactionCode((rs4.getProperty("TransactionCode").toString()));
						spInfo.setTransactionStatus((rs4.getProperty("TransactionStatus").toString()));
						String tdetails=(rs4.getProperty("TransactionDetails").toString().replace("anyType{}", ""));
						spInfo.setTransactionDetails(tdetails);
						spInfo.setDateCreated((rs4.getProperty("DateCreated").toString().replace("T", " ")));
						addSchProg.add(spInfo);

					}
					return addSchProg;
					// Toast.makeText(getApplicationContext(),resultString.toString(), Toast.LENGTH_LONG).show();
				}

			}
			catch (Exception e) {

				Log.e(tag, e.getMessage() + e.getStackTrace());
				return null;
			}


		} catch (Exception e) {

			Log.e(tag, e.getMessage() + e.getStackTrace());
		}

		return null;
	}

	public ArrayList<TransferFundsInfo> TransactionsByCodeLoad(String transactionCode){
		try {

			SoapObject request = new SoapObject(UtilityHelper.NAMESPACE, UtilityHelper.METHOD_NAME_TransactionByCodeGet);
			request.addProperty("transactionCode", transactionCode);
			SoapSerializationEnvelope envelope =
					new SoapSerializationEnvelope(SoapEnvelope.VER11);
			envelope. dotNet = true;
			envelope.setOutputSoapObject(request);
			HttpTransportSE androidHttpTransport = new HttpTransportSE(UtilityHelper.SERVER_URL,60000);
			androidHttpTransport.setXmlVersionTag("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
			androidHttpTransport.call(UtilityHelper.NAMESPACE + UtilityHelper.METHOD_NAME_TransactionByCodeGet, envelope);

			try {

				//int count=ks.getPropertyCount();
				//String str1=ks.getProperty(0).toString();
				// String str2=ks.getProperty(1).toString();
				//System.out.print(str1 + ">>" + str2);
				if (envelope.getResponse() instanceof SoapFault) {
					String str= ((SoapFault) envelope.bodyIn).faultstring;
					Log.i("Error Fees", str);

				} else {
					KvmSerializable ks = (KvmSerializable)envelope.getResponse();
					// Object ks = (Object)envelope.getResponse();

					//ArrayList<FeedItem> lstrfid=new ArrayList<FeedItem>();
					// anyType{ResultCourseSummaryInfo=anyType{PortalId=0; StudentId=0; TotalTest=0;
					//TotalExams=0; Pos_Name=CNDDS; };

					if (ks==null) {
						return null;
					}
					//RegistratioNumber=kpt/casss/10/2095; AmountDue=24,500.00; AmountPaid=24,500.00; Balance=0.00; Level=100; SessioName=2010/2011; }
					int cnt=ks.getPropertyCount();
					Log.i("Counter", String.valueOf(cnt));
					ArrayList<TransferFundsInfo> addSchProg=new ArrayList<TransferFundsInfo>();
					for(int i=0;i<ks.getPropertyCount();i++)
					{
						TransferFundsInfo spInfo=new TransferFundsInfo();
						SoapObject rs4=(SoapObject)  ks.getProperty(i);
						spInfo.setAmount(Double.valueOf(rs4.getProperty("Amount").toString()));
						spInfo.setIsSuccess((rs4.getProperty("IsSuccess").toString()));
						spInfo.setReceiverAccountName((rs4.getProperty("ReceiverAccountName").toString()));
						spInfo.setReceiverAccountNo((rs4.getProperty("ReceiverAccountNo").toString()));
						spInfo.setTransactionCode((rs4.getProperty("TransactionCode").toString()));
						spInfo.setSenderUsername((rs4.getProperty("SenderUsername").toString().replace("anyType{}", "")));
						spInfo.setSenderFullName((rs4.getProperty("SenderFullName").toString().replace("anyType{}", "")));
						spInfo.setTransactionStatus((rs4.getProperty("TransactionStatus").toString()));
						String tdetails=(rs4.getProperty("TransactionDetails").toString().replace("anyType{}", ""));
						spInfo.setTransactionDetails(tdetails);
						spInfo.setDateCreated((rs4.getProperty("DateCreated").toString().replace("T", " ")));
						addSchProg.add(spInfo);

					}
					return addSchProg;
					// Toast.makeText(getApplicationContext(),resultString.toString(), Toast.LENGTH_LONG).show();
				}

			}
			catch (Exception e) {

				Log.e(tag, e.getMessage() + e.getStackTrace());
				return null;
			}


		} catch (Exception e) {

			Log.e(tag, e.getMessage() + e.getStackTrace());
		}

		return null;
	}

	public String UsersEmailRecoveryCode(String email)
	{

		try {
			SoapObject request = new SoapObject(UtilityHelper.NAMESPACE, UtilityHelper.METHOD_NAME_UsersEmailRecoveryCodeSend);
			//String RegNo, int CampusGistId, Boolean isLiked
			request.addProperty("email", email);

			SoapSerializationEnvelope envelope =
					new SoapSerializationEnvelope(SoapEnvelope.VER11);
			envelope. dotNet = true;
			envelope.setOutputSoapObject(request);
			HttpTransportSE androidHttpTransport = new HttpTransportSE(UtilityHelper.SERVER_URL,120000);
			androidHttpTransport.setXmlVersionTag("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
			androidHttpTransport.call(UtilityHelper.NAMESPACE + UtilityHelper.METHOD_NAME_UsersEmailRecoveryCodeSend, envelope);

			SoapPrimitive ks = (SoapPrimitive)envelope.getResponse();

			// KvmSerializable ks = (KvmSerializable)envelope.getResponse();

			return ks.toString();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return null;

	}

	public String UsersAutoChangePassword(String email,String newPassword,String resetCode)
	{

		try {
			SoapObject request = new SoapObject(UtilityHelper.NAMESPACE, UtilityHelper.METHOD_NAME_UsersAutoChangePassword);
			//String RegNo, int CampusGistId, Boolean isLiked
			request.addProperty("email", email);
			request.addProperty("newPassword", newPassword);
			request.addProperty("resetCode", resetCode);

			SoapSerializationEnvelope envelope =
					new SoapSerializationEnvelope(SoapEnvelope.VER11);
			envelope. dotNet = true;
			envelope.setOutputSoapObject(request);
			HttpTransportSE androidHttpTransport = new HttpTransportSE(UtilityHelper.SERVER_URL,120000);
			androidHttpTransport.setXmlVersionTag("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
			androidHttpTransport.call(UtilityHelper.NAMESPACE + UtilityHelper.METHOD_NAME_UsersAutoChangePassword, envelope);

			SoapPrimitive ks = (SoapPrimitive)envelope.getResponse();

			// KvmSerializable ks = (KvmSerializable)envelope.getResponse();

			return ks.toString();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return null;

	}


	public String UsersAdd(final UsersInfo bd)
	{
		try {
			SoapObject request = new SoapObject(UtilityHelper.NAMESPACE,
					UtilityHelper.METHOD_NAME_UsersAdd);

			request.addProperty("hosturl", UtilityHelper.URL_Host);
			request.addProperty("Surname",bd.getSurname());
			request.addProperty("Othernames", bd.getOthernames());
			request.addProperty("Email", bd.getEmail());
			request.addProperty("MobileNo", bd.getMobileNo());
			request.addProperty("PassCode", bd.getPassCode());

			//request.addProperty("IsActive",bd.getIsActive());
			request.addProperty("MobileOrWeb","mobile");
			request.addProperty("BizName", bd.getBizName());


			SoapSerializationEnvelope envelope =
					new SoapSerializationEnvelope(SoapEnvelope.VER11);
			envelope. dotNet = true;
			envelope.setOutputSoapObject(request);
			HttpTransportSE androidHttpTransport = new HttpTransportSE(UtilityHelper.SERVER_URL,60000);
			androidHttpTransport.setXmlVersionTag("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
			androidHttpTransport.call(UtilityHelper.NAMESPACE + UtilityHelper.METHOD_NAME_UsersAdd, envelope);


			SoapPrimitive ks = (SoapPrimitive)envelope.getResponse();

			// KvmSerializable ks = (KvmSerializable)envelope.getResponse();
			if (ks !=null) {
				return ks.toString();
			}

		}
		catch (Exception e) {
			Log.e("", e.getMessage() + e.getStackTrace());
		}
		return null;

	}
	public ArrayList<UsersInfo> LoginUser(String Username,String Password){
		try {

			SoapObject request = new SoapObject(UtilityHelper.NAMESPACE, UtilityHelper.METHOD_NAME_UsersLoginList);
			request.addProperty("Username", Username);
			request.addProperty("Password", Password);
			SoapSerializationEnvelope envelope =
					new SoapSerializationEnvelope(SoapEnvelope.VER11);
			envelope. dotNet = true;
			envelope.setOutputSoapObject(request);
			HttpTransportSE androidHttpTransport = new HttpTransportSE(UtilityHelper.SERVER_URL,120000);
			androidHttpTransport.setXmlVersionTag("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
			androidHttpTransport.call(UtilityHelper.NAMESPACE + UtilityHelper.METHOD_NAME_UsersLoginList, envelope);

			try {


				//int count=ks.getPropertyCount();
				//String str1=ks.getProperty(0).toString();
				// String str2=ks.getProperty(1).toString();
				//System.out.print(str1 + ">>" + str2);
				if (envelope.getResponse() instanceof SoapFault) {
					String str= ((SoapFault) envelope.bodyIn).faultstring;
					Log.i("Error Fees", str);

				} else {
					KvmSerializable ks = (KvmSerializable)envelope.getResponse();
					// Object ks = (Object)envelope.getResponse();

					//ArrayList<FeedItem> lstrfid=new ArrayList<FeedItem>();
					// anyType{ResultCourseSummaryInfo=anyType{PortalId=0; StudentId=0; TotalTest=0;
					//TotalExams=0; Pos_Name=CNDDS; };

					if (ks==null) {
						return null;
					}
					//RegistratioNumber=kpt/casss/10/2095; AmountDue=24,500.00; AmountPaid=24,500.00; Balance=0.00; Level=100; SessioName=2010/2011; }
					int cnt=ks.getPropertyCount();
					Log.i("Counter", String.valueOf(cnt));
					ArrayList<UsersInfo> addSchProg=new ArrayList<UsersInfo>();
					for(int i=0;i<ks.getPropertyCount();i++)
					{
						UsersInfo spInfo=new UsersInfo();
						SoapObject rs4=(SoapObject)  ks.getProperty(i);
						spInfo.setEmail((rs4.getProperty("Email").toString()));
						spInfo.setEmailVerificationCode((rs4.getProperty("EmailVerificationCode").toString()));
						spInfo.setFirstname((rs4.getProperty("Firstname").toString()));
						spInfo.setHasConfirmedEmail(Boolean.parseBoolean(rs4.getProperty("HasConfirmedEmail").toString()));
						spInfo.setIsActive(Boolean.parseBoolean(rs4.getProperty("IsActive").toString()));
						spInfo.setMobileNo((rs4.getProperty("MobileNo").toString()));
						spInfo.setOthernames((rs4.getProperty("Othernames").toString().replace("anyType{}", "")));
						spInfo.setSurname((rs4.getProperty("Surname").toString()));
						spInfo.setUsername((rs4.getProperty("Username").toString()));
						addSchProg.add(spInfo);
						break;
					}
					return addSchProg;
					// Toast.makeText(getApplicationContext(),resultString.toString(), Toast.LENGTH_LONG).show();
				}

			}
			catch (Exception e) {

				Log.e(tag, e.getMessage() + e.getStackTrace());
				return null;
			}


		} catch (Exception e) {

			Log.e(tag, e.getMessage() + e.getStackTrace());

		}

		return null;
	}


	public BVNSearchInfo BVNSearchQuick(String... args)
	{

		try {
			SoapObject request = new SoapObject(UtilityHelper.NAMESPACE, UtilityHelper.METHOD_NAME_BVNSearch);
			//String RegNo, int CampusGistId, Boolean isLiked
			request.addProperty("BVN", args[0].toString());

			SoapSerializationEnvelope envelope =
					new SoapSerializationEnvelope(SoapEnvelope.VER11);
			envelope. dotNet = true;
			envelope.setOutputSoapObject(request);
			HttpTransportSE androidHttpTransport = new HttpTransportSE(UtilityHelper.SERVER_URL,120000);
			androidHttpTransport.setXmlVersionTag("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
			androidHttpTransport.call(UtilityHelper.NAMESPACE + UtilityHelper.METHOD_NAME_BVNSearch, envelope);

			if (envelope.getResponse() instanceof SoapFault) {
				String str= ((SoapFault) envelope.bodyIn).faultstring;
				Log.i("Error Fees", str);

			} else {
				KvmSerializable ks = (KvmSerializable)envelope.getResponse();
				// Object ks = (Object)envelope.getResponse();

				//ArrayList<FeedItem> lstrfid=new ArrayList<FeedItem>();
				// anyType{ResultCourseSummaryInfo=anyType{PortalId=0; StudentId=0; TotalTest=0;
				//TotalExams=0; Pos_Name=CNDDS; };

				if (ks==null) {
					return null;
				}
				//RegistratioNumber=kpt/casss/10/2095; AmountDue=24,500.00; AmountPaid=24,500.00; Balance=0.00; Level=100; SessioName=2010/2011; }
				int cnt=ks.getPropertyCount();
				Log.i("Counter", String.valueOf(cnt));
				//ArrayList<BVNSearchInfo> addSchProg=new ArrayList<BVNSearchInfo>();

					BVNSearchInfo spInfo=new BVNSearchInfo();
					SoapObject rs4 = (SoapObject)envelope.getResponse();

					spInfo.setEmail((rs4.getProperty("Email").toString().replace("anyType{}", "")));
					spInfo.setBVN((rs4.getProperty("BVN").toString().replace("anyType{}", "")));
					spInfo.setBase64Image((rs4.getProperty("Base64Image").toString().replace("anyType{}", "")));
					spInfo.setEnrollmentBank((rs4.getProperty("EnrollmentBank").toString().replace("anyType{}", "")));
					spInfo.setFirstName((rs4.getProperty("FirstName").toString().replace("anyType{}", "")));
					spInfo.setLastName((rs4.getProperty("LastName").toString().replace("anyType{}", "")));
					spInfo.setMiddleName((rs4.getProperty("MiddleName").toString().replace("anyType{}", "")));
					spInfo.setNameOnCard((rs4.getProperty("NameOnCard").toString().replace("anyType{}", "")));
					//addSchProg.add(spInfo);
					return spInfo;
					//break;

				//return addSchProg;
				// Toast.makeText(getApplicationContext(),resultString.toString(), Toast.LENGTH_LONG).show();
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return null;

	}


}
