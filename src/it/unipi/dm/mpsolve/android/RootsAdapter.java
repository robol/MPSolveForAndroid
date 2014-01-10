package it.unipi.dm.mpsolve.android;

import java.util.ArrayList;

import android.content.Context;
import android.database.DataSetObserver;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.TextView;

public class RootsAdapter implements ListAdapter {
	
	/**
	 * @brief Application Context. 
	 */
	private Context context;
	
	/**
	 * @brief Application LayoutInflater. 
	 */
	private LayoutInflater inflater = null;
	
	private ArrayList<DataSetObserver> observers = new ArrayList<DataSetObserver>(0);
	
	public String[] roots;
	public String[] radius;
	
	public RootsAdapter(Context context) {
		this.context = context;
		roots = null;
		radius = null;
	}
	
	public void setPoints(String[] points) {
		roots = points;
		
		for (DataSetObserver o : observers) {
			o.onChanged();
		}
	}

	@Override
	public int getCount() {
		return (roots == null) ? 0 : roots.length;
	}

	@Override
	public Object getItem(int position) {
		return roots[position];
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public int getItemViewType(int position) {
		return 0;
	}
	
	private LayoutInflater getInflater() {
		if (inflater == null) {
			inflater = (LayoutInflater) context.getSystemService
					(Context.LAYOUT_INFLATER_SERVICE);
		}
		
		return inflater;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View v = convertView;
		
		if (v == null) {			
			v = getInflater().inflate(R.layout.root_view, null);
		}
		
		Log.d("MPSolve", "position = " + position + ", root = " + roots[position]);
		
		TextView rootText = (TextView) v.findViewById(R.id.rootText);
		rootText.setText(roots[position]);
		
		TextView radiusText = (TextView) v.findViewById(R.id.radiusText);
		radiusText.setText("Radius: not implemented");		
		
		return v;
	}

	@Override
	public int getViewTypeCount() {
		return 1;
	}

	@Override
	public boolean hasStableIds() {
		return false;
	}

	@Override
	public boolean isEmpty() {
		return getCount() == 0;
	}

	@Override
	public void registerDataSetObserver(DataSetObserver observer) {
		observers.add(observer);
		
	}

	@Override
	public void unregisterDataSetObserver(DataSetObserver observer) {
		observers.remove(observer);		
	}

	@Override
	public boolean areAllItemsEnabled() {
		return false;
	}

	@Override
	public boolean isEnabled(int position) {
		return false;
	}

}
