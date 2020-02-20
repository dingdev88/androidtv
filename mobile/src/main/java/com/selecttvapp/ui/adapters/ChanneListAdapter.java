package com.selecttvapp.ui.adapters;


import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.selecttvapp.R;
import com.selecttvapp.model.ChannelModel;

import java.util.List;


public class ChanneListAdapter extends ArrayAdapter<ChannelModel> {
	  private final Context context;
	  private final List<ChannelModel> values;
		private boolean bBlack = true;
	  public ChanneListAdapter(Context context, List<ChannelModel> values) {
	    super(context, -1, values);
	    this.context = context;
	    this.values = values;
	  }
	public void setBlack(boolean bVal){
		this.bBlack = bVal;
	}

	  @SuppressLint("ViewHolder")
	@Override
	  public View getView(int position, View convertView, ViewGroup parent) {
	    LayoutInflater inflater = (LayoutInflater) context
	        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	    View rowView = null;
	    ViewGroup viewGroup = null;
	    final View view;
	    final ViewHolder holder;
	    if( convertView == null ){
	    	viewGroup = (ViewGroup) LayoutInflater.from(context.getApplicationContext()).inflate(R.layout.alert_dialog_item, null);
	    	holder = new ViewHolder();
	    	holder.txtTitle = (TextView)viewGroup.findViewById(R.id.txtLabel);

			view = viewGroup;
			view.setTag(holder);
			convertView = view;
	    }else{
	    	holder = (ViewHolder) convertView.getTag();
	    	view = convertView;
	    }

		 // String item = this.values.get(position);
		  //holder.txtTitle.setText(item);
		  if( this.bBlack )
		  	holder.txtTitle.setTextColor(Color.BLACK);
		  return convertView;
	  }
	  
	  
	  private static class ViewHolder {
			TextView txtTitle;

	  }
} 
