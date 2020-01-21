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

    public final class RobotConstants {
        public static final int FrontLeft = 2;
        public static final int FrontRight = 1;
        public static final int BackLeft = 4;
        public static final int BackRight = 3;
        // public static final int IntakeRight = 5;
        public static final int ConveyerMotorID = 6;
        public static final int TurretMotorID = 7;
    }

    public final class OIConstants {
        public static final int RYStick = 5;
        public static final int LYStick = 1;
        public static final int RXStick = 4;
        public static final int LXStick = 0;
        public static final int Ybutton = 4;

    }
}
