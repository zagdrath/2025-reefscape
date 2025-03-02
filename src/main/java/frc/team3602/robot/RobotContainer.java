/*
 * Copyright (C) 2025 Team 3602 All rights reserved. This work is
 * licensed under the terms of the MIT license which can be found
 * in the root directory of this project.
 */

package frc.team3602.robot;

import com.ctre.phoenix6.swerve.SwerveModule.DriveRequestType;
import com.pathplanner.lib.auto.AutoBuilder;

import com.ctre.phoenix6.Utils;
import com.ctre.phoenix6.swerve.SwerveRequest;

import au.grapplerobotics.*;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.button.CommandJoystick;
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import edu.wpi.first.wpilibj2.command.sysid.SysIdRoutine.Direction;

import static edu.wpi.first.units.Units.*;

import frc.team3602.robot.Constants.ElevatorConstants;
import frc.team3602.robot.generated.TunerConstants;
import frc.team3602.robot.subsystems.DrivetrainSubsystem;
import frc.team3602.robot.subsystems.ElevatorSubsystem;
import frc.team3602.robot.subsystems.IntakeSubsystem;
import frc.team3602.robot.subsystems.PivotSubsystem;

import static frc.team3602.robot.Constants.OperatorInterfaceConstants.*;
import static frc.team3602.robot.Constants.VisionConstants.*;

public class RobotContainer {

  private double MaxSpeed = TunerConstants.kSpeedAt12Volts.in(MetersPerSecond); // kSpeedAt12Volts desired top speed
  private double MaxAngularRate = RotationsPerSecond.of(0.75).in(RadiansPerSecond); // 3/4 of a rotation per second max
                                                                                    // angular velocity

  /* Setting up bindings for necessary control of the swerve drive platform */
  private final SwerveRequest.FieldCentric drive = new SwerveRequest.FieldCentric()
      .withDeadband(MaxSpeed * 0.1).withRotationalDeadband(MaxAngularRate * 0.1) // Add a 10% deadband
      .withDriveRequestType(DriveRequestType.OpenLoopVoltage); // Use open-loop control for drive motors
  private final SwerveRequest.SwerveDriveBrake brake = new SwerveRequest.SwerveDriveBrake();
  private final SwerveRequest.PointWheelsAt point = new SwerveRequest.PointWheelsAt();

  private final Telemetry logger = new Telemetry(MaxSpeed);

  /* laser can */
  private LaserCan lc;

  /* Operator interfaces */
  private final CommandXboxController xboxController = new CommandXboxController(kXboxControllerPort);
  // for simulation
  private final CommandJoystick joystick = new CommandJoystick(0);
  private final CommandJoystick joystick2 = new CommandJoystick(1);

  /* Subsystems */
  private final DrivetrainSubsystem drivetrainSubsys = TunerConstants.createDrivetrain();
  private final ElevatorSubsystem elevatorSubsys = new ElevatorSubsystem();
  private final PivotSubsystem pivotSubsys = new PivotSubsystem(
      elevatorSubsys.elevatorSimMech.getRoot("Pivot Root", 0.75, 0.7), () -> elevatorSubsys.elevatorViz.getLength());
  private final IntakeSubsystem intakeSubsys = new IntakeSubsystem(
      elevatorSubsys.elevatorSimMech.getRoot("Intake Wheel Root", 0.75, 0.3),
      () -> elevatorSubsys.elevatorViz.getLength(), () -> pivotSubsys.pivotSim.getAngleRads());

  // // private final Vision vision = new Vision(drivetrainSubsys);
  private final Superstructure superstructure = new
  Superstructure(drivetrainSubsys, elevatorSubsys, intakeSubsys, pivotSubsys
  );

  /* Autonomous */
  private final SendableChooser<Command> autoChooser;// = new SendableChooser<>();

  public RobotContainer() {
    autoChooser = AutoBuilder.buildAutoChooser();

    laserCanInit();
    configDefaultCommands();
    configButtonBindings();
    configAutonomous();
  }

  private void configDefaultCommands() {
    if (Utils.isSimulation()) {
      drivetrainSubsys.setDefaultCommand(
          drivetrainSubsys.applyRequest(() -> drive.withVelocityX(-joystick.getRawAxis(0) * MaxSpeed) // Drive forward
                                                                                                      // with negative Y
                                                                                                      // (forward)
              .withVelocityY(joystick.getRawAxis(1) * MaxSpeed) // Drive left with negative X (left)
              .withRotationalRate(-joystick2.getRawAxis(1) * MaxAngularRate)) // Drive counterclockwise with negative X
                                                                              // (left)
      );
    } else {
      drivetrainSubsys.setDefaultCommand(
          drivetrainSubsys.applyRequest(() -> drive.withVelocityX(-xboxController.getLeftY() * MaxSpeed) // Drive
                                                                                                         // forward with
                                                                                                         // negative Y
                                                                                                         // (forward)
              .withVelocityY(-xboxController.getLeftX() * MaxSpeed) // Drive left with negative X (left)
              .withRotationalRate(-xboxController.getRightX() * MaxAngularRate) // Drive counterclockwise with negative
                                                                                // X (left)
          ));
    }
  }

  /**
   * Function that is called in the constructor where we configure operator
   * interface button bindings.
   */
  private void configButtonBindings() {
    if (Utils.isSimulation()) {

      joystick.button(1).whileTrue(elevatorSubsys.setHeight(0.0));
      joystick.button(2).onTrue(elevatorSubsys.setHeight(1.0));
      // joystick.button(3).onTrue(superstructure.scoreCoral());
      joystick.button(4).onTrue(drivetrainSubsys.flypathToCoralStation());

      joystick2.button(1).onTrue(pivotSubsys.setAngle(-90));
      joystick2.button(2).onTrue(pivotSubsys.setAngle(0));
      joystick2.button(3).onTrue(pivotSubsys.setAngle(90));
      joystick2.button(4).onTrue(pivotSubsys.setAngle(150));
    } else {
      // xboxController.a().whileTrue(drivetrainSubsys.applyRequest(() -> brake));
      // xboxController.b().whileTrue(drivetrainSubsys.applyRequest(() ->
      // point.withModuleDirection(new Rotation2d(-xboxController.getLeftY(),
      // -xboxController.getLeftX()))));

      xboxController.a().onTrue(superstructure.scoreL4CoralCommand());
      xboxController.b().onTrue(pivotSubsys.setAngle(0));
      xboxController.y().onTrue(pivotSubsys.setAngle(30));
      // xboxController.a().whileTrue(pivotSubsys.setAngle(0.6));
      // xboxController.b().whileTrue(pivotSubsys.setAngle(0.2));
      // xboxController.y().whileTrue(pivotSubsys.setAngle(0.4));

      xboxController.leftTrigger().onTrue(elevatorSubsys.setHeight(0));
      // xboxController.a().onTrue(pivotSubsys.testPivot(1.5));
      // xboxController.b().onTrue(pivotSubsys.testPivot(-1.5));
      xboxController.x().onTrue(pivotSubsys.stopPivot());
      //xboxController.y().onTrue(intakeSubsys.runIntake(-3.0)).onFalse(intakeSubsys.stopIntake());

    //bxController.x().onTrue(intakeSubsys.stopIntake());

      // reset the field-centric heading on left bumper press
      xboxController.leftBumper().onTrue(drivetrainSubsys.runOnce(() -> drivetrainSubsys.seedFieldCentric()));

      drivetrainSubsys.registerTelemetry(logger::telemeterize);
    }
  }

  public void laserCanInit() {
    lc = new LaserCan(0);
    // Optionally initialise the settings of the LaserCAN, if you haven't already
    // done so in GrappleHook
    try {
      lc.setRangingMode(LaserCan.RangingMode.SHORT);
      lc.setRegionOfInterest(new LaserCan.RegionOfInterest(8, 8, 16, 16));
      lc.setTimingBudget(LaserCan.TimingBudget.TIMING_BUDGET_33MS);
    } catch (ConfigurationFailedException e) {
      System.out.println("Configuration failed! " + e);
    }
  }

  public void laserCanPeriodic() {
    LaserCan.Measurement measurement = lc.getMeasurement();
    if (measurement != null && measurement.status == LaserCan.LASERCAN_STATUS_VALID_MEASUREMENT) {
      System.out.println("The target is " + measurement.distance_mm + "mm away!");
    } else {
      System.out.println("Oh no! The target is out of range, or we can't get a reliable measurement!");
      // You can still use distance_mm in here, if you're ok tolerating a clamped
      // value or an unreliable measurement.
    }
  }

  /**
   * Function that returns the currently selected autonomous routine in the
   * SendableChooser.
   * 
   * @return Currently selected autonomous routine.
   */
  public Command getAutonomousCommand() {
    return autoChooser.getSelected();
  }

  private void configAutonomous() {
    SmartDashboard.putData(autoChooser);
  }

  public Pose2d getPose() {
    return drivetrainSubsys.getState().Pose;
  }

  public void resetSimulation() {
    // vision.reset();
  }

  public void updateSimulation() {
    // vision.update(getPose());
  }

}