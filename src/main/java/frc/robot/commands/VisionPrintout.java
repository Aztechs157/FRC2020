/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj.command.Command;
import frc.robot.Pixy2Controller;
import frc.robot.Robot;
//import frc.robot.subsystems.VisionTest;

/**
 * An example command.  You can replace me with your own command.
 */
public class VisionPrintout extends Command {
  public VisionPrintout() {
    // Use requires() here to declare subsystem dependencies
    requires(Robot.vision);
  }

  // Called just before this Command runs the first time
  @Override
  protected void initialize() {
  }

  // Called repeatedly when this Command is scheduled to run
  @Override
  protected void execute() {
    System.out.println("test");
    Pixy2Controller.Target[] targets = Robot.vision.getBlocks();
    System.out.println(targets.length);
    for (int i = 0; i < targets.length; i++) {
      Pixy2Controller.Target n = targets[i];
      System.out.println("target "+i+": ");
      System.out.println("color: "+n.sig);
      System.out.println("pos: ("+n.x+", "+n.y+")");
      System.out.println("WxH: "+n.width+"x"+n.height);
    }
  }

  // Make this return true when this Command no longer needs to run execute()
  @Override
  protected boolean isFinished() {
    return true;
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
