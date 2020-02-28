/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.ColorWheel;

public class SpinToColor extends CommandBase {
    private ColorWheel colorWheel;

    /**
     * Creates a new SpinToColor.
     */
    public SpinToColor(final ColorWheel colorWheel) {
        this.colorWheel = colorWheel;
        // Use addRequirements() here to declare subsystem dependencies.
    }

    // Called when the command is initially scheduled.
    @Override
    public void initialize() {
        colorWheel.resetStage3State();
    }

    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute() {

    }

    // Called once the command ends or is interrupted.
    @Override
    public void end(final boolean interrupted) {
        colorWheel.ticksOnColor = 0;
    }

    // Returns true when the command should end.
    @Override
    public boolean isFinished() {
        return colorWheel.spinWheelState();
    }
}
