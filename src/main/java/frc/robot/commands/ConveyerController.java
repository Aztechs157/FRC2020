package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Conveyer;

public class ConveyerController extends CommandBase {

    private final Conveyer conveyer;

    /**
     * Creates a new ConveyerMotor.
     */
    public ConveyerController(final Conveyer conveyer) {
        this.conveyer = conveyer;
        addRequirements(conveyer);
    }

    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute() {
        // Robot.conveyer.conveyerMotor.set(1);
    }
}
