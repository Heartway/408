package com.ian.carassist.view;

/**
 * Created by Terry on 2016/4/17.
 */
import java.io.DataOutputStream;
import java.io.IOException;

import com.ian.carassist.MyWindowManager;
import com.ian.carassist.service.AssistService;
import com.ian.carassist.R;

import android.app.Instrumentation;
import android.content.Context;
import android.content.Intent;
import android.hardware.input.InputManager;
import android.net.wifi.WifiManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class FloatWindowBigView extends LinearLayout {

	/**
	 * 记录大悬浮窗的宽度
	 */
	public static int viewWidth;

	/**
	 * 记录大悬浮窗的高度
	 */
	public static int viewHeight;
	
	TextView mIpTextView;
	Context mContext;

	public FloatWindowBigView(final Context context) {
		super(context);
		mContext = context;
		LayoutInflater.from(context).inflate(R.layout.float_windows, this);
		View view = findViewById(R.id.big_windows);
		viewWidth = view.getLayoutParams().width;
		viewHeight = view.getLayoutParams().height;
		Button adbBtn = (Button) findViewById(R.id.adb);
		Button homeBtn = (Button) findViewById(R.id.home);
		Button closeBtn = (Button) findViewById(R.id.settings);
		Button backBtn = (Button) findViewById(R.id.close);
		adbBtn.setOnClickListener(mButtonListener);

		mIpTextView = (TextView) findViewById(R.id.ip);
	}

	private OnClickListener mButtonListener = new OnClickListener() {
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.adb:
				execRootCmd("stop adbd");
				execRootCmd("setprop service.adb.tcp.port 5555");
				execRootCmd("start adbd");
				WifiManager wm = (WifiManager) mContext.getSystemService("wifi");
				if (wm.isWifiEnabled()) {
					int ipInt = wm.getConnectionInfo().getIpAddress();
					mIpTextView.setText(intToIp(ipInt));
				}
				break;
			case R.id.home:
				InputManager im = (InputManager)mContext.getSystemService(Context.INPUT_SERVICE);
				 Instrumentation inst = new Instrumentation();
				                     // 调用inst对象的按键模拟方法
				                      inst.sendKeyDownUpSync(3);
				break;
			case R.id.settings:
				break;
			case R.id.close:
				MyWindowManager.removeBigWindow(mContext);
				break;
			default:
				break;
			}
		}
	};

	public static int execRootCmd(String cmd) {
		int result = -1;
		DataOutputStream dos = null;
		try {
			Process p = Runtime.getRuntime().exec("su");
			dos = new DataOutputStream(p.getOutputStream());
			Log.i("ca", cmd);
			dos.writeBytes(cmd + "\n");
			dos.flush();
			dos.writeBytes("exit\n");
			dos.flush();
			p.waitFor();
			result = p.exitValue();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (dos != null) {
				try {
					dos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return result;
	}
	
	private String intToIp(int ipInt)
	  {
	    return (ipInt & 0xFF) + "." + (ipInt >> 8 & 0xFF) + "." + (ipInt >> 16 & 0xFF) + "." + (ipInt >> 24 & 0xFF);
	  }
}