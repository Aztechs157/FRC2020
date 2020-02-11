/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.PrintCommand;
import edu.wpi.first.wpilibj2.command.RunCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.subsystems.Drive;

public class AutoGroup extends SequentialCommandGroup {

    // A MAN HAS FALLEN INTO THE RIVER OF LEGO CITY
    /**
     * Add your docs here.
     */
    public AutoGroup(Drive drive) {
        addCommands(new DriveForward(-64.8, drive), /* new DriveTurn(-57, drive), */
                new PrintCommand(" "));
    }
}
