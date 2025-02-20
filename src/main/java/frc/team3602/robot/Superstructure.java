package frc.team3602.robot;

import frc.team3602.robot.subsystems.DrivetrainSubsystem;
import frc.team3602.robot.subsystems.ElevatorSubsystem;
import frc.team3602.robot.subsystems.GripperSubsystem;
import frc.team3602.robot.subsystems.IntakeSubsystem;
import frc.team3602.robot.subsystems.PivotSubsystem;

public class Superstructure {
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

}
