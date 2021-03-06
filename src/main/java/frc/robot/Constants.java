/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

/**
 * The Constants class provides a convenient place for teams to hold robot-wide
 * numerical or boolean constants. This class should not be used for any other
 * purpose. All constants should be declared globally (i.e. public static). Do
 * not put anything functional in this class.
 *
 * <p>
 * It is advised to statically import this class (or one of its inner classes)
 * wherever the constants are needed, to reduce verbosity.
 */
public final class Constants {

    public final class DriveConstants {
        public static final int FrontLeft = 2;
        public static final int FrontRight = 1;
        public static final int BackLeft = 4;
        public static final int BackRight = 3;
        public static final double compensationRate = 0.25;
        public static final double speedInPercent = .70;
    }

    public final class ShooterConstants {
        public static final int Intake = 5;
        // public static final int conveyorMotor = 6;
        public static final int intakeArmMotorID = 12;
        public static final int shooter = 9;
        public static final int conveyorMotor = 6;
        public static final int TurretMotorID = 8;
        public static final int kicker = 7;

        // comment
    }

    public final class ClimbingConstants {
        public static final int winchMotorId = 13;
        public static final int armMotorId = 14;
    }

    public final class ColorWheelConstants {
        public static final int colorWheelLiftMotorId = 10;
        public static final int colorWheelSpinMotorId = 11;
        // spinMotorId not on the robot yet
    }
}
