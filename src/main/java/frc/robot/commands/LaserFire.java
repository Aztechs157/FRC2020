/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj.Relay.Value;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.RobotContainer;
import frc.robot.subsystems.Vision;

public class LaserFire extends CommandBase {

    private boolean on;
    private Vision vision;

    /**
     * Creates a new LaserFire2.
     */
    public LaserFire(boolean on, Vision vision) {
        this.on = on;
        this.vision = vision;
        addRequirements(vision);
    }

    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute() {
        if (on) {
            vision.laser.set(Value.kForward);
        } else {
            vision.laser.set(Value.kOff);
        }
    }

    @Override
    public boolean isFinished() {
        return false;
    }
}
