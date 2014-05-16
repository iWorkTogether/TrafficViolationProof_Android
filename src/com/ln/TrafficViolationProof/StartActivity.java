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

		// get����
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
		// post����
		button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// ����post����
				HttpPostDataSource postDatasource = HttpForker
						.newHttpPostDataSource(REQUE_URL_STRING);
				postDatasource.setTimeoutMs(HTTP_TIME_OUT);

				postDatasource.setBody(encrypt(new ViolationQueryProtocol()
						.pack()));

				// ��������ִ�ж��У�ִ��post����
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

		// TODO::����Э�飬��ʱû��ʵ��,ֱ�����json��
		String r = new String(((HttpTaskResponse) response).data);
		Object tmp = new ViolationQueryProtocol()
				.unpack(((HttpTaskResponse) response).data);
		// �Ƿ���Υ��
		ViolationResult result = null;
		if (tmp instanceof ViolationResult) {
			result = (ViolationResult) tmp;
		}
		// ��֯չ��
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
		
		//TODO::���µĽ�����չ��Υ����Ϣ
		//Υ�½��棺�ο����ѻ��չʾ���棺�л����У���ѯΥ�´���أ���ͼģʽ�鿴���ܱ�Υ�£������˵�Υ�µص㣬ԭ��
		//ÿ����Ŀ������ҳ�����Ϊ�Ѵ���(�����ٳ������µĲ�ѯ�����)���鿴Υ�µص�(ȫ��ͼ)
		//ÿ��Υ����Ŀ����ƣ�����ͼ�λ���ʱ�䣬�ص㣬ԭ�򣬴�����ʩ��ȫ��ͼ���������Բ鿴ȫ��ͼ����
		//���죺
		//1.���в�ѯ�������ѯ��Ͳ����ٲ鿴Υ����Ϣ�ˣ��˳���ʷΥ��
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
		// ʧ�ܺ�Ĵ���
		TextView tView = (TextView) findViewById(R.id.tv_result);
		String msg = TextUtils.isEmpty(message) ? String.format(
				Locale.getDefault(), "ʧ���ˣ�������: %d", errorCode) : message;
		tView.setText(msg);
	}

	public static byte[] encrypt(byte[] data) {
		// ��ѹ���������
		return Des.encode(Des.DES_KEY, GZip.getGZipCompressed(data),
				Des.STRING_IV);
	}

	/**
	 * �ƶ����ݴ�����ܺ���
	 * 
	 * @param data
	 * @return
	 */
	public static byte[] decrypt(byte[] data) {
		try {
			// �Ƚ���base64����
			data = Base64.decode(data, Base64.DEFAULT);
			return GZip.getGZipUncompress(Des.decode(Des.DES_KEY, data,
					Des.STRING_IV));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new byte[0];
	}

}
