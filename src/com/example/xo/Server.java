package com.example.xo;

import java.io.IOException;
import java.util.UUID;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.util.Log;

public class Server extends Thread {

	
	private final BluetoothServerSocket serverSocket;
	public static BluetoothSocket socket;
	 
	 public Server(BluetoothAdapter bluetooth) {
		 BluetoothServerSocket tmp = null;
		 try {
			 tmp = MainActivity.bluetoothAdapter.listenUsingRfcommWithServiceRecord("Крестики-нолики",
					 UUID.fromString("f9c668b0-6f3f-11e4-9803-0800200c9a66"));
		 } catch (IOException e) { }
		 serverSocket = tmp;
	 }
	 
	 public void run() {
	 socket = null;
		 while (true) {
			 try {
				 socket = serverSocket.accept();
			 } catch (IOException e) {
				 break;
			 }
			 if (socket != null) {
				 Log.d("logs","Сервер: сокет получен");
				 try {
					 serverSocket.close();
				 } catch (IOException e) {
					 e.printStackTrace();
				 }
				 break;
			 }
		 }
	 }
	 

	/** отмена ожидания сокета */
	 public void cancel() {
		 try {
			 serverSocket.close();
		 } catch (IOException e) { }
	 }
}
