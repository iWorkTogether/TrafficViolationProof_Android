package com.ln.TrafficViolationProof;

import android.app.Activity;
import android.os.Bundle;
/*
 * 违章查询的结果页面
 * 
 * //TODO::
		//违章界面：参考车友会的展示界面：切换城市；查询违章处理地；地图模式查看；周边违章（其他人的违章地点，原因）
		//每个项目：详情页；标记为已处理(将不再出现在新的查询结果中)；查看违章地点(全景图)
		//每个违章项目的设计：尽量图形化（时间，地点，原因，处罚措施，全景图（暗含可以查看全景图））
		//延伸：
		//1.现有查询软件，查询后就不能再查看违章信息了：退出历史违章
		//2.
 */
public class ViolationListActivity extends Activity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_violation_list_activity);
	}
}
