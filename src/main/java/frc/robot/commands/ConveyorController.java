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
