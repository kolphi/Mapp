package at.fhooe.mc.linzbrowser.android;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class HortAdapter extends ArrayAdapter<DataHort> {
	private ArrayList<DataHort> items;
	private LayoutInflater li;
	
	public HortAdapter(Context context, int id, int name,ArrayList<DataHort> items) {
		super(context, id, name, items);
		
		this.items = items;
		li = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	
	static class ViewHolder {
		public TextView title;
		public TextView sub;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		
		if (null == convertView) {
		    convertView = li.inflate(R.layout.list_element_2_lines, null);
		    
		    holder = new ViewHolder();
		    
		    holder.title = (TextView) convertView.findViewById(R.id.list_element_2_lines_title);
            holder.sub = (TextView) convertView.findViewById(R.id.list_element_2_lines_sub);
		    
		    convertView.setTag(holder);
		    
		} else {
		    holder = (ViewHolder) convertView.getTag();
		}

		holder.title.setText(items.get(position).name);
		holder.sub.setText(items.get(position).address);
		
		return convertView;
	}
	
}

