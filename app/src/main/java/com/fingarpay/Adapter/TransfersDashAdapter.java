package com.fingarpay.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.fingarpay.R;
import com.fingarpay.helper.TransferFundsInfo;
import com.fingarpay.helper.UtilityHelper;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


@SuppressLint({ "DefaultLocale", "SimpleDateFormat" })
public class TransfersDashAdapter extends BaseAdapter implements Filterable {

	// XML node keys
	LayoutInflater inflater;
	List<TransferFundsInfo> smartCollection;
	List<TransferFundsInfo> smartCollectionFiltered;
	ViewHolder holder;
	public TransfersDashAdapter() {
		// TODO Auto-generated constructor stub
	}
	
	public TransfersDashAdapter(Context act, List<TransferFundsInfo> map) {
		
		this.smartCollection = map;
		this.smartCollectionFiltered=map;
		
		inflater = (LayoutInflater) act
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	
	public int getCount() {
		// TODO Auto-generated method stub
//		return idlist.size();
		return smartCollectionFiltered.size();
	}

	public Object getItem(int position) {
	    return smartCollectionFiltered.get(position);
	}

	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return smartCollectionFiltered.get(position).getId();
	}

	@SuppressWarnings("deprecation")
	public View getView(int position, View convertView, ViewGroup parent) {
		 
		View vi=convertView;
	    if(convertView==null){
	     
	      vi = inflater.inflate(R.layout.listitem_transactionhistory, null);
	      holder = new ViewHolder();
	     
	      holder.txtService = (TextView)vi.findViewById(R.id.txt_product); // city name
	     // holder.txtTransCode = (TextView)vi.findViewById(R.id.txt); // city weather overview
	      holder.txtAmount = (TextView)vi.findViewById(R.id.txt_amount);
	      holder.txtDateCreated = (TextView)vi.findViewById(R.id.txt_timestamp);
	     // holder.imgBttnFavHymm=(ImageView)vi.findViewById(R.id.txtMobile);
	     
	      vi.setTag(holder);
	    }
	    else{
	    	
	    	holder = (ViewHolder)vi.getTag();
	    }

	      // Setting all values in listview
	     holder.txtTransCode.setText(String.valueOf(smartCollectionFiltered.get(position).getTransactionCode()));
	      holder.txtService.setText(smartCollectionFiltered.get(position).getTransactionDetails());
	      //holder.txtAccountName.setText(smartCollectionFiltered.get(position).getReceiverAccountName());
	      String amount= String.valueOf(smartCollectionFiltered.get(position).getAmount());
	      try {
	    	  Locale.setDefault(Locale.US);
	    	 
	    	  amount = new DecimalFormat("#,###.##").format(Double.parseDouble(amount));
		} catch (Exception e) {
			Log.e("", e.getMessage() + e.getStackTrace());
		}
	      holder.txtAmount.setText(amount);
	      
	      String date = UtilityHelper.getValidDateAsString(smartCollectionFiltered.get(position).getDateCreated());
	     
	   
	      
	      
	      holder.txtDateCreated.setText(date);
	      
	      	try {
				if (smartCollectionFiltered.get(position).getTransactionStatus().contains("success")) {
					holder.imgBttnFavHymm.setImageResource(R.drawable.iconvalidatesuccess);
				}else if(smartCollectionFiltered.get(position).getTransactionStatus().toLowerCase().contains("pending")){
					holder.imgBttnFavHymm.setImageResource(R.drawable.helpicngrey);
				}
				else{
					holder.imgBttnFavHymm.setImageResource(R.drawable.iconvalidatefail);
				}
				} catch (Exception e)
	      		{
					Log.e("", e.getMessage() + e.getStackTrace());
				}
		     
	      
	      return vi;
	}
	
	/*
	 * 
	 * */
	static class ViewHolder{
		TextView Id;
		TextView txtTransCode;
		TextView txtService;
		TextView txtAccountName;
		TextView txtAmount;
		TextView txtDateCreated;
		ImageView imgBttnFavHymm;
	}

	@Override
	public Filter getFilter() {
		Filter filter = new Filter() {

	        @SuppressWarnings("unchecked")
	        @Override
	        protected void publishResults(CharSequence constraint, FilterResults results) {

	        	smartCollectionFiltered  = (ArrayList<TransferFundsInfo>) results.values; // has the filtered values
	            notifyDataSetChanged();  // notifies the data with new filtered values
	        }

	        @Override
	        protected FilterResults performFiltering(CharSequence constraint) {
	            FilterResults results = new FilterResults();        // Holds the results of a filtering operation in values
	            ArrayList<TransferFundsInfo> FilteredArrList = new ArrayList<TransferFundsInfo>();

	            if (smartCollection == null) {
	            	smartCollection = new ArrayList<TransferFundsInfo>(smartCollectionFiltered); // saves the original data in mOriginalValues
	            }

	            /********
	             * 
	             *  If constraint(CharSequence that is received) is null returns the mOriginalValues(Original) values
	             *  else does the Filtering and returns FilteredArrList(Filtered)  
	             *
	             ********/
	            if (constraint == null || constraint.length() == 0) {

	                // set the Original result to return  
	                results.count = smartCollection.size();
	                results.values = smartCollection;
	            } else {
	                constraint = constraint.toString().toLowerCase();
	                for (int i = 0; i < smartCollection.size(); i++) {
	                    String HymmNumber = smartCollection.get(i).getReceiverAccountNo();
	                    String HymmDetails = smartCollection.get(i).getReceiverAccountName();
	                    if (HymmNumber.toLowerCase().contains(constraint.toString()) ||
	                    		HymmDetails.toLowerCase().contains(constraint.toString())) {
	                    	TransferFundsInfo srInfo=new TransferFundsInfo();
	                    	srInfo=smartCollection.get(i);
	                        FilteredArrList.add(srInfo);
	                    }
	                }
	                // set the Filtered result to return
	                results.count = FilteredArrList.size();
	                results.values = FilteredArrList;
	            }
	            return results;
	        }
	    };
	    return filter;
	}
	
}
