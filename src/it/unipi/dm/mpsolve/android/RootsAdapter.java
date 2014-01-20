package it.unipi.dm.mpsolve.android;

import java.util.ArrayList;
import android.content.Context;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

/**
 * @brief Adapter for the {@link RootsListFragment} 
 * @author Leonardo Robol <leo@robol.it>
 *
 */
public class RootsAdapter implements ListAdapter {
	
	/**
	 * @brief Application Context. Should be the MainActivity of the Application.  
	 */
	private Context context;
	
	/**
	 * @brief A List of the DataSetObserver that are registerd in this Adapter. 
	 */
	private ArrayList<DataSetObserver> observers = new ArrayList<DataSetObserver>(0);
	
	/**
	 * @brief The array containing the approximations of the last polynomial solved. 
	 * May be null in case no polynomial has been solved yet. 
	 */
	public Approximation[] roots;
	
	/**
	 * @briec Create a new (empty) RootsAdapter. 
	 * @param context The current Android context. 
	 */
	public RootsAdapter(Context context) {
		this.context = context;
		roots = null;
	}
	
	/**
	 * @brief Update the array of Approximations contained in the adapter. 
	 * 
	 * This method will also notify all the DataSetObserver registered with
	 * this Adapter.
	 *  
	 * @param points The new values for the points array. 
	 */
	public void setPoints(Approximation[] points) {
		roots = points;
		
		for (DataSetObserver o : observers) {
			o.onChanged();
		}
	}

	/**
	 * @brief Get the number of approximations in the ListAdapter. 
	 */
	@Override
	public int getCount() {
		return (roots == null) ? 0 : roots.length;
	}

	/**
	 * @brief Obtain the Approximation in the specified position. 
	 * @param position The position of the desired approximaition. 
	 */
	@Override
	public Object getItem(int position) {
		return roots[position];
	}

	/**
	 * @brief Get the id for an Approximation. 
	 * @param position The position of the Approximation whose id should be returned. 
	 */
	@Override
	public long getItemId(int position) {
		return position;
	}

	/**
	 * @brief The the type of View required for the item in the given position. 
	 * @param position The position of the item whose type of View should be returned. 
	 * 
	 * This method return always 0 in the current implementation. 
	 */
	@Override
	public int getItemViewType(int position) {
		return 0;
	}

	/**
	 * @brief Create a View that display the value of the Approximation in the given
	 * position. 
	 * 
	 * @param position The position of the desired {@link Approximation}. 
	 * @param convertView null, or a {@link View} to recycle. 
	 * @param parent the {@link ViewGroup} where the {@link View} should be placed. 
	 */
	@Override
	public View getView(final int position, View convertView, final ViewGroup parent) {
		View v = convertView;
		
		if (v == null) {
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(
					Context.LAYOUT_INFLATER_SERVICE); 			
			v = inflater.inflate(R.layout.root_view, null);
		}
		
		final LinearLayout root = (LinearLayout) v.findViewById(R.id.rootViewRoot);
		
		// Decide the color based on even odd positions. 
		if (position % 2 == 0)
			root.setBackgroundColor(
					context.getResources().getColor(R.color.list_even_background));
		else
			root.setBackgroundColor(
					context.getResources().getColor(R.color.list_odd_background));
		
		TextView rootText = (TextView) v.findViewById(R.id.rootText);
		rootText.setText(roots[position].valueRepresentation);
		
		TextView radiusText = (TextView) v.findViewById(R.id.radiusText);
		radiusText.setText("Radius: " + roots[position].radiusRepresentation);
		
		TextView statusText = (TextView) v.findViewById(R.id.statusText);
		statusText.setText("Status: " + roots[position].status);
		
		v.setClickable(true);
		v.setFocusable(true);
		
		final ListView listView = (ListView) parent;

		// If we are child of a MainActivity chances are that there we are
		// a marked approximation
		if (ApplicationData.getMarkedPosition() == position) {
			v.setBackgroundColor(context.getResources().getColor(
					R.color.list_marked_background));
		}
		
		v.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				listView.performItemClick(v, position, getItemId(position));
				RootsAdapter.this.getView(position, v, parent);
			}
		});
		
		return v;
	}

	/**
	 * @brief Get the number of {@link View} types that are used by this adapter. 
	 * @return Always 1, since we only have a single {@link View} in the current implementation. 
	 */
	@Override
	public int getViewTypeCount() {
		return 1;
	}

	/**
	 * @brief Not implemented yet. 
	 */
	@Override
	public boolean hasStableIds() {
		return false;
	}

	/**
	 * @brief Detect if the {@link RootsAdapter} is empty. 
	 * @return true if the {@link RootsAdapter} is empty, false otherwise. 
	 */
	@Override
	public boolean isEmpty() {
		return getCount() == 0;
	}

	/**
	 * @brief Register a new {@link DataSetObserver} that will be notified 
	 * of the changes to the current data set. 
	 */
	@Override
	public void registerDataSetObserver(DataSetObserver observer) {
		observers.add(observer);
		
	}

	/**
	 * @brief Unregister a {@link DataSetObserver} previously registered through a call
	 * to registerDataObserver(). 
	 */
	@Override
	public void unregisterDataSetObserver(DataSetObserver observer) {
		observers.remove(observer);		
	}

	/**
	 * @brief Not implemented yet. 
	 */
	@Override
	public boolean areAllItemsEnabled() {
		return false;
	}

	/**
	 * @brief Not implemented yet. 
	 */	
	@Override
	public boolean isEnabled(int position) {
		return false;
	}

}
