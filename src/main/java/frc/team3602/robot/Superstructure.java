/*
 * Copyright (C) 2025, FRC Team 3602. All rights reserved. This work
 * is licensed under the terms of the MIT license which can be found
 * in the root directory of this project.
 */

package frc.team3602.robot;

import edu.wpi.first.wpilibj2.command.*;

import edu.wpi.first.wpilibj2.command.Command;
import frc.team3602.robot.subsystems.DrivetrainSubsystem;
import frc.team3602.robot.subsystems.ElevatorSubsystem;
import frc.team3602.robot.subsystems.GripperSubsystem;
import frc.team3602.robot.subsystems.IntakeSubsystem;
import frc.team3602.robot.subsystems.PivotSubsystem;

public class Superstructure extends SubsystemBase{
    private DrivetrainSubsystem driveSubsys;
    private ElevatorSubsystem elevatorSubsys;
    private GripperSubsystem gripperSubsys;
    private IntakeSubsystem intakeSubsys;
    private PivotSubsystem pivotSubsys;
    private Vision vision;

    public double mostRecentElevatorHeight;

    public Superstructure(DrivetrainSubsystem driveSubsys, ElevatorSubsystem elevatorSubsys, GripperSubsystem gripperSubsys, IntakeSubsystem intakeSubsys, PivotSubsystem pivotSubsys, Vision vision){
        this.driveSubsys = driveSubsys;
        this.elevatorSubsys = elevatorSubsys;
        this.gripperSubsys = gripperSubsys;
        this.intakeSubsys = intakeSubsys;
        this.pivotSubsys = pivotSubsys;
        this.vision = vision;
    }

    //private final WaitUntilCommand waitForElevator = new WaitUntilCommand(() -> elevatorSubsys.isNearGoalHeight());
    

    // public Command scoreCoral(double elevatorHeight){
    //     return runOnce(() -> {
    //         Commands.sequence(
    //         elevatorSubsys.setHeight(elevatorHeight),

    //          waitForElevator.andThen(gripperSubsys.runGripper(3.0))
                    
    //         );
    //     });
    // }
         
    public double newElevatorHeight;

    public Command scoreCoral(){
        newElevatorHeight = elevatorSubsys.elevatorHeight.getSelected().doubleValue();

        return Commands.sequence(

            Commands.print("starting sequence"),
            elevatorSubsys.setHeight(newElevatorHeight),
            Commands.print("Elevator Height set"),
            Commands.none().until(() -> elevatorSubsys.isNearGoalHeight()),

            
            
            gripperSubsys.runGripper(3.0)
                
        );
    }




}
