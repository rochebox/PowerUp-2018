// RobotBuilder Version: 2.0
//
// This file was generated by RobotBuilder. It contains sections of
// code that are automatically generated and assigned by robotbuilder.
// These sections will be updated in the future when you export to
// Java from RobotBuilder. Do not put any code or make any change in
// the blocks indicating autogenerated code or it will be lost on an
// update. Deleting the comments indicating the section will prevent
// it from being updated in the future.


package org.usfirst.frc3566.Jan7.commands;
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
	private boolean dir;
	private double startDegree, endDegree, deltaDegree, error, previous_error;

	
	private double P=0.02, I=0, D = 0.002;
    double integral, derivative;
    
    
    public Rotate() {

    	SmartDashboard.putNumber("P", P);
        SmartDashboard.putNumber("I", I);
        SmartDashboard.putNumber("D", D);

        SmartDashboard.putNumber("maxPower", maxPower); 
    }

    // Called just before this Command runs the first time
    @Override
    protected void initialize() {
    	
    	dir= Robot.var.rotateDirection;
    	//direction==true: turn to right
    	//for deltaDegree: left+ right-
    	deltaDegree = Robot.var.rotateAngle;
    	deltaDegree *= (dir? -1:1);
    	
    	startDegree = 0;
    	endDegree = startDegree + deltaDegree;
    	
    	/*
    switch((int)Math.abs(deltaDegree)) {
    	case 30:
    		P = 0.024;
    		I = 0;
    		D = 0.002;
    		maxPower = 0.5;
    		break;
    	case 45:
    		P = 0.0214;
    		I = 0;
    		D = 0.002;
    		maxPower = 0.5;
    		break;
    	case 60:
    		P = 0.019;
    		I = 0;
    		D = 0.002;
    		maxPower = 0.5;
    		break;
    	case 90:
    		P = 0.017;
    		I = 0;
    		D = 0.002;
    		maxPower = 0.5;
    		break;
    		
    	default:
    		P=SmartDashboard.getNumber("P", 0);
            I=SmartDashboard.getNumber("I", 0);
            D=SmartDashboard.getNumber("D", 0);
           
            maxPower=SmartDashboard.getNumber("maxPower", 1);
    }

    SmartDashboard.putNumber("P", P);
    SmartDashboard.putNumber("I", I);
    SmartDashboard.putNumber("D", D);

    SmartDashboard.putNumber("maxPower", maxPower);
    */
    	
    	 P=1/(2.2*Math.abs(deltaDegree)+18)+0.012;
    	 I=0;
    	 D=0.002;
        
        
        RobotMap.pigeon.setYaw(0, 0);
        this.setTimeout(5);
        
    }

    // Called repeatedly when this Command is scheduled to run
    @Override
    protected void execute() {
    	
    	PID();
    	SmartDashboard.putNumber("power", spd);
    	
    	spd*=maxPower;
    	Robot.driveTrain.rotate(spd);
    }

    // Make this return true when this Command no longer needs to run execute()
    @Override
    protected boolean isFinished() {
    	
    	return this.isTimedOut();
    		//return (spd<0.05) || this.isTimedOut();
    	
    }

    // Called once after isFinished returns true
    @Override
    protected void end() {
    	Robot.driveTrain.stopDrive();
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    @Override
    protected void interrupted() {
    	end();
    }
    
    
    public void PID(){
    	double yaw = SmartDashboard.getNumber("Yaw", 0);
        error = endDegree - yaw; // Error = Target - Actual
        this.integral += (error*.02); // Integral is increased by the error*time (which is .02 seconds using normal IterativeRobot)
        derivative = (error - this.previous_error) / .02;
        this.spd = P*error + I*this.integral + D*derivative;
        previous_error = error;
        
    }

}




/*


//NORMAL/BRUTE-FORCE WAY
public class Rotate extends Command { 
 
 
 	private double spd, OriginalSPD; 
 	private boolean dir; 
 	private double startDegree, endDegree, deltaDegree, allowedError, error; 
 
 
     public Rotate(double delta, double speed, boolean direction) { 
     	//direction==true: turn to right 
     	//for deltaDegree: left+ right- 
     	deltaDegree = delta; 
     	deltaDegree *= (direction? -1:1); 
 
     	spd = speed; 
     	OriginalSPD = speed;
     	dir = direction; 
     } 
 
 
     // Called just before this Command runs the first time 
     @Override 
     protected void initialize() { 
     	 
     	startDegree = SmartDashboard.getNumber("Yaw", 0); 
     	endDegree = startDegree + deltaDegree; 
     	allowedError = 2; 
     	    	 
     } 
 
 
     // Called repeatedly when this Command is scheduled to run 
     @Override 
     protected void execute() { 
     	Robot.driveTrain.rotate(spd, dir); 
     	SmartDashboard.putBoolean("RotateFin", this.isFinished());
     	SmartDashboard.putString("RotatingDirection", (dir? "right":"left"));
     } 
 
 
     // Make this return true when this Command no longer needs to run execute() 
     @Override 
     protected boolean isFinished() { 
     	double value = SmartDashboard.getNumber("Yaw", -30000); 
     	if( endDegree-allowedError <= value 
     			&& value <= endDegree+allowedError) { //if actual yaw is in the range of allowed Error 
     		
//     		new FinishRotate (endDegree, allowedError, dir).start();
//     		SmartDashboard.putNumber("count", SmartDashboard.getNumber("count", 0)+1);
//     		
     		return true; 
     	}else { //this part usually won't run, because isFinished() is run frequently enough
     		//that the robot notices reaching the desired "range" and ends the command. However,
     		//because of inertia the robot keeps spinning and ends up getting a bigger final rotation.
     		//To put the robot back to the originally intended position, we initiate another command
     		//to check on the robot when the robot thinks it's all set (return true above)
     		if (value < endDegree-allowedError) {
     			dir = false;
     			
     		}else if (value > endDegree+allowedError){
     			dir = true;
     		}
     	//	error = Math.abs(endDegree - value);
     	//	spd = OriginalSPD * (error/100);
     		
     		return false; 
     	} 
     } 
 
 
     // Called once after isFinished returns true 
     @Override 
     protected void end() { 
     	Robot.driveTrain.stopDrive(); 
     } 
 
 
     // Called when another command which requires one or more of the same 
     // subsystems is scheduled to run 
     @Override 
     protected void interrupted() { 
     	end(); 
     } 
 } 


*/
