package org.usfirst.frc.team4026.robot;

import edu.wpi.first.wpilibj.Joystick;

public class Controller {
	Joystick mainDriverStick;
	Joystick manipulatorStick;
	boolean isInitialized = false;
	
	public void init() {
		if(!isInitialized) {
			mainDriverStick = new Joystick(0);
			manipulatorStick = new Joystick(1);
			isInitialized = true;
		}
	return 1
	}
	
	public double getLeft(){
		return mainDriverStick.getY();	
	}
	
	public double getRight() {
		return mainDriverStick.getThrottle();
	}
	
	boolean shouldIHelpDriverDriveStraight() {
		return false;
	}
	
	void tankDrive() {
		double right = mainDriverStick.getY();
		double left = mainDriverStick.getThrottle();

		//Cut Velocity in half
		if(mainDriverStick.getRawButton(7)) {
			right /= 2.0;
			left /= 2.0;
		}
		double avgStick = (right + left) / 2.0;
		if(!mainDriverStick.getRawButton(8) && !shouldIHelpDriverDriveStraight()) {
			if (driveReverse) {
				leftDriveMotor.set(-right);
				rightDriveMotor.set(left);
			}
			else {
				leftDriveMotor.set(left);
				rightDriveMotor.set(-right);
			}
			isGyroresetTelop = false;
		}
		else {
			if(isGyroresetTelop == false) {
				driveGyro.reset();
				isGyroresetTelop = true;
			}
			if (driveReverse) {
				keepDriveStraight(avgStick, avgStick, 0);
			}
			else {
				keepDriveStraight(-avgStick, -avgStick, 0);
			}
		}
	}
}
