package org.usfirst.frc.team4026.robot;

import edu.wpi.first.wpilibj.Joystick;

public class Controller {
	Joystick controller1;
	Joystick controller2;
	
	public void init(){
		controller1 = new Joystick(0);
		controller2 = new Joystick(1);
	}
	public double getLeft(){
		return controller1.getY();
		
		
	}
	public double getRight(){
		return controller1.getThrottle();
	}

}
