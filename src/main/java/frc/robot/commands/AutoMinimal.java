/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.subsystems.Drive;

public class AutoMinimal extends SequentialCommandGroup {
    /**
     * Creates a new AutoMinimal.
     */
    public AutoMinimal(Drive drive) {
        // DriveForward command = new DriveForward(16.2, true, drive);
        // command.drivepower = .1;
        // addCommands(command);
        System.out.println("Minimal");

    }
}
