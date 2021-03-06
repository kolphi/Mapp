package at.fhooe.mc.linzguide.android.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import at.fhooe.mc.linzguide.android.R;
import at.fhooe.mc.linzguide.android.util.DataGeopoint;
import at.fhooe.mc.linzguide.android.util.RawDataProvider;

public class CategoryAdapter extends ArrayAdapter<DataGeopoint> {
	private ArrayList<DataGeopoint> items;
	private LayoutInflater li;
	
	public CategoryAdapter(Context context, int id, int name,ArrayList<DataGeopoint> items) {
		super(context, id, name, items);
		
		this.items = items;
		li = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	
	static class ViewHolder {
		public TextView title;
		public TextView sub;
		public ImageView icon;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		
		if (null == convertView) {
		    convertView = li.inflate(R.layout.list_element_2_lines, null);
		    
		    holder = new ViewHolder();
		    
		    holder.title = (TextView) convertView.findViewById(R.id.list_element_2_lines_title);
            holder.sub = (TextView) convertView.findViewById(R.id.list_element_2_lines_sub);
            holder.icon = (ImageView) convertView.findViewById(R.id.list_element_2_lines_img);
		    
		    convertView.setTag(holder);
		    
		} else {
		    holder = (ViewHolder) convertView.getTag();
		}

		holder.title.setText(items.get(position).name);
		holder.sub.setText(items.get(position).address);
		holder.icon.setBackgroundResource(RawDataProvider.icons[items.get(position).position]);
		
		return convertView;
	}
	
}


