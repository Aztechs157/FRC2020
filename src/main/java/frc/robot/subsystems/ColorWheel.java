/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import com.revrobotics.ColorMatch;
import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.util.Color;

import com.revrobotics.ColorSensorV3;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants.ColorWheelConstants;
import frc.robot.util.NEO;

public class ColorWheel extends SubsystemBase {

    private final ColorSensorV3 colorSensor = new ColorSensorV3(I2C.Port.kOnboard);

    private final ColorMatch colorMatcher = new ColorMatch();

    // These require a ColorMatch instance so they're not in Constants
    private final Color redTarget = ColorMatch.makeColor(0.561, 0.232, 0.114);
    private final Color greenTarget = ColorMatch.makeColor(0.197, 0.561, 0.240);
    private final Color blueTarget = ColorMatch.makeColor(0.143, 0.427, 0.429);
    private final Color yellowTarget = ColorMatch.makeColor(0.361, 0.524, 0.113);

    private final NEO liftMotor = new NEO(ColorWheelConstants.liftMotorId, MotorType.kBrushless);
    private final NEO spinMotor = new NEO(ColorWheelConstants.spinMotorId, MotorType.kBrushless);

    /**
     * Creates a new ColorWheel.
     */
    public ColorWheel() {
        colorMatcher.addColorMatch(redTarget);
        colorMatcher.addColorMatch(greenTarget);
        colorMatcher.addColorMatch(blueTarget);
        colorMatcher.addColorMatch(yellowTarget);
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

    @Override
    public void periodic() {
        // This method will be called once per scheduler run
    }
}
