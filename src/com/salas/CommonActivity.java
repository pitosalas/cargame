package com.salas;

import org.anddev.andengine.engine.Engine;
import org.anddev.andengine.engine.camera.ZoomCamera;
import org.anddev.andengine.engine.options.EngineOptions;
import org.anddev.andengine.engine.options.EngineOptions.ScreenOrientation;
import org.anddev.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.entity.scene.Scene.IOnSceneTouchListener;
import org.anddev.andengine.extension.input.touch.controller.MultiTouch;
import org.anddev.andengine.extension.input.touch.controller.MultiTouchController;
import org.anddev.andengine.extension.input.touch.detector.PinchZoomDetector;
import org.anddev.andengine.extension.input.touch.detector.PinchZoomDetector.IPinchZoomDetectorListener;
import org.anddev.andengine.extension.input.touch.exception.MultiTouchException;
import org.anddev.andengine.input.touch.TouchEvent;
import org.anddev.andengine.input.touch.detector.ScrollDetector.IScrollDetectorListener;
import org.anddev.andengine.input.touch.detector.ScrollDetector;
import org.anddev.andengine.input.touch.detector.SurfaceScrollDetector;
import org.anddev.andengine.ui.activity.BaseGameActivity;

import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

/**
 * (c) 2010 Nicolas Gramlich 
 * (c) 2011 Zynga Inc.
 * 
 * @author Nicolas Gramlich
 * @since 22:10:28 - 11.04.2010
 */
public abstract class CommonActivity extends BaseGameActivity implements
	IOnSceneTouchListener, IScrollDetectorListener, IPinchZoomDetectorListener {
	// ===========================================================
	// Constants
	// ===========================================================

	private static final int MENU_TRACE = Menu.FIRST;
	
	public int screenWidth;
	public int screenHeight;
	protected ZoomCamera camera;
	private SurfaceScrollDetector scrollDetector;
	private PinchZoomDetector pinchDetector;


	public Engine engine;

	private float startZoomfactor;


	@Override
	public boolean onCreateOptionsMenu(final Menu pMenu) {
		pMenu.add(Menu.NONE, MENU_TRACE, Menu.NONE, "Start Method Tracing");
		return super.onCreateOptionsMenu(pMenu);
	}

	@Override
	public boolean onPrepareOptionsMenu(final Menu pMenu) {
		pMenu.findItem(MENU_TRACE).setTitle(this.mEngine.isMethodTracing() ? "Stop Method Tracing" : "Start Method Tracing");
		return super.onPrepareOptionsMenu(pMenu);
	}

	@Override
	public boolean onMenuItemSelected(final int pFeatureId, final MenuItem pItem) {
		switch(pItem.getItemId()) {
			case MENU_TRACE:
				if(this.mEngine.isMethodTracing()) {
					this.mEngine.stopMethodTracing();
				} else {
					this.mEngine.startMethodTracing("AndEngine_" + System.currentTimeMillis() + ".trace");
				}
				return true;
			default:
				return super.onMenuItemSelected(pFeatureId, pItem);
		}
	}

	// ===========================================================
	// Methods
	// ===========================================================
	
	public boolean checkEnableMultiTouch(Engine engine) {
		try {
			if(MultiTouch.isSupported(this)) {
				engine.setTouchController(new MultiTouchController());
				return true;
			} else {
				Toast.makeText(this, "Sorry your device does NOT support MultiTouch!\n\n(No PinchZoom is possible!)", Toast.LENGTH_LONG).show();
				return false;
			}
		} catch (final MultiTouchException e) {
			Toast.makeText(this, "Sorry your Android Version does NOT support MultiTouch!\n\n(No PinchZoom is possible!)", Toast.LENGTH_LONG).show();
			return false;
		}

	}

// Calls that are used in onCreateEngine
	public void configScreenInfo() {
		DisplayMetrics metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);
		screenWidth = metrics.widthPixels;
		screenHeight = metrics.heightPixels;
		Log.d("COM", "screen wid="+screenWidth+", ht="+screenHeight);
	}
	
	public void configCamera() {
		camera = new ZoomCamera(0, 0, screenWidth, screenHeight);
	}
	
	public Engine configEngine() {
		final EngineOptions engineOptions = new EngineOptions(true,
				ScreenOrientation.LANDSCAPE, new RatioResolutionPolicy(
						screenWidth, screenHeight), camera);
		engineOptions.setNeedsSound(true);
		engineOptions.setNeedsMusic(true);
		engineOptions.getTouchOptions().setRunOnUpdateThread(true);
		return new Engine(engineOptions);
	}
	
	public void configSceneTouchNScroll(Scene s, boolean pinchEnabled) {
		s.setOnAreaTouchTraversalFrontToBack();
		scrollDetector = new SurfaceScrollDetector(this);
		if (pinchEnabled) {
			try {
				pinchDetector = new PinchZoomDetector(this);
			} catch (final MultiTouchException e) {
				pinchDetector = null;
			}
		} else {
			pinchDetector = null;
		}
		s.setOnSceneTouchListener(this);
		s.setTouchAreaBindingEnabled(true);
	}
	
	@Override
	public void onScroll(final ScrollDetector pScollDetector,
			final TouchEvent pTouchEvent, final float pDistanceX,
			final float pDistanceY) {
		final float zoomFactor = camera.getZoomFactor();
		camera.offsetCenter(-pDistanceX / zoomFactor, -pDistanceY / zoomFactor);
	}

	@Override
	public void onPinchZoomStarted(final PinchZoomDetector pPinchZoomDetector,
			final TouchEvent pTouchEvent) {
		startZoomfactor = camera.getZoomFactor();
	}

	@Override
	public void onPinchZoom(final PinchZoomDetector pPinchZoomDetector,
			final TouchEvent pTouchEvent, final float pZoomFactor) {
		camera.setZoomFactor(startZoomfactor * pZoomFactor);
	}

	@Override
	public void onPinchZoomFinished(final PinchZoomDetector pPinchZoomDetector,
			final TouchEvent pTouchEvent, final float pZoomFactor) {
		camera.setZoomFactor(startZoomfactor * pZoomFactor);
	}

	@Override
	public boolean onSceneTouchEvent(final Scene pScene,
			final TouchEvent pSceneTouchEvent) {
		if (pinchDetector != null) {
			pinchDetector.onTouchEvent(pSceneTouchEvent);

			if (pinchDetector.isZooming()) {
				scrollDetector.setEnabled(false);
			} else {
				if (pSceneTouchEvent.isActionDown()) {
					scrollDetector.setEnabled(true);
				}
				scrollDetector.onTouchEvent(pSceneTouchEvent);
			}
		} else {
			this.scrollDetector.onTouchEvent(pSceneTouchEvent);
		}

		return true;
	}

}
