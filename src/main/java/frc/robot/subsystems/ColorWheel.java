/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved. */
/* Open Source Software - may be modified and shared by FRC teams. The code
*/
/* must be accompanied by the FIRST BSD license file in the root directory of
*/
/* the project. */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import com.revrobotics.ColorMatch;

import edu.wpi.first.wpilibj.Counter;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.Counter.Mode;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.util.Color;

import com.revrobotics.ColorSensorV3;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.ColorWheelConstants;
import frc.robot.util.NEO;
import frc.robot.util.PID;

public class ColorWheel extends SubsystemBase {

    private final ColorSensorV3 colorSensor = new ColorSensorV3(I2C.Port.kMXP);

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

    private final NEO liftMotor = new NEO(ColorWheelConstants.colorWheelLiftMotorId, MotorType.kBrushed);
    private final NEO spinMotor = new NEO(ColorWheelConstants.colorWheelSpinMotorId, MotorType.kBrushless);

    private Turret turret;

    /**
     * Creates a new ColorWheel.
     */
    public ColorWheel(Turret turret) {
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
    }

    public double position = 98;
    private PID colorWheelPID = new PID(0.0001, 0, 0, 0, 0, 0, 0, 0, 0);

    public void stop() {
        spinMotor.set(0);
    }

    public void spin() {
        spinMotor.set(1);
    }

    public void run(double s) {
        liftMotor.set(s);
    }

    public double getPos() {
        return encoder.getPeriod() * ((double) 360 / 1024) * 1000000;
    }

    public enum ColorResult {
        Red(0), Green(1), Blue(2), Yellow(3), Unknown(4);

        public int value;

        private ColorResult(final int value) {
            this.value = value;
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
            MoveArm();
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
            MoveArm();
            colorWheelState = COLORWHEEL.END;
            break;
        case END:

            break;

        default:
            break;
        }
    }

    public void MoveArm() {
        run(0.20);
        SmartDashboard.putNumber("amps", liftMotor.getOutputCurrent());
        // run(colorWheelPID.pidCalculate(position, getPos()) * 0.05);
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
