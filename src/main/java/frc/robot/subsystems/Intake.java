/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.util.SlewRate;
import frc.robot.util.controllers.ControllerSet;
import frc.robot.util.controllers.LogitechController;
import frc.robot.util.controllers.PlaneController;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANEncoder;

public class Intake extends SubsystemBase {
    // public final NEO IntakeRight;
    /**
     * Creates a new Intake.
     */
    public final CANSparkMax intakeMotor = new CANSparkMax(Constants.ShooterConstants.Intake, MotorType.kBrushless);
    public final CANEncoder intakeEncoder = intakeMotor.getEncoder();
    private int ballCount;
    private DigitalInput intakeSensor = new DigitalInput(2);
    private ControllerSet controller;
    public boolean allowIntake = true;
    private double intakeSpeed = 0.83;
    private IntakeArm intakearm;
    public boolean jam = false;
    private final double nominalCurrent = 10;
    private STATEMACHINE currentState = STATEMACHINE.INTAKE;
    private double temp = 0;
    private SlewRate rate = new SlewRate(1.0);

    private enum STATEMACHINE {
        INTAKE, UNJAMSTART, TESTJAM, DONE
    };

    public Intake(ControllerSet controller, IntakeArm intakearm) {
        this.controller = controller;
        this.intakearm = intakearm;
        // setDefaultCommand(new IntakeTrigger(this));
        // setDefaultCommand(new IntakeUnjam(this));
        Shuffleboard.getTab("Driver").addString("ball count", () -> {
            return "" + ballCount();
        });
        // Shuffleboard.getTab("Test").addNumber("Intake current", this::tempMaxCurr);
        // ballCountEntry = Shuffleboard.getTab("Test").add("ballCountSet",
        // ballCount).getEntry();
        // Shuffleboard.putNumber();

    }

    public double tempMaxCurr() {
        if (intakeMotor.getOutputCurrent() > temp) {
            temp = intakeMotor.getOutputCurrent();
        }
        return temp;
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

    public boolean intakeResistance() {
        if (intakeMotor.getOutputCurrent() >= nominalCurrent) {
            jam = true;
        }

        return jam;
    }

    public boolean StateMachine() {
        switch (currentState) {
        case INTAKE:
            if (intakeResistance()) {
                currentState = STATEMACHINE.UNJAMSTART;
            }
            break;
        case UNJAMSTART:
            allowIntake = false;
            runSpeed(-0.5);
            if (getVelocityMotor() < 0) {
                currentState = STATEMACHINE.TESTJAM;
            }
            break;
        case TESTJAM:
            if (intakeResistance()) {
                runSpeed(-0.5);
            } else {
                allowIntake = true;
                currentState = STATEMACHINE.DONE;
            }
            break;
        case DONE:
            break;
        default:
            break;
        }
        return currentState == STATEMACHINE.DONE;
    }

    public void run() {
        intakeMotor.set(rate.rateCalculate(intakeSpeed));
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
        return intakeEncoder.getVelocity();
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
                // intakearm.position = intakearm.outPos;
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
            var leftTrigger = controller.useAxis(LogitechController.LEFT_TRIGGER_HELD,
                    PlaneController.LEFT_HAND_TRIGGER_HELD);
            intakeMotor.set(leftTrigger * intakeSpeed);
            if (leftTrigger > 0.1 && ballCount <= 4) {
                intakearm.position = intakearm.outPos;
            } else {
                intakearm.position = 0;
            }
        }
    }

    public void intakeArmUp() {
        intakearm.position = 0;
    }

    public void intakeArmDown() {
        intakearm.position = intakearm.outPos;
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
