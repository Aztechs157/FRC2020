/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.commands.TeleopDrive;
import frc.robot.util.PID;
import frc.robot.util.SlewRate;
import frc.robot.util.controllers.ControllerSet;
import frc.robot.util.controllers.LogitechController;
import frc.robot.util.controllers.PlaneController;

import com.revrobotics.CANEncoder;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

public class Drive extends SubsystemBase {

    private final ControllerSet controller;

    public final CANSparkMax frontLeft = new CANSparkMax(Constants.DriveConstants.FrontLeft, MotorType.kBrushless);
    public final CANSparkMax frontRight = new CANSparkMax(Constants.DriveConstants.FrontRight, MotorType.kBrushless);
    public final CANSparkMax backLeft = new CANSparkMax(Constants.DriveConstants.BackLeft, MotorType.kBrushless);
    public final CANSparkMax backRight = new CANSparkMax(Constants.DriveConstants.BackRight, MotorType.kBrushless);

    public final CANEncoder flEncoder = frontLeft.getEncoder();
    public final CANEncoder frEncoder = frontRight.getEncoder();
    public final CANEncoder blEncoder = backLeft.getEncoder();
    public final CANEncoder brEncoder = backRight.getEncoder();

    public final PID drivePID = new PID(1.35, 0, 0, 100, 0, 100, 0, 2, -2);
    public final PID gyroDrivePID = new PID(0.055, 0, 0.000002, 999999, 0, 999999, 0, 3, -3);
    public final SlewRate slew = new SlewRate(0.5);

    public final SPI.Port kGyroPort = SPI.Port.kOnboardCS0;
    public final ADXRS450_Gyro driveGyro = new ADXRS450_Gyro(kGyroPort);
    public static SlewRate leftSlew = new SlewRate(1.2);
    public static SlewRate rightSlew = new SlewRate(1.2);
    public static double globalDeadZone = 0.07;

    public boolean isArcade = Preferences.getInstance().getBoolean("useArcade", false);

    // public final double drivepower = leftSlew.rateCalculate(1);
    // public final AnalogInput driveGyro = new
    // AnalogInput(Constants.DriveConstants.driveGyro);

    public Drive(final ControllerSet controller) {
        this.controller = controller;
        driveGyro.calibrate();
        flEncoder.setPosition(0);
        frEncoder.setPosition(0);
        brEncoder.setPosition(0);
        blEncoder.setPosition(0);
        driveGyro.reset();
        setDefaultCommand(new TeleopDrive(this));

        flEncoder.setPositionConversionFactor(2.105);
        frEncoder.setPositionConversionFactor(2.105);
        blEncoder.setPositionConversionFactor(2.105);
        brEncoder.setPositionConversionFactor(2.105);
        // Shuffleboard.getTab("Test").add("Gyro", driveGyro);
        // frontRight.setInverted(InvertType.InvertMotorOutput);
        // 1backRight.setInverted(InvertType.FollowMaster);

        frontLeft.setInverted(true);
        backLeft.setInverted(true);
    }

    public void setCoastMode() {
        frontLeft.setIdleMode(IdleMode.kCoast);
        frontRight.setIdleMode(IdleMode.kCoast);
        backLeft.setIdleMode(IdleMode.kCoast);
        backRight.setIdleMode(IdleMode.kCoast);

    }

    public void setBrakeMode() {
        frontLeft.setIdleMode(IdleMode.kBrake);
        frontRight.setIdleMode(IdleMode.kBrake);
        backLeft.setIdleMode(IdleMode.kBrake);
        backRight.setIdleMode(IdleMode.kBrake);
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
        // System.out.println("getleftencoder" + frontLeft.getPosition());
        // System.out.println("gyropower = " + getAngle());
        // System.out.println("FL " + frontLeft.getPosition() + " BL " +
        // backLeft.getPosition() + " FR "
        // + frontRight.getPosition() + " BR " + backRight.getPosition());
        // System.out.println("back left = " + backLeft.getPosition());
        // System.out.println("front left = " + frontLeft.getPosition());
        // System.out.println("back right = " + backLeft.getPosition());
        // System.out.println("Front right = " + frontRight.getPosition());

        // front left tests
        // test 1 -63
        // test 2 -68
        // test 3 -63.14
        // test 4 -67
        // -73
        // 9 units is 20 inches 12 feet is 63.6
        // R 62
        // L -73
        // System.out.println("LeftFrontEncoder = " + frontLeft.getPosition());
        // System.out.println("RightFrontEncoder = " + frontRight.getPosition());
        // System.out.println("LeftBackEncoder = " + backLeft.getPosition());
        // System.out.println("RightBackEncoder = " + backRight.getPosition());
    }

    public void tankdrive() {

        var leftVal = -controller.useAxis(LogitechController.LEFT_STICK_Y, PlaneController.LEFT_HAND_STICK_Y);
        var rightVal = -controller.useAxis(LogitechController.RIGHT_STICK_Y, PlaneController.RIGHT_HAND_STICK_Y);
        leftVal = driveMap(leftVal, globalDeadZone);
        rightVal = driveMap(rightVal, globalDeadZone);
        // left y axis= 1, right y axis= 5
        frontLeft.set(leftSlew.rateCalculate(leftVal));
        frontRight.set(rightSlew.rateCalculate(rightVal));
        // System.out.println("drivepower = " + Autonomous.drivepower + " frontRight = "
        // System.out.println("Encoder= " + frontLeft.getPosition());
        // System.out.println("GyroPrint = " + getAngle());

        // + frontRight.getPosition() +"frontLeft = " + frontLeft.getPosition());
        backLeft.set(leftSlew.rateCalculate(leftVal));
        backRight.set(rightSlew.rateCalculate(rightVal));
        // System.out.println("Giro angle:" + driveGyro.getAngle());
        // 12 ft = leftquad = 2717.5 Rightquad = 1430.72
        // 12 ft = leftquad = 2711.5 Rightquad = 1259.00
        // 12 ft = leftquad = 2727.25 Rightquad = 1347.75
        // 12 ft = leftquad average 2718.75 // Rightquad average = 1345.82

    }

    public void arcadedrive(boolean isSingleStick) {
        // Y is inverted auto by driverstation so have it uninverted
        var yVal = -controller.useAxis(LogitechController.LEFT_STICK_Y, PlaneController.LEFT_HAND_STICK_Y);
        yVal = driveMap(yVal, globalDeadZone);
        // Use rightX in dual, leftX in single
        var xVal = (isSingleStick)
                ? controller.useAxis(LogitechController.LEFT_STICK_X, PlaneController.LEFT_HAND_STICK_X)
                : controller.useAxis(LogitechController.RIGHT_STICK_X, PlaneController.RIGHT_HAND_STICK_X);
        xVal = driveMap(xVal, globalDeadZone);

        // Calculate into tank drive form
        var leftVal = minmax(-1, yVal + xVal, 1);
        var rightVal = minmax(-1, yVal - xVal, 1);

        // Use the calculated vals
        frontLeft.set(leftSlew.rateCalculate(leftVal));
        frontRight.set(rightSlew.rateCalculate(rightVal));
        backLeft.set(leftSlew.rateCalculate(leftVal));
        backRight.set(rightSlew.rateCalculate(rightVal));
    }

    public double minmax(double min, double mid, double max) {
        if (min > mid)
            return min;
        if (mid > max)
            return max;
        return mid;
    }

    public double driveMap(double in, double deadZone) {
        if (in <= deadZone && in >= -deadZone) {
            return 0;
        } else if (in < deadZone) {
            return map(in, -1, -deadZone, -1, 0);
        } else {
            return map(in, deadZone, 1, 0, 1);
        }
    }

    private double map(final double x, final double in_min, final double in_max, final double out_min,
            final double out_max) {
        return (x - in_min) * (out_max - out_min) / (in_max - in_min) + out_min;
    }

    // public void arcadedrive() {
    // // SmartDashboard.putNumber("LXValue",
    // // controller.getRawAxis(Constants.OIConstants.LXStick));
    // var yVal = -controller.getLeftStickY();
    // var xVal = controller.getRightStickX();
    // // if (yVal > .05 || yVal < 0.5) {
    // frontLeft.set(leftSlew.rateCalculate(yVal));
    // frontRight.set(rightSlew.rateCalculate(yVal));
    // backLeft.set(leftSlew.rateCalculate(yVal));
    // backRight.set(rightSlew.rateCalculate(yVal));
    // // }
    // // if (xVal > .05 || xVal < 0.5) {
    // frontLeft.set(leftSlew.rateCalculate(xVal));
    // frontRight.set(rightSlew.rateCalculate(xVal));
    // backLeft.set(leftSlew.rateCalculate(xVal));
    // backRight.set(rightSlew.rateCalculate(xVal));
    // // }
    // }

    public double getLeftEncoder() {
        // return RobotContainer.driveLeftQuad.getDistance();
        return flEncoder.getPosition();

    }

    public double getRightEncoder() {
        // return RobotContainer.driveRightQuad.getDistance();
        return frEncoder.getPosition();
    }

    public double getAngle() {
        return driveGyro.getAngle();
    }

    public void autoDrive(final double leftPower, final double rightPower) {
        // System.out.println("frontleft.getposision = " + frontLeft.getPosition());
    }
}
