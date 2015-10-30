package com.aodianyun.vissdkanddemo;

import java.text.SimpleDateFormat;

import com.aodianyun.Vis_Sdk;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;
import cn.nodemedia.LivePublisher;

public class VisActivity extends Activity implements OnClickListener{
	private SurfaceView smallSv;
	private SurfaceView bigSv;
	private RelativeLayout bigLayout,smallLayout;
	private Button btn_test,btnSwitchCam,btnChange,flashBtn,micBtn;
	private Vis_Sdk vis;
	private Boolean enableVideo = true, isStartVis = false,bPlayOnly = false,bCanPublish=true,bPubishOnly=false;
	private boolean bBigPublish=false;
	private String httpUrl = null,app = null, stream = null,pwd,uid;
	private EditText logText;
    private int x;
    private int y;
	private boolean isMicOn = true;
	private boolean isCamOn = true;
	private boolean isFlsOn = false;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_vis);
		
		bigLayout = (RelativeLayout) findViewById(R.id.testlayout);
		smallLayout = (RelativeLayout) findViewById(R.id.smalllayout);
		smallSv = (SurfaceView) findViewById(R.id.surfaceview1);
		bigSv = (SurfaceView) findViewById(R.id.cameraView);
		smallSv.getHolder().setFormat(PixelFormat.TRANSPARENT);
		//sv.setZOrderOnTop(true);
		smallSv.setZOrderMediaOverlay(true);
		btn_test = (Button)findViewById(R.id.button1);
		btn_test.setOnClickListener(this);
		btnSwitchCam = (Button) findViewById(R.id.button_sw);
		btnSwitchCam.setOnClickListener(this);
		btnChange = (Button) findViewById(R.id.button_change);
		btnChange.setOnClickListener(this);
		flashBtn = (Button) findViewById(R.id.button_flash);
		flashBtn.setOnClickListener(this);
		micBtn = (Button) findViewById(R.id.button_mic);
		micBtn.setOnClickListener(this);
		logText = (EditText) findViewById(R.id.editText3);
		// 隐藏上麦控件
		this.isShowPublishActvie(false);
		// 获取vis相关参数
		httpUrl = (String)SharedPreUtil.get(this, "visUrl", "http://vis.aodianyun.com:5000");
		app = (String)SharedPreUtil.get(this, "visApp", "demo");
		stream = (String)SharedPreUtil.get(this, "visStream", "visdemo");
		pwd =(String)SharedPreUtil.get(this, "visPwd", "123456");
		uid = (String)SharedPreUtil.get(this, "visUid", "1029");
		vis = new Vis_Sdk(this);
		vis.SetVisDelegate(new Vis_Sdk.VisDelegate(){

			@Override
			public void onEventCallback(int event, String msg) {
				// TODO Auto-generated method stub
				Message message = new Message();
				Bundle b = new Bundle();
				b.putString("msg", msg);
				message.setData(b);
				message.what = event;
				handler.sendMessage(message);
			}

		});
		// 仅仅传递playview 表示只播放，不上麦
		if(vis.Init(smallSv, bigSv, httpUrl, app, stream, pwd,uid)==false){
			bCanPublish = false;
		}
		vis.setBufferTime(1000);
		vis.StartPlay();
		smallSv.setVisibility(View.GONE);
		bPlayOnly = true;
		isStartVis = false;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId())
		{
		case R.id.button1:
			 OnButton1();	
			 break;
		case R.id.button_sw:
			 OnButton_sw();
			 break;
		case R.id.button_change:
			OnBtnChange();
			break;
		case R.id.button_flash:
			OnBtnFlash();
			break;
		case R.id.button_mic:
			OnBtnMic();
			break;
		 default:
			 break;
		}
	}
	
	public void OnButton1(){
		if(isStartVis == true){
			this.isShowPublishActvie(false);
			vis.Stop();
			smallSv.setVisibility(View.GONE);
			vis.StartPlay();
			//bPubishOnly = true;
			isStartVis = false;
			btn_test.setText("发布");
		}else{
			if(vis.IsCanPublish()==false)
			{
				Toast.makeText(VisActivity.this, "目前没有上麦位置，请重试", Toast.LENGTH_SHORT).show();
				return;
			}
			this.isShowPublishActvie(true);
			vis.StopPlay();
			vis.Start();
			smallSv.setVisibility(View.VISIBLE);
			//bPubishOnly = true;
			isStartVis = true;
			btn_test.setText("停止");
		}
	}
	
	public void OnButton_sw(){
		vis.SwitchCam();
	}
	
	private void OnBtnChange(){
		bBigPublish = !bBigPublish;
		if(bBigPublish){
			smallSv.setZOrderMediaOverlay(false);
			bigSv.setZOrderMediaOverlay(true);
			bigSv.getHolder().setFormat(PixelFormat.TRANSPARENT);
			this.smallLayout.removeAllViews();
			this.bigLayout.removeView(this.bigSv);
			//this.bigLayout.removeAllViews();
			this.bigLayout.addView(this.smallSv);
			this.smallLayout.addView(this.bigSv);
		}else{
			smallSv.setZOrderMediaOverlay(true);
			bigSv.setZOrderMediaOverlay(false);
			//bigSv.getHolder().setFormat(PixelFormat.TRANSPARENT);
			this.smallLayout.removeAllViews();
			this.bigLayout.removeView(this.smallSv);
			//this.bigLayout.removeAllViews();
			this.bigLayout.addView(this.bigSv);
			this.smallLayout.addView(this.smallSv);
		}
	}
	
	private void OnBtnFlash()
	{
		int ret = -1;
		if(isFlsOn) {
			ret = vis.setFlashEnable(false);
		}else {
			ret = vis.setFlashEnable(true);
		}
		if(ret == -1) {
			//无闪光灯,或处于前置摄像头,不支持闪光灯操作
		}else if(ret == 0) {
			//闪光灯被关闭
			flashBtn.setBackgroundResource(R.drawable.cameraflashoff);
			isFlsOn = false;
		}else {
			//闪光灯被打开
			flashBtn.setBackgroundResource(R.drawable.cameraflashon);
			isFlsOn = true;
		}
	}
	
	private void OnBtnMic()
	{
		isMicOn = !isMicOn;
		vis.setMicEnable(isMicOn); //设置是否打开麦克风
		if (isMicOn) {
			micBtn.setBackgroundResource(R.drawable.micon);
			handler.sendEmptyMessage(2101);
		} else {
			micBtn.setBackgroundResource(R.drawable.micoff);
			handler.sendEmptyMessage(2100);
		}
	}
	
	private void isShowPublishActvie(boolean isShow)
	{
		if(isShow){
			btnSwitchCam.setVisibility(View.VISIBLE);
			btnChange.setVisibility(View.VISIBLE);
			flashBtn.setVisibility(View.VISIBLE);
			micBtn.setVisibility(View.VISIBLE);
		}else{
			btnSwitchCam.setVisibility(View.GONE);
			btnChange.setVisibility(View.GONE);
			flashBtn.setVisibility(View.GONE);
			micBtn.setVisibility(View.GONE);
		}
	}
	
    public boolean intersect(View v1, View v2) {
        int v2left = v2.getLeft();
        int v2right = v2.getRight();
        int v2top = v2.getTop();
        int v2bottom = v2.getBottom();
       
        int v1left = v1.getLeft();
        int v1right = v1.getRight();
        int v1top = v1.getTop();
        int v1bottom = v1.getBottom();
       
        return !(v2left > v1right || v2right < v1left || v2top > v1bottom ||
                 v2bottom < v1top);        
    }

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		new Thread(new Runnable() {
			@Override
			public void run() {
				//stopPlay 为同步阻塞方法，
				vis.Stop();
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
				 Toast.makeText(VisActivity.this, "正在连接视频",
				 Toast.LENGTH_SHORT).show();
				break;
			case 1001:
				 Toast.makeText(VisActivity.this, "视频连接成功",
				 Toast.LENGTH_SHORT).show();
				break;
			case 1002:
				// Toast.makeText(LivePlayerDemoActivity.this, "视频连接失败",
				// Toast.LENGTH_SHORT).show();
				break;
			case 1004:
				//Toast.makeText(LivePlayerDemoActivity.this, "视频播放结束", Toast.LENGTH_SHORT).show();
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
				smallLayout.setVisibility(View.GONE);
				smallLayout.setVisibility(View.VISIBLE);
				break;
			case 1104:
				/**
				 * 得到 解码后得到的视频高宽值,可用于重绘surfaceview的大小比例 格式为:{width}x{height}
				 * 本例使用LinearLayout内嵌SurfaceView
				 * LinearLayout的大小为最大高宽,SurfaceView在内部等比缩放,画面不失真
				 * 等比缩放使用在不确定视频源高宽比例的场景,用上层LinearLayout限定了最大高宽
				 */
				String[] info = msg.getData().getString("msg").split("x");
//				srcWidth = Integer.valueOf(info[0]);
//				srcHeight = Integer.valueOf(info[1]);
//				doVideoFix();
				break;
				
				
			case 2000:
				Toast.makeText(VisActivity.this, "正在发布视频", Toast.LENGTH_SHORT).show();
				break;
			case 2001:
				Toast.makeText(VisActivity.this, "视频发布成功", Toast.LENGTH_SHORT).show();
				//videoBtn.setBackgroundResource(R.drawable.ic_video_start);
				//isStarting = true;
				break;
			case 2002:
				Toast.makeText(VisActivity.this, "视频发布失败", Toast.LENGTH_SHORT).show();
				break;
			case 2004:
				Toast.makeText(VisActivity.this, "视频发布结束", Toast.LENGTH_SHORT).show();
				//videoBtn.setBackgroundResource(R.drawable.ic_video_stop);
				//isStarting = false;
				break;
			case 2005:
				Toast.makeText(VisActivity.this, "网络异常,发布中断", Toast.LENGTH_SHORT).show();
				break;
			case 2100:
				// mic off
				//micBtn.setBackgroundResource(R.drawable.ic_mic_off);
				Toast.makeText(VisActivity.this, "麦克风静音", Toast.LENGTH_SHORT).show();
				break;
			case 2101:
				// mic on
				//micBtn.setBackgroundResource(R.drawable.ic_mic_on);
				Toast.makeText(VisActivity.this, "麦克风恢复", Toast.LENGTH_SHORT).show();
				break;
			case 2102:
				// camera off
				//camBtn.setBackgroundResource(R.drawable.ic_cam_off);
				Toast.makeText(VisActivity.this, "摄像头传输关闭", Toast.LENGTH_SHORT).show();
				break;
			case 2103:
				// camera on
				//camBtn.setBackgroundResource(R.drawable.ic_cam_on);
				Toast.makeText(VisActivity.this, "摄像头传输打开", Toast.LENGTH_SHORT).show();
				break;
			// VISevent
			case 3:
				 Toast.makeText(VisActivity.this, msg.getData().getString("msg"),
						 Toast.LENGTH_SHORT).show();
				 break;
			default:
				break;
			}
		}
	};
	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		// TODO Auto-generated method stub
		SurfaceView sv = (SurfaceView) findViewById(R.id.surfaceview1);
		SurfaceView svPublish = (SurfaceView) findViewById(R.id.cameraView);
		LayoutParams lpPlay = (LayoutParams)sv.getLayoutParams();
		LayoutParams lpPublish = (LayoutParams)svPublish.getLayoutParams();
		LayoutParams tmp=null;
//		if(bPlayOnly){
//			tmp = lpPlay;
//			lpPlay = lpPublish;
//			lpPublish = tmp;
//			//sv.setZOrderOnTop(false);
//		}else if(bPubishOnly){
//			tmp = lpPlay;
//			lpPlay = lpPublish;
//			lpPublish = tmp;
//		}
		
		
		
		sv.setLayoutParams(lpPlay);
		super.onWindowFocusChanged(hasFocus);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}
}
