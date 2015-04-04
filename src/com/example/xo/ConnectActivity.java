package com.example.xo;

import java.util.ArrayList;
import java.util.Set;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class ConnectActivity extends ActionBarActivity {
	
	TextView info;
	ListView list;
	static Client client;
	private BluetoothAdapter bluetoothAdapter = MainActivity.bluetoothAdapter;
	final ArrayList<BluetoothDevice> discoveredDevices = new ArrayList<BluetoothDevice>();
	
	BroadcastReceiver receiver;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.connect_activity);
		
		info = (TextView) findViewById(R.id.textView1);
		list = (ListView) findViewById(R.id.listView1);
		
		ArrayAdapter<String> pariedAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1);
		
		Set<BluetoothDevice> pariedDevices = bluetoothAdapter.getBondedDevices();//Список сопряженных устройств
		
		if(pariedDevices.size() > 0)
			for (BluetoothDevice device : pariedDevices){
				pariedAdapter.add(device.getName());
				discoveredDevices.add(device);
			}
		
		OnItemClickListener itemClick = new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				StringBuffer infoOfDevice;
				infoOfDevice = new StringBuffer("Имя: " + discoveredDevices.get(arg2).getName() + "\n"
						+ "Адрес: " + discoveredDevices.get(arg2).getAddress());
				info.setText(infoOfDevice);
				client = new Client(discoveredDevices.get(arg2));
				client.start();
				Intent intent1 = new Intent(getApplicationContext(), MainActivity.class);
				intent1.putExtra("isZero", true);
				startActivity(intent1);
			}
		};
		list.setAdapter(pariedAdapter);
		list.setOnItemClickListener(itemClick);
	}
	public void onClick(View v){
		switch(v.getId()){
		case R.id.search_button:
			final ArrayAdapter<String> discoveredAdapter = new ArrayAdapter<String>(this,
					android.R.layout.simple_list_item_1);
			
			discoveredDevices.clear();
			
			if (bluetoothAdapter.startDiscovery()){
				
				 receiver = new BroadcastReceiver(){
	
					@Override
					public void onReceive(Context context, Intent intent) {
							String action = intent.getAction();
							if(BluetoothDevice.ACTION_FOUND.equals(action)){
								BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
								discoveredDevices.add(device);
								discoveredAdapter.add(device.getName());
								Log.d("my_logs", device.getName());
							}
					}
					
				};
				IntentFilter intentFilter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
				registerReceiver(receiver, intentFilter);
				list.setAdapter(discoveredAdapter);
			}
		}
	}
	protected void onDestroy(){
		super.onDestroy();
		unregisterReceiver(receiver);
	}
}
