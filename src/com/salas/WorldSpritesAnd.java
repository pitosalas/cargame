package com.salas;

import static com.google.common.base.Preconditions.*;

import java.util.*;

import org.anddev.andengine.entity.primitive.Line;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.extension.physics.box2d.util.constants.PhysicsConstants;

import com.salas.Level.IntersectionHandler;
import com.salas.Level.RoadLoopHandler;
import com.salas.entity.*;
import com.salas.world.*;

import android.util.Log;

// AndEngine implementation of World class from carmodel
public class WorldSpritesAnd extends WorldSprites {

	public Scene scene;
	WorldAnd world;
	Line redVector;
	Line greenVector;
	TextBox toolTipA;
	TextBox toolTipB;
	private HashMap<String, Line>lines = new HashMap<String, Line>();

	
	WorldSpritesAnd(WorldAnd worldA) {
		world = worldA;
	}
	
	public void attach(EntitySprite s) {
		scene.attachChild(((EntitySpriteAnd)s).spriteAnd());
	}

	@Override
   public void showCursorAt(Vector2 loc) {
		UtilitySprites.setCursorSprite("$MAIN", box2d2PixelCoordinate(loc));
	}
	
	static Vector2 pixel2Box2dCoordinate(Vector2 from) {
		Float pixel2meter = new Float(1.0f/PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT);
		Log.i("entity", pixel2meter.toString());
		return from.mul(pixel2meter);
	}
	
	static Vector2 box2d2PixelCoordinate(Vector2 from) {
		return from.cpy().mul(PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT);
	}

	@Override
	public void showRedVectorFrom(Vector2 origin, Vector2 vector) {
	if (redVector == null) {
			redVector = new Line(0, 0, 0, 0, 2.0f);
			redVector.setColor(1.0f, 0.0f, 0.0f);
			((WorldSpritesAnd) world.sprites).scene.attachChild(redVector);
		}
		final Vector2 startpoint = box2d2PixelCoordinate(origin);
		final Vector2 endpoint = box2d2PixelCoordinate(vector).add(startpoint);
		redVector.setPosition(startpoint.x, startpoint.y, endpoint.x, endpoint.y);
	}

	@Override
	public void showGreenVectorFrom(Vector2 origin, Vector2 vector) {
	if (greenVector == null) {
			greenVector = new Line(0, 0, 0, 0, 2.0f);
			greenVector.setColor(0.0f, 1.0f, 0.0f);
			scene.attachChild(greenVector);
		}
		final Vector2 startpoint = box2d2PixelCoordinate(origin);
		final Vector2 endpoint = box2d2PixelCoordinate(vector).add(startpoint);
		greenVector.setPosition(startpoint.x, startpoint.y, endpoint.x, endpoint.y);
	}
	
   @Override
   public void showRoadGraph(Level level) {
	   level.loopOverAllIntersections(new IntersectionHandler() {         
         @Override
         public void intersection(Intersection inter, String name, Vector2 position) {
            UtilitySprites.setCursorSprite(name, box2d2PixelCoordinate(position)); 
         }
      });
	   level.loopOverAllRoads(new RoadLoopHandler() {
         @Override public void road(String name, Vector2 fromVector, Vector2 toVector) {
            setLineSprite(name, fromVector, toVector);
         }
      });
	}

	public void setLineSprite(String name, Vector2 start, Vector2 end) {
      Line newline;
      checkNotNull(name);
      if ((newline = lines.get(name)) == null) {
         newline = new Line(0, 0, 0, 0, 2.0f);
         newline.setColor(new Random().nextFloat(), new Random().nextFloat(), new Random().nextFloat());
         ((WorldSpritesAnd) world.sprites).scene.attachChild(newline);
         lines.put(name, newline);
      } else {
         final Vector2 startpoint = box2d2PixelCoordinate(start);
         final Vector2 endpoint = box2d2PixelCoordinate(end);
         newline.setPosition(startpoint.x, startpoint.y, endpoint.x, endpoint.y);
      }
   }

   @Override
   public void showTooltipA(float x, float y, String text) {
		Vector2 pixelWhere = box2d2PixelCoordinate(new Vector2(x,y));
		if (toolTipA == null) {
			toolTipA = new TextBox((int) pixelWhere.x, (int) pixelWhere.y);
			scene.attachChild(toolTipA.textBox);
		}
		toolTipA.textBox.setColor(1.0f, 1.0f, 0.0f);
		toolTipA.textBox.setPosition(pixelWhere.x+15, pixelWhere.y+30);
		toolTipA.textBox.setText(text);
	}

   public void showTooltipB(float x, float y, String text) {
		Vector2 pixelWhere = box2d2PixelCoordinate(new Vector2(x,y));
		if (toolTipB == null) {
			toolTipB = new TextBox((int) pixelWhere.x, (int) pixelWhere.y);
			scene.attachChild(toolTipB.textBox);
		}
		toolTipB.textBox.setPosition(pixelWhere.x+15, pixelWhere.y-60);
		toolTipB.textBox.setColor(0.0f, 1.0f, 1.0f);
		toolTipB.textBox.setText(text);
	}
}
