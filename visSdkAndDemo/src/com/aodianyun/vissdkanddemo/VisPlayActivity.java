/*******************************************************************************
 * Copyright (c) 2015 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package com.aodianyun.vissdkanddemo;

import java.text.SimpleDateFormat;

import com.aodianyun.Vis_Sdk;
import com.aodianyun.vissdkanddemo.R.drawable;

import android.app.Activity;
import android.view.View.OnClickListener;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class VisPlayActivity extends Activity implements OnClickListener{
	boolean isPlaying;
	int tsID;
	String outTsPath;
	SurfaceView sv;
	EditText logText;
	Boolean showLog, enableVideo;
	float srcWidth;
	float srcHeight;
	DisplayMetrics dm;
	Vis_Sdk  vis;
	String httpUrl,app,stream;
	Button btn_play;
    private int x;
    private int y;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_play);
		dm = getResources().getDisplayMetrics();
		
		vis = new Vis_Sdk(this);
		vis.SetVisDelegate(new Vis_Sdk.VisDelegate() {
			@Override
			public void onEventCallback(int event, String msg) {
				Message message = new Message();
				Bundle b = new Bundle();
				b.putString("msg", msg);
				message.setData(b);
				message.what = event;
				handler.sendMessage(message);
			}
		});
		
		sv = (SurfaceView) findViewById(R.id.surfaceview_play);
		logText = (EditText) findViewById(R.id.editText3);
		logText.setVisibility(View.GONE);
		btn_play = (Button) findViewById(R.id.btn_visplay);
		btn_play.setOnClickListener(this);
		setListener();
		
		httpUrl = (String)SharedPreUtil.get(this, "visUrl", "http://vis.aodianyun.com:5000");
		app = (String)SharedPreUtil.get(this, "visApp", "demo");
		stream = (String)SharedPreUtil.get(this, "visStream", "visdemo");
		String pwd = (String)SharedPreUtil.get(this, "visPwd", "123456");
		String uid = (String)SharedPreUtil.get(this, "visUid", "1029");
		vis.Init(null, sv, httpUrl, app, stream, pwd,uid);
	}
	
	private void setListener(){
		sv.setOnTouchListener(new View.OnTouchListener() {
            
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                
                switch (motionEvent.getAction() & MotionEvent.ACTION_MASK) {
                  case MotionEvent.ACTION_DOWN: {
                      x = (int)motionEvent.getRawX();
                      y = (int)motionEvent.getRawY();
                      break;
                  }
                  case MotionEvent.ACTION_UP: {      
                	  if((int)motionEvent.getRawX() - x > 8*3 && (int)motionEvent.getRawY() > 8*3)
                	  {
                          Toast.makeText(getApplicationContext(), 
                                  "显示日志", Toast.LENGTH_SHORT).show();
                          logText.setVisibility(View.VISIBLE);
                	  }else{
                		  logText.setVisibility(View.GONE);
                	  }
                		  
                      break;
                  }
                  case MotionEvent.ACTION_MOVE: {

                      break;                    
                  }
                }
                return true;
            }
        });
	}
	
	/**
	 *监听手机旋转，不销毁activity进行画面旋转，再缩放显示区域
	 */
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		doVideoFix();
	}
	
	/**
	 * 视频画面高宽等比缩放，此SDK——demo 取屏幕高宽做最大高宽缩放
	 */
	private void doVideoFix() {
		float maxWidth = dm.widthPixels;
		float maxHeight = dm.heightPixels;
		float fixWidth;
		float fixHeight;
		if (srcWidth / srcHeight <= maxWidth / maxHeight) {
			fixWidth = srcWidth * maxHeight / srcHeight;
			fixHeight = maxHeight;
		} else {
			fixWidth = maxWidth;
			fixHeight = srcHeight * maxWidth / srcWidth;
		}
		ViewGroup.LayoutParams lp = sv.getLayoutParams();
		lp.width = (int)fixWidth;
		lp.height = (int)fixHeight;
		
		sv.setLayoutParams(lp);
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		new Thread(new Runnable() {
			@Override
			public void run() {
				//stopPlay 为同步阻塞方法，
				vis.StopPlay();
			}
		}).start();
		
	}
	
	private Handler handler = new Handler() {
		// 回调处理
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);

			StringBuffer sb = new StringBuffer();
			SimpleDateFormat sDateFormat = new SimpleDateFormat("HH:mm:ss:SSS");
			String sRecTime = sDateFormat.format(new java.util.Date());
			sb.append(sRecTime);
			sb.append(" - ");
			sb.append(msg.getData().getString("msg"));
			sb.append("\r\n");
			logText.append(sb);

			switch (msg.what) {
			case 1000:
				// Toast.makeText(LivePlayerDemoActivity.this, "正在连接视频",
				// Toast.LENGTH_SHORT).show();
				break;
			case 1001:
				// Toast.makeText(LivePlayerDemoActivity.this, "视频连接成功",
				// Toast.LENGTH_SHORT).show();
				break;
			case 1002:
				// Toast.makeText(LivePlayerDemoActivity.this, "视频连接失败",
				// Toast.LENGTH_SHORT).show();
				break;
			case 1004:
				Toast.makeText(VisPlayActivity.this, "视频播放结束", Toast.LENGTH_SHORT).show();
				break;
			case 1005:
				// Toast.makeText(LivePlayerDemoActivity.this, "网络异常,播放中断",
				// Toast.LENGTH_SHORT).show();
				break;
			case 1006:
				// Toast.makeText(LivePlayerDemoActivity.this, "视频数据未找到",
				// Toast.LENGTH_SHORT).show();
				break;
			case 1007:
				// Toast.makeText(LivePlayerDemoActivity.this, "音频数据未找到",
				// Toast.LENGTH_SHORT).show();
				break;
			case 1008:
				// Toast.makeText(LivePlayerDemoActivity.this, "无法打开视频解码器",
				// Toast.LENGTH_SHORT).show();
				break;
			case 1009:
				// Toast.makeText(LivePlayerDemoActivity.this, "无法打开音频解码器",
				// Toast.LENGTH_SHORT).show();
				break;
			case 1100:
				System.out.println("NetStream.Buffer.Empty");
				break;
			case 1102:
				System.out.println("NetStream.Buffer.Full");
				break;
			case 1103:
				System.out.println("Stream EOF");
				break;
			case 1104:
				/**
				 * 得到 解码后得到的视频高宽值,可用于重绘surfaceview的大小比例 格式为:{width}x{height}
				 * 本例使用LinearLayout内嵌SurfaceView
				 * LinearLayout的大小为最大高宽,SurfaceView在内部等比缩放,画面不失真
				 * 等比缩放使用在不确定视频源高宽比例的场景,用上层LinearLayout限定了最大高宽
				 */
				String[] info = msg.getData().getString("msg").split("x");
				srcWidth = Integer.valueOf(info[0]);
				srcHeight = Integer.valueOf(info[1]);
				doVideoFix();
				break;
				
			case 3:
				vis.Stop();
				Toast.makeText(VisPlayActivity.this,msg.getData().getString("msg"),Toast.LENGTH_SHORT).show();
				break;
			default:
				break;
			}
		}
	};

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()){
		case R.id.btn_visplay:
			if(isPlaying){
				btn_play.setText(R.string.btn_play);
				vis.StopPlay();
			}else{
				btn_play.setText(R.string.btn_stopplay);
				vis.StartPlay();
			}
			isPlaying = !isPlaying;
			break;
		}
	}

}
