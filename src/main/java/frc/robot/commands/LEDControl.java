/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.Vision;

public class LEDControl extends CommandBase {
    private Intake intake;
    private Vision vision;

    /**
     * Creates a new LEDControl.
     */
    public LEDControl(Intake intake, Vision vision) {
        this.intake = intake;
        this.vision = vision;
        // Use addRequirements() here to declare subsystem dependencies.
        addRequirements(vision);
    }

    // Called when the command is initially scheduled.
    @Override
    public void initialize() {
    }

    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute() {
        if (intake.ballCount() == 0) {
            vision.turnLight(false);
        } else {
            vision.turnLight(true);
        }
    }

    // Called once the command ends or is interrupted.
    @Override
    public void end(boolean interrupted) {
    }

    // Returns true when the command should end.
    @Override
    public boolean isFinished() {
        return false;
    }
}
