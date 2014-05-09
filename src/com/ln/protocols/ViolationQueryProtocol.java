package com.ln.protocols;

import java.net.URLEncoder;

import org.json.JSONObject;

import android.text.TextUtils;

public class ViolationQueryProtocol {
	private static String AREA = "area";// 查询所在地区的违章
	private static String LICNUMBER = "licNumber";// 车牌号
	private static String ENGINENUMBER = "engineNumber";// 发动机号[部分地区必选，部分地区必选但是不匹配]
	private static String FRAMENUMBER = "frameNumber";// 车架号[部分地区必选]

	private String _area="北京";
	private String _licNumber="冀F810JH";
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
	
	//TODO::暂时没实现
	public Object unpack(byte[] data)
	{
		return null;
	}

}
