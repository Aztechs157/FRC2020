/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.commands.IntakeTrigger;
import frc.robot.util.LogitechController;
import frc.robot.util.NEO;
//import sun.font.TrueTypeFont;

public class Intake extends SubsystemBase {
    // public final NEO IntakeRight;
    /**
     * Creates a new Intake.
     */
    private final NEO intakeMotor;
    private int ballCount;
    private boolean gotBall = false;
    private DigitalInput intakeSensor = new DigitalInput(2);
    private LogitechController controller;

    public Intake(LogitechController controller) {
        this.controller = controller;
        intakeMotor = new NEO(Constants.ShooterConstants.Intake, MotorType.kBrushless);
        setDefaultCommand(new IntakeTrigger(this));

    }

    // public ConveyerSensors(LogitechController controller) {

    // }

    @Override
    public void periodic() {
        System.out.println(intakeSensor.get());
    }

    public void run() {
        intakeMotor.set(0.75);
    }

    public void stop() {
        intakeMotor.set(0.0);
    }

    public int ballCount() {
        return ballCount;
    }

    public boolean get() {
        if (!gotBall) {
            if (intakeSensor.get()) {
                gotBall = true;
                ballCount++;
            }
        } else {
            if (!intakeSensor.get()) {
                gotBall = false;
            }
        }
        return gotBall;
    }

    // // private int printCount;
    public void runIntake() {
        intakeMotor.set(controller.getRawAxis(2));
    }

    // printCount++;
    // if (printCount > 25) {
    // System.out.println("gotBall: " + gotBall);
    // }
    // if (ballCount == 5) {
    // System.out.println("Fully Loaded");
    // }
    // }
}
