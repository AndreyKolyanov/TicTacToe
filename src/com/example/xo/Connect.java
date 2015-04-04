package com.example.xo;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.bluetooth.BluetoothSocket;
import android.os.Handler;
import android.os.Message;

public class Connect extends Thread {

	
	 private final BluetoothSocket socket;
	 private final InputStream inStream;
	 private final OutputStream outStream;
	 Handler mHandler;
	 Message msg;
	 
	 public Connect(BluetoothSocket socket, Handler handler) {
		 this.socket = socket;	
		 InputStream tmpIn = null;
		 OutputStream tmpOut = null;
		 this.mHandler = handler;
	 
	 	 try {
	 		 tmpIn = socket.getInputStream();
	 		 tmpOut = socket.getOutputStream();
	 	 } catch (IOException e) { }
	 
	 	 inStream = tmpIn;
	 	 outStream = tmpOut;
	 	}
	 
	 public void run() {
		 byte[] buffer = new byte[1]; // �������� ������
		 int bytes; // bytes returned from read()
	 
		 // ������������ InputStream ���� �� ���������� ����������
		 while (true) {
			 try {
				 // ������ �� InputStream
				 bytes = inStream.read(buffer);
				 // �������� ����������� ����� ������� ������������
				msg = mHandler.obtainMessage(1, bytes, -1, buffer);
				mHandler.sendMessage(msg);
			 } catch (IOException e) {
				 break;
			 }
		 }
	 }
	 
	 /* �������� ���� ����� �� ������� ������������, ����� ��������� ������ 
	 ���������� ���������� */
	 public void write(byte[] bytes) {
		 try {
			 outStream.write(bytes);
		 } catch (IOException e) { }
	 }
	 
	 /* �������� ���� ����� �� ������� ������������, 
	 ����� ��������� ���������� */
	 public void cancel() {
		 try {
			 socket.close();
		 } catch (IOException e) { }
	 }
}
