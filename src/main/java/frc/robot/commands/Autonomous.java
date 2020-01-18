/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.RobotContainer;
import frc.robot.subsystems.Drive;

public class Autonomous extends CommandBase {
  DriveTarget target = new DriveTarget(2718, 0, 50, 15.0);
  /**
   * Creates a new Autonomous.
   */
  public Autonomous() {
    // Use addRequirements() here to declare subsystem dependencies.
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {
  }

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
  /*  if(RobotContainer.driveLeftQuad.getDistance() <= 2718.75 ){
      Drive.frontLeft.set(Drive.drivePID.pidCalculate(2718.75, RobotContainer.driveLeftQuad.getDistance())*0.25);
      Drive.frontRight.set(-Drive.drivePID.pidCalculate(2718.75, RobotContainer.driveLeftQuad.getDistance())*0.35);
    } */
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return target.execute();
  }
}
