package com.example.xo;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

public class MainActivity extends ActionBarActivity {

	//private static final int REQUEST_ENABLE_BT = 1;
	TextView tv;
	GridView grid;
	Button s_button, c_button;
	
	MyAdapter adapter;
	
	boolean isZero = false;
	boolean allowed = true;
	
	BluetoothSocket socket;
	
	Connect connect;
	Server server;
	Handler handler;
	public static BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		tv = (TextView) findViewById(R.id.textView1);
		grid = (GridView) findViewById(R.id.gridView1);
		s_button = (Button) findViewById(R.id.server_button);
		c_button = (Button) findViewById(R.id.client_button);
		
		handler = new Handler(){
			public void handleMessage(android.os.Message msg) {
				switch(msg.what){
				case 1:
					byte[] a = (byte[])msg.obj;
					String s = "";
					if (isZero){
						 s = "X";
					}else{
						s = "0";
					}
					adapter.setData((int)a[0],s);
					adapter.notifyDataSetChanged();
					allowed = true;
					tv.setText("Ходите...");
				}
			}
		};
		
		adapter = new MyAdapter(getApplicationContext(), android.R.layout.simple_list_item_1);
		 grid.setAdapter(adapter);
		 grid.setOnItemClickListener(new OnItemClickListener(){
			 
			 public void onItemClick(AdapterView<?> parent, View v,
						int position, long id) {
					String s = "";
					if (isZero){
						 s = "0";
					}else{
						s = "X";
					}
					if((adapter.getItem(position) == null)&&(allowed)){
						adapter.setData(position, s);
						byte[] b = {(byte) position};
						connect.write(b);
						adapter.notifyDataSetChanged();
						allowed = false;
						tv.setText("Ждите...");
						if (inspection() == 1){
							tv.setText("win X");
						}
					}
				} 
		 });
		 if (bluetoothAdapter != null){
				if(!bluetoothAdapter.isEnabled()){
					Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
				    discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
				    startActivity(discoverableIntent);
				}
			}
	}
	
	public int inspection(){
		StringBuffer temp[] = adapter.Data;
		if ((temp[0] != null)&&(temp[1] != null)&&(temp[2] != null)){
			if ((temp[0].equals(temp[1]))&&(temp[2].equals(temp[1]))&&(temp[0].equals(new StringBuffer("X")))){
				return 1;
			}
		}
		return 0;
	}
	
	protected void onResume(){
		super.onResume();
		Log.d("logs","onResume() сработал");
		isZero = getIntent().getBooleanExtra("isZero", false);
		Log.d("logs", isZero +"");
		if (isZero == true){
			allowed = false;
			s_button.setClickable(false);
			while(true){
				Log.d("logs", "ждем сокет");
				if(ConnectActivity.client.socket != null){
					this.socket = ConnectActivity.client.socket;
					Log.d("logs","MainActivity: сокет получен");
					connect = new Connect(this.socket, this.handler);
					connect.start();
					break;
				}
			}
		}
	}
	
	public void OnClick(View v){
		switch(v.getId()){
		case R.id.server_button:
			
			c_button.setClickable(false);
			Server server = new Server(bluetoothAdapter);
			server.start();
			Log.d("logs", "соединение открыто");
			while(true){
				if(server.socket != null){
					this.socket = server.socket;
					Log.d("logs","MainActivity: сокет получен");
					connect = new Connect(this.socket, this.handler);
					connect.start();
					break;
				}
			}
			break;
		case R.id.client_button:
			s_button.setClickable(false);
			isZero = true;
			Intent intent = new Intent(this, ConnectActivity.class);
			startActivity(intent);
			break;
		}
	}
	
	
	public void onItemSelected(AdapterView<?> parent, View v, int position,
			long id) {
		// TODO Auto-generated method stub
		tv.setText("Выбранный элемент: " + adapter.getItem(position));
	}

	public void onNothingSelected(AdapterView<?> parent) {
		// TODO Auto-generated method stub
		tv.setText("Выбранный элемент: ничего");
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
}

