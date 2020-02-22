/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.subsystems.Drive;

public class AutoLeft extends SequentialCommandGroup {
    /**
     * Creates a new AutoLeft.
     */
    public AutoLeft(Drive drive) {
        DriveTurn command = new DriveTurn(45, drive);
        addCommands(command);
        System.out.println("Left");

    }
}
