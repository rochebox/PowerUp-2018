package org.usfirst.frc.team3566.robot.commands;

import java.util.ArrayList;

import org.usfirst.frc.team3566.robot.POINT;

import edu.wpi.first.wpilibj.command.CommandGroup;

/**
 *
 */

public class CompleteRoute extends CommandGroup {
	
    public CompleteRoute(ArrayList<POINT> routeToComplete) {

    	ArrayList<POINT> route = routeToComplete;
    	
    	for(int p=0; p<routeToComplete.size(); p++) {
    		addSequential(new CalculateAngle(route.get(p)));
    		addSequential(new Rotate());
    		addSequential(new DriveStraight());
    	} 
    }
}
