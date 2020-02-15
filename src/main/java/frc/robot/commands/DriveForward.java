/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.util.Units;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Constants;
import frc.robot.Constants.DriveConstants;
import frc.robot.subsystems.Drive;
import com.ctre.phoenix.motorcontrol.NeutralMode;
// import jdk.nashorn.internal.ir.IfNode;

public class DriveForward extends CommandBase {
    private final Drive drive;
    private double drivepower;
    public double gyropower;
    private final double units;
    double kP = .1;
    double heading;
    private boolean gyroEnabled;

    /**
     * Creates a new DriveForward.
     */
    public DriveForward(final double units, final boolean gyroEnabled, final Drive drive) {
        this.units = units;
        this.gyroEnabled = gyroEnabled;
        this.drive = drive;

        addRequirements(drive);
        // Use addRequirements() here to declare subsystem dependencies.
        drivepower = drive.drivePID.pidCalculate(64.8, drive.frontLeft.getPosition());
        // drivepower = drive.slew.rateCalculate(drivepower, 1125); // 1125
        // gyropower = drive.gyroDrivePID.pidCalculate(0, drive.getAngle());

    }

    // Called when the command is initially scheduled.
    @Override
    public void initialize() {
        heading = drive.getAngle();
        drive.frontRight.tare();
        drive.frontLeft.tare();
        drive.backRight.tare();
        drive.backLeft.tare();
    }

    double count = 0;

    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute() {
        // System.out.println("execute driveforward");
        double angleChange = heading - drive.getAngle();
        SmartDashboard.putNumber("angleChange", angleChange);
        SmartDashboard.putNumber("frontLeftSpeed", drive.frontLeft.getPosition());
        SmartDashboard.putNumber("frontRightSpeed", drive.frontRight.getPosition());
        SmartDashboard.putNumber("backLeftSpeed", drive.backLeft.getPosition());
        SmartDashboard.putNumber("backRightSpeed", drive.backRight.getPosition());
        // 81.83 is 12 feet
        // System.out.println("gyro =" + drive.getAngle());

        final double angle = drive.getAngle();
        /*
         * if (count++ > 49) { count = 0; String angleDirection = angle < 0 ? "left" :
         * angle > 0 ? "right" : "straight"; SmartDashboard.putString("angleDirection",
         * angleDirection); SmartDashboard.putNumber("drive power", drivepower);
         * SmartDashboard.putNumber("angle", angle);
         */

        // if (angleChange > 3) {
        // drive.frontLeft.set(Drive.leftSlew.rateCalculate(drivepower - angleChange));
        // drive.backLeft.set(Drive.leftSlew.rateCalculate(drivepower - angleChange));
        // drive.frontRight.set(-Drive.rightSlew.rateCalculate(drivepower +
        // angleChange));
        // drive.backRight.set(-Drive.rightSlew.rateCalculate(drivepower +
        // angleChange));

        // }
        // drivepower = .8;
        // else {
        // drive.frontLeft.set(Drive.leftSlew.rateCalculate(drivepower));
        // drive.backLeft.set(Drive.leftSlew.rateCalculate(drivepower));
        // drive.frontRight.set(-Drive.rightSlew.rateCalculate(drivepower));
        // drive.backRight.set(-Drive.rightSlew.rateCalculate(drivepower));

        // }

        // drive.frontRight.set(-(drivepower + kP * angleChange));
        // drive.backRight.set(-(drivepower + kP * angleChange));
        // drive.frontLeft.set(drivepower + kP * angleChange);
        // drive.backLeft.set(drivepower + kP * angleChange);
        // }
        if (gyroEnabled == true) {

            if (Math.abs(angleChange) > 2) {
                // Left
                // changed to right
                // System.out.println("Robot Has Drifted");
                if (angleChange > 0) {
                    // Drifting Right
                    drive.frontRight.set(Constants.DriveConstants.compensationRate);
                    drive.backRight.set(Constants.DriveConstants.compensationRate);
                    drive.frontLeft.set(Drive.leftSlew.rateCalculate(drivepower));
                    drive.backLeft.set(Drive.leftSlew.rateCalculate(drivepower));
                    System.out.println("left");
                } else if (angleChange < 0) {
                    // Drifting Left
                    drive.frontLeft.set(Constants.DriveConstants.compensationRate);
                    drive.backLeft.set(Constants.DriveConstants.compensationRate);
                    drive.frontRight.set(Drive.rightSlew.rateCalculate(drivepower));
                    drive.backRight.set(Drive.rightSlew.rateCalculate(drivepower));
                    System.out.println("right");
                }
            } else {
                drive.frontLeft.set(Drive.leftSlew.rateCalculate(drivepower));
                drive.backLeft.set(Drive.leftSlew.rateCalculate(drivepower));
                drive.frontRight.set(Drive.rightSlew.rateCalculate(drivepower));
                drive.backRight.set(Drive.rightSlew.rateCalculate(drivepower));

            }
        } else {
            // Forward

            // System.out.println("Forward");

            drive.frontLeft.set(Drive.leftSlew.rateCalculate(drivepower));
            drive.backLeft.set(Drive.leftSlew.rateCalculate(drivepower));
            drive.frontRight.set(Drive.rightSlew.rateCalculate(drivepower));
            drive.backRight.set(Drive.rightSlew.rateCalculate(drivepower));
            // System.out.println("working");
        }
        // drive.frontLeft.setNeutralMode(NeutralMode.Brake);
        // drive.frontRight.setNeutralMode(NeutralMode.Brake);
        // drive.backLeft.setNeutralMode(NeutralMode.Brake);
        // drive.backRight.setNeutralMode(NeutralMode.Brake);
    }

    // frontLeft.set(1); // + gyropower));
    // frontRight.set(-(1)); // gyropower);
    // backLeft.set(1);
    // backRight.set(-(1));
    // System.out.println("GyroPrint = " + getAngle());
    // System.out.println("Sigma = " + drivePID.sigma);
    // System.out.println("drivepower = " + drivepower);
    // System.out.println("gyropower = " + gyropower);
    // System.out.println("left = " + backLeft.getVelocity());
    // System.out.println("right = " + backRight.getVelocity());

    // Called once the command ends or is interrupted.
    @Override
    public void end(final boolean interrupted) {
        System.out.println("Interupted");
        drive.frontLeft.set(0);
        drive.backLeft.set(0);
        drive.frontRight.set(0);
        drive.backRight.set(0);
    }

    // Returns true when the command should end.
    @Override
    public boolean isFinished() {
        return (drive.frontLeft.getPosition() > units);

        // return false;
    }
}
