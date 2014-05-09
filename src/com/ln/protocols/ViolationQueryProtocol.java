package com.ln.protocols;

import java.net.URLEncoder;

import org.json.JSONObject;

import android.text.TextUtils;

public class ViolationQueryProtocol {
	private static String AREA = "area";// ��ѯ���ڵ�����Υ��
	private static String LICNUMBER = "licNumber";// ���ƺ�
	private static String ENGINENUMBER = "engineNumber";// ��������[���ֵ�����ѡ�����ֵ�����ѡ���ǲ�ƥ��]
	private static String FRAMENUMBER = "frameNumber";// ���ܺ�[���ֵ�����ѡ]

	private String _area="����";
	private String _licNumber="��F810JH";
	private String _engineNumber="hgdddf";
	private String _frameNumber;
	
	public void set(String area,String licNumber,String engineNumber,String frameNumber)
	{
		_area = area;
		_licNumber=licNumber;
		_engineNumber=engineNumber;
		_frameNumber=frameNumber;
	}
	
	public byte[] pack() {
		JSONObject object = new JSONObject();
		try {
			if(!TextUtils.isEmpty(_area))
			{
				object.put(AREA, URLEncoder.encode(_area));
			}
			
			if(!TextUtils.isEmpty(_licNumber))
			{
				object.put(LICNUMBER, URLEncoder.encode(_licNumber));
			}
			
			if(!TextUtils.isEmpty(_engineNumber))
			{
				object.put(ENGINENUMBER, _engineNumber);
			}
			
			if(!TextUtils.isEmpty(_frameNumber))
			{
				object.put(FRAMENUMBER, "123456");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return object.toString().getBytes();
	}
	
	//TODO::��ʱûʵ��
	public Object unpack(byte[] data)
	{
		return null;
	}

}
