package com.fingarpay.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.fingarpay.helper.BanksInfo;

import java.util.List;

/**
 * This is your own Adapter implementation which displays
 * the ArrayList of "Guy"-Objects.
 */
public class BanksAdapter extends BaseAdapter implements SpinnerAdapter {

    /**
     * The internal data (the ArrayList with the Objects).
     */
	private LayoutInflater inflater;
    private final List<BanksInfo> data;
    private Context activity;
    public BanksAdapter(Context activity, List<BanksInfo> data){
        this.data = data;
        this.activity=activity;
    }

    /**
     * Returns the Size of the ArrayList
     */
    @Override
    public int getCount() {
        return data.size();
    }

    /**
     * Returns one Element of the ArrayList
     * at the specified position.
     */
    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }
    /**
     * Returns the View that is shown when a element was
     * selected.
     */
    @Override
    public View getView(int position, View recycle, ViewGroup parent) {
    	
    	if (inflater == null)
			inflater = (LayoutInflater) activity
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        TextView text;
        if (recycle != null){
            // Re-use the recycled view here!
            text = (TextView) recycle;
        } else {
            // No recycled view, inflate the "original" from the platform:
            text = (TextView)inflater.inflate(
                    android.R.layout.simple_dropdown_item_1line, parent, false
            );
        }
        text.setTextColor(Color.BLACK);
        text.setText(data.get(position).getBankName());
        return text;
    }


}
