/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import frc.robot.util.PID;
import frc.robot.util.SlewRate;
import frc.robot.subsystems.Drive;
import edu.wpi.first.wpilibj.Timer;

public class DriveTarget {
    private final int target;
    private double startTime;
    private final double time;
    private final double targetAngle;
    private boolean slewCut;
    private double encoder;
    private double drivePower;
    private final PID drivePID;
    private final PID gyroDrivePID;
    private SlewRate slewRate;
    private double leftPower;
    private double rightPower;
    private int repsAtTarget;
    private final int tolerance;
    private boolean firstIteration;
    private boolean megaSlew = false;
    private final Drive drive;

    public DriveTarget(final Drive drive, final int target, final double targetAngle, final int tolerance,
            final double time) {
        this.drive = drive;
        this.target = target;
        this.time = time;
        this.targetAngle = targetAngle;
        this.tolerance = tolerance;
        slewCut = false;
        drivePID = new PID(0.028, 0.1, 0.000005, 10, 0, 999999, 0, 3, -3);
        gyroDrivePID = new PID(0.03, 0, 0.000002, 999999, 0, 999999, 0, 3, -3);
        slewRate = new SlewRate(0.8);
        firstIteration = true;
    }

    public DriveTarget(final Drive drive, final int target, final double targetAngle, final int tolerance,
            final double time, final boolean slew) {
        this.drive = drive;
        this.target = target;
        this.time = time;
        this.targetAngle = targetAngle;
        this.slewCut = slew;
        this.tolerance = tolerance;
        drivePID = new PID(0.028, 0.1, 0.000005, 10, 0, 999999, 0, 3, -3);
        gyroDrivePID = new PID(0.03, 0, 0.000002, 999999, 0, 999999, 0, 3, -3);
        slewRate = new SlewRate(0.5);
        slewCut = !slew;
        firstIteration = true;
    }

    public DriveTarget(final Drive drive, final int target, final double targetAngle, final int tolerance,
            final double time, final boolean slew, final boolean megaSlew) {
        this.drive = drive;
        this.target = target;
        this.time = time;
        this.targetAngle = targetAngle;
        this.slewCut = slew;
        this.tolerance = tolerance;
        drivePID = new PID(0.028, 0.1, 0.000005, 10, 0, 999999, 0, 3, -3);
        gyroDrivePID = new PID(0.03, 0, 0.000002, 999999, 0, 999999, 0, 3, -3);
        slewRate = new SlewRate(0.5);
        slewCut = !slew;
        firstIteration = true;
        this.megaSlew = megaSlew;
    }

    public boolean execute() {

        if (firstIteration) {
            startTime = Timer.getFPGATimestamp();
            slewRate = new SlewRate((!megaSlew) ? 1.6 : 0.2);
            firstIteration = false;
        }
        encoder = (drive.getRightEncoder() + drive.getLeftEncoder()) / 2.0;
        drivePower = drivePID.pidCalculate(target, encoder);

        if (!slewCut) {
            drivePower = slewRate.rateCalculate(drivePower);
        }
        if (Math.abs(drivePower) >= 0.9) {
            slewCut = true;
        }

        leftPower = drivePower - gyroDrivePID.pidCalculate(targetAngle, drive.getAngle());
        leftPower = ((leftPower > 0) ? 1 : -1) * Math.min(1, Math.abs(leftPower));

        rightPower = drivePower + gyroDrivePID.pidCalculate(targetAngle, drive.getAngle());
        rightPower = ((rightPower > 0) ? 1 : -1) * Math.min(1, Math.abs(rightPower));

        drive.autoDrive(leftPower, rightPower);
        if (Math.abs(encoder - target) < tolerance) {
            repsAtTarget++;
            if (repsAtTarget >= 5) {
                return true;
            } else {
                return false;
            }
        } else if (Timer.getFPGATimestamp() - startTime >= time) {
            return true;
        } else {
            repsAtTarget = 0;
            return false;
        }
    }
}
