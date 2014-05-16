package com.ln.model;

import java.util.ArrayList;
import java.util.List;

public class ViolationResult {
	public String _detail;// "查询到您有1条违章信息！",
	public String _lastUpdateTime;// "6:43",
	public List<Penalty> _penalties = new ArrayList<Penalty>();
}
