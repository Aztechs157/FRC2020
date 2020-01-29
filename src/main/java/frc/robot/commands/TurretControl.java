package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Turret;
import frc.robot.util.LogitechController;

public class TurretControl extends CommandBase {

    private double count = 0;
    private final Turret shooter;
    private final LogitechController controller;

    /**
     * Creates a new ShooterControl2.
     */
    public TurretControl(final Turret shooter, final LogitechController controller) {
        this.shooter = shooter;
        this.controller = controller;
        addRequirements(shooter);
    }

    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute() {
        double joyValx;
        double Scale;

        count++;
        if (count == 12) {
            // System.out.println(shooter.LeftRight.getPosition());
            count = 0;
        }

        joyValx = -controller.getRawAxis(4);
        Scale = 0.1;
        shooter.moveShooter(joyValx * Scale);
        // RobotContainer.shooter.UpDown.set(RobotContainer.m_oi.controller2.getRawAxis(5));
        // RobotContainer.shooter.LeftRight.set(RobotContainer.m_oi.controller2.getRawAxis(4));
    }
}
