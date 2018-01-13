package org.usfirst.frc3566.Jan7.commands;

import org.usfirst.frc3566.Jan7.Robot;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 */
public class FinishRotate extends Command {

	private double endDegree, allowedErr;
	private boolean DIR, pause;
	private double fixedSlowSpeed = 0.2;
	
    public FinishRotate(double targetDegree, double allowedError, boolean dir) {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
    	endDegree = targetDegree;
    	allowedErr = allowedError;
    	DIR = dir;
    	pause = false;
    }

	// Called just before this Command runs the first time
    protected void initialize() {
    	this.setTimeout(1);
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	if(!pause) {
    	Robot.driveTrain.rotate(fixedSlowSpeed, DIR);
    	}
    	SmartDashboard.putBoolean("FinishRotateFin", this.isFinished());
     	SmartDashboard.putString("RotatingDirection", (DIR? "right":"left"));
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
    	double value = SmartDashboard.getNumber("Yaw", -30000); 
    	if (value < endDegree-allowedErr) {
 			DIR = false;
 			
 		}else if (value > endDegree+allowedErr){
 			DIR = true;
 			
 		}else if( endDegree-allowedErr <= value 
     			&& value <= endDegree+allowedErr) {
 			pause = true;
 			SmartDashboard.putNumber("count", SmartDashboard.getNumber("count", 0)-1);
 			return true;
 		}
//    	if(this.isTimedOut()) {
//    		SmartDashboard.putNumber("count", SmartDashboard.getNumber("count", 0)-1);
//    		return true;
//    	}
        return false;
    }

    // Called once after isFinished returns true
    protected void end() {
    	Robot.driveTrain.stopDrive();
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    	end();
    }
}