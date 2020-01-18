/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import frc.robot.Robot;

public class ShooterControl extends Command {
  int count = 0;
  public ShooterControl() {
    requires(Robot.shooter);
    // Use requires() here to declare subsystem dependencies
    // eg. requires(chassis);
  }

  // Called just before this Command runs the first time
  @Override
  protected void initialize() {
  }

  // Called repeatedly when this Command is scheduled to run
  @Override
  protected void execute() {
    double joyValx;
    double Scale;
   

    count++;
    if (count == 12) {
      System.out.println(Robot.shooter.LeftRight.getPosition());
      count = 0;
    }
    


    joyValx = -Robot.m_oi.controller2.getRawAxis(4);
    Scale = 0.1;
    Robot.shooter.moveShooter(joyValx * Scale);
    //Robot.shooter.UpDown.set(Robot.m_oi.controller2.getRawAxis(5));
    //Robot.shooter.LeftRight.set(Robot.m_oi.controller2.getRawAxis(4));
  }

  // Make this return true when this Command no longer needs to run execute()
  @Override
  protected boolean isFinished() {
    return false;
  }

  // Called once after isFinished returns true
  @Override
  protected void end() {
  }

  // Called when another command which requires one or more of the same
  // subsystems is scheduled to run
  @Override
  protected void interrupted() {
  }
}
