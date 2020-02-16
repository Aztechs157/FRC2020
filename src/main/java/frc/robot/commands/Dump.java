/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Conveyor;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.Kicker;

public class Dump extends CommandBase {
    /**
     * Creates a new Dump.
     */
    private Intake intake;
    private Conveyor conveyor;
    private Kicker kicker;

    public Dump(Intake intake, Conveyor conveyor, Kicker kicker) {
        this.intake = intake;
        this.conveyor = conveyor;
        this.kicker = kicker;
        addRequirements(conveyor);
    }

    // Called when the command is initially scheduled.
    @Override
    public void initialize() {
        intake.zeroBallCount();
        conveyor.resetStateMachine();
    }

    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute() {
        intake.allowIntake = false;
        intake.runSpeed(-0.60);
        conveyor.runSpeed(-0.20);
        kicker.runSpeed(-0.05);
    }

    // Called once the command ends or is interrupted.
    @Override
    public void end(boolean interrupted) {
        intake.allowIntake = true;
        intake.stop();
        conveyor.stop();
        kicker.stop();
    }

    // Returns true when the command should end.
    @Override
    public boolean isFinished() {
        return false;
    }
}
