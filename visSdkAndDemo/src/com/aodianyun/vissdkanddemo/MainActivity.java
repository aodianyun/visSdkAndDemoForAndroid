package com.aodianyun.vissdkanddemo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends Activity implements OnClickListener{
	EditText visUrl,visApp,visStream,visPwd,visUid;
	private Button btnVisStart;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		visUrl = (EditText) findViewById(R.id.editText_vis_url);
		visApp = (EditText) findViewById(R.id.editText_app);
		visStream = (EditText) findViewById(R.id.editText_stream);
		visPwd = (EditText) findViewById(R.id.editText_pwd);
		visUid = (EditText) findViewById(R.id.editText_uid);
		
		btnVisStart = (Button) findViewById(R.id.btn_vis_publish);
		btnVisStart.setOnClickListener(this);
		
		visUrl.setText((String)SharedPreUtil.get(this, "visUrl", "http://vis.aodianyun.com:5000"));
		visApp.setText((String)SharedPreUtil.get(this, "visApp", "demo"));
		visStream.setText((String)SharedPreUtil.get(this, "visStream", "visdemo"));
		visPwd.setText((String)SharedPreUtil.get(this, "visPwd", "123456"));
		visUid.setText((String)SharedPreUtil.get(this, "visUid", "1029"));
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
			SharedPreUtil.put(MainActivity.this, "visUrl", visUrl.getText().toString());
			SharedPreUtil.put(MainActivity.this, "visApp", visApp.getText().toString());
			SharedPreUtil.put(MainActivity.this, "visStream", visStream.getText().toString());
			SharedPreUtil.put(MainActivity.this, "visPwd", visPwd.getText().toString());
			SharedPreUtil.put(MainActivity.this, "visUid", visUid.getText().toString());
			MainActivity.this.startActivity(new Intent(MainActivity.this,VisActivity.class));
		 default:
			 break;
		}
	}
	

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

}
