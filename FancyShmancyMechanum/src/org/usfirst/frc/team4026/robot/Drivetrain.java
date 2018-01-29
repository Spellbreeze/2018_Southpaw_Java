package org.usfirst.frc.team4026.robot;

import edu.wpi.first.wpilibj.AnalogGyro;
import edu.wpi.first.wpilibj.Jaguar;
import edu.wpi.first.wpilibj.VictorSP;
import edu.wpi.first.wpilibj.hal.PowerJNI;

public class Drivetrain implements Subsystem {
	
	private static final int FRONT_LEFT = 0;
	private static final int FRONT_RIGHT = 1;
	private static final int REAR_LEFT = 2;
	private static final int REAR_RIGHT = 3;
	
	private static final int GYRO_PORT = 0;
	
	static final double MAX_BATTERY = 12.3;
	
	private boolean driveReverse;
	
	private AnalogGyro driveGyro;
	private boolean isGyroresetTelop;
	
	private VictorSP rightDriveMotor;
	private VictorSP leftDriveMotor;
	
	public Jaguar FrontLeft;
	public Jaguar FrontRight;
	public Jaguar RearLeft;
	public Jaguar RearRight;
	
	private AnalogGyro theGyro;
	boolean isInitialized = false;
	
	public int init() {
		
		if(!isInitialized) {
			
		FrontLeft = new Jaguar(FRONT_LEFT);
		FrontRight = new Jaguar(FRONT_RIGHT);
		RearLeft = new Jaguar (REAR_LEFT);
		RearRight = new Jaguar (REAR_RIGHT);
		
		theGyro = new AnalogGyro(GYRO_PORT);
		
		isInitialized = true;
		return 0;
		}
		//Return 1 if tries to reinit
		return 1;
	}
	
	public int act(Robot robot) {
		double left = -robot.controller.getLeft();
		double right = robot.controller.getRight();
		FrontLeft.set(left); 
		RearLeft.set(left);
		FrontRight.set(right);
		RearRight.set(right);
		return 0;
		
		
	}
	
	public double getGyroHeading(){
		return theGyro.getAngle();
	}
	public int shutdown(){
		stopDrive();
		return 1;
	}
	
	private void stopDrive(){
		FrontLeft.set(0);
		FrontRight.set(0);
		RearLeft.set(0);
		RearRight.set(0);
	}
	
	
	boolean shouldIHelpDriverDriveStraight() {
		return false;
	}
	
	void keepDriveStraight(double leftDriveVel, double rightDriveVel, double targetAngle) {
		double error = 0;
		error = targetAngle - driveGyro.getAngle();
		double correctionFactor = (error/75.0);

		if(leftDriveVel > 0.9)
			leftDriveVel = 0.9;
		else if(leftDriveVel < -0.9)
			leftDriveVel = -0.9;

		if(rightDriveVel > 0.9)
			rightDriveVel = 0.9;
		else if(rightDriveVel < -0.9)
			rightDriveVel = -0.9;

		if(targetAngle > (driveGyro.getAngle() - 0.5) || targetAngle < (driveGyro.getAngle() + 0.5)) {
			leftDriveMotor.set(((-leftDriveVel) + correctionFactor) * batteryCompensationPct());
			rightDriveMotor.set((rightDriveVel + correctionFactor) * batteryCompensationPct());
		}
		else {
			leftDriveMotor.set(-leftDriveVel * batteryCompensationPct());
			rightDriveMotor.set(rightDriveVel * batteryCompensationPct());
		}
	}
	
	void tankDrive(Controller foo) {
		double right = foo.mainDriverStick.getY();
		double left = foo.mainDriverStick.getThrottle();

		//Cut Velocity in half
		if(foo.mainDriverStick.getRawButton(7)) {
			right /= 2.0;
			left /= 2.0;
		}
		double avgStick = (right + left) / 2.0;
		if(!foo.mainDriverStick.getRawButton(8) && !shouldIHelpDriverDriveStraight()) {
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
	
	double batteryCompensationPct() {
		double batteryScaleFactor = 0.0;

		// RobotController.getInstance().getBatteryVoltage issue https://www.chiefdelphi.com/forums/showthread.php?p=1717817
		batteryScaleFactor = MAX_BATTERY / PowerJNI.getVinVoltage();

		return batteryScaleFactor;
	}
}
