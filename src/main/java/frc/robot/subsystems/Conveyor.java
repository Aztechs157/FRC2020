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
import frc.robot.util.NEO;
import frc.robot.util.controllers.Controller;
import frc.robot.Constants;
import frc.robot.commands.ConveyerControl;
import frc.robot.subsystems.Intake;

public class Conveyor extends SubsystemBase {
    public final NEO conveyorMotor;

    private Intake intake;
    private Kicker kicker;
    private DigitalInput conveyorBottom = new DigitalInput(3);
    private Controller controller;
    private double pos;
    private Shooter shooter;
    private boolean firstTime = false;
    private int maxBalls = 4;
    private double maxSpeed = 0.40;

    private enum STATEMACHINE {
        WAIT, STARTCONVEYOR, SHIFT, INTOKICKER, STOP, CLEARKICKER, PANICSTOP
    };

    private STATEMACHINE state = STATEMACHINE.WAIT;

    /**
     * Creates a new Conveyer.
     */
    public Conveyor(Controller controller, Intake intake, Kicker kicker) {
        conveyorMotor = new NEO(Constants.ShooterConstants.conveyorMotor, MotorType.kBrushless);
        this.intake = intake;
        this.kicker = kicker;
        this.controller = controller;
        setDefaultCommand(new ConveyerControl(this, controller));
    }

    @Override
    public void periodic() {
        // stateMachine();
        // shift();
        // This method will be called once per scheduler run
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
                intake.ballCountIncrement();
                intake.allowIntake = true;
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
                    state = STATEMACHINE.CLEARKICKER;
                }
            }
            break;
        case INTOKICKER:
            kicker.halfRun();
            run();
            intake.allowIntake = false;
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
            if (intake.get()) {
                stop();
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
        return conveyorMotor.getVelocity();
    }

    public void resetStateMachine() {
        state = STATEMACHINE.WAIT;
    }

}
