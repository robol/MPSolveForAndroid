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
	
	private double x_range = 1.0;
	private double y_range = 1.0;
	
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
		// Paint used for the X and Y axis. 
		axisPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		axisPaint.setColor(
				getResources().getColor(R.color.rootsRendererView_axis));
		
		// Paint used for the points of the plot
		pointsPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		pointsPaint.setColor(
				getResources().getColor(R.color.rootsRendererView_points));
		pointsPaint.setStyle(Style.FILL_AND_STROKE);
		pointsPaint.setMaskFilter(new BlurMaskFilter(2, Blur.INNER));		
	}
	
	@Override
	public void onSizeChanged (int w, int h, int oldw, int oldh) {
		width = w;
		height = h;
		
		if (w < h) {
			x_range = 1.0;
			y_range = height / width;
		}
		else {
			y_range = 1.0;
			x_range = width / height;
		}
		
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
		float x_axis_level = (float) Math.max(0.0, y_center - .95 * scale * y_range);
		x_axis_level = (float) Math.min(x_axis_level, y_center + .95 * scale * y_range);
		
		PointF start = pointToLocalCoords(x_center - .95 * x_range * scale, 
				x_axis_level);
		PointF end   = pointToLocalCoords(x_center + .95 * scale * x_range, 
				x_axis_level);
		
		canvas.drawLine(start.x, start.y, end.x, end.y, axisPaint);
		
		// TODO: Draw nicer axis by marking the scale and refining this function
	}
	
	private void drawYAxis(Canvas canvas) {
		float y_axis_level = (float) Math.max(0.0, x_center - .95 * scale * x_range);
		y_axis_level = (float) Math.min(y_axis_level, x_center + .95 * scale * x_range);
		
		PointF start = pointToLocalCoords(y_axis_level, 
				y_center - .95 * scale * y_range);
		PointF end   = pointToLocalCoords(y_axis_level, 
				y_center + .95 * scale * y_range);
		
		canvas.drawLine(start.x, start.y, end.x, end.y, axisPaint);
		
		// Compute a reasonable distance for the ticks that we should draw
		// on the axis.
		double step = getAxisStep(scale * y_range);
		Log.d("MPSolve", "axisStep = " + step);
		for (double t = y_center - 10.0 * step; t <= y_center + 10.0 * step; t += step) {
			PointF tick = pointToLocalCoords(y_axis_level, t);			
			canvas.drawLine(tick.x - 2, tick.y, tick.x + 2, tick.y, axisPaint);
		}
	}
	
	private double getAxisStep(double axisRange) {
		return 0.1;
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
		double min_wh = Math.min(width, height);
		
		// Get local coordinates w.r.t to the center. 
		double rel_x = (x - x_center) / scale * min_wh / 2;
		double rel_y = (y - y_center) / scale * min_wh / 2;
		
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
    	double maximumX = points[0].realValue;
    	double minimumX = maximumX;
    	double maximumY = points[0].imagValue;
    	double minimumY = maximumY; 
    	
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
    	
    	Log.d("MPSolve", "Scale = " + scale);
    	Log.d("MPSolve", "Center = (" + x_center + ", " + y_center + ")");
    	
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
