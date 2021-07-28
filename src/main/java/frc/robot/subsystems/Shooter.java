/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import com.revrobotics.CANEncoder;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.util.PID;

public class Shooter extends SubsystemBase {
    private final CANSparkMax shooterMotor;

    /**
     * Creates a new Shooter.
     *
     * @param operatorcontroller
     */
    // var entry;
    private double targetRPM = 4300;
    public CANSparkMax LeftRight;
    public CANSparkMax UpDown;
    private Intake intake;
    private final Kicker kicker;
    private final Conveyor conveyor;
    private int count = 0;
    private final int SHOOTTIME = 12;
    private boolean first = true;
    private double currentPower = 0;
    private PID shootPid = new PID(0.00001, 0, 0.000003, 0, 0, 0, 0, 0, 0);
    private CANEncoder shooterEncoder;

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
    public Shooter(Kicker kicker, Conveyor conveyor, Intake intake) {

        shooterMotor = new CANSparkMax(Constants.ShooterConstants.shooter, MotorType.kBrushless);
        shooterMotor.setInverted(true);
        shooterEncoder = shooterMotor.getEncoder();
        this.kicker = kicker;
        this.conveyor = conveyor;
        this.intake = intake;
        this.conveyor.addshooter(this);

        // entry = Shuffleboard.getTab("Test").addNumber("target RPM", valueSupplier)
        // Shuffleboard.getTab("Test").addNumber("shooter speed",
        // this::getVelocityMotor);
    }

    public void stopAll() {
        kicker.stop();
        conveyor.stop();
        stop();
        intake.stop();
    }

    public void setSpeed(double speed) {
        currentPower += shootPid.pidCalculate(speed, shooterEncoder.getVelocity());
        if (currentPower > 1)
            currentPower = 1;
        else if (currentPower < -1)
            currentPower = -1;
        shooterMotor.set(currentPower);
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
        return shooterEncoder.getVelocity();
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
            if (shooterEncoder.getVelocity() >= 3300) {
                semiAutoState = SEMIAUTO.SHOOT;
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
            if (intake.ballCount() >= intake.maxBalls) {

                setSpeed(targetRPM + 400);
            } else {
                setSpeed(targetRPM);
            }
            if (shooterEncoder.getVelocity() >= targetRPM - 50 && shooterEncoder.getVelocity() <= targetRPM + 50) {
                if (kicker.get()) {
                    autoState = AUTOMATIC.SHOOT;
                } else {
                    autoState = AUTOMATIC.SPEEDLOAD;
                }
            }
            break;
        case SHOOT:
            setSpeed(targetRPM);
            if (first) {
                // System.out.println("Decrementing ball count");
                first = false;
            }
            kicker.halfRun();
            if (!kicker.get()) {
                count++;
            } else {
                count = 0;
            }
            if (count > SHOOTTIME) {
                intake.ballCountDecrement();
                first = true;
                count = 0;
                if (intake.ballCount() > 0) {
                    if (shooterEncoder.getVelocity() > 0) {
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
            setSpeed(targetRPM);
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
        return shooterEncoder.getPosition();
    }

    public void runSpeed(double d) {
        shooterMotor.set(d);
    }
}
