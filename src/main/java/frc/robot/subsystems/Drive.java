/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import edu.wpi.first.wpilibj.AnalogGyro;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.util.LogitechController;
import frc.robot.util.NEO;
import frc.robot.util.PID;
import frc.robot.util.SlewRate;

import com.revrobotics.CANSparkMaxLowLevel.MotorType;

public class Drive extends SubsystemBase {

    private final LogitechController controller;

    public NEO frontLeft;
    private NEO frontRight;
    private NEO backLeft;
    private NEO backRight;
    private AnalogGyro driveGyro;
    public PID drivePID;
    public PID gyroDrivePID;
    public SlewRate slew;
    private double leftPower = 0;
    private double rightPower = 0;
    public double drivePower;
    private double initAngle = 0;

    public Drive(final LogitechController controller) {
        this.controller = controller;
        frontLeft = new NEO(Constants.DriveConstants.FrontLeft, MotorType.kBrushless);
        frontRight = new NEO(Constants.DriveConstants.FrontRight, MotorType.kBrushless);
        backLeft = new NEO(Constants.DriveConstants.BackLeft, MotorType.kBrushless);
        backRight = new NEO(Constants.DriveConstants.BackRight, MotorType.kBrushless);
        gyroDrivePID = new PID(0.03, 0, 0.000002, 999999, 0, 999999, 0, 3, -3);
        drivePID = new PID(.1, 0, 0, 100, 0, 100, 0, 2, -2);
        slew = new SlewRate(0.5);
        frontRight.tare();
        frontLeft.tare();
        driveGyro = new AnalogGyro(0);

        // frontRight.setInverted(InvertType.InvertMotorOutput);
        // 1backRight.setInverted(InvertType.FollowMaster);
    }
    /*
     * private Drive getInstance(){ if(instance == null){ instance = new Drive(); }
     * return instance;
     *
     * }
     */

    @Override
    public void periodic() {
        // frontLeft.set(controller.getRawAxis(Constants.OIConstants.LYStick));
        // frontRight.set(controller.getRawAxis(Constants.OIConstants.RYStick));
        tankdrive();
        // System.out.println("getleftencoder" + frontLeft.getPosition());
        // System.out.println("gyropower = " + getAngle());
    }

    public void tankdrive() {
        // left y axis= 1, right y axis= 5
        frontLeft.set(-controller.getRawAxis(Constants.OIConstants.LYStick));
        frontRight.set(controller.getRawAxis(Constants.OIConstants.RYStick));
        backLeft.set(-controller.getRawAxis(Constants.OIConstants.LYStick));
        backRight.set(controller.getRawAxis(Constants.OIConstants.RYStick));
        // System.out.println("Giro angle:" + driveGyro.getAngle());
        // 12 ft = leftquad = 2717.5 Rightquad = 1430.72
        // 12 ft = leftquad = 2711.5 Rightquad = 1259.00
        // 12 ft = leftquad = 2727.25 Rightquad = 1347.75
        // 12 ft = leftquad average 2718.75 // Rightquad average = 1345.82

    }

    public double getLeftEncoder() {
        // return RobotContainer.driveLeftQuad.getDistance();
        return frontLeft.getPosition();

    }

    public double getRightEncoder() {
        // return RobotContainer.driveRightQuad.getDistance();
        return frontLeft.getPosition();
    }

    public double getAngle() {
        return driveGyro.getAngle();
    }

    public void AutoDrive(final double leftPower, final double rightPower) {
        // System.out.println("frontleft.getposision = " + frontLeft.getPosition());
        if (frontLeft.getPosition() <= 81.83) {
            // 81.83 is 12 feet

            // drivepower *= 0.5;
            frontLeft.set(leftPower); // + gyropower));
            frontRight.set(rightPower); // gyropower);
            backLeft.set(leftPower);
            backRight.set(rightPower);

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

        }

    }

    public void autoTurn(double speed) {
        // leftPower = drivePower - gyroDrivePID.pidCalculate(initAngle, getAngle());
        // leftPower = ((leftPower > 0) ? 1 : -1) * Math.min(1, Math.abs(leftPower));

        // rightPower = drivePower + gyroDrivePID.pidCalculate(initAngle, getAngle());
        // rightPower = ((rightPower > 0) ? 1 : -1) * Math.min(1, Math.abs(rightPower));
        int count = 0;
        /*
         * while (count <= 100) { System.out.println(getAngle()); count++; }
         */

        if (getAngle() >= -60) {
            frontLeft.set(speed);
            backLeft.set(speed);
            frontRight.set(speed);
            backRight.set(speed);
        }
    }
}
