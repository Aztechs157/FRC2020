/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.CounterBase.EncodingType;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.PID_Wescott;
import frc.robot.Robot;
import frc.robot.RobotContainer;

import com.ctre.phoenix.motorcontrol.InvertType;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;


public class Drive extends SubsystemBase {
public Drive drive;

public static WPI_TalonSRX frontLeft;
public static WPI_TalonSRX frontRight;
private static WPI_TalonSRX backLeft;
private static WPI_TalonSRX backRight;
public static PID_Wescott drivePID;

  public Drive() {
    frontLeft = new WPI_TalonSRX(Constants.DriveConstants.FrontLeft);
    frontRight = new WPI_TalonSRX(Constants.DriveConstants.FrontRight);
    backLeft = new WPI_TalonSRX(Constants.DriveConstants.BackLeft);
    backRight = new WPI_TalonSRX(Constants.DriveConstants.BackRight);
    drivePID = new PID_Wescott(.0625, 0, 0, 100, 0, 100, 0, 2, -2);
    backRight.follow(frontRight);
    backLeft.follow(frontLeft);
   // frontRight.setInverted(InvertType.InvertMotorOutput);
    //1backRight.setInverted(InvertType.FollowMaster);
  }
  /*public Drive getInstance(){
    if(instance == null){
      instance = new Drive();
    }
    return instance;

  }*/

  @Override
  public void periodic() {
    //frontLeft.set(RobotContainer.joystick.getRawAxis(Constants.OIConstants.LYStick));
    //frontRight.set(RobotContainer.joystick.getRawAxis(Constants.OIConstants.RYStick));
    tankdrive();
  }

  public static void tankdrive() {
   //left y axis= 1, right y axis= 5
    frontLeft.set(-RobotContainer.joystick.getRawAxis(Constants.OIConstants.LYStick));
    frontRight.set(RobotContainer.joystick.getRawAxis(Constants.OIConstants.RYStick));
    System.out.println("driveLeftQuad = " + RobotContainer.driveLeftQuad.getDistance() +"driveRightQuad = " + RobotContainer.driveRightQuad.getDistance());
// 12 ft = leftquad = 2717.5  Rightquad = 1430.72 
// 12 ft = leftquad = 2711.5  Rightquad = 1259.00
// 12 ft = leftquad = 2727.25 Rightquad = 1347.75
// 12 ft = leftquad average 2718.75 // Rightquad average = 1345.82



}

public static double getLeftEncoder() {
  return RobotContainer.driveLeftQuad.getDistance();

}

public static double getRightEncoder() {
	return RobotContainer.driveRightQuad.getDistance();
}

public static double getAngle() {
	return RobotContainer.driveGyro.getAngle();
}

public static void AutoDrive(double leftPower, double rightPower) {
  frontLeft.set(leftPower);
  frontRight.set(rightPower);
}


}
