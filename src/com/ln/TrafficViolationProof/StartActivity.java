package com.ln.TrafficViolationProof;

import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.idreems.sdk.lightvolley.HttpForker;
import com.idreems.sdk.lightvolley.HttpGetDataSource;
import com.idreems.sdk.lightvolley.HttpPostDataSource;
import com.idreems.sdk.lightvolley.HttpTaskResponse;
import com.idreems.sdk.lightvolley.LightVolley;
import com.idreems.sdk.lightvolley.TaskForker;
import com.idreems.sdk.lightvolley.TaskListener;
import com.idreems.sdk.lightvolley.TaskQueue;
import com.idreems.sdk.lightvolley.TaskResponse;
import com.ln.common.Des;
import com.ln.common.GZip;
import com.ln.model.Penalty;
import com.ln.model.ViolationResult;
import com.ln.protocols.ViolationQueryProtocol;

public class StartActivity extends Activity implements TaskListener {
	private static final int HTTP_TIME_OUT = 10000;
	private static final String REQUE_URL_STRING = "http://trafficviolationproof.duapp.com/trafficquery.php";
	TaskQueue _taskQueue = LightVolley.newTaskQueue();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_start_activity);

		Button button = (Button) findViewById(R.id.btn_get_request);

		// get请求
		button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				HttpGetDataSource dataSource = HttpForker
						.newHttpGetDataSource(REQUE_URL_STRING);
				dataSource.setTimeoutMs(HTTP_TIME_OUT);

				_taskQueue.add(TaskForker.newHttpTask(getApplicationContext(),
						dataSource, StartActivity.this));
			}
		});

		button = (Button) findViewById(R.id.btn_post_request);
		// post请求
		button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// 创建post请求
				HttpPostDataSource postDatasource = HttpForker
						.newHttpPostDataSource(REQUE_URL_STRING);
				postDatasource.setTimeoutMs(HTTP_TIME_OUT);

				postDatasource.setBody(encrypt(new ViolationQueryProtocol()
						.pack()));

				// 加入任务执行队列，执行post请求
				_taskQueue.add(TaskForker.newHttpTask(getApplicationContext(),
						postDatasource, StartActivity.this));
			}
		});
	}

	/**
	 * ResponseListener
	 */
	@Override
	public void onSuccess(TaskResponse response) {
		if (!(response instanceof HttpTaskResponse)) {
			return;
		}
		HttpTaskResponse httpResponse = (HttpTaskResponse) response;
		if (httpResponse == null || httpResponse.data == null) {
			return;
		}

		// TODO::解析协议，暂时没有实现,直接输出json了
		String r = new String(((HttpTaskResponse) response).data);
		Object tmp = new ViolationQueryProtocol()
				.unpack(((HttpTaskResponse) response).data);
		// 是否有违章
		ViolationResult result = null;
		if (tmp instanceof ViolationResult) {
			result = (ViolationResult) tmp;
		}
		// 组织展现
		if (result != null) {
			if (result._penalties == null || result._penalties.size() == 0) {
				r = result._detail;
			} else {
				StringBuilder allPenalitiesBuilder = new StringBuilder();
				List<Penalty> penaltyList = result._penalties;
				for (Penalty penalty : penaltyList) {
					allPenalitiesBuilder.append(String.format(
							"%s %s %s %s %s\n\n", penalty._timeString,
							penalty._locationString, penalty._reasonString,
							penalty._fineString, penalty._pointsString));
				}
				r = allPenalitiesBuilder.toString();
			}
		}
		
		//TODO::在新的界面中展现违章信息
		//违章界面：参考车友会的展示界面：切换城市；查询违章处理地；地图模式查看；周边违章（其他人的违章地点，原因）
		//每个项目：详情页；标记为已处理(将不再出现在新的查询结果中)；查看违章地点(全景图)
		//每个违章项目的设计：尽量图形化（时间，地点，原因，处罚措施，全景图（暗含可以查看全景图））
		//延伸：
		//1.现有查询软件，查询后就不能再查看违章信息了：退出历史违章
		//2.
		if (!TextUtils.isEmpty(r)) {
			TextView tView = (TextView) findViewById(R.id.tv_result);
			tView.setText(r);
		}

		// time to release
		if (_taskQueue.size() == 0) {
			_taskQueue.stop();
		}
	}

	@Override
	public void onFailure(int errorCode, String message) {
		// 失败后的处理
		TextView tView = (TextView) findViewById(R.id.tv_result);
		String msg = TextUtils.isEmpty(message) ? String.format(
				Locale.getDefault(), "失败了，错误码: %d", errorCode) : message;
		tView.setText(msg);
	}

	public static byte[] encrypt(byte[] data) {
		// 先压缩，后加密
		return Des.encode(Des.DES_KEY, GZip.getGZipCompressed(data),
				Des.STRING_IV);
	}

	/**
	 * 云端数据传输解密函数
	 * 
	 * @param data
	 * @return
	 */
	public static byte[] decrypt(byte[] data) {
		try {
			// 先进行base64解码
			data = Base64.decode(data, Base64.DEFAULT);
			return GZip.getGZipUncompress(Des.decode(Des.DES_KEY, data,
					Des.STRING_IV));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new byte[0];
	}

}
