package com.ln.model;

public class Penalty {
/**
 * "lpn": "冀F810JH",
                    "time": "2014-04-19 14:51:00",
                    "location": "二拨子桥下 北向南",
                    "reason": "违反禁令标志指示的",
                    "penalty": "100",
                    "points": "3",
                    "illegal": "13441",
                    "db_status": "0",
                    "tip": "不可申请代办",//TODO::增加解析
 */
	public String _licenceNumberString;//"冀F810JH"
	public String _timeString;//"2014-04-19 14:51:00"
	public String _locationString;//"二拨子桥下 北向南"
	public String _reasonString;//"违反禁令标志指示的"
	public String _fineString;//"100",
	public String _pointsString;//"3"
	public String _illegalCodeString;//"13441"
	public String _tipString;//"不可申请代办"
	
	@Override
	public String toString()
	{
		return "时间:"+_timeString+"\n"+"地点："+_locationString+"\n"+"原因："+_reasonString+"\n"+"罚款:"+_fineString+"\n"+"扣分:"+_pointsString+"\n"+"提示:"+_tipString+"\n";
	}
}
