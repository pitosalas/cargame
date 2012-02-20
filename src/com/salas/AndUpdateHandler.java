package com.salas;

import org.anddev.andengine.engine.handler.timer.*;
import com.salas.world.*;

public class AndUpdateHandler implements ITimerCallback {
   float timerType;

   AndUpdateHandler(float time) {
      timerType = time;
   }

   @Override
   public void onTimePassed(TimerHandler pTimerHandler) {
      if (timerType == World.LONG_TICK_SECONDS) {
         WorldAnd.singleton().longClockTickUpdates();
      } else if (timerType == World.MED_TICK_SECONDS) {
         WorldAnd.singleton().mediumClockTickUpdates();
      } else if (timerType == World.SHORT_TICK_SECONDS) {
         WorldAnd.singleton().shortClockTickUpdates();
      }
   }
}
