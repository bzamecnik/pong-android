package me.zamecnik.android.pong;

import android.app.Activity;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

public class GameActivity extends Activity {

	static final String LOG_TAG = "Game";

	private GameView gameView;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		TextView fpsTextView = (TextView) findViewById(R.id.fpsTextView);
		FpsCounter fpsCounter = new FpsCounter(fpsTextView);

		LinearLayout layout = (LinearLayout) findViewById(R.id.frame);
		

		gameView = new PongGameView(getApplicationContext());
		gameView.setFpsCounter(fpsCounter);

		layout.addView(gameView);
	}

	@Override
	protected void onPause() {
		super.onPause();
		gameView.pause();
	}
}