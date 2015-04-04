package com.example.xo;

import java.io.IOException;
import java.util.UUID;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.util.Log;

public class Client extends Thread {

	public static BluetoothSocket socket;
	private final BluetoothDevice device;
	 
	 public Client(BluetoothDevice device) {
		 BluetoothSocket tmp = null;
		 this.device = device;
		 try {
			 tmp = this.device.createRfcommSocketToServiceRecord(UUID.fromString("f9c668b0-6f3f-11e4-9803-0800200c9a66"));
		 } catch (IOException e) { }
		 socket = tmp;
	 }
	 
	 public void run() {
		 MainActivity.bluetoothAdapter.cancelDiscovery();
		 try {
			 socket.connect();
			 if (socket != null){
				 Log.d("logs","Клиент: сокет получен");
			 }
		 } catch (IOException connectException) {
			 try {
				 socket.close();
			 } catch (IOException closeException) { }
		return;
	 }
	 
	 }
	 

	/** отмена ожидания сокета */
	 public void cancel() {
		 try {
			 socket.close();
		 } catch (IOException e) { }
	 }
}
