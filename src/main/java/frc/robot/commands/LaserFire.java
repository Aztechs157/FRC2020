package frc.robot.commands;

import edu.wpi.first.wpilibj.Relay.Value;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Vision;

public class LaserFire extends CommandBase {

    private final boolean on;
    private final Vision vision;

    /**
     * Creates a new LaserFire2.
     */
    public LaserFire(final boolean on, final Vision vision) {
        this.on = on;
        this.vision = vision;
        addRequirements(vision);
    }

    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute() {
        if (on) {
            vision.laser.set(Value.kForward);
        } else {
            vision.laser.set(Value.kOff);
        }
    }

    @Override
    public boolean isFinished() {
        return false;
    }
}
