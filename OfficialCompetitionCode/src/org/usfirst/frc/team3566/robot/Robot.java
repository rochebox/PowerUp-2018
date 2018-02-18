/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package org.usfirst.frc.team3566.robot;

import org.usfirst.frc.team3566.robot.commands.Autonomous;
import org.usfirst.frc.team3566.robot.subsystems.BPU;
import org.usfirst.frc.team3566.robot.subsystems.Climber;
import org.usfirst.frc.team3566.robot.subsystems.DriveTrain;
import org.usfirst.frc.team3566.robot.subsystems.Elevator;

import edu.wpi.cscore.UsbCamera;
import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Spark;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Robot extends TimedRobot {
	//constants
	public static final double RAMP=0.4;
	public static final POINT leftStart = new POINT(3.75, 1.5), 
			middleStart = new POINT(14.5, 1.5), rightStart = new POINT(23.5, 1.5);
	//variables
	public static Variables var;
	public static Timer time;
	public static double maxCurrent;
	//subsystems
	public static BPU bpu;
	public static DriveTrain drivetrain;
	public static Elevator elevator;
	public static Climber climber;
	//sensors
	public static Encoder encoderL, encoderR;
	public static Spark light;

	UsbCamera cam1;
	
	public static OI oi;
	
	Autonomous auto;
	
	SendableChooser<POINT> startingPosition = new SendableChooser<>();
	SendableChooser<Integer> autoTarget = new SendableChooser<>();

	@Override
	public void robotInit() {
		RobotMap.init();
		//IMPORTANT THAT VAR IS INSTANTIATED FIRST
		var = new Variables();
		
		drivetrain = new DriveTrain();
		bpu = new BPU();
		elevator = new Elevator();
	//	climber = new Climber();
		
		oi = new OI();
		startingPosition.addDefault("P1", leftStart);
		startingPosition.addObject("P2", middleStart);
		startingPosition.addObject("P3", rightStart);
		startingPosition.setName("startingPos");
		
		autoTarget.addDefault("OurSwitch", 0);
		autoTarget.addObject("Scale", 1);
		autoTarget.addObject("OppSwitch", 2);
		autoTarget.setName("autoTarget");
		
		SmartDashboard.putData("startingPosition", startingPosition);
		SmartDashboard.putData("autoTarget", autoTarget);
		
		time = new Timer();
		
		//encoder wheel perimeter 227.13mm
		encoderL = new Encoder(0,1,false,Encoder.EncodingType.k4X);
		encoderL.setDistancePerPulse(-0.63);
		
		encoderR=encoderL;
		
//		encoderR = new Encoder(2,3,false,Encoder.EncodingType.k4X);
//		encoderR.setDistancePerPulse(2.394);
		
		/*
		cam1 = CameraServer.getInstance().startAutomaticCapture(0);
        cam1.setResolution(1024, 768);
        cam1.setFPS(30);
        */
        
//        var.reset();
//        drivetrain.ramp(RAMP);
        SmartDashboard.putNumber("maxPower", 1);
        
        light = new Spark(0);
        SmartDashboard.putNumber("LightPattern", 0);
	}

	@Override
	public void disabledInit() {

	}

	@Override
	public void disabledPeriodic() {
		Scheduler.getInstance().run();
	}

	@Override
	public void autonomousInit() {
		auto = new Autonomous(new POINT(startingPosition.getSelected().getX(), startingPosition.getSelected().getY()), 
				autoTarget.getSelected(), (startingPosition.getSelected().equals(leftStart)? 'L' : 
					(startingPosition.getSelected().equals(middleStart)? 'M':'R')));
		//auto will receive info on our starting position coordinates, the code char for our starting position, and the target
		//we're going for
		if (auto != null) {
			auto.start();
		}
	}

	@Override
	public void autonomousPeriodic() {
		Scheduler.getInstance().run();
	}

	@Override
	public void teleopInit() {
		if (auto != null) {
			auto.cancel();
		}
		encoderL.reset();
		encoderR.reset();
		maxCurrent=0;
	}

	@Override
	public void teleopPeriodic() {
		Scheduler.getInstance().run();
		oi.updateCommands();
		var.updateValues();
//		maxCurrent=Math.max(maxCurrent,RobotMap.RL.getOutputCurrent());
		//if(cnt%100==0)System.out.printf("max current %.2f\n",maxCurrent);
		//System.out.printf("L %.0f R%.0f\n",encoderL.getDistance(),encoderR.getDistance());
	     SmartDashboard.putNumber("leftElevCurr", RobotMap.ElevLeft.getOutputCurrent());
	       SmartDashboard.putNumber("RightElevCurr", RobotMap.ElevRight.getOutputCurrent());
	}

	@Override
	public void testPeriodic() {
		System.out.println(elevator.elevatorEncoder.getValue());
		light.set(SmartDashboard.getNumber("LightPattern", 0));
	}
}
