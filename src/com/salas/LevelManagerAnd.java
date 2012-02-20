package com.salas;

import static com.google.common.base.Preconditions.*;

import java.util.*;

import org.anddev.andengine.engine.*;
import org.anddev.andengine.entity.layer.tiled.tmx.*;
import org.anddev.andengine.entity.scene.*;
import org.anddev.andengine.extension.physics.box2d.util.constants.*;

import com.salas.TMXManager.EdgesTMXListener;
import com.salas.vehicle.*;

import android.content.*;

// Manages the descriptors of the levels. Reads them in from json file
// and returns them as java GameLevel objects. Also manages resources that are needed for the levels.
public class LevelManagerAnd extends LevelManager{
   
   private Engine engine;
   private Context context;
   private HashMap<String, TMXManager> fileManagers;

   LevelManagerAnd(Engine e, Context c) {
      engine = e;
      context = c;
      fileManagers = new HashMap<String, TMXManager>();
   }

   // If we've not read this file before, read it and store the TMX structure.
   void loadLevelFromDisk() {
      checkNotNull(current);
      if (fileManagers.get(current) == null) {
         TMXManager tmx = new TMXManager(context, engine);
         tmx.processTMXFile(current);
         fileManagers.put(current, tmx);
         gameLevels.put(current, new Level());
      }
   }

   void attachTiles(Scene s) {
      loadLevelFromDisk();
      fileManagers.get(current).arttachTiles(s);
   }

   public void prepareVehicles(final WorldAnd world) {
      loadLevelFromDisk();
      fileManagers.get(current).processVehicleObjects(
            new TMXManager.VehicleTMXListener() {

               @Override
               public void vehicleAdd(String name, int startPixX,
                     int startPixY, float startRotation) {
                  float posX = startPixX / PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT;
                  float posY = startPixY / PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT;
                  VehicleEntity newVehicle = new VehicleEntity(world, new EntityBodyAnd(world), new EntitySpriteAnd());
                  world.department.vehicles.registerEntity(newVehicle);
                  newVehicle.sprite.loadResources(world);
                  newVehicle.setName(name);
                  newVehicle.setStartingPos(posX, posY);
                  newVehicle.setStartingRotation(startRotation);
               }
            });
   }

   public void prepareRoads() {
      loadLevelFromDisk();
      final Level level = gameLevels.get(current);
      fileManagers.get(current).processNodesObjects(
            new TMXManager.NodesTMXListener() {
               @Override
               public void nodeAdd(String name, int startPixX, int startPixY) {
                  float posX = startPixX / PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT;
                  float posY = startPixY / PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT;
                  level.addIntersection(name, posX, posY);
               }
            }, 
            new EdgesTMXListener() {
               @Override
               public void edgeAdd(String edgeName, String sourceNode, String destNode) {
                  level.addRoad(edgeName, sourceNode, destNode);
               }
            });
   }

   public int getMapHeightInPixels() {
      loadLevelFromDisk();
      TMXTiledMap tilemap = checkNotNull(fileManagers.get(current).tmxTiledMap);
      return tilemap.getTileRows() * tilemap.getTileHeight();
   }   
  
   public int getMapWidthInPixels() {
      loadLevelFromDisk();
      TMXTiledMap tilemap = checkNotNull(fileManagers.get(current).tmxTiledMap);
      return tilemap.getTileColumns() * tilemap.getTileWidth();
   }

   public Vector2 getCenterPos() {
      return new Vector2(getMapWidthInPixels()/2.0f, getMapHeightInPixels()/2.0f);
   }   

}
