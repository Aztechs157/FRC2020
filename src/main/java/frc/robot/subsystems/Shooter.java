/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import org.ejml.simple.ConvertToDenseException;

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
    private boolean first = true;

    private enum SEMIAUTO {
        END, MOVEBALL, STOPMOVE, SPINSHOOTER, SHOOTERUPTOSPEED, SHOOT, PANICSTOP, BACKSPIN
    };

    private enum AUTOMATIC {
        MOVEBALL, BACKSPIN, SPINSHOOTER, SHOOT, SPEEDLOAD, END, PANICSTOP
    };

    AUTOMATIC autoState = AUTOMATIC.SPINSHOOTER;
    SEMIAUTO semiAutoState = SEMIAUTO.END;

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
        this.conveyor.addshooter(this);
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
        semiAutoState = SEMIAUTO.END;
        autoState = AUTOMATIC.SPINSHOOTER;
    }

    public void stop() {
        shooterMotor.set(0);
    }

    public double getVelocityMotor() {
        return shooterMotor.getVelocity();
    }

    public void SemiAuto() {
        switch (semiAutoState) {
        case END:
            conveyor.stop();
            kicker.stop();
            stop();
            if (conveyor.getVelocityMotor() == 0 && kicker.getVelocityMotor() == 0 && getVelocityMotor() == 0) {
                semiAutoState = SEMIAUTO.MOVEBALL;
            }
            break;
        case MOVEBALL:
            if (!kicker.get()) {
                kicker.run();
                conveyor.run();
                intake.run();
            } else {
                semiAutoState = SEMIAUTO.STOPMOVE;
            }
            break;
        case STOPMOVE:
            conveyor.stop();
            kicker.stop();
            intake.stop();
            semiAutoState = SEMIAUTO.BACKSPIN;
            break;
        case BACKSPIN:
            runSpeed(-0.25);
            if (kicker.get()) {
                stop();
                semiAutoState = SEMIAUTO.SPINSHOOTER;
            }
            break;
        case SPINSHOOTER:
            run();
            if (shooterMotor.getVelocity() >= 4300) {
                semiAutoState = SEMIAUTO.SHOOT;
                intake.ballCountDecrement();
            }
            break;
        case SHOOT:
            kicker.halfRun();
            if (!kicker.get()) {
                count++;
            } else {
                count = 0;
            }
            if (count > SHOOTTIME) {
                semiAutoState = SEMIAUTO.END;
            }
            break;
        case PANICSTOP:
            kicker.stop();
            intake.stop();
            stop();
            conveyor.stop();
            break;
        default:
            semiAutoState = SEMIAUTO.PANICSTOP;
            break;
        }

    }

    public boolean automatic() {
        SmartDashboard.putNumber("motorSpeed", getVelocityMotor());
        boolean retval = false;
        switch (autoState) {
        case MOVEBALL:
            kicker.run();
            conveyor.run();
            if (kicker.get()) {
                kicker.stop();
                conveyor.stop();
                autoState = AUTOMATIC.BACKSPIN;
            }
            break;
        case BACKSPIN:
            runSpeed(-0.25);
            if (kicker.get()) {
                stop();
                autoState = AUTOMATIC.SPINSHOOTER;
            }
            break;
        case SPINSHOOTER:
            run();
            if (shooterMotor.getVelocity() >= 4800) {
                if (kicker.get()) {
                    autoState = AUTOMATIC.SHOOT;
                } else {
                    autoState = AUTOMATIC.SPEEDLOAD;
                }
            }
            break;
        case SHOOT:
            if (first) {
                intake.ballCountDecrement();
                first = false;
            }
            kicker.halfRun();
            if (!kicker.get()) {
                count++;
            } else {
                count = 0;
            }
            if (count > SHOOTTIME) {
                first = true;
                if (intake.ballCount() > 0) {
                    if (shooterMotor.getVelocity() > 0) {
                        autoState = AUTOMATIC.SPINSHOOTER;
                    } else {
                        autoState = AUTOMATIC.MOVEBALL;
                    }
                } else {
                    autoState = AUTOMATIC.END;
                }
            }
            break;
        case SPEEDLOAD:
            conveyor.run();
            kicker.run();
            if (kicker.get()) {
                conveyor.stop();
                autoState = AUTOMATIC.SHOOT;
            }
            break;
        case END:
            conveyor.stop();
            stop();
            kicker.stop();
            retval = true;
            break;
        default:
        case PANICSTOP:
            kicker.stop();
            conveyor.stop();
            stop();
            break;
        }
        return retval;
    }

    @Override
    public void periodic() {
        // This method will be called once per scheduler run
    }

    public double getPosMotor() {
        return shooterMotor.getPosition();
    }

    public void runSpeed(double d) {
        shooterMotor.set(d);
    }
}
