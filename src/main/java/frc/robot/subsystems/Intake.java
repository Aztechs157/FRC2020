/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.NEO;
import frc.robot.RobotContainer;

public class Intake extends SubsystemBase {
    // public final NEO IntakeRight;
    /**
     * Creates a new Intake.
     */
    public Intake() {
        // IntakeRight = new NEO(Constants.RobotConstants.IntakeRight,
        // MotorType.kBrushless);
    }

    public void runIntake(double speed) {
        // IntakeRight.set(speed);
    }

    @Override
    public void periodic() {
        // This method will be called once per scheduler run
    }
}

// Y is button 4
