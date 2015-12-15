package com.aodianyun.vissdkanddemo;

import java.text.SimpleDateFormat;

import com.aodianyun.Vis_Sdk;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import cn.nodemedia.LivePlayer;

public class MainActivity extends Activity implements OnClickListener{
	final float heng =  0.75f;
	EditText visUrl,visApp,visStream,visPwd,visUid;
	String visUrlStr,visAppStr,visStreamStr,visPwdStr,visUidStr;
	float srcWidth;
	float srcHeight;
	DisplayMetrics dm;
	LinearLayout llSv;
	RelativeLayout rl_content;
	boolean isStartPlay = true;
	private Button btnVisStart,btnVisPlay;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		dm = getResources().getDisplayMetrics();
		
		visUrl = (EditText) findViewById(R.id.editText_vis_url);
		visApp = (EditText) findViewById(R.id.editText_app);
		visStream = (EditText) findViewById(R.id.editText_stream);
		visPwd = (EditText) findViewById(R.id.editText_pwd);
		visUid = (EditText) findViewById(R.id.editText_uid);
		btnVisStart = (Button) findViewById(R.id.btn_vis_publish);
		btnVisStart.setOnClickListener(this);
		btnVisPlay = (Button) findViewById(R.id.btn_vis_play);
		btnVisPlay.setOnClickListener(this);
		
		rl_content = (RelativeLayout) findViewById(R.id.rl_main_content);
		
		visUrl.setText((String)SharedPreUtil.get(this, "visUrl", "http://vis.aodianyun.com:5000"));
		visApp.setText((String)SharedPreUtil.get(this, "visApp", "demo"));
		visStream.setText((String)SharedPreUtil.get(this, "visStream", "visdemo"));
		visPwd.setText((String)SharedPreUtil.get(this, "visPwd", "123456"));
		visUid.setText((String)SharedPreUtil.get(this, "visUid", "1029"));		
		getParam();
	
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
		switch(v.getId()){
		case R.id.btn_vis_publish:
			saveParam();
			MainActivity.this.startActivity(new Intent(MainActivity.this,VisActivity.class));
			break;
		case R.id.btn_vis_play:
			saveParam();
			MainActivity.this.startActivity(new Intent(MainActivity.this,VisPlayActivity.class));
			break;
		 default:
			 break;
		}
	}
	
	private void saveParam(){
		SharedPreUtil.put(MainActivity.this, "visUrl", visUrl.getText().toString());
		SharedPreUtil.put(MainActivity.this, "visApp", visApp.getText().toString());
		SharedPreUtil.put(MainActivity.this, "visStream", visStream.getText().toString());
		SharedPreUtil.put(MainActivity.this, "visPwd", visPwd.getText().toString());
		SharedPreUtil.put(MainActivity.this, "visUid", visUid.getText().toString());
	}
	
	// 从控制界面获取设置好的vis参数
	private void getParam(){
		visUrlStr = visUrl.getText().toString();
		visAppStr = visApp.getText().toString();
		visStreamStr = visStream.getText().toString();
		visPwdStr = visPwd.getText().toString();
		visUidStr =  visUid.getText().toString();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}
}
