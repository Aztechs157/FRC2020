package frc.robot.subsystems;

import edu.wpi.first.wpilibj.AnalogGyro;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.util.LogitechController;
import frc.robot.util.NEO;
import frc.robot.util.PID;
import frc.robot.util.SlewRate;

import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import com.ctre.phoenix.motorcontrol.InvertType;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

public class Drive extends SubsystemBase {

    private final LogitechController controller;

    public static NEO frontLeft;
    public static NEO frontRight;
    private static NEO backLeft;
    private static NEO backRight;
    private static AnalogGyro driveGyro;
    public static PID drivePID;
    public static PID gyroDrivePID;
    public static SlewRate slew;

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
     * public Drive getInstance(){ if(instance == null){ instance = new Drive(); }
     * return instance;
     *
     * }
     */

    @Override
    public void periodic() {
        // frontLeft.set(controller.getRawAxis(Constants.OIConstants.LYStick));
        // frontRight.set(controller.getRawAxis(Constants.OIConstants.RYStick));
        tankdrive();
    }

    public void tankdrive() {
        // left y axis= 1, right y axis= 5
        frontLeft.set(-controller.getRawAxis(Constants.OIConstants.LYStick));
        frontRight.set(controller.getRawAxis(Constants.OIConstants.RYStick));
        // System.out.println("drivepower = " + Autonomous.drivepower + " frontRight = "
        // + frontRight.getPosition() +"frontLeft = " + frontLeft.getPosition());
        backLeft.set(-controller.getRawAxis(Constants.OIConstants.LYStick));
        backRight.set(controller.getRawAxis(Constants.OIConstants.RYStick));
        // 12 ft = leftquad = 2717.5 Rightquad = 1430.72
        // 12 ft = leftquad = 2711.5 Rightquad = 1259.00
        // 12 ft = leftquad = 2727.25 Rightquad = 1347.75
        // 12 ft = leftquad average 2718.75 // Rightquad average = 1345.82

    }

    public static double getLeftEncoder() {
        // return RobotContainer.driveLeftQuad.getDistance();
        return frontLeft.getPosition();

    }

    public static double getRightEncoder() {
        // return RobotContainer.driveRightQuad.getDistance();
        return frontLeft.getPosition();
    }

    public static double getAngle() {
        return driveGyro.getAngle();
    }

    public static void AutoDrive(final double leftPower, final double rightPower) {
        frontLeft.set(leftPower);
        frontRight.set(rightPower);
    }

}
