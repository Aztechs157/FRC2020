/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Intake;

public class IntakeTrigger extends CommandBase {

    private final Intake intake;

    /**
     * Creates a new IntakeTrigger2.
     */
    public IntakeTrigger(final Intake intake) {
        this.intake = intake;
        addRequirements(intake);
    }

    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute() {
        // Robot.intake.Intake.set(Robot.m_oi.controller1.getRawAxis(2));
    }
}
