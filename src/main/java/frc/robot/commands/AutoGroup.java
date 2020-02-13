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

    public enum autoMode {
        rightSide, leftSide, middleField, offLine
    }

    public final autoMode autoArray[] = { autoMode.rightSide, autoMode.middleField, autoMode.leftSide,
            autoMode.offLine };

    // public final autoMode getautoMode() {
    // return autoArray[getPotVal()];
    // }

    public AutoGroup(Drive drive) {
        addCommands(new DriveForward(-64.8, false, drive)); /* new DriveTurn(-57, drive), */

        // -64.8
        // switch (autoMode) {
        // case rightSide:
        // addSequential(new rightSide());
        // break;

        // case middleField:
        // addSequential(new middleField());
        // break;

        // case leftSide:
        // addSequential(new leftSide());
        // break;

        // case offLine:
        // addSequential(new offline());

        // }
    }

}
