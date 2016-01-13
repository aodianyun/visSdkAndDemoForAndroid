package com.aodianyun.util;

import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.json.JSONObject;

import com.aodianyun.dms.android.DMS;

import android.util.Log;

public class DmsTools {

	public static void push(String msg,String app, String stream){
		try{
			JSONObject json = new JSONObject();
			json.put("app", app);
			json.put("stream", stream);
			json.put("message", msg);
			String tmp = json.toString();
			DMS.publish("vis_log", tmp.getBytes(), new IMqttActionListener() {
				
				@Override
				public void onSuccess(IMqttToken arg0) {
					// TODO Auto-generated method stub
					Log.i("DMS", "success"+arg0.toString());
				}
				
				@Override
				public void onFailure(IMqttToken arg0, Throwable arg1) {
					// TODO Auto-generated method stub
					Log.i("DMS", "failure"+arg0.toString());
				}
			});
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
