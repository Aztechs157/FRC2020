/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Drive;

public class DriveTurn extends CommandBase {

    public Drive drive;
    private double angle;
    public double drivepower;

    /**
     * Creates a new DriveTurn.
     */
    public DriveTurn(double angle, Drive drive) {
        this.angle = angle;
        this.drive = drive;
        addRequirements(drive);
        drive.drivePID.pidCalculate(83, drive.frontLeft.getPosition());
        drivepower = drive.slew.rateCalculate(drivepower, 1125);
        // Use addRequirements() here to declare subsystem dependencies.
    }

    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute() {
        // System.out.println("drivepower = ");// + drivepower);

        if (drive.getAngle() >= angle) {
            drive.frontLeft.set(.5);
            drive.backLeft.set(.5);
            drive.frontRight.set(.5);
            drive.backRight.set(.5);
        }

    }

    // Returns true when the command should end.
    @Override
    public boolean isFinished() {
        return (drive.getAngle() < angle);
    }
}
