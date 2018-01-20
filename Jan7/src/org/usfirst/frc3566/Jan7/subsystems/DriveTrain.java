// RobotBuilder Version: 2.0
//
// This file was generated by RobotBuilder. It contains sections of
// code that are automatically generated and assigned by robotbuilder.
// These sections will be updated in the future when you export to
// Java from RobotBuilder. Do not put any code or make any change in
// the blocks indicating autogenerated code or it will be lost on an
// update. Deleting the comments indicating the section will prevent
// it from being updated in the future.


package org.usfirst.frc3566.Jan7.subsystems;

import org.usfirst.frc3566.Jan7.RobotMap;
import org.usfirst.frc3566.Jan7.commands.*;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;

// BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=IMPORTS
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import edu.wpi.first.wpilibj.RobotDrive;

// END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=IMPORTS


/**
 *
 */
public class DriveTrain extends Subsystem {

    // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CONSTANTS

    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=CONSTANTS

    // BEGIN AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=DECLARATIONS
    private final WPI_TalonSRX FL = RobotMap.FrontLeft;
    private final WPI_TalonSRX RL = RobotMap.RearLeft;
    private final WPI_TalonSRX FR = RobotMap.FrontRight;
    private final WPI_TalonSRX RR = RobotMap.RearRight;
    private final DifferentialDrive drive = RobotMap.driveTrainDrive;

    // END AUTOGENERATED CODE, SOURCE=ROBOTBUILDER ID=DECLARATIONS

    @Override
    public void initDefaultCommand() {

        // Set the default command for a subsystem here.
        setDefaultCommand(new DriveWithJoysticks());
    }

    @Override
    public void periodic() {
        // Put code here to be run every loop

    }
    
   public void stopDrive() {
	  FL.stopMotor();
	  RL.stopMotor();
	  FR.stopMotor();
	  RR.stopMotor();
   }

   public void goForward(double spd) {
	   FL.set(spd);
	   RL.set(spd);
	   FR.set(spd);
	   RR.set(spd);
   }
   
   public void rotate(double spd, boolean dir) {
	   FL.set(spd*(dir? 1:-1));
	   FR.set(spd*(dir? -1:1));
	   RL.set(spd*(dir? 1:-1));
	   RR.set(spd*(dir? -1:1));
   }
   
   public void rotate(double spd) {
	   FL.set(spd*-1);
	   FR.set(spd);
	   RL.set(spd*-1);
	   RR.set(spd);
   }
    // Put methods for controlling this subsystem
    // here. Call these from Commands.

}

