package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.RobotContainer;
import frc.robot.subsystems.Shooter;

public class ShooterControl extends CommandBase {

    private double count = 0;
    private final Shooter shooter;

    /**
     * Creates a new ShooterControl2.
     */
    public ShooterControl(final Shooter shooter) {
        this.shooter = shooter;
        addRequirements(shooter);
    }

    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute() {
        double joyValx;
        double Scale;

        count++;
        if (count == 12) {
            System.out.println(shooter.LeftRight.getPosition());
            count = 0;
        }

        joyValx = -RobotContainer.joystick.getRawAxis(4);
        Scale = 0.1;
        shooter.moveShooter(joyValx * Scale);
        // RobotContainer.shooter.UpDown.set(RobotContainer.m_oi.controller2.getRawAxis(5));
        // RobotContainer.shooter.LeftRight.set(RobotContainer.m_oi.controller2.getRawAxis(4));
    }
}
