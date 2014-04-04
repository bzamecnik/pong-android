package me.zamecnik.android.pong;

import android.widget.TextView;

public class FpsCounter {
	private static final int MIN_FPS_DRAW_PERIOD_MILLIS = 200;
	private static final double SMOOTHING_COEFFICIENT = 0.1;

	private final TextView textView;
	private final ScalarExpSmoother fpsSmoother = new ScalarExpSmoother(
		SMOOTHING_COEFFICIENT);

	private long lastFpsDrawTime;

	public FpsCounter(TextView textView) {
		this.textView = textView;
	}

	public void updateFps(double fps) {
		final double smoothedFps = fpsSmoother.smooth(fps);
		long now = System.currentTimeMillis();

		boolean canDrawFps = now - lastFpsDrawTime > MIN_FPS_DRAW_PERIOD_MILLIS;
		if (canDrawFps) {
			textView.post(new Runnable() {
				public void run() {
					textView.setText(String.format("FPS: %.2f", smoothedFps));
				}
			});
			lastFpsDrawTime = now;
		}
	}
}
