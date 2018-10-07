package com.fingarpay.helper;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.Arrays;


import android.app.Application;
import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.media.ThumbnailUtils;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.util.Log;

import com.fingarpay.BRMicro.NETLH_E;
import com.fingarpay.BRMicro.PARA_TABLE;
import com.fingarpay.BRMicro.SerialPort;


public class FingerPrintCommandManager {
	
	private static FingerPrintCommandManager manager ;
	private  NETLH_E netlh ;  //ָ�Ʋ������
	private SerialPort mSerialPort ;//���ڲ������
	
	/* ���ڲ��� */
	private String comPath = "/dev/ttyMT1";//���ڵ�ַ
	private int baudRate = 115200; //������
	private int dataBit = 8;//����λ
	private int stopBit = 2 ;//ֹͣλ
	private int checkMode = 0;//У��λ
	private int deviceAdd = 0xffffffff;//�豸��ַ
	private int password = 0xffffffff;//����
	
	
	private int ret = 0 ; //�������ر�ʶ
	
	private final int SUCCESS = 1; //�����ɹ�
	private final int FAIL = 0;  //����ʧ��
	
	public static final int BUFFER_A = 0;  //������A
	public static final int BUFFER_B = 1 ; //������B
	
	private Handler handler ;
	
	///storage/sdcard0/FingerLil.bmp
	//private final String mFingerImagePath =Environment.get().getPath() + "/FingerLil.bmp";
	public static String mFingerImagePath = "/mnt/sdcard/Finger.bmp";//��ʱ����ָ��ͼƬ��·������·�������޸�
	//private  String mFingerImagePath =Environment.getExternalStorageDirectory().getPath() + "/FingerLil.bmp";//��ʱ����ָ��ͼƬ��·������·�������޸�
	public FingerPrintCommandManager(Handler handler){
		this.handler = handler;
		netlh = new NETLH_E();
		mSerialPort = new SerialPort();
	};
	public FingerPrintCommandManager(){
		
		netlh = new NETLH_E();
		mSerialPort = new SerialPort();
	};
	private static boolean isExternalStorageReadOnly() {  
		  String extStorageState = Environment.getExternalStorageState();  
		  if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(extStorageState)) {  
		   return true;  
		  }  
		  return false;  
		 }  

	private static boolean isExternalStorageAvailable() {  
		  String extStorageState = Environment.getExternalStorageState();  
		  if (Environment.MEDIA_MOUNTED.equals(extStorageState)) {  
		   return true;  
		  }  
		  return false;  
		 } 
		 
	
	public boolean open(){
//		String openDevice = 
		sendMSG("open device");
//		sendMSG("���豸");
		//���豸����
//		mSerialPort.rfidPoweron();
		mSerialPort.psam_poweron();
		
//		mSerialPort.zigbee_poweron();
		//�л�
		mSerialPort.switch2Channel(12);
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ret = netlh.ConfigCommParameterCom(comPath, //���ڵ�ַ
				baudRate, //������
				dataBit, //����λ
				stopBit, //ֹͣλ
				checkMode,//У��λ
				deviceAdd,//�豸��ַ
				password);//����
		if(ret == FAIL){//��ʧ�ܣ��ٴδ�
			netlh.CmdDeviceGetChmod(comPath);
			ret = netlh.ConfigCommParameterCom(comPath, //���ڵ�ַ
					baudRate, //������
					dataBit, //����λ
					stopBit, //ֹͣλ
					checkMode,//У��λ
					deviceAdd,//�豸��ַ
					password);//����
		}
		if(ret == FAIL){
			return false ;
		}
		return true ;
	}
	
	/**
	 * �ر��豸
	 */
	public void close(){
		sendMSG("close device");
		if(mSerialPort != null){
			mSerialPort.psam_poweroff();
		}
		if(netlh != null){
			netlh.CommClose();
		}
	}
	
	int[] _ErrFlag = new int[512]; //������洢��
	/**
	 * ��ȡָ��ͼ��,ָ��ͼ�񻺴��SDĿ¼�� ��������SD��д��Ȩ��
	 * @return 
	 */
	public Bitmap getFPImage(){
		//sendMSG("��ȡָ��ͼ��...");
		sendMSG("get FP image...");
		Bitmap bp = null;
		//̽���Ƿ�����ָ
		ret = netlh.CmdDetectFinger(_ErrFlag);
		if(ret == 1 && _ErrFlag[0] == 0){//��ָ�ڴ�������
			netlh.CmdGetRedressImage(0, _ErrFlag);//��ȡָ��ͼ��
			if(_ErrFlag[0] == 0){//��ȡ��ָ��ͼ��
				ret = netlh.CmdUpLoadRedressImage(null); // �ϴ�ͼ��
				if(ret == 1){
					if (!isExternalStorageAvailable()) {
						mFingerImagePath = Environment.getDataDirectory().getPath() + "/FingerLil.bmp";;
					}else {
						//mFingerImagePath = "/mnt/sdcard/Finger.bmp";
						mFingerImagePath = Environment.getExternalStorageDirectory().getPath() + "/Finger.bmp";
					}
					
					bp=ThumbnailUtils.extractThumbnail(BitmapFactory.decodeFile(mFingerImagePath), 260, 300);
					
					//bp = BitmapFactory.decodeFile(mFingerImagePath,null);
					if (bp !=null) {
						//sendMSG("��ȡָ��ͼ��ɹ�");
						sendMSG("get FP image success");
						return bp;
					}
				
				}
			}
		}
		sendMSG("Error��" + _ErrFlag[0]);
		//sendMSG("�����룺" + _ErrFlag[0]);
		return null;
	}
	
	public Bitmap getFPImageFromCache(NETLH_E netlh){
		//sendMSG("��ȡָ��ͼ��...");
		sendMSG("get FP image...");
		Bitmap bp = null;
		ret=1;
		//̽���Ƿ�����ָ
		//ret = netlh.CmdDetectFinger(_ErrFlag);
		if(ret == 1 && _ErrFlag[0] == 0){//��ָ�ڴ�������
			netlh.CmdGetRedressImage(0, _ErrFlag);//��ȡָ��ͼ��
			if(_ErrFlag[0] == 0){//��ȡ��ָ��ͼ��
				ret = netlh.CmdUpLoadRedressImage(null); // �ϴ�ͼ��
				if(ret == 1){
					if (!isExternalStorageAvailable()) {
						mFingerImagePath = Environment.getDataDirectory().getPath() + "/FingerLil.bmp";;
					}else {
						//mFingerImagePath = "/mnt/sdcard/Finger.bmp";
						mFingerImagePath = Environment.getExternalStorageDirectory().getPath() + "/Finger.bmp";
					}
					
					bp=ThumbnailUtils.extractThumbnail(BitmapFactory.decodeFile(mFingerImagePath), 260, 300);
					
					//bp = BitmapFactory.decodeFile(mFingerImagePath,null);
					if (bp !=null) {
						//sendMSG("��ȡָ��ͼ��ɹ�");
						sendMSG("get FP image success");
						return bp;
					}
				
				}
			}
		}
		sendMSG("Error��" + _ErrFlag[0]);
		//sendMSG("�����룺" + _ErrFlag[0]);
		return null;
	}
	
	/**
	 * ����ָ���������������洢�ڻ�����buffer�У�bufferΪָ��ģ���е�BUFFER_A��BUFFER_B
	 * @param buffer ָ��ģ��洢��
	 * @return
	 */
	public boolean genChara(int buffer){
		try {
			sendMSG("Gen chara...");
			//̽���Ƿ�����ָ
			ret = netlh.CmdDetectFinger(_ErrFlag);
			if(ret == 1 && _ErrFlag[0] == 0){//��ָ�ڴ�������
				netlh.CmdGetRedressImage(0, _ErrFlag);//��ȡָ��ͼ��
				if(_ErrFlag[0] == 0){//��ȡ��ָ��ͼ��
					//��������ģ��
					ret = netlh.CmdGenChar(buffer, _ErrFlag);
					if(ret == 1 && _ErrFlag[0] == 0){
						if(buffer == BUFFER_A){
							//sendMSG("����ָ������A");
							sendMSG("Gen chara A");
						}else{
							//sendMSG("����ָ������B");
							sendMSG("Gen chara B");
						}
						return true ;
					}
				}
			}
//			sendMSG("�����룺" + _ErrFlag[0]);
			sendMSG("Error��" + _ErrFlag[0]);
		} catch (Exception e) {

			Log.e("Finger Thread", e.getMessage() + e.getStackTrace());
			
		}
		return false ;
	}
	
	/**
	 * �ϴ�ָ��ģ�壬��ָ��ģ��Ĵ洢����ȡָ��ģ�����ݣ���СΪ256�ֽ�
	 * @param bufferID
	 * @return
	 */
	public byte[] getChara(int bufferID ){
//		sendMSG("�ϴ�ָ��ģ��...");
		sendMSG("Upload chara...");
		byte[] templet = new byte[256];
		//��eAlg�㷨��ȡģ��
		ret = netlh.CmdGetChar_eAlg(bufferID, templet, _ErrFlag);
		if(ret == 1 && _ErrFlag[0] == 0){
//			sendMSG("�ϴ�ָ��ģ��ɹ�");
			sendMSG("Upload chara success");
		}else{
			templet = null;
//			sendMSG("�����룺" + _ErrFlag[0]);
			sendMSG("Error��" + _ErrFlag[0]);
		}
		return templet;
	}
	
	/**
	 * ����ָ��ģ�壬��PC������һ��ָ��ģ�嵽��һ����������,buffer_a,buffer_b
	 * @param bufferID ������
	 * @param templet  ģ������
	 * @return
	 */
	public boolean putChara(int bufferID, byte[] templet){
		ret = netlh.CmdPutChar_eAlg(bufferID, templet, _ErrFlag);
		if(ret == 1 && _ErrFlag[0] == 0){
			return true;
		}
		return false;
	}
	
	PARA_TABLE ptab = new PARA_TABLE();//ϵͳ������
	
	
	/**
	 * �洢ָ��ģ��
	 */
	public boolean mergeChara(int addr){
//		sendMSG("�洢ָ��ģ��...");
		sendMSG("Store FP templet...");
		int[] _RetScore = new int[10];//��ֵ
		int[] _RetMbIndex = new int[10];
		//�ϲ�����
		ret = netlh.CmdMatchChar(_RetScore, _ErrFlag);
		if(ret == 1 && _ErrFlag[0] == 0){
			/**
			 * ����˵����
			 * 10  д���ģ���ַ
			 * _RetMbIndex �����ģ��ָ�ƿ�����ָ��ģ�壬�򷵻ظõ�ַ��
			 * _RetScore �Աȷ�ֵ
			 */
			ret = netlh.CmdStoreChar(addr, _RetMbIndex, _RetScore, _ErrFlag);
			if(ret == 1 && _ErrFlag[0] == 0){
				//����ģ��ɹ�
//				sendMSG("�洢ָ��ģ��ɹ�����ַΪ��" + addr);
				sendMSG("Store FP templet success��address��" + addr);
				return true;
			}
		}
//		sendMSG("�����룺" + _ErrFlag[0]);
		sendMSG("Error��" + _ErrFlag[0]);
		return false;
	}
	
	/**
	 * �Ա�bufferA,buferB�е����������ضԱȷ�ֵ
	 * @return
	 */
	public int matchChara(){
//		sendMSG("�ȶ�BUFFER_A��BUFFER_B�е�����...");
		sendMSG("compare BUFFER_A with BUFFER_B...");
		int[] _RetScore = new int[10];//��ֵ
		Arrays.fill(_RetScore, 0);
		ret = netlh.CmdMatchChar(_RetScore, _ErrFlag);
		if(ret == 1 && _ErrFlag[0] == 0){
			//����ģ��ɹ�
//			sendMSG("�ȶԳɹ�����ֵΪ��" + _RetScore[0]);
			sendMSG("compare success��score��" + _RetScore[0]);
			return _RetScore[0];
		}
		return 0;
	}
	
	/**
	 * ָ��ģ�嵥һ�ȶԣ�����ֵ����50ʱ���Ա�ͨ��
	 * @param bufferID  ������ID
	 * @param iAddr  ָ��ģ��ģ��洢��ַ
	 * @return int �Աȷ�ֵ
	 */
	public int verifyChara(int bufferID, int iAddr){
		int[] _RetScore = new int[10];
		ret = netlh.CmdVerifyChar(bufferID, iAddr, _RetScore, _ErrFlag);
		return 0 ;
	}
	
	/**
	 * �Ǽ�ģ��,
	 * @param addr ָ��ģ���ַ
	 */
	public boolean enroll(int addr){
		//����ָ������A //����ָ������B
		if(genChara(BUFFER_A) && genChara(BUFFER_B)){
			//�洢ָ��
			return mergeChara(addr);
		}
		
		
		return false ;
	}
	
	/**
	 * ��ȡָ��ģ������������boolean���飬����ǰ������ֵΪtrue����ģ�����ݣ���Ϊfalse��û��ģ������
	 * @return
	 */
	public boolean[] getFreeAddress(){
//		sendMSG("��ȡָ��ģ������...");
		sendMSG("Get templet index...");
		boolean []fingerFlag = new boolean[1000];
		Arrays.fill(fingerFlag, false);
		byte[] gMBIndex = new byte[17 + 820 + 2 + 10];
		for(int i = 0; i < fingerFlag.length; i+=250){
			Arrays.fill(gMBIndex, (byte) 0x00);
			/**
			 * //��ȡָ��ģ������
			 * gMBIndex ��Ż�ȡ����ָ��ģ������ (1:��ģ�壬 0:û��ģ��)
			 * i ��ʼ��ַ
			 * 250 ��ȡ����
			 */
			ret = netlh.CmdGetMBIndex(gMBIndex, i, 250, _ErrFlag);
			if(ret == 1 && _ErrFlag[0] == 0){
				for(int y = 0; y < 250; y++){
					//��ָ��ģ�����
					if (gMBIndex[y] != 0x00){
						int index = i + y ;
						fingerFlag[index] = true;
//						sendMSG("��ַ[" + index + "] �Ѵ洢ָ��ģ��");
						sendMSG("address[" + index + "] store templet");
					}
						
				}
			}
		}
		return fingerFlag;
	}
	
	//����ָ��,�����Ѿ�����FLASH�е�ָ��ģ�塣
	public int searchChara(int bufferID){
//		sendMSG("����ָ��...");
		sendMSG("Search FP ...");
		int[] _RetMbIndex = new int[10];
		int [] _RetScore = new int[10];
		//��ȡָ������
		if(genChara(bufferID)){
			ret = netlh.CmdSearchChar(bufferID, _RetMbIndex, _RetScore, _ErrFlag);
			if(ret == 1 && _ErrFlag[0] == 0){
//				sendMSG("������ָ��ģ�壬��ַΪ[" + _RetMbIndex[0] + "]");
				sendMSG("Search FP success��address[" + _RetMbIndex[0] + "]");
			}else{
//				sendMSG("������Ϊ[" + _ErrFlag[0] + "]");
				sendMSG("Error[" + _ErrFlag[0] + "]");
			}
		}
		return -1;
	}
	
	//ɾ������ָ��ģ��
	public boolean emptyChara(){
//		sendMSG("ɾ������ָ��ģ��...");
		sendMSG("Empty all templet...");
		boolean flag = false ;
		ret = netlh.CmdEmptyChar(_ErrFlag);
		if(ret == 1 && _ErrFlag[0] == 0){
//			sendMSG("ɾ������ָ��ģ�����ݳɹ�");
			sendMSG("Empty all templet success");
			flag = true;
		}
		return false ;
	}
	
	//���ͷ�����Ϣ
	public void sendMSG(String data){
		if(handler != null){
			Message msg = new Message();
			Bundle bundle = new Bundle();
			bundle.putString("msg", "--" + data + "\r\n");
			msg.setData(bundle);
			handler.sendMessage(msg);
		}
	}
	
	private void sleep(int time){
		try {
			Thread.sleep(time);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	

}
