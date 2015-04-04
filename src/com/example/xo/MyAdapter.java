package com.example.xo;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class MyAdapter extends ArrayAdapter<StringBuffer> {

	private Context context;
	
	public static StringBuffer[] Data = new StringBuffer[9];
	
	public MyAdapter(Context context, int textViewResourceId) {
		super(context, textViewResourceId, Data);
		
		this.context = context;
	}

	public StringBuffer getItem(int position) {
		return Data[position];
	}

	public void setData(int position, String value) {
		Data[position] = new StringBuffer(value);
	}
	
	public View getView(int position, View convertView, ViewGroup parent){
		TextView label = (TextView) convertView;
		
		if (convertView == null){
			
			convertView = new TextView(context);
			label = (TextView) convertView;
			
		}
		label.setBackgroundColor(Color.parseColor("#880000ff"));
		label.setTextSize(50);
		label.setText(Data[position]);
		return convertView;
		
	}

}
