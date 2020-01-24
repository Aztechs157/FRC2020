/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.util.LogitechController;
import frc.robot.util.NEO;

public class Conveyor extends SubsystemBase {

    private final NEO conveyerMotor = new NEO(Constants.RobotConstants.ConveyerMotorID, MotorType.kBrushless);
    private final LogitechController controller;

    /**
     * Creates a new Conveyer.
     */
    public Conveyor(final LogitechController controller) {
        this.controller = controller;
    }

    @Override
    public void periodic() {
        System.out.println(controller.getRawAxis(2));
        conveyerMotor.set(controller.getRawAxis(2));
    }
}
