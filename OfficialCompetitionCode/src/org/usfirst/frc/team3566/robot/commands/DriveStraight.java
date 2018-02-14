package org.usfirst.frc.team3566.robot.commands;

import org.usfirst.frc.team3566.robot.Robot;
import org.usfirst.frc.team3566.robot.RobotMap;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class DriveStraight extends Command {
	static final double maxSpeed=2500;
	double  P=0.0008, I=0.00035, D=0.00018;
//    double P=0.0006,I=0.0002,D=0.0001;
	double integral, previousError, derivative, setPoint = 2000;
    double power=0,error=0;
    double maxPower=1;
    double startTime,time,length;
    double startAngle;
    boolean isAuto;
    
    //input in feet
    public DriveStraight(double distanceInFt) {
    	setPoint= distanceInFt*304.8;
    	isAuto=false;
    	requires(Robot.drivetrain);
    }
    
    public DriveStraight() {
    	isAuto=true;
    	requires(Robot.drivetrain);
    }

    @Override
    protected void initialize() {
    	//Robot.var.distance's unit is ft, while DriveStraight needs to use Encoder in cm. 
    	//we're converting units here in the beginning to get rid of the problem
    	if(isAuto)setPoint=Robot.var.distance * 304.8;   //1 ft = 304.8 mm. Distance (ft) converted to mm for encoder drive
    	length=setPoint;
    	Robot.var.collision.isCollide=false;
    	startAngle=Robot.var.getTheta();
    	setPoint+=Robot.var.getEncoder();
    	error = setPoint - Robot.var.getEncoder();
    	startTime=Robot.time.get();
    }
    
    void ramp()
    {
    	if(time<0.5 && error>0)
    		power=Math.min(time*2, power);
    	else if(time<0.5 && error<0) power=Math.max(time*-2, power);
    	if(Robot.encoderL.getRate()>maxSpeed*0.7)
    		power=Math.min(1.6-Robot.encoderL.getRate()/maxSpeed, power);
    	else if(Robot.encoderL.getRate()<maxSpeed*-0.7)
    		power=Math.max(-1.6-Robot.encoderL.getRate()/maxSpeed, power);
    	
    }
    
    void PID() {
    	error = setPoint - Robot.var.getEncoder();
        this.integral += (error*.02);
        derivative = (error - previousError) / .02;
        previousError=error;
        if(power>=0.99||power<=-0.99)integral=0;
        power = P*error + I*this.integral + D*derivative;
    }
    
    @Override
    protected void execute() {
    	//Robot.var.updateXY();
    	time=Robot.time.get()-startTime;
        PID();
//        ramp();
    	RobotMap.drive.tankDrive(power,-power);
    }

    @Override
    protected boolean isFinished() {
    	double errAngle=(Robot.var.getTheta()-startAngle+360)%360;
//    	if(errAngle>15 && errAngle<345)return true;
    	if(Robot.oi.joystick1.getRawButton(3))return true;
    	if(Robot.var.isCollide)return true;
    	if(Math.abs(error)<100&&Robot.encoderL.getRate()<100)return true;//needs consideration
    	if(time> Math.abs(length) / 300 )return true;
    	return false;
    }

    @Override
    protected void end(){
    	if(Robot.var.isCollide)
    		System.out.println("drive straight stopped due to collision");
    	double errAngle=(Robot.var.getTheta()-startAngle+360)%360;
    	if(errAngle>15 && errAngle<345)
    		System.out.println("drive straight stopped due to bad direction");
    	System.out.println("straight stop");
    	Robot.drivetrain.stopDrive();
    }

    @Override
    protected void interrupted() {
    }
}
