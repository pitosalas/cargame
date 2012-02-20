package com.salas;

import org.anddev.andengine.engine.Engine;
import org.anddev.andengine.engine.handler.timer.*;
import org.anddev.andengine.entity.scene.*;

import com.salas.world.*;

import android.content.Context;
import android.util.*;

public class WorldAnd extends World<WorldBodies, WorldSprites> {
   public Dashboard dash;
   public Engine engine;
   public boolean multiTouch;
   public Context aeCtx;

   private static WorldAnd world = null;

   public static WorldAnd singleton() {
      if (world == null) {
         world = new WorldAnd();
      }
      return world;
   }

   // Ensure this is a singleton by hiding the contructor as private!
   private WorldAnd() { }

   @Override
   public void unloadResources() {
      super.unloadResources();
   }

   public void startAllUpdateHandlers() {
     Scene s = ((WorldSpritesAnd) sprites).scene; 
     s.registerUpdateHandler(new TimerHandler(LONG_TICK_SECONDS, true, new AndUpdateHandler(LONG_TICK_SECONDS)));
     s.registerUpdateHandler(new TimerHandler(MED_TICK_SECONDS, true, new AndUpdateHandler(MED_TICK_SECONDS)));
     s.registerUpdateHandler(new TimerHandler(SHORT_TICK_SECONDS, true, new AndUpdateHandler(SHORT_TICK_SECONDS)));
     s.registerUpdateHandler(((WorldBodiesAnd) bodies).worldBox2d);

   }
   
   // Framework will call this once per SHORT_TICK_SECONDS
   public void shortClockTickUpdates() {
      super.shortClockTickUpdates();
   }
   
   // Framework will call this method once LONG_TICK_SECONDS
   public void longClockTickUpdates() {
      super.longClockTickUpdates();
   
   }
   
   // Framework will call this method once MEDIUM_TICK_SECONDS
   public void mediumClockTickUpdates() {
      super.mediumClockTickUpdates();
   }
   
   @Override
   public void logI(String tag, String message) {
      Log.i(tag, message);
    }

}