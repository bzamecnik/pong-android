package me.zamecnik.android.pong;

import android.content.Context;
import android.graphics.Canvas;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public abstract class GameView extends SurfaceView implements
	SurfaceHolder.Callback {

	static final String LOG_TAG = GameView.class.getSimpleName();

	private static final float MAX_FPS = 60;
	private static final float MIN_DT = 1000.0f / MAX_FPS;

	private Thread drawingThread;
	private final SurfaceHolder surfaceHolder;
	private FpsCounter fpsCounter;

	/** time since the start of the game in milliseconds */
	private long time;

	private float timeDelta;

	public GameView(Context context) {
		super(context);

		surfaceHolder = getHolder();
		surfaceHolder.addCallback(this);
	}

	private void update() {
		long now = System.currentTimeMillis();
		timeDelta = 0.001f * (time > 0 ? now - time : 0);
		if (timeDelta != 0 && fpsCounter != null) {
			fpsCounter.updateFps(1.0 / timeDelta);
		}
		time = now;

		onUpdate();
	}

	protected abstract void onUpdate();

	protected abstract void onRender(Canvas canvas);

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
		int height) {
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		drawingThread = new Thread(new Runnable() {
			public void run() {
				while (!Thread.currentThread().isInterrupted()) {
					update();
					Canvas canvas = null;
					try {
						canvas = surfaceHolder.lockCanvas();
						if (canvas != null) {
							onRender(canvas);
						}
					} finally {
						if (canvas != null) {
							surfaceHolder.unlockCanvasAndPost(canvas);
						}
					}

					long now = System.currentTimeMillis();
					float sleepTime = (time - now) + MIN_DT;
					if (sleepTime > 0) {
						try {
							Thread.sleep((long) sleepTime);
						} catch (InterruptedException e) {
							Log.i(LOG_TAG, "interrupted");
						}
					}
				}
			}
		});
		drawingThread.start();
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		if (null != drawingThread) {
			drawingThread.interrupt();
		}
	}

	public void pause() {
		// TODO: better resolve stopping the game thread
		surfaceDestroyed(getHolder());
	}

	public void setFpsCounter(FpsCounter fpsCounter) {
		this.fpsCounter = fpsCounter;
	}

	protected long getTime() {
		return time;
	}

	protected float getTimeDelta() {
		return timeDelta;
	}

}