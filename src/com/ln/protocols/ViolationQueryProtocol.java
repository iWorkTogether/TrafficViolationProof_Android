package com.ln.protocols;

import java.net.URLEncoder;

import org.json.JSONObject;

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
	
	//TODO::暂时没实现
	/*
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
	public Object unpack(byte[] data)
	{
		//TODO::解析json
		return null;
	}

}
