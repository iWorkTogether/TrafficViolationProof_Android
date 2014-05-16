package com.ln.protocols;

import java.net.URLEncoder;

import org.json.JSONArray;
import org.json.JSONObject;

import com.ln.model.Penalty;
import com.ln.model.ViolationResult;

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
	
	private ViolationResult _result = new ViolationResult();
	
	//获取查询结果
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
	 * 存在时，返回非null对象
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
		
		return penalty;
	}
	
	public Object unpack(byte[] data)
	{
		
		if (data==null||data.length==0) {
			return null;
		}
		
		//TODO::解析json
		try {
			JSONObject jsonObj = new JSONObject(new String(data));
			//parse info
			final Object info = getJsonObject(jsonObj,"info");
			Object ret = info;
			
			if (ret==null|| !(ret instanceof JSONObject) ) {
				return null;
			}
			
			ret = getJsonObject((JSONObject)ret, "@attributes");
			if (ret==null) {
				return null;
			}
			
			ret = getJsonObject((JSONObject)ret, "error");
			if (ret instanceof String) {
				String error = (String)ret;
				if (TextUtils.isEmpty(error)) {
					return null;
				}
				try {
					if (Integer.valueOf(error)!=0) {
						return null;
					}
				} catch (Exception e) {
					e.printStackTrace();
					return null;
				}
			}
			final JSONObject item = (JSONObject)ret;
			ret = getJsonObject(item, "detail");
			if (ret instanceof String) 
			{
				_result._detail=(String)ret;
			}
			
			ret = getJsonObject(item, "lastUpdateTime");
			if (ret instanceof String) 
			{
				_result._lastUpdateTime=(String)ret;
			}
			
			//parse wz(obj or array)
			if (info==null|| !(info instanceof JSONObject) ) {
				return null;
			}
			ret = getJsonObject((JSONObject)info,"wz");
			
			if (ret==null) {
				return null;
			}
			
			//object or array
			if (ret instanceof JSONObject) {
				ret = getJsonObject((JSONObject)ret, "@attributes");
				if (ret==null) {
					return null;
				}
				
				Penalty penalty = parsePenalty((JSONObject)ret);
				if(penalty!=null)
				{
					_result._penalties.add(penalty);
				}
			}
			else if(ret instanceof JSONArray) {
				JSONArray array = (JSONArray)ret;
				for (int i = 0; i < array.length(); i++) {
					ret = array.get(i);
					
					if (ret instanceof JSONObject) {
						ret = getJsonObject((JSONObject)ret, "@attributes");
						if (ret==null) {
							return null;
						}
						
						Penalty penalty = parsePenalty((JSONObject)ret);
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
	
	
	//TODO::暂时没实现
	/*
	 * 
	 * 
json:
{
    "info": {
        "@attributes": {
            "type": "wz_v2",
            "error": "0",
            "detail": "查询到您有1条违章信息！",
            "lastUpdateTime": "6:43",
            "requestid": "64621256",
            "postage": "0"
        },
        "wz": {
            "@attributes": {
                "wzid": "0",
                "msgid": "0",
                "lpn": "冀F810JH",
                "time": "2014-04-19 14:51:00",
                "location": "二拨子桥下 北向南",
                "reason": "违反禁令标志指示的",
                "penalty": "100",
                "points": "3",
                "illegal": "13441",
                "db_status": "0",
                "tip": "不可申请代办",
                "fee": "0.0",
                "city": ""
            }
        }
    }
}


多条：
{
    "info": {
        "@attributes": {
            "type": "wz_v2",
            "error": "0",
            "detail": "查询到您有1条违章信息！",
            "lastUpdateTime": "6:43",
            "requestid": "64621256",
            "postage": "0"
        },
        "wz": [
            {
                "@attributes": {
                    "wzid": "0",
                    "msgid": "0",
                    "lpn": "冀F810JH",
                    "time": "2014-04-19 14:51:00",
                    "location": "二拨子桥下 北向南",
                    "reason": "违反禁令标志指示的",
                    "penalty": "100",
                    "points": "3",
                    "illegal": "13441",
                    "db_status": "0",
                    "tip": "不可申请代办",
                    "fee": "0.0",
                    "city": ""
                }
            },
            {
                "@attributes": {
                    "wzid": "1",
                    "msgid": "1",
                    "lpn": "冀F810JH",
                    "time": "2014-04-19 14:51:00",
                    "location": "二拨子桥下 北向南",
                    "reason": "违反禁令标志指示的",
                    "penalty": "100",
                    "points": "3",
                    "illegal": "13441",
                    "db_status": "0",
                    "tip": "不可申请代办",
                    "fee": "0.0",
                    "city": ""
                }
            }
        ]
    }
}
	 */
	

}
