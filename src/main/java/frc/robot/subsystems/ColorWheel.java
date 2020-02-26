/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved. */
/* Open Source Software - may be modified and shared by FRC teams. The code
*/
/* must be accompanied by the FIRST BSD license file in the root directory of
*/
/* the project. */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.revrobotics.ColorMatch;

import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.Counter;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.Counter.Mode;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.util.Color;

import com.revrobotics.ColorSensorV3;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.ColorWheelConstants;
import frc.robot.util.NEO;
import frc.robot.util.PID;

public class ColorWheel extends SubsystemBase {

    private final ColorSensorV3 colorSensor = new ColorSensorV3(I2C.Port.kOnboard);

    private final ColorMatch colorMatcher = new ColorMatch();

    // These require a ColorMatch instance so they're not in Constants
    private final Color redTarget = ColorMatch.makeColor(0.561, 0.232, 0.114);
    private final Color greenTarget = ColorMatch.makeColor(0.197, 0.561, 0.240);
    private final Color blueTarget = ColorMatch.makeColor(0.143, 0.427, 0.429);
    private final Color yellowTarget = ColorMatch.makeColor(0.361, 0.524, 0.113);

    private enum ArmState {
        MoveTurret, LiftArm, Done
    };

    private enum SpinWheelState {
        SpinColorWheel, OnBlue, MoveTurret, DropArm, Done
    };

    private enum ArmPosition {
        Up(90), Down(180);

        public double pos;

        private ArmPosition(final double pos) {
            this.pos = pos;
        }
    }

    public enum ColorResult {
        Red(0), Green(1), Blue(2), Yellow(3), Unknown(4);

        public int value;

        private ColorResult(final int value) {
            this.value = value;
        }

        @Override
        public String toString() {
            if (this.value == 0) {
                return "Red";
            } else if (this.value == 1) {
                return "Green";
            } else if (this.value == 2) {
                return "Blue";
            } else if (this.value == 3) {
                return "Yellow";
            } else {
                return "Unknown";
            }
        }
    }

    private final Counter encoder = new Counter(Mode.kSemiperiod);
    private final TalonSRX liftMotor = new TalonSRX(ColorWheelConstants.colorWheelLiftMotorId);
    private final NEO spinMotor = new NEO(ColorWheelConstants.colorWheelSpinMotorId, MotorType.kBrushless);

    private final Turret turret;

    private ArmState curretArmState = ArmState.MoveTurret;
    private SpinWheelState currentSpinState = SpinWheelState.SpinColorWheel;
    private int blueCount = 0;

    public NetworkTableEntry pVal;

    public PID colorWheelPID = new PID(0.1, 0, 0, 0, 0, 0, 0, 0, 0);

    /**
     * Creates a new ColorWheel.
     */
    public ColorWheel(final Turret turret) {
        liftMotor.setInverted(true);
        this.turret = turret;

        colorMatcher.addColorMatch(redTarget);
        colorMatcher.addColorMatch(greenTarget);
        colorMatcher.addColorMatch(blueTarget);
        colorMatcher.addColorMatch(yellowTarget);

        encoder.setSemiPeriodMode(true);
        encoder.setUpSource(9);
        encoder.reset();

        final ShuffleboardTab tab = Shuffleboard.getTab("Test");
        tab.addString("Color Sensed", () -> getColor().toString());
        tab.addNumber("Arm Pos", this::getArmPos);
        tab.addNumber("Amps", liftMotor::getOutputCurrent);
        tab.addString("Arm State", () -> this.curretArmState.toString());
        tab.addString("Spin State", () -> this.currentSpinState.toString());

        pVal = tab.add("P Val", colorWheelPID.optionSets[0].kP).getEntry();
    }

    public void stopSpinning() {
        spinMotor.set(0);
    }

    public void startSpinning() {
        spinMotor.set(0.5);
    }

    public void runLift(double s) {
        liftMotor.set(ControlMode.PercentOutput, s);
    }

    public double getArmPos() {
        return encoder.getPeriod() * (360.0 / 1024.0) * 1000000;// equation for degree/tic converted to seconds.
    }

    public ColorResult getColor() {

        final var detectedColor = colorSensor.getColor();
        final var match = colorMatcher.matchClosestColor(detectedColor);

        // Match the resulting color to the static matches to return it's enum value
        if (match.color == redTarget) {
            return ColorResult.Red;
        } else if (match.color == greenTarget) {
            return ColorResult.Green;
        } else if (match.color == blueTarget) {
            return ColorResult.Blue;
        } else if (match.color == yellowTarget) {
            return ColorResult.Yellow;
        } else {
            return ColorResult.Unknown;
        }
    }

    public void colorWheelState() {
        switch (curretArmState) {
        case MoveTurret:
            if (turret.LeftRight.getPosition() < 45) {
                turret.moveShooter(.5);
            } else {
                turret.moveShooter(0);
                curretArmState = ArmState.LiftArm;
            }
            break;
        case LiftArm:
            if (getArmPos() > 100) {
                moveArm(ArmPosition.Up);
            } else {
                stopArm();
                curretArmState = ArmState.Done;
            }
            break;
        default:
            break;
        }
    }

    public void spinWheelState() {
        switch (currentSpinState) {
        case SpinColorWheel:
            startSpinning();
            if (getColor() == ColorResult.Blue) {
                blueCount++;
                if (blueCount >= 7) {
                    stopSpinning();
                    currentSpinState = SpinWheelState.MoveTurret;
                } else {
                    // System.out.println("Not Blue");
                    currentSpinState = SpinWheelState.OnBlue;
                }
            }
            break;
        case OnBlue:
            startSpinning();
            if (getColor() != ColorResult.Blue) {
                currentSpinState = SpinWheelState.SpinColorWheel;
            }
            break;
        case MoveTurret:
            if (turret.LeftRight.getPosition() < 45) {
                turret.moveShooter(0.5);
            } else {
                turret.moveShooter(0);
                currentSpinState = SpinWheelState.DropArm;
            }
            break;
        case DropArm:
            if (getArmPos() < 170) {
                moveArm(ArmPosition.Down);
            } else {
                stopArm();
                currentSpinState = SpinWheelState.Done;
            }
            break;
        case Done:
        default:
            break;
        }
    }

    public void moveArm(final ArmPosition pos) {
        runLift(colorWheelPID.pidCalculate(pos.pos, getArmPos()));
    }

    public void stopArm() {
        runLift(0);
    }
}
