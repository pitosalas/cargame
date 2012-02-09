package com.salas;

import static com.google.common.base.Preconditions.*;
import org.anddev.andengine.engine.Engine;
import org.anddev.andengine.entity.layer.tiled.tmx.*;
import org.anddev.andengine.entity.layer.tiled.tmx.util.exception.TMXLoadException;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.opengl.texture.TextureOptions;
import org.anddev.andengine.util.Debug;

import android.content.Context;

/* Create the level sprites using an optional filename level.tmxFilename.
 *  
 * A TMX file is created with the tool Tiled (although other tools exist) and
 * describes both the graphic files that represent the level as well as meta data for tiles.
 */
public class TMXManager {

   private Context activity;
   private Engine engine;
   TMXTiledMap tmxTiledMap = null;

   TMXManager(Context act, Engine eng) {
      engine = eng;
      activity = act;
   }

   public void processTMXFile(String filename) {
      try {
         final TMXLoader tmxLoader = new TMXLoader(activity,
               engine.getTextureManager(),
               TextureOptions.BILINEAR_PREMULTIPLYALPHA);
         tmxTiledMap = tmxLoader.loadFromAsset(activity, filename);
      } catch (final TMXLoadException tmxle) {
         Debug.e(tmxle);
      }
   }

   void arttachTiles(Scene s) {
      checkNotNull(tmxTiledMap);
      for (TMXLayer t : tmxTiledMap.getTMXLayers()) {
         s.attachChild(t);
      }
   }

   void processVehicleObjects(VehicleTMXListener handler) {
      for (TMXObjectGroup oGroup : tmxTiledMap.getTMXObjectGroups()) {
         if (oGroup.getName().equalsIgnoreCase("vehicles")) {
            for (TMXObject vehObject : oGroup.getTMXObjects()) {
               String name = vehObject.getName();
               int startPixX = vehObject.getX();
               int startPixY = vehObject.getY();
               float startRotation = 0;
               for (TMXObjectProperty p : vehObject.getTMXObjectProperties()) {
                  if (p.getName().equalsIgnoreCase("Rotation")) {
                     startRotation = new Float(p.getValue());
                  }
               }
               handler.vehicleAdd(name, startPixX, startPixY, startRotation);
            }
         }
      }
   }

   void processNodesObjects(NodesTMXListener handler) {
      for (TMXObjectGroup oGroup : tmxTiledMap.getTMXObjectGroups()) {
         if (oGroup.getName().equalsIgnoreCase("nodes")) {
            for (TMXObject nodeObject : oGroup.getTMXObjects()) {
               String name = nodeObject.getName();
               int startPixX = nodeObject.getX();
               int startPixY = nodeObject.getY();
               handler.nodeAdd(name, startPixX, startPixY);

            }
         }
      }
   }

   public interface VehicleTMXListener {
      void vehicleAdd(String name, int startPixX, int startPixY,
            float startRotation);
   }

   public interface NodesTMXListener {
      void nodeAdd(String name, int startPixX, int startPixY);
   }

}
