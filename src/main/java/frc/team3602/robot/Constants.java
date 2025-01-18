/*
 * Copyright (C) 2025 Team 3602 All rights reserved. This work is
 * licensed under the terms of the MIT license which can be found
 * in the root directory of this project.
 */

package frc.team3602.robot;

import edu.wpi.first.math.Matrix;
import edu.wpi.first.math.VecBuilder;
import edu.wpi.first.math.geometry.Rotation3d;
import edu.wpi.first.math.geometry.Transform3d;
import edu.wpi.first.math.geometry.Translation3d;
import edu.wpi.first.math.numbers.N1;
import edu.wpi.first.math.numbers.N3;
import edu.wpi.first.units.AngleUnit;
import edu.wpi.first.units.DistanceUnit;
import edu.wpi.first.units.Measure;
import static edu.wpi.first.units.Units.*;

import edu.wpi.first.apriltag.AprilTagFieldLayout;
import edu.wpi.first.apriltag.AprilTagFields;

public final class Constants {
    public final class OperatorInterfaceConstants {
      public final static int kXboxControllerPort = 0;
      public final static int kControlPanelPort = 1;
    }

    public final class VisionConstants {
      public static final AprilTagFieldLayout kFieldLayout = AprilTagFields.k2025Reefscape.loadAprilTagLayoutField();

      public static final String kMod0CameraName = "mod0Camera";
      public static final String kMod1CameraName = "mod1Camera";
      public static final String kMod2CameraName = "mod2Camera";
      public static final String kMod3CameraName = "mod3Camera";

      public static final Transform3d kRobotToMod0CameraTransform = new Transform3d(
        new Translation3d(0.0, 0.0, 0.0),
        new Rotation3d(0.0, 0.0, 0.0)
      );
      public static final Transform3d kRobotToMod1CameraTransform = new Transform3d(
        new Translation3d(0.0, 0.0, 0.0),
        new Rotation3d(0.0, 0.0, 0.0)
      );
      public static final Transform3d kRobotToMod2CameraTransform = new Transform3d(
        new Translation3d(0.0, 0.0, 0.0),
        new Rotation3d(0.0, 0.0, 0.0)
      );
      public static final Transform3d kRobotToMod3CameraTransform = new Transform3d(
        new Translation3d(0.0, 0.0, 0.0),
        new Rotation3d(0.0, 0.0, 0.0)
      );

      public static final Measure<DistanceUnit> kCameraHeight = Inches.of(4);
      public static final Measure<AngleUnit> kCameraPitch = Degrees.of(45); //23.5

      public static final Matrix<N3, N1> kSingleTagStdDevs = VecBuilder.fill(4, 4, 8);
      public static final Matrix<N3, N1> kMultiTagStdDevs = VecBuilder.fill(0.5, 0.5, 1);
    }

    public final class PivotConstants {
      // PID Constants
      public final static double KP = 1.0;
      public final static double KI = 0.0;
      public final static double KD = 0.0;

      //  ffe Constants
      public final static double KS = 5.0;
      public final static double KG = 2.0;
      public final static double KV = 0.9;
      public final static double KA = 0.1;

      // Simulation Constants
      public final static int gearing = 36;
      public final static double lengthMeters = 0.5;
      public final static double massKg = 3.0;
    }
}