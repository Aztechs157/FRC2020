/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.util.LogitechController;
import frc.robot.util.NEO;

public class Kicker extends SubsystemBase {
    private NEO kicker;
    private boolean gotBall = false;
    private DigitalInput sensor3 = new DigitalInput(4);
    public int printCount;
    public int ballCount;

    /**
     * Creates a new Kicker.
     */
    public Kicker(LogitechController controller) {
        // kicker = new NEO(Constants.ShooterConstants.kicker, MotorType.kBrushless);
    }

    public void CountingSensors(LogitechController controller) {
        kicker.set(controller.getRawAxis(3));

        if (!gotBall) {
            if (sensor3.get()) {
                gotBall = true;
                kicker.set(0);
                ballCount++;
            }
        } else {
            if (!gotBall)
                gotBall = false;
        }
    }

    @Override
    public void periodic() {
        // This method will be called once per scheduler run
    }
}
