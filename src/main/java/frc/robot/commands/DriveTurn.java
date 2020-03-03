/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

// import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Drive;
import frc.robot.util.PID;

public class DriveTurn extends CommandBase {

    public Drive drive;
    private double angle;
    public double drivepower;

    public final PID gyroTurnPID = new PID(.0092, 0, .004, 0, 0, 0, 0, 0, 0);

    // original P is 0.0022
    /**
     * Creates a new DriveTurn.
     */
    public DriveTurn(double angle, Drive drive) {
        this.angle = angle;
        this.drive = drive;
        addRequirements(drive);
        // Use addRequirements() here to declare subsystem dependencies.
        // Shuffleboard.getTab("Test").addNumber("Raw Gyro Angle", drive::getAngle);
        // Shuffleboard.getTab("Test").addNumber("Times Ran", () -> timesRan);
    }

    @Override
    public void initialize() {
        drive.driveGyro.reset();
    }

    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute() {
        if (drive.getAngle() <= angle) {
            double power = .5 * gyroTurnPID.pidCalculate(angle, drive.getAngle());
            drive.frontLeft.set(power);
            drive.backLeft.set(power);
            drive.frontRight.set(-power);
            drive.backRight.set(-power);
        }

    }

    // Returns true when the command should end.
    @Override
    public boolean isFinished() {
        return (drive.getAngle() > angle);
    }

    @Override
    public void end(boolean interrupted) {
        drive.frontLeft.set(0);
        drive.backLeft.set(0);
        drive.frontRight.set(0);
        drive.backRight.set(0);
    }
}
