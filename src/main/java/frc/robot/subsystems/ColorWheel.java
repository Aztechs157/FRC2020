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
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.Counter.Mode;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
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
        NotOnBlue, OnBlue, SpinToColor, MoveTurret, DropArm, Done, SpinBack
    };

    public enum ArmPosition {
        Up(88), Down(185);

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

    public String getRawColor() {
        // final RawColor rawcolor = colorSensor.getRawColor();

        return "R:" + colorSensor.getRed() + "  G:" + colorSensor.getGreen() + "  B:" + colorSensor.getBlue();
    }

    private final Counter encoder = new Counter(Mode.kSemiperiod);
    private final TalonSRX liftMotor = new TalonSRX(ColorWheelConstants.colorWheelLiftMotorId);
    private final NEO spinMotor = new NEO(ColorWheelConstants.colorWheelSpinMotorId, MotorType.kBrushless);

    private final Turret turret;

    public ArmState currentArmState = ArmState.MoveTurret;
    public SpinWheelState currentSpinState = SpinWheelState.NotOnBlue;
    private int blueCount = 0;
    private int waitCount = 0;
    public int ticksOnColor = 0;

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

        // final ShuffleboardTab tab = Shuffleboard.getTab("Test");
        Shuffleboard.getTab("Driver").addString("Color Sensed", () -> getColor().toString());
        // tab.addNumber("Arm Pos", this::getArmPos);
        // tab.addNumber("Amps", liftMotor::getOutputCurrent);
        // tab.addString("Arm State", () -> this.curretArmState.toString());
        // tab.addString("Spin State", () -> this.currentSpinState.toString());
        // tab.addNumber("Blue Count", () -> this.blueCount);
        Shuffleboard.getTab("Driver").addString("Color Desired", () -> {
            return getRequiredColor().toString();
        });
        // tab.addNumber("ticks on color", () -> this.ticksOnColor);
        // tab.addString("Raw Color", this::getRawColor);

        // pVal = tab.add("P Val", colorWheelPID.optionSets[0].kP).getEntry();
    }

    public void stopSpinning() {
        spinMotor.set(0);
    }

    public void startSpinning() {
        spinMotor.set(0.5);// TODO change to seperate for other spinning
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

    public ColorResult getRequiredColor() {
        final var gamedata = DriverStation.getInstance().getGameSpecificMessage();

        if (gamedata.length() == 0)
            return ColorResult.Unknown;

        switch (gamedata.charAt(0)) {
        case 'R':
            return ColorResult.Red;
        case 'G':
            return ColorResult.Green;
        case 'B':
            return ColorResult.Blue;
        case 'Y':
            return ColorResult.Yellow;
        default:
            return ColorResult.Unknown;
        }
    }

    public boolean colorWheelState(ArmPosition pos) {
        if (pos == ArmPosition.Up) {
            switch (currentArmState) {
            case MoveTurret:
                if (turret.LeftRight.getPosition() < 43) {
                    turret.moveShooter(.5);
                } else {
                    turret.moveShooter(0);
                    currentArmState = ArmState.LiftArm;
                }
                break;
            case LiftArm:
                if (getArmPos() > 98) {
                    moveArm(pos);
                } else {
                    stopArm();
                    currentArmState = ArmState.Done;
                }
                break;
            default:
                break;
            }
        } else {
            switch (currentArmState) {
            case MoveTurret:
                if (turret.LeftRight.getPosition() < 43) {
                    turret.moveShooter(.5);
                } else {
                    turret.moveShooter(0);
                    currentArmState = ArmState.LiftArm;
                }
                break;
            case LiftArm:
                if (getArmPos() < 180) {
                    moveArm(pos);
                } else {
                    stopArm();
                    currentArmState = ArmState.Done;
                }
                break;
            default:
                break;
            }
        }
        return currentArmState == ArmState.Done;
    }

    public boolean spinWheelState() {
        switch (currentSpinState) {
        case NotOnBlue:
            startSpinning();
            if (getColor() == ColorResult.Blue) {
                blueCount++;
                if (blueCount >= 8) {
                    // stopSpinning();
                    currentSpinState = SpinWheelState.SpinBack;
                } else {
                    // System.out.println("Not Blue");
                    currentSpinState = SpinWheelState.OnBlue;
                }
            }
            break;
        case OnBlue:
            startSpinning();
            if (getColor() != ColorResult.Blue) {
                currentSpinState = SpinWheelState.NotOnBlue;
            }
            break;
        case SpinToColor:
            startSpinning();
            if (getColor() == getRequiredColor()) {
                ticksOnColor++;
            } else {
                ticksOnColor = 0;
            }
            if (ticksOnColor > 10) {
                currentSpinState = SpinWheelState.SpinBack;
            }
            break;
        case SpinBack:
            spinMotor.set(-0.20);
            waitCount++;
            if (waitCount >= 25) {
                currentSpinState = SpinWheelState.MoveTurret;
                waitCount = 0;
                stopSpinning();
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
            if (getArmPos() < 180) {
                moveArm(ArmPosition.Down);
            } else {
                stopArm();
                currentSpinState = SpinWheelState.Done;
            }
            break;
        case Done:
            stopSpinning();
            blueCount = 0;
            ticksOnColor = 0;
        default:
            break;
        }
        return currentSpinState == SpinWheelState.Done;
    }

    public void moveArm(final ArmPosition pos) {
        runLift(colorWheelPID.pidCalculate(pos.pos, getArmPos()));
    }

    public void stopArm() {
        runLift(0);
    }

    public void resetArmState() {
        currentArmState = ArmState.MoveTurret;
    }

    public void resetStage2State() {
        currentSpinState = SpinWheelState.NotOnBlue;
    }

    public void resetStage3State() {
        currentSpinState = SpinWheelState.SpinToColor;
    }

    public void stopSpinState() {
        currentSpinState = SpinWheelState.Done;
        blueCount = 0;
        ticksOnColor = 0;
    }
}
