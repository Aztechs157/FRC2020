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
    private int count = 0;
    private boolean firstTime = true;

    private enum COLORWHEEL {
        MOVETURRET, LIFTARM, SPINCOLORWHEEL, ONBLUE, DROPARM, END
    };

    COLORWHEEL colorWheelState = COLORWHEEL.MOVETURRET;
    Counter encoder;

    private final TalonSRX liftMotor = new TalonSRX(ColorWheelConstants.colorWheelLiftMotorId);
    private final NEO spinMotor = new NEO(ColorWheelConstants.colorWheelSpinMotorId, MotorType.kBrushless);

    private Turret turret;

    public NetworkTableEntry pVal;

    /**
     * Creates a new ColorWheel.
     */
    public ColorWheel(Turret turret) {
        liftMotor.setInverted(true);
        this.turret = turret;
        colorMatcher.addColorMatch(redTarget);
        colorMatcher.addColorMatch(greenTarget);
        colorMatcher.addColorMatch(blueTarget);
        colorMatcher.addColorMatch(yellowTarget);
        encoder = new Counter(Mode.kSemiperiod);
        encoder.setSemiPeriodMode(true);
        // encoder.setDownSource(9);
        encoder.setUpSource(9);
        // encoder.setDistancePerPulse();
        encoder.reset();
        Shuffleboard.getTab("Test").addString("Color Sensed", () -> {
            return getColor().toString();
        });
        pVal = Shuffleboard.getTab("Test").add("P Val", colorWheelPID.optionSets[0].kP).getEntry();

    }

    public double position = 98;
    public PID colorWheelPID = new PID(0.1, 0, 0, 0, 0, 0, 0, 0, 0);

    public void stop() {
        spinMotor.set(0);
    }

    public void spin() {
        spinMotor.set(0.5);
    }

    public void run(double s) {
        liftMotor.set(ControlMode.PercentOutput, s);
    }

    public double getPos() {
        return encoder.getPeriod() * ((double) 360 / 1024) * 1000000;// equation for degree/tic converted to seconds.
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
                return "unknown";
            }
        }
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
        switch (colorWheelState) {
        case MOVETURRET:
            turret.MoveTurret();
            if (firstTime) {
                colorWheelState = COLORWHEEL.LIFTARM;
                firstTime = false;
            } else {
                colorWheelState = COLORWHEEL.DROPARM;
            }
            break;
        case LIFTARM:
            // MoveArm();
            colorWheelState = COLORWHEEL.SPINCOLORWHEEL;
            break;
        case SPINCOLORWHEEL:
            spin();
            if (getColor() == ColorResult.Blue) {
                // System.out.println("Blue");

                count++;
                if (count >= 7) {
                    stop();
                    colorWheelState = COLORWHEEL.MOVETURRET;
                } else {
                    // System.out.println("Not Blue");
                    colorWheelState = COLORWHEEL.ONBLUE;
                }
            }
            break;
        case ONBLUE:
            spin();
            if (getColor() != ColorResult.Blue) {
                colorWheelState = COLORWHEEL.SPINCOLORWHEEL;
            }
            break;
        case DROPARM:
            // MoveArm();
            colorWheelState = COLORWHEEL.END;
            break;
        case END:

            break;

        default:
            break;
        }
    }

    public void MoveArm(double pos) {
        // SmartDashboard.putNumber("amps", liftMotor.getOutputCurrent());// have
        // someone push back to measure current.
        // slowly back robot up autonomously untill
        // contact is amde with color wheel ?
        run(colorWheelPID.pidCalculate(pos, getPos()));
    }

    public void stopArm() {
        run(0);
    }

    @Override
    public void periodic() {
        // This method will be called once per scheduler run
        SmartDashboard.putNumber("value", getPos());

    }
}
