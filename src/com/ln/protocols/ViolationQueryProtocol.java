package com.ln.protocols;

import java.net.URLEncoder;

import org.json.JSONObject;

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
	
	//TODO::��ʱûʵ��
	/*
	 * 
json:
{
    "info": {
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
}


������
{
    "info": {
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
        ]
    }
}
	 */
	public Object unpack(byte[] data)
	{
		//TODO::����json
		return null;
	}

}
