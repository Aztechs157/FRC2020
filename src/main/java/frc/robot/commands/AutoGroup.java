/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.subsystems.Drive;

public class AutoGroup extends SequentialCommandGroup {
    // A MAN HAS FALLEN INTO THE RIVER OF LEGO CITY

    public enum AutoOptions {
        StartShootFar, StartShootMid, StartShootClose, Minimal
    }
    // start shoot is going in front of the target in autonimous and shooting in our
    // target, points doubled if in auto mode

    public AutoGroup(AutoOptions type, Drive drive) {

        // bill wants shuffle board to go and change where we go using a slider
        switch (type) {

        case StartShootFar:
            // add shoot code
            addCommands(new DriveForward(16.2, true, drive), new DriveTurn(-90, drive),
                    new DriveForward(32.4, true, drive)/* , new DriveTurn(-90, drive), new AutoDriveAndShoot(drive) */);
            System.out.println("Far done");

            break;

        case StartShootMid:
            // add shoot code
            addCommands(new DriveForward(16.2, true, drive), new DriveTurn(-90, drive),
                    new DriveForward(21.6, true, drive), new DriveTurn(-90, drive));
            System.out.println("Mid close");

            break;

        case StartShootClose:
            // Start facing backwards
            // add shoot code before drive foward
            /* addCommands(new DriveForward(-16.2, true, drive), */// new AutoShootAndDrive(drive);
            System.out.println("Close done");
            break;

        }

        // 64.8 is 12 feet, we don'nt want to run into a wall for the second time
        // 32.4 is 6 feet
        // 16.2 is 3 feet
        // 27 is 5 feet
        // 21.6 is 4 feet
    }

}
