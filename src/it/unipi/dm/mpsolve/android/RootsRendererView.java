package it.unipi.dm.mpsolve.android;

import android.content.Context;
import android.database.DataSetObserver;
import android.graphics.BlurMaskFilter;
import android.graphics.BlurMaskFilter.Blur;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

public class RootsRendererView extends View {
	
	private RootsAdapter adapter;
	private DataSetObserver observer = null;
	
	private Paint axisPaint = null;
	private Paint pointsPaint = null;
	
	private double width = 0;
	private double height = 0;
	
	private double scale = 1.0;
	private double x_center = 0.0;
	private double y_center = 0.0;
		
	public RootsRendererView(Context context) {
		super(context);
		initPaints();
		Log.d("MPsolve", "called RootsRendererView(Context)");
	}

	public RootsRendererView(Context context, AttributeSet set) {
		super(context, set);
		initPaints();
		Log.d("MPsolve", "called RootsRendererView(Context, AttributeSet)");	
	}
	
	public void setRootsAdapter (RootsAdapter adapter) {
    	
    	if (observer != null)
			this.adapter.unregisterDataSetObserver(observer);
		
		this.adapter = adapter;
		
		final RootsAdapter currentAdapter = adapter;

		observer = new DataSetObserver() {
			@Override 
			public void onChanged() {
				inflatePoints(currentAdapter.roots);
			}
		};
		
		adapter.registerDataSetObserver(observer);
	}
	
	/** 
	 * @brief Prepare the Paints for later user in the onDraw() method. 
	 */
	private void initPaints() {
		axisPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		axisPaint.setColor(Color.BLACK);
		
		pointsPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		pointsPaint.setColor(Color.RED);
		pointsPaint.setStyle(Style.FILL_AND_STROKE);
		pointsPaint.setMaskFilter(new BlurMaskFilter(2, Blur.INNER));		
	}
	
	@Override
	public void onSizeChanged (int w, int h, int oldw, int oldh) {
		width = w;
		height = h;
		
		if (adapter.roots != null)
			inflatePoints(adapter.roots);
	}
	
	@Override
	public void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		
		// Draw the points
		if (adapter.roots != null) {
			for (Approximation p : adapter.roots) {
				PointF coords = approximationToLocalCoords(p);
				canvas.drawCircle(coords.x, coords.y, 4, pointsPaint);
			}
		}
		
		// Draw the X and Y axis, possibly translated if it's not visible
		drawXAxis(canvas);
		drawYAxis(canvas);
	}
	
	private void drawXAxis(Canvas canvas) {
		float x_axis_level = (float) Math.max(0.0, y_center - .5 * scale);
		x_axis_level = (float) Math.min(x_axis_level, y_center + .5 * scale);
		
		PointF start = pointToLocalCoords(x_center - .95 * scale, x_axis_level);
		PointF end   = pointToLocalCoords(x_center + .95 * scale, x_axis_level);
		
		canvas.drawLine(start.x, start.y, end.x, end.y, axisPaint);
		
		// TODO: Draw nicer axis by marking the scale and refining this function
	}
	
	private void drawYAxis(Canvas canvas) {
		float y_axis_level = (float) Math.max(0.0, x_center - .5 * scale);
		y_axis_level = (float) Math.min(y_axis_level, x_center + .5 * scale);
		
		PointF start = pointToLocalCoords(y_axis_level, y_center - .95 * scale);
		PointF end   = pointToLocalCoords(y_axis_level, y_center + .95 * scale);
		
		canvas.drawLine(start.x, start.y, end.x, end.y, axisPaint);
		
		// TODO: Draw nicer axis by marking the scale and refining this function
	}		
	
	/** 
	 * @brief Get the local coordinates in the View for the points in position
	 * i of the RootsAdapter. 
	 * 
	 * @param i The index of the point to draw. 
	 * @return The coordinates on the View where the point should be drawn. 
	 */
	private PointF approximationToLocalCoords(Approximation point) {
		return pointToLocalCoords(point.realValue, point.imagValue);
	}
	
	private PointF pointToLocalCoords(double x, double y) {		
		// Get local coordinates w.r.t to the center. 
		double rel_x = (x - x_center) / scale * width / 2;
		double rel_y = (y - y_center) / scale * height / 2;
		
		// Remember that graphics coordinates are flipped on the vertical axis
		// w.r.t the standard ones. 
		return new PointF((float) (rel_x + width / 2), (float) (height / 2 - rel_y));
	}

	/**
     * @brief Inflate the points stored in the MainActivity into the WebView
     * so that they will be plotted. 
     * 
     * This method should be called after altering the value of MainActivity.points
     * or when the WebView needs to reload the plot (for example after a layout
     * change). 
     */
    public void inflatePoints(Approximation[] points) {
    	if (points == null)
    		return;
    	
    	// We need to compute an appropriate scale for the plot. 
    	double maximumX = 0.0;
    	double minimumX = 0.0;
    	double maximumY = 0.0;
    	double minimumY = 0.0; 
    	
    	for (Approximation p : points) {
    		minimumX = Math.min(p.realValue, minimumX);
    		minimumY = Math.min(p.imagValue, minimumY);
    		maximumX = Math.max(p.realValue, maximumX);
    		maximumY = Math.max(p.imagValue, maximumY);
    	}
    	
    	// Compute the needed center and scale
    	x_center = (maximumX + minimumX) / 2; 
    	y_center = (maximumY + minimumY) / 2;
    	
    	// Here, remember that 1.0 means that we'll show the square
    	// [-.5, .5] \times [-.5, .5], i.e., that width/2 pixel are 1.0 
    	// on the real scale.
    	scale = .75 * Math.max (maximumX - minimumX, 
    				maximumY - minimumY);
    	
    	// Ensure that the scale is big enough that we don't have issues
    	// representing the numbers as floats. 
    	scale = Math.max (1.0e-20, scale);
    	
    	// Handle the particular case with only one root, or no roots
    	// at all. We set the convential scale of 1.0 in that case. 
    	if (points.length <= 1) {
    		scale = 1.0;
    	}
    	
    	// Invalidate the View so the points will actually be shown on the plot. 
    	invalidate();
    }

}
