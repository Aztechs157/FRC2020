/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import edu.wpi.first.wpilibj.AnalogGyro;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.CounterBase.EncodingType;
import edu.wpi.first.wpilibj2.command.CommandScheduler;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.NEO;
import frc.robot.PID_Wescott;
import frc.robot.Robot;
import frc.robot.RobotContainer;
import frc.robot.SlewRate;
import frc.robot.commands.Autonomous;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANDigitalInput.LimitSwitch;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import com.ctre.phoenix.motorcontrol.InvertType;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;


public class Drive extends SubsystemBase {
public Drive drive;

public static NEO frontLeft;
public static NEO frontRight;
private static NEO backLeft;
private static NEO backRight;
private static AnalogGyro driveGyro;
public static PID_Wescott drivePID;
public static PID_Wescott gyroDrivePID;
public static SlewRate slew;
  public Drive() {
    frontLeft = new NEO(Constants.DriveConstants.FrontLeft, MotorType.kBrushless);
    frontRight = new NEO(Constants.DriveConstants.FrontRight, MotorType.kBrushless);
    backLeft = new NEO(Constants.DriveConstants.BackLeft, MotorType.kBrushless);
    backRight = new NEO(Constants.DriveConstants.BackRight, MotorType.kBrushless);
    gyroDrivePID = new PID_Wescott(0.03, 0, 0.000002, 999999, 0, 999999, 0, 3, -3);
    drivePID = new PID_Wescott(.1, 0, 0, 100, 0, 100, 0, 2, -2);
    slew = new SlewRate(0.5);
    frontRight.tare();
    frontLeft.tare();
    driveGyro = new AnalogGyro(0);
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
    // System.out.println("drivepower = " + Autonomous.drivepower + " frontRight = " + frontRight.getPosition() +"frontLeft = " + frontLeft.getPosition());
    backLeft.set(-RobotContainer.joystick.getRawAxis(Constants.OIConstants.LYStick));
    backRight.set(RobotContainer.joystick.getRawAxis(Constants.OIConstants.RYStick));
// 12 ft = leftquad = 2717.5  Rightquad = 1430.72 
// 12 ft = leftquad = 2711.5  Rightquad = 1259.00
// 12 ft = leftquad = 2727.25 Rightquad = 1347.75
// 12 ft = leftquad average 2718.75 // Rightquad average = 1345.82



}

public static double getLeftEncoder() {
  //return RobotContainer.driveLeftQuad.getDistance();
  return frontLeft.getPosition();

}

public static double getRightEncoder() {
//	return RobotContainer.driveRightQuad.getDistance();
return frontLeft.getPosition();
}

public static double getAngle() {
	return driveGyro.getAngle();
}

public static void AutoDrive(double leftPower, double rightPower) {
  frontLeft.set(leftPower);
  frontRight.set(rightPower);
}


}