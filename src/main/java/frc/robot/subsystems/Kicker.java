/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import com.revrobotics.CANEncoder;
import com.revrobotics.CANSparkMax;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;

public class Kicker extends SubsystemBase {
    public CANSparkMax kicker;
    private boolean gotBall = false;
    private DigitalInput kickerSensor = new DigitalInput(5);
    public int printCount;
    private CANEncoder kickEncoder;

    /**
     * Creates a new Kicker.
     */
    public Kicker() {
        kicker = new CANSparkMax(Constants.ShooterConstants.kicker, MotorType.kBrushless);
        kickEncoder = kicker.getEncoder();
    }

    public void halfRun() {
        kicker.set(0.29);
    }

    public void run() {
        kicker.set(0.4);
    }

    public void stop() {
        kicker.set(0);
    }

    public double getVelocityMotor() {
        return kickEncoder.getVelocity();
    }

    public boolean get() {
        if (gotBall == false) {
            if (kickerSensor.get() == true) {
                gotBall = true;
            }
        } else {
            if (kickerSensor.get() == false) {
                gotBall = false;
            }
        }
        return gotBall;
    }

    public void runSpeed(double s) {
        kicker.set(s);
    }

    @Override
    public void periodic() {
        // System.out.println("Ball: " + gotBall);
        // This method will be called once per scheduler run
    }
}
