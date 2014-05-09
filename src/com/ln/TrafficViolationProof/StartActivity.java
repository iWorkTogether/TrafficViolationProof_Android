package com.ln.TrafficViolationProof;

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

		//TODO::解析协议，暂时没有实现,直接输出json了
//		new ViolationQueryProtocol().unpack(((HttpTaskResponse) response).data);
		String r = new String(((HttpTaskResponse) response).data);
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
