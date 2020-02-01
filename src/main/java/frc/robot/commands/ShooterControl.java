/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Shooter;
import frc.robot.util.LogitechController;

public class ShooterControl extends CommandBase {

    private final Shooter shooter;
    private final LogitechController controller;

    /**
     * Creates a new ShooterControl2.
     */
    public ShooterControl(final Shooter shooter, final LogitechController controller) {
        this.shooter = shooter;
        this.controller = controller;
        addRequirements(shooter);
    }

    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute() {
        double joyValx;
        double Scale;

        joyValx = -controller.getRawAxis(4);
        Scale = 0.1;
        shooter.moveShooter(joyValx * Scale);
        // RobotContainer.shooter.UpDown.set(RobotContainer.m_oi.controller2.getRawAxis(5));
        // RobotContainer.shooter.LeftRight.set(RobotContainer.m_oi.controller2.getRawAxis(4));
    }
}
