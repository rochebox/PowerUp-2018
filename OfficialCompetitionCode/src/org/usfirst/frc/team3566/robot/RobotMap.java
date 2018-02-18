package org.usfirst.frc.team3566.robot;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.ctre.phoenix.sensors.PigeonIMU;

import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;


public class RobotMap {

    public static WPI_TalonSRX FL;
    public static WPI_TalonSRX RL;
    public static WPI_TalonSRX FR;
    public static WPI_TalonSRX RR;
    
    
    public static WPI_TalonSRX BPUleft, BPUright;
    public static WPI_TalonSRX ElevLeft, ElevRight;
    public static WPI_TalonSRX Tilter;
    public static WPI_TalonSRX climber;
    
    public static PigeonIMU pigeon;
    
    public static SpeedControllerGroup left, right;
    public static SpeedControllerGroup Elev, BPU;
    
    public static DifferentialDrive drive;
    
    public static void init() {
       FL = new WPI_TalonSRX(60); //60
       RL = new WPI_TalonSRX(40);  //40
       FR = new WPI_TalonSRX(10);  //10
       SmartDashboard.putData("Front Right", FR);
       RR = new WPI_TalonSRX(50);    //50
       FR.setInverted(true);
       RR.setInverted(true);
        
       left = new SpeedControllerGroup(FL, RL);
//       SmartDashboard.putData("Left Drive", left);
       right = new SpeedControllerGroup(FR, RR);
//       SmartDashboard.putData("Right Drive", right);
       drive = new DifferentialDrive(left, right);
       //pigeonIMU is connected to the talon with port 2.
       pigeon = new PigeonIMU(FR);
        
       BPUleft = new WPI_TalonSRX(15); //left grabber
       BPUleft.setInverted(true);
       SmartDashboard.putData("BPUleft",BPUleft);
       BPUright = new WPI_TalonSRX(16); //right grabber
       SmartDashboard.putData("BPUright", BPUright);
       BPU = new SpeedControllerGroup(BPUleft, BPUright);
       SmartDashboard.putData("BPU", BPU);
              
       ElevRight = new WPI_TalonSRX(35);  //35
       SmartDashboard.putData("Right Elevator", ElevRight);
       
       ElevLeft = new WPI_TalonSRX(17); //17
       SmartDashboard.putData("Left Elevator", ElevLeft);
       ElevLeft.setInverted(true);
       
       Elev = new SpeedControllerGroup(ElevLeft, ElevRight);
       SmartDashboard.putData("Elevator", Elev);
  
//       climber = new WPI_TalonSRX(25);	//25
//       Tilter = new WPI_TalonSRX(18);	//18
       
//       drive.setSafetyEnabled(true);
//       drive.setExpiration(0.1);
//       drive.setMaxOutput(1.0);
    }
    
}
