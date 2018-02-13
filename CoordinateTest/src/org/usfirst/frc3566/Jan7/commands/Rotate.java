package org.usfirst.frc3566.Jan7.commands;
// RobotBuilder Version: 2.0
//
// This file was generated by RobotBuilder. It contains sections of
// code that are automatically generated and assigned by robotbuilder.
// These sections will be updated in the future when you export to
// Java from RobotBuilder. Do not put any code or make any change in
// the blocks indicating autogenerated code or it will be lost on an
// update. Deleting the comments indicating the section will prevent
// it from being updated in the future.



import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import org.usfirst.frc3566.Jan7.Robot;
import org.usfirst.frc3566.Jan7.RobotMap;

/**
 *
 *
 */

public class Rotate extends Command {

	//NOTE: VALUES BELOW ARE ONLY FOR THE TEST DRIVETRAIN, NOT THE ACTUAL ROBOT!!
	//A pretty good set of PID values: P:0.02, I:0.005; D:0.002
	//really good values for 60 degrees: P:0.019 I:0 D:0.002 mxSPD:0.5
	//really good values for 30 degrees: P:0.024 I:0 D:0.002 mxSPD:0.5
	//really good values for 45 degrees: P:0.0214 I:0 D:0.002 mxSPD:0.5
	//really good values for 90 degrees: P:0.017 I:0 D:0.002 mxSPD:0.5
	//a function for P and theta: P=1/(2.2theta+18)+0.012 (for the positive cases!)
	
	private double spd=0, maxPower=0.5;
	private double startDegree, endDegree, deltaDegree, error, previous_error;
	public boolean isAuto;
	
	private double P=0.02, I=0, D = 0.002;
    double integral, derivative;
    
    
    public Rotate() {
    	isAuto=true;
    	//System.out.println(deltaDegree);
    }
    
    public Rotate(double deltaD) {
    	deltaDegree=deltaD;
    	isAuto=false;
    	//System.out.println("trying to rotate: "+deltaDegree+" degrees!");
    }

    // Called just before this Command runs the first time
    @Override
    protected void initialize() {
    	SmartDashboard.putBoolean("Driving", false);
    	System.out.println(deltaDegree);
    	this.setTimeout(5);
    	if(isAuto)deltaDegree = Robot.var.rotateTheta;
    	//System.out.println("rotate "+deltaDegree);
    	//18,11,06 e-2
    	P=0.03;
    	I=0.035;
    	D=0.01;

    	startDegree = Robot.var.getTheta();
    	endDegree = startDegree - deltaDegree;
    	//System.out.println(deltaDegree);
    	//System.out.println("rotate"+((Robot.var.getTheta()-endDegree)+360)%360);
        //SmartDashboard.putNumber("maxPower", maxPower); 
    }

    // Called repeatedly when this Command is scheduled to run
    @Override
    protected void execute() {
    	
    	PID();
    	SmartDashboard.putNumber("power", spd);
    	SmartDashboard.putNumber("error", error);
    	
    	spd*=maxPower;
    	RobotMap.driveTrainDrive.tankDrive(spd, -spd);
    }

    // Make this return true when this Command no longer needs to run execute()
    @Override
    protected boolean isFinished() {
    	
    	if( Math.abs(error)<2&&Robot.encoderL.getRate()<500)return true;
    	if(this.isTimedOut())return true;
    	return false;
    	
    }

    // Called once after isFinished returns true
    @Override
    protected void end() {
    	Robot.driveTrain.stopDrive();
    	SmartDashboard.putBoolean("Driving", true);
    	//SmartDashboard.putNumber("LastRotate", SmartDashboard.getNumber("Yaw", 0));
    	//RobotMap.pigeon.setYaw(0, 0);
    }
    

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    @Override
    protected void interrupted() {
    	end();
    }
    
    
    public void PID(){
    	double theta = Robot.var.getTheta();
        error = ((theta-endDegree)+360)%360;
        if(error>180)error-=360;
        
        this.integral += (error*.02);
        derivative = (error - this.previous_error) / .02;
        if(Math.abs(spd)>0.8)integral=0;
        this.spd = P*error + I*this.integral + D*derivative;
        previous_error = error;
    }

}


