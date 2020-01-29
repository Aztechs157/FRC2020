/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.commands.ShooterControl;
import frc.robot.util.LogitechController;
import frc.robot.util.NEO;

public class Shooter extends SubsystemBase {
    public NEO shooter;

    /**
     * Creates a new Shooter.
     *
     * @param operatorcontroller
     */
    public Shooter(LogitechController controller) {
        shooter = new NEO(8, MotorType.kBrushless);
        setDefaultCommand(new ShooterControl(this, controller));
    }

    @Override
    public void periodic() {
        // This method will be called once per scheduler run
    }
}
