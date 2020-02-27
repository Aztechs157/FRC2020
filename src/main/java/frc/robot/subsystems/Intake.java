/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.util.NEO;
//import sun.font.TrueTypeFont;
import frc.robot.util.controllers.Controller;

public class Intake extends SubsystemBase {
    // public final NEO IntakeRight;
    /**
     * Creates a new Intake.
     */
    private final NEO intakeMotor;
    private int ballCount;
    private boolean gotBall = false;
    private DigitalInput intakeSensor = new DigitalInput(2);
    private Controller controller;
    public boolean allowIntake = true;
    private double intakeSpeed = 0.80;
    private IntakeArm intakearm;
    private NetworkTableEntry ballCountEntry;

    public Intake(Controller controller, IntakeArm intakearm) {
        this.controller = controller;
        this.intakearm = intakearm;
        intakeMotor = new NEO(Constants.ShooterConstants.Intake, MotorType.kBrushless);
        // setDefaultCommand(new IntakeTrigger(this));
        Shuffleboard.getTab("Test").addNumber("ball count", () -> {
            return (double) ballCount();
        });
        // ballCountEntry = Shuffleboard.getTab("Test").add("ballCountSet",
        // ballCount).getEntry();
        // Shuffleboard.putNumber();

    }

    // public ConveyerSensors( Controller controller) {

    // }

    @Override
    public void periodic() {
        // ballCount = (int) ballCountEntry.getDouble(ballCount);
        // System.out.println(intakeSensor.get());
        // System.out.println(ballCount);
    }

    public void runSpeed(double s) {
        intakeMotor.set(s);
    }

    public void run() {
        intakeMotor.set(intakeSpeed);
    }

    public void stop() {
        intakeMotor.set(0.0);
    }

    public int ballCount() {
        return ballCount;
    }

    public void ballCountSet(int ballcount) {
        this.ballCount = ballcount;
    }

    public int ballCountIncrement() {
        ballCount++;
        return ballCount;
    }

    public int ballCountDecrement() {
        ballCount--;
        return ballCount;
    }

    public void zeroBallCount() {
        ballCount = 0;
        // testing with 3 should be 0
    }

    public double getVelocityMotor() {
        return intakeMotor.getVelocity();
    }

    public boolean get() {
        // if (gotBall == false) {
        // if (intakeSensor.get() == true) {
        // gotBall = true;
        // ballCount++;
        // }
        // } else {
        // if (intakeSensor.get() == false) {
        // gotBall = false;
        // }
        // }
        return intakeSensor.get();
    }

    public void buttonIntake() {
        if (allowIntake) {
            if (ballCount() < 4) {
                intakearm.position = intakearm.outPos;
                intakeMotor.set(intakeSpeed);
            } else {
                intakeMotor.set(0);
                intakearm.position = 0;
            }

        }
    }

    // // private int printCount;
    public void runIntake() {
        // System.out.println(controller.getLeftTrigger());
        if (allowIntake) {
            intakeMotor.set(controller.getLeftTrigger() * intakeSpeed);
            if (controller.getLeftTrigger() > 0.1 && ballCount <= 4) {
                intakearm.position = intakearm.outPos;
            } else {
                intakearm.position = 0;
            }
        }
    }

    public void intakeArmUp() {
        intakearm.position = 0;
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
