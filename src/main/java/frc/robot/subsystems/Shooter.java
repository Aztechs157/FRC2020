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
import frc.robot.commands.ShooterControl;
import frc.robot.util.LogitechController;
import frc.robot.util.NEO;

public class Shooter extends SubsystemBase {
    private final NEO shooterMotor;

    /**
     * Creates a new Shooter.
     *
     * @param operatorcontroller
     */
    public NEO LeftRight;
    public NEO UpDown;

    // public Shooter() {
    // LeftRight = new Servo(0);
    // UpDown = new Talon(1);
    // testTalon = new AnalogPotentiometer(1);
    // }
    public Shooter(LogitechController controller) {
        shooterMotor = new NEO(Constants.ShooterConstants.shooter, MotorType.kBrushless);
        setDefaultCommand(new ShooterControl(this, controller));
    }

    public void Shoot(LogitechController controller) {
        shooterMotor.set(-controller.getRightTrigger());
    }

    @Override
    public void periodic() {
        // This method will be called once per scheduler run
    }
}
