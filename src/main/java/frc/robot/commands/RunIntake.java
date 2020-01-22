package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Intake;

public class RunIntake extends CommandBase {

    private final Intake intake;

    /**
     * Creates a new RunIntake.
     */
    public RunIntake(final Intake intake) {
        this.intake = intake;
        addRequirements(intake);
    }
}
