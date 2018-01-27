package org.usfirst.frc.team4026.robot;

import edu.wpi.first.wpilibj.Joystick;

public class Controller {
	Joystick mainDriverStick;
	Joystick manipulatorStick;
	boolean isInitialized = false;
	
	public int init() {
		if(!isInitialized) {
			mainDriverStick = new Joystick(0);
			manipulatorStick = new Joystick(1);
			isInitialized = true;
		}
	return 1;
	}
	
	public double getLeft(){
		return mainDriverStick.getY();	
	}
	
	public double getRight() {
		return mainDriverStick.getThrottle();
	}
	
	public boolean getRawButton(int button) {
		return driveGamepad.getRawButton(button);
	}
	@Override
	public int shutdown() {
		
		return 1;
	}
}
