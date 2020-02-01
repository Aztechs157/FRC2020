/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Conveyor;

public class ConveyorController extends CommandBase {

    private final Conveyor conveyor;

    /**
     * Creates a new ConveyerMotor.
     */
    public ConveyorController(final Conveyor conveyor) {
        this.conveyor = conveyor;
        addRequirements(conveyor);
    }

    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute() {
        // Robot.conveyer.conveyerMotor.set(1);
    }
}
