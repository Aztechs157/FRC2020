/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.RobotContainer;
import frc.robot.subsystems.Drive;

public class Autonomous extends CommandBase {
    // DriveTarget target = new DriveTarget(2718, 0, 50, 15.0);
    /**
     * Creates a new Autonomous.
     */
    public Autonomous() {
        // Use addRequirements() here to declare subsystem dependencies.
    }

    // Called when the command is initially scheduled.
    @Override
    public void initialize() {
    }

    // Called every time the scheduler runs while the command is scheduled.
    // 2718.75
    @Override
    public void execute() {
        if (Drive.frontLeft.getPosition() <= 75.83) {
            double drivepower = Drive.drivePID.pidCalculate(75.83, Drive.frontLeft.getPosition());
            // drivepower = Drive.slew.rateCalculate(drivepower);
            double gyropower = 0;// Drive.gyroDrivePID.pidCalculate(0, Drive.getAngle());
            Drive.frontLeft.set((drivepower - gyropower));
            Drive.frontRight.set(-(drivepower + gyropower));
            System.out.println("drivepower = " + drivepower);
        }
    }

    // Called once the command ends or is interrupted.
    @Override
    public void end(boolean interrupted) {
    }

    // Returns true when the command should end.
    @Override
    public boolean isFinished() {
        return Drive.frontLeft.getPosition() >= 75.83;
    }
}
