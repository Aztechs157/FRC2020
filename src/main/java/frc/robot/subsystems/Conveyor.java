/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import com.revrobotics.CANSparkMaxLowLevel.MotorType;

// import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.DigitalInput;
// import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.util.PID;
import frc.robot.util.controllers.ControllerSet;
import frc.robot.Constants;
import frc.robot.commands.ConveyerControl;
import frc.robot.subsystems.Intake;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANEncoder;

public class Conveyor extends SubsystemBase {
    public final CANSparkMax conveyorMotor = new CANSparkMax(Constants.ShooterConstants.conveyorMotor,
            MotorType.kBrushless);
    public final CANEncoder conveyorEncoder = conveyorMotor.getEncoder();
    private Intake intake;
    private IntakeArm intakearm;
    private Kicker kicker;
    private DigitalInput conveyorBottom = new DigitalInput(3);
    private Shooter shooter;
    private int maxBalls = 4;
    private double maxSpeed = 0.40;
    public double currentSpeed = 0;
    public double temp = 0;;
    public PID conveyorPID = new PID(0.00003, 0, 0, 0, 0, 100, 0, 0, 0);

    private enum STATEMACHINE {
        WAIT, STARTCONVEYOR, SHIFT, INTOKICKER, STOP, CLEARKICKER, PANICSTOP
    };

    private STATEMACHINE state = STATEMACHINE.WAIT;

    /**
     * Creates a new Conveyer.
     */
    public Conveyor(ControllerSet controller, Intake intake, Kicker kicker, IntakeArm intakearm) {
        this.intake = intake;
        this.kicker = kicker;
        this.intakearm = intakearm;
        setDefaultCommand(new ConveyerControl(this, controller));
        // Shuffleboard.getTab("Test").addNumber("Speed", this::getVelocityMotor);
        // Shuffleboard.getTab("Test").addNumber("Pval conveyor real", () -> {
        // return conveyorPID.optionSets[0].kP * 1000000;
        // // });
        // pVal = Shuffleboard.getTab("Test").add("Pval conveyorF",
        // conveyorPID.optionSets[0].kP * 1000000).getEntry();
        // dVal = Shuffleboard.getTab("Test").add("Dval conveyor",
        // conveyorPID.optionSets[0].kD * 1000000).getEntry();
    }

    public void setSpeed(double speed) {
        currentSpeed += conveyorPID.pidCalculate(speed, conveyorEncoder.getVelocity());
        if (currentSpeed > 1)
            currentSpeed = 1;
        else if (currentSpeed < -1)
            currentSpeed = -1;
        conveyorMotor.set(currentSpeed);
    }

    @Override
    public void periodic() {
        // stateMachine();
        // shift();
        // This method will be called once per scheduler run
    }

    public double getMaxSpeed() {
        if (getVelocityMotor() > temp) {
            temp = getVelocityMotor();
        }
        return temp;
    }

    public boolean getBottom() {
        return conveyorBottom.get();
    }

    public void addshooter(Shooter shooter) {
        this.shooter = shooter;
    }

    public void stateMachine() {
        switch (state) {
        case WAIT:
            if (intake.get()) {
                currentSpeed = 0;
                intake.ballCountIncrement();
                // intake.allowIntake = true;
                if (intake.ballCount() <= maxBalls - 1) {
                    state = STATEMACHINE.STARTCONVEYOR;
                } else {
                    state = STATEMACHINE.INTOKICKER;
                }
            }
            break;
        case STARTCONVEYOR:

            run();
            intake.allowIntake = false;
            intake.run();
            kicker.stop();
            if (conveyorBottom.get()) {
                state = STATEMACHINE.SHIFT;
            }
            break;
        case SHIFT:
            if (conveyorBottom.get()) {
                run();
                kicker.stop();
                intake.run();
            } else {
                stop();
                intake.stop();
                intake.allowIntake = true;
                if (intake.ballCount() <= maxBalls - 1) {
                    state = STATEMACHINE.WAIT;
                } else {
                    intakearm.position = 0;
                    state = STATEMACHINE.CLEARKICKER;

                }
            }
            break;
        case INTOKICKER:

            kicker.halfRun();
            run();
            // intake.allowIntake = false;
            intake.run();
            if (kicker.get()) {
                kicker.stop();
                stop();
                intake.stop();
                // intake.allowIntake = false;
                state = STATEMACHINE.STARTCONVEYOR;
            }
            break;
        case CLEARKICKER:
            conveyorMotor.set(-maxSpeed / 2);
            kicker.runSpeed(-0.05);
            if (intake.get()) {
                stop();
                kicker.stop();
                state = STATEMACHINE.STOP;
            }
            break;
        case STOP:
            // stop();
            // kicker.stop();
            if (intake.ballCount() <= 0) {
                state = STATEMACHINE.WAIT;
            }
            break;
        case PANICSTOP:
            kicker.stop();
            stop();

            break;
        default:
            state = STATEMACHINE.PANICSTOP;

            break;
        }
    }

    public void shift() {
        if (intake.ballCount() < 1 && intake.get()) {
            intake.run();
            run();
            kicker.halfRun();
        } else {
            if (kicker.get()) {
                stop();
                intake.stop();
                kicker.stop();
            } else {
                shooter.runSpeed(-0.03);
            }
        }
    }

    // public void test() {
    // conveyorMotor.set(controller.getRawAxis(2));
    // }
    public void runSpeed(double s) {
        conveyorMotor.set(s);
    }

    public void run() {
        conveyorMotor.set(maxSpeed);
    }

    public void stop() {
        conveyorMotor.set(0.0);
    }

    public double getVelocityMotor() {
        return conveyorEncoder.getVelocity();
    }

    public void resetStateMachine() {
        state = STATEMACHINE.WAIT;
    }

}
