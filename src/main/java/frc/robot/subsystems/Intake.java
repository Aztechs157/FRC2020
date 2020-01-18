/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.NEO;
import frc.robot.commands.IntakeTrigger;

public class Intake extends SubsystemBase {
  /**
   * Creates a new Intake2.
   */
  
  public NEO motor;
  public Intake() {
    //motor = new NEO(4, MotorType.kBrushless);
    //setDefaultCommand(new IntakeTrigger());
  }

  @Override
  public void periodic() {
    // This method will be called once per scheduler run
  }
}
