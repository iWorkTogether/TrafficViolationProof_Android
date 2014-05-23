package com.ln.protocols;

import java.net.URLEncoder;

import org.json.JSONArray;
import org.json.JSONObject;

import com.ln.model.Penalty;
import com.ln.model.ViolationResult;

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
	
	private ViolationResult _result = new ViolationResult();
	
	//��ȡ��ѯ���
	public ViolationResult getViolationResult()
	{
		return _result;
	}
	
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
	
	/**
	 * ����ʱ�����ط�null����
	 * @param obj
	 * @param key
	 * @return
	 */
	private static Object getJsonObject(JSONObject obj,String key)
	{
		if (obj==null||TextUtils.isEmpty(key)) {
			return null;
		}
		
		try {
			if (obj.has(key)) {
				return obj.get(key);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	private static Penalty parsePenalty(JSONObject tmp)
	{
		Penalty penalty = new Penalty();
		Object ret = getJsonObject(tmp, "lpn");
		if (ret instanceof String) 
		{
			penalty._licenceNumberString = (String)ret;
		}
		
		ret = getJsonObject(tmp, "time");
		if (ret instanceof String) 
		{
			penalty._timeString = (String)ret;
		}
		
		ret = getJsonObject(tmp, "location");
		if (ret instanceof String) 
		{
			penalty._locationString = (String)ret;
		}
		
		ret = getJsonObject(tmp, "reason");
		if (ret instanceof String) 
		{
			penalty._reasonString = (String)ret;
		}
		
		ret = getJsonObject(tmp, "penalty");
		if (ret instanceof String) 
		{
			penalty._fineString = (String)ret;
		}
		
		ret = getJsonObject(tmp, "points");
		if (ret instanceof String) 
		{
			penalty._pointsString = (String)ret;
		}
		
		ret = getJsonObject(tmp, "illegal");
		if (ret instanceof String) 
		{
			penalty._illegalCodeString = (String)ret;
		}
		
		ret = getJsonObject(tmp, "tip");
		if (ret instanceof String) 
		{
			penalty._tipString = (String)ret;
		}
		
		return penalty;
	}
	
	public Object unpack(byte[] data)
	{	
		if (data==null||data.length==0) {
			return _result;
		}
		
		//TODO::����json
		try {
			final JSONObject baseJsonObj = new JSONObject(new String(data));
			//parse info
			final Object attributesJsonObj = getJsonObject(baseJsonObj,"@attributes");
			Object tmpObj= attributesJsonObj;
			
			if (attributesJsonObj==null|| !(attributesJsonObj instanceof JSONObject) ) {
				return _result;
			}
			
			tmpObj = getJsonObject((JSONObject)tmpObj, "error");
			if (tmpObj instanceof String) {
				String error = (String)tmpObj;
				if (TextUtils.isEmpty(error)) {
					return _result;
				}
				try {
					if (Integer.valueOf(error)!=0) {
						return _result;
					}
				} catch (Exception e) {
					e.printStackTrace();
					return _result;
				}
			}
			tmpObj = getJsonObject((JSONObject)attributesJsonObj, "detail");
			if (tmpObj instanceof String) 
			{
				_result._detail=(String)tmpObj;
			}
			
			tmpObj = getJsonObject((JSONObject)attributesJsonObj, "lastUpdateTime");
			if (tmpObj instanceof String) 
			{
				_result._lastUpdateTime=(String)tmpObj;
			}
			
			//parse wz(obj or array)
			tmpObj = getJsonObject(baseJsonObj,"wz");
			
			if (tmpObj==null) {
				return _result;
			}
			
			//object or array
			if (tmpObj instanceof JSONObject) {
				tmpObj = getJsonObject((JSONObject)tmpObj, "@attributes");
				if (tmpObj==null) {
					return _result;
				}
				
				Penalty penalty = parsePenalty((JSONObject)tmpObj);
				if(penalty!=null)
				{
					_result._penalties.add(penalty);
				}
			}
			else if(tmpObj instanceof JSONArray) {
				JSONArray array = (JSONArray)tmpObj;
				for (int i = 0; i < array.length(); i++) {
					tmpObj = array.get(i);
					
					if (tmpObj instanceof JSONObject) {
						tmpObj = getJsonObject((JSONObject)tmpObj, "@attributes");
						if (tmpObj==null) {
							return _result;
						}
						
						Penalty penalty = parsePenalty((JSONObject)tmpObj);
						if(penalty!=null)
						{
							_result._penalties.add(penalty);
						}
					}
				}
				
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return _result;
	}
	
	
	//TODO::��ʱûʵ��
	/*
	 * 
	 * 
json:
    {
        "@attributes": {
            "type": "wz_v2",
            "error": "0",
            "detail": "��ѯ������1��Υ����Ϣ��",
            "lastUpdateTime": "6:43",
            "requestid": "64621256",
            "postage": "0"
        },
        "wz": {
            "@attributes": {
                "wzid": "0",
                "msgid": "0",
                "lpn": "��F810JH",
                "time": "2014-04-19 14:51:00",
                "location": "���������� ������",
                "reason": "Υ�������־ָʾ��",
                "penalty": "100",
                "points": "3",
                "illegal": "13441",
                "db_status": "0",
                "tip": "�����������",
                "fee": "0.0",
                "city": ""
            }
        }
}


������
 {
        "@attributes": {
            "type": "wz_v2",
            "error": "0",
            "detail": "��ѯ������1��Υ����Ϣ��",
            "lastUpdateTime": "6:43",
            "requestid": "64621256",
            "postage": "0"
        },
        "wz": [
            {
                "@attributes": {
                    "wzid": "0",
                    "msgid": "0",
                    "lpn": "��F810JH",
                    "time": "2014-04-19 14:51:00",
                    "location": "���������� ������",
                    "reason": "Υ�������־ָʾ��",
                    "penalty": "100",
                    "points": "3",
                    "illegal": "13441",
                    "db_status": "0",
                    "tip": "�����������",
                    "fee": "0.0",
                    "city": ""
                }
            },
            {
                "@attributes": {
                    "wzid": "1",
                    "msgid": "1",
                    "lpn": "��F810JH",
                    "time": "2014-04-19 14:51:00",
                    "location": "���������� ������",
                    "reason": "Υ�������־ָʾ��",
                    "penalty": "100",
                    "points": "3",
                    "illegal": "13441",
                    "db_status": "0",
                    "tip": "�����������",
                    "fee": "0.0",
                    "city": ""
                }
            }
        }
    ], 
    "@attributes": {
        "detail": "��ѯ������2��Υ����Ϣ��", 
        "requestid": "72720301", 
        "error": "0", 
        "type": "wz_v2", 
        "tmbSite": "http://www.bjjtgl.gov.cn/publish/portal0/", 
        "lastUpdateTime": "9:41", 
        "postage": "0"
    }
	 */
	

}
