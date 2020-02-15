/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.util.controllers.Controller;
import frc.robot.util.NEO;

public class Kicker extends SubsystemBase {
    public NEO kicker;
    private final Intake intake;
    private boolean gotBall = false;
    private DigitalInput kickerSensor = new DigitalInput(5);
    public int printCount;
    private Controller controller;

    /**
     * Creates a new Kicker.
     */
    public Kicker(Controller controller, Intake intake) {
        kicker = new NEO(Constants.ShooterConstants.kicker, MotorType.kBrushless);
        this.intake = intake;
        // setDefaultCommand(KickerControl(this, LogitechController controller));
    }

    public void runIntake() {
        kicker.set(0.5);
    }

    public void run() {
        kicker.set(1);
    }

    public void stop() {
        kicker.set(0);
    }

    public double getVelocityMotor() {
        return kicker.getVelocity();
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

    @Override
    public void periodic() {
        // System.out.println("Ball: " + gotBall);
        // This method will be called once per scheduler run
    }
}
