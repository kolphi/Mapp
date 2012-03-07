package at.fhooe.mc.linzguide.android.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import at.fhooe.mc.linzguide.android.R;
import at.fhooe.mc.linzguide.android.util.RawDataProvider;

public class CategoriesAdapter extends ArrayAdapter<String> {
	private String[] items;
	private LayoutInflater li;
	
	public CategoriesAdapter(Context context, int id, int name, String[] items) {
		super(context, id, name, items);
		
		this.items = items;
		li = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	
	static class ViewHolder {
		public TextView title;
		public ImageView icon;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		
		if (null == convertView) {
		    convertView = li.inflate(R.layout.list_element_1_line_and_icon, null);
		    
		    holder = new ViewHolder();
		    
		    holder.title = (TextView) convertView.findViewById(R.id.list_element_1_line_and_icon_title);
            holder.icon = (ImageView) convertView.findViewById(R.id.list_element_1_line_and_icon_img);
		    
		    convertView.setTag(holder);
		    
		} else {
		    holder = (ViewHolder) convertView.getTag();
		}

		holder.title.setText(RawDataProvider.catsNames[position]);
		holder.icon.setBackgroundResource(RawDataProvider.icons[position]);
		
		return convertView;
	}
	
}


