package com.aodianyun.vissdkanddemo;

import android.app.Application;
import android.util.Log;

import com.aodianyun.dms.android.DMS;
import org.eclipse.paho.client.mqttv3.*;

public class VisApplication extends Application {
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		initDMS();
		subDMS();
	}
	/**
	 * 初始化DMS
	 */
	private void initDMS(){
		DMS.init(getApplicationContext(), "pub_13e6f2889ff97bd034ac893eef88a767", "sub_d543e5e2861f9d2b920e762bf45b5991", new MqttCallback(){
		     @Override
		    public void connectionLost(Throwable throwable) {
		        Log.i("DMS","LOSTED");
		    }

		    @Override
		    public void messageArrived(String s, MqttMessage mqttMessage) throws Exception {
		        Log.i("DMS","RECV  TOPIC: "+s+ " MESSAGE :"+new String( mqttMessage.getPayload()) );
		    }

		    @Override
		    public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {

		    }
		});
	}
	
	private void subDMS(){
		try{
		    final String topic = "vis_log";
		    DMS.connect(new IMqttActionListener() {
		        @Override
		        public void onSuccess(IMqttToken iMqttToken) {
		            Log.i("DMS","connect success ");
		            try{
		                DMS.subscribe(topic,new IMqttActionListener() {
		                    @Override
		                    public void onSuccess(IMqttToken iMqttToken) {
		                        Log.i("DMS","subscribe success ");
		                    }
		                    @Override
		                    public void onFailure(IMqttToken iMqttToken, Throwable throwable) {
		                        Log.i("DMS","subscribe failure ");
		                    }
		                });
//		                DMS.publish(topic, "hello,world".getBytes(), new IMqttActionListener() {
//		                    @Override
//		                    public void onSuccess(IMqttToken asyncActionToken) {
//		                         Log.i("DMS","publish success ");
//		                    }
//
//		                    @Override
//		                    public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
//		                        Log.i("DMS","publish failure ");
//		                    }
//		                });
		            }catch( MqttException e){
		                e.printStackTrace();
		            }
		        }
		        @Override
		        public void onFailure(IMqttToken iMqttToken, Throwable throwable) {
		            Log.i("DMS","connect failure ");
		        }
		    });
		}catch( MqttException e){
		    e.printStackTrace();
		}
	}
}
