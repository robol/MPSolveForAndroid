package it.unipi.dm.mpsolve.android;

import it.unipi.dm.mpsolve.android.ApplicationData.MarkedPositionChangedListener;

import java.text.DecimalFormat;

import android.content.Context;
import android.database.DataSetObserver;
import android.graphics.BlurMaskFilter;
import android.graphics.BlurMaskFilter.Blur;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

/**
 * @brief Custom {@link View} that renders the {@link Approximation} currently
 * stored in the global {@link RootsAdapter}. 
 * 
 * As the name suggests, it's currently used in the {@link RootsRendererFragment}
 * and implements the {@link MarkedPositionChangedListener} interface to be notified
 * of the user selection in the {@link RootsListFragment}. 
 * 
 * @author Leonardo Robol <leo@robol.it>
 *
 */
public class RootsRendererView extends View 
	implements ApplicationData.MarkedPositionChangedListener {
	
	private RootsAdapter adapter;
	private DataSetObserver observer = null;
	
	private Paint axisPaint = null;
	private Paint pointsPaint = null;
	private Paint markedPointsPaint = null;
	
	private double width = 0;
	private double height = 0;
	
	private double x_range = 1.0;
	private double y_range = 1.0;
	
	private double scale = 1.0;
	private double x_center = 0.0;
	private double y_center = 0.0;
	
	private int pointSize = 4;
	private int tickWidth = 2;
	
	private double x_axis_level;	
	private double y_axis_level;
	
	
	private DecimalFormat axisFormat;
	
	/**
	 * @brief Build a new RootsRendererView object. 
	 * @param context The current Android context. 
	 */
	public RootsRendererView(Context context) {
		super(context);
		buildView();
	}

	/**
	 * @brief Build a new RootsRendererView object. 
	 * @param context The current Android context.
	 * @param set The AttributeSet for the view.  
	 */	
	public RootsRendererView(Context context, AttributeSet set) {
		super(context, set);
		buildView();
	}
	
	private void buildView () {
		initPaints();		
		ApplicationData.registerMarkedPositionChangedListener(this);
	}
	
	/**
	 * @brief Set (or replace) the RootsAdapter in this object. It also causes
	 * a registration of a custom {@link DataSetObserver} so that this {@link View}
	 * will be notified of the future changes in the {@link Approximation}. 
	 * 
	 * @param adapter The new {@link RootsAdapter}. 
	 */
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
	 * 
	 * Note that this method peform also some other basic initializations, such
	 * as computing some sizes in a device-independent way and initializing
	 * some String formatter. 
	 * 
	 *  As a rule of thumb, every expensive operation that does not need to
	 *  be performed on every onDraw() call should be called here. 
	 */
	private void initPaints() {
		axisFormat = new DecimalFormat("#.#E0");
		
		// Compute a Device independent point size
		pointSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 
				4, getResources().getDisplayMetrics());
		
		// ...and in the same spirit get a device independent width for the
		// ticks that we will draw on the axis. 
		tickWidth = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 
				3, getResources().getDisplayMetrics());
		
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
		
		// Paint similar to the above, but used for points marked
		// in the Approximation list. 
		markedPointsPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		markedPointsPaint.setColor(
				getResources().getColor(R.color.rootsRendererView_MarkedPoints));
		markedPointsPaint.setStyle(Style.FILL_AND_STROKE);
		markedPointsPaint.setMaskFilter(new BlurMaskFilter (2, Blur.INNER));
	}
	
	/**
	 * @brief Recompute the necessary values to adapt the {@link View} to its
	 * new size.
	 */
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
	
	/**
	 * @brief Perform a draw operation on the given {@link Canvas}. 
	 * @param canvas The 2D {@link Canvas} where the {@link Approximation} will be
	 * rendered. 
	 */
	@Override
	public void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		int markedPosition = ApplicationData.getMarkedPosition();
		
		// Draw the points
		if (adapter.roots != null) {
			for (int i = 0; i < adapter.getCount(); i++) {
				if (i != markedPosition) {
					Approximation p = adapter.roots[i];
					PointF coords = approximationToLocalCoords(p);
					canvas.drawCircle(coords.x, coords.y, pointSize, pointsPaint);
				}
			}
			
			if (markedPosition >= 0) {
				Approximation p = adapter.roots[markedPosition];
				PointF coords = approximationToLocalCoords(p);
				canvas.drawCircle(coords.x, coords.y, pointSize, markedPointsPaint);				
			}
		}
		
		// Draw the X and Y axis, possibly translated if it's not visible
		computeAxisLevels();
		drawXAxis(canvas);
		drawYAxis(canvas);
	}
	
	private void computeAxisLevels() {
		x_axis_level = (float) Math.max(0.0, y_center - .95 * scale * y_range);
		x_axis_level = (float) Math.min(x_axis_level, y_center + .95 * scale * y_range);
		
		y_axis_level = (float) Math.max(0.0, x_center - .95 * scale * x_range);
		y_axis_level = (float) Math.min(y_axis_level, x_center + .95 * scale * x_range);		
	}
	
	/**
	 * @brief Draw the X axis on the {@link View}. 
	 * @param canvas The {@link Canvas} where the {@link View} is being drawn. 
	 */	
	private void drawXAxis(Canvas canvas) {
		PointF start = pointToLocalCoords(x_center - .95 * x_range * scale, 
				x_axis_level);
		PointF end   = pointToLocalCoords(x_center + .95 * scale * x_range, 
				x_axis_level);
		
		canvas.drawLine(start.x, start.y, end.x, end.y, axisPaint);
		
		// Compute a reasonable distance for the ticks that we should draw on the axis.
		double step = getAxisStep(scale * x_range);
		double center = Math.floor(x_center / step) * step;
		for (double t = 0.0; t < .9 * scale * x_range; t += step) {
			PointF tick = pointToLocalCoords(center + t, x_axis_level);			
			canvas.drawLine(tick.x, tick.y - tickWidth, tick.x, tick.y + tickWidth, axisPaint);
			
			String text = axisFormat.format(center + t);
			if (Math.abs(center + t - y_axis_level) > .5 * step) {
				canvas.drawText(text, tick.x - axisPaint.measureText(text) / 2, 
						tick.y - 2 * tickWidth, axisPaint);
			}
						
			tick = pointToLocalCoords(center - t, x_axis_level);			
			canvas.drawLine(tick.x, tick.y - tickWidth, tick.x, tick.y + tickWidth, axisPaint);
			text = axisFormat.format(center - t);			
			if (Math.abs(center - t - y_axis_level) > .5 * step && t != 0.0) {
				canvas.drawText(text, tick.x - axisPaint.measureText(text) / 2, 
						tick.y - 2 * tickWidth, axisPaint);
			}
						
		}		
	}
	
	/**
	 * @brief Draw the Y axis on the {@link View}. 
	 * @param canvas The {@link Canvas} where the {@link View} is being drawn. 
	 */
	private void drawYAxis(Canvas canvas) {
		PointF start = pointToLocalCoords(y_axis_level, 
				y_center - .95 * scale * y_range);
		PointF end   = pointToLocalCoords(y_axis_level, 
				y_center + .95 * scale * y_range);
		
		canvas.drawLine(start.x, start.y, end.x, end.y, axisPaint);
		
		// Compute a reasonable distance for the ticks that we should draw on the axis.
		double step = getAxisStep(scale * y_range);
		double center = Math.floor(y_center / step) * step;
		for (double t = 0.0; t < .9 * scale * y_range; t += step) {
			PointF tick = pointToLocalCoords(y_axis_level, center + t);			
			canvas.drawLine(tick.x - tickWidth, tick.y,	tick.x + tickWidth, tick.y, axisPaint);
			
			if (Math.abs(center + t - x_axis_level) > .5 * step)
				canvas.drawText(axisFormat.format(center + t), tick.x + 2 * tickWidth, 
						tick.y + axisPaint.getTextSize() / 2, axisPaint);
			
			tick = pointToLocalCoords(y_axis_level, center - t);			
			canvas.drawLine(tick.x - tickWidth, tick.y,	tick.x + tickWidth, tick.y, axisPaint);
			if (t != 0.0 && Math.abs(center - t - x_axis_level) > .5 * step)
				canvas.drawText(axisFormat.format(center - t), tick.x + 2 * tickWidth, 
						tick.y + axisPaint.getTextSize() / 2, axisPaint);
		}
	}
	
	/**
	 * @brief Try to estimate a reasonable step between the ticks that
	 * will be drawn on the axis. 
	 * 
	 * @param axisRange is the stretch factor of the axis for which we should compute
	 * the scale. 
	 * 
	 * @return A (not so) wild guess of the reasonable step.  
	 */
	private double getAxisStep(double axisRange) {
		double realScaleLog = Math.log10(axisRange) ;
		double flooredValue = Math.floor(realScaleLog);
		
		double baseScale = Math.pow(10.0, flooredValue);
		
		if (realScaleLog - flooredValue > Math.log(2.0)) {
			baseScale *= 2;
		}
		
		if (realScaleLog - flooredValue > Math.log(5.0)) {
			baseScale *= 2.5;
		}
		
		return baseScale;
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
     * 
     * @param points The new points that should be drawn. 
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

    /**
     * @brief This method is called when the user change the selected
     * position in the {@link RootsListFragment}. 
     * 
     * It will trigger an invalidate event on the {@link View} that will
     * be re-drawn. 
     */
	@Override
	public void onMarkedPositionChanged(int position) {
		invalidate();
	}

}
