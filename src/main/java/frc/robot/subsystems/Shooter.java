/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.RunCommand;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.commands.ShooterControl;
import frc.robot.util.NEO;
import frc.robot.util.controllers.Controller;

public class Shooter extends SubsystemBase {
    private final NEO shooterMotor;

    /**
     * Creates a new Shooter.
     *
     * @param operatorcontroller
     */
    public NEO LeftRight;
    public NEO UpDown;
    private Controller controller;
    private Intake intake;
    private final Kicker kicker;
    private final Conveyor conveyor;
    private boolean motorUpToSpeed = false;
    private int count = 0;
    private final int SHOOTTIME = 50;

    private enum STATEMACHINE {
        END, MOVEBALL, STOPMOVE, SPINSHOOTER, SHOOTERUPTOSPEED, SHOOT, PANICSTOP
    };

    STATEMACHINE state = STATEMACHINE.END;

    // public Shooter() {
    // LeftRight = new Servo(0);
    // UpDown = new Talon(1);
    // testTalon = new AnalogPotentiometer(1);
    // }
    public Shooter(Controller controller, Kicker kicker, Conveyor conveyor, Intake intake) {
        shooterMotor = new NEO(Constants.ShooterConstants.shooter, MotorType.kBrushless).inverted();
        this.controller = controller;
        this.kicker = kicker;
        this.conveyor = conveyor;
        this.intake = intake;
    }

    public void stopAll() {
        kicker.stop();
        conveyor.stop();
        stop();
    }

    public void run() {
        shooterMotor.set(1);
    }

    public void resetStateMachine() {
        state = STATEMACHINE.END;
    }

    public void stop() {
        shooterMotor.set(0);
    }

    public double getVelocityMotor() {
        return shooterMotor.getVelocity();
    }

    public void StateMachine() {
        switch (state) {
        case END:
            conveyor.stop();
            kicker.stop();
            stop();
            if (conveyor.getVelocityMotor() == 0 && kicker.getVelocityMotor() == 0 && getVelocityMotor() == 0) {
                state = STATEMACHINE.MOVEBALL;
            }
            break;
        case MOVEBALL:
            if (!kicker.get()) {
                kicker.run();
                conveyor.run();
            } else {
                state = STATEMACHINE.STOPMOVE;
            }
            break;
        case STOPMOVE:
            conveyor.stop();
            kicker.stop();
            state = STATEMACHINE.SPINSHOOTER;
            break;
        case SPINSHOOTER:
            run();
            if (shooterMotor.getVelocity() >= 4300) {
                state = STATEMACHINE.SHOOT;
                intake.ballCountDecrement();
            }
            break;
        case SHOOT:
            kicker.run();
            if (!kicker.get()) {
                count++;
            } else {
                count = 0;
            }
            if (count > SHOOTTIME) {
                state = STATEMACHINE.END;
            }
            break;
        case PANICSTOP:
            kicker.stop();
            intake.stop();
            stop();
            conveyor.stop();
            break;
        default:
            state = STATEMACHINE.PANICSTOP;
            break;
        }
    }

    @Override
    public void periodic() {
        // This method will be called once per scheduler run
    }
}
