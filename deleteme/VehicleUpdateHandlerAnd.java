package com.salas;

import org.anddev.andengine.engine.handler.timer.ITimerCallback;

import com.salas.vehicle.*;

public class VehicleUpdateHandlerAnd extends VehicleUpdateHandler implements ITimerCallback {

	public VehicleUpdateHandlerAnd(VehicleEntity theVehicle) {
		super(theVehicle);
	}

}
