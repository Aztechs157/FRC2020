/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import frc.robot.Robot;
import frc.robot.Pixy2Controller.Target;

public class TrackTarget extends Command {
  double map(double x, double in_min, double in_max, double out_min, double out_max)
  {
    return (x - in_min) * (out_max - out_min) / (in_max - in_min) + out_min;
  }
  private final double LRMUL = 0.00001;
  private final double UDMUL = 0.004;
  private final double LRTARGET = 158;
  int count = 0;
  public TrackTarget() {
    // Use requires() here to declare subsystem dependencies
    // eg. requires(chassis);
    requires(Robot.shooter);
  }

  // Called just before this Command runs the first time
  @Override
  protected void initialize() {
    Robot.vision.turnLight(true);
    count = 0;
  }

  // Called repeatedly when this Command is scheduled to run
  @Override
  protected void execute() {
    Target[] targets = Robot.vision.getBlocks();
    System.out.println(targets.length);
    if (targets.length == 1)
    {
      count = 0;
      //System.out.println(Robot.vision.LR+(-(LRTARGET-targets[0].x))*LRMUL);
      Robot.shooter.moveShooter(Robot.vision.pid.pidCalculate(LRTARGET, targets[0].x)*0.1);
      Robot.vision.setVertical(map(targets[0].y, 0, 207, 0.65, 0.4));

    }
    else
    {
      count++;
      //Robot.vision.setHorizontal(0);
    }

  }

  // Make this return true when this Command no longer needs to run execute()
  @Override
  protected boolean isFinished() {
    boolean retVal = true;
    double joyValx;
    
    joyValx = Robot.m_oi.controller2.getRawAxis(4);

    if (joyValx > -0.01 && joyValx < 0.01) {
      retVal = false;
    }
    if (count >= 40)
    {
      retVal = true;
    }
    return retVal;
  }

  // Called once after isFinished returns true
  @Override
  protected void end() {
    
    Robot.vision.setHorizontal(0);
    Robot.vision.turnLight(false);
  }

  // Called when another command which requires one or more of the same
  // subsystems is scheduled to run
  @Override
  protected void interrupted() {
    
    Robot.vision.setHorizontal(0);
    Robot.vision.turnLight(false);
  }
}
