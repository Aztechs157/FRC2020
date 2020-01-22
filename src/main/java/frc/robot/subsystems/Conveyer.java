/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.util.NEO;
import frc.robot.RobotContainer;

public class Conveyer extends SubsystemBase {
    public NEO conveyerMotor;

    /**
     * Creates a new Conveyer.
     */
    public Conveyer() {
        conveyerMotor = new NEO(Constants.RobotConstants.ConveyerMotorID, MotorType.kBrushless);
    }

    @Override
    public void periodic() {
        System.out.println(RobotContainer.joystick.getRawAxis(2));
        conveyerMotor.set(RobotContainer.joystick.getRawAxis(2));
    }
}
