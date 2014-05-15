package com.ln.model;

import java.util.ArrayList;
import java.util.List;

public class ViolationResult {
	String _detail;// "查询到您有1条违章信息！",
    String _lastUpdateTime;// "6:43",
	List<Penalty> penalties = new ArrayList<Penalty>();
}
