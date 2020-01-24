/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.util.LogitechController;
import frc.robot.util.Pixy2Controller.Target;
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.Vision;

public class TrackTarget extends CommandBase {

    // private final double LRMUL = 0.00001;
    // private final double UDMUL = 0.004;
    private final double LRTARGET = 158;
    int count = 0;
    private final Shooter shooter;
    private final Vision vision;
    private final LogitechController controller;

    /**
     * Creates a new TrackTarget2.
     */
    public TrackTarget(final Shooter shooter, final Vision vision, final LogitechController controller) {
        this.shooter = shooter;
        this.vision = vision;
        this.controller = controller;
        addRequirements(shooter);
        addRequirements(vision);
    }

    // Called when the command is initially scheduled.
    @Override
    public void initialize() {
        vision.turnLight(true);
        count = 0;
    }

    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute() {
        final Target[] targets = vision.getBlocks();
        System.out.println(targets.length);
        if (targets.length == 1) {
            count = 0;
            // System.out.println(vision.LR+(-(LRTARGET-targets[0].x))*LRMUL);
            shooter.moveShooter(vision.pid.pidCalculate(LRTARGET, targets[0].x) * 0.1);
            vision.setVertical(map(targets[0].y, 0, 207, 0.65, 0.4));

        } else {
            count++;
            // vision.setHorizontal(0);
        }
    }

    private double map(final double x, final double in_min, final double in_max, final double out_min,
            final double out_max) {
        return (x - in_min) * (out_max - out_min) / (in_max - in_min) + out_min;
    }

    // Called once the command ends or is interrupted.
    @Override

    public void end(final boolean interrupted) {

        vision.setHorizontal(0);
        vision.turnLight(false);
    }

    // Returns true when the command should end.
    @Override
    public boolean isFinished() {
        boolean retVal = true;
        double joyValx;

        joyValx = controller.getRawAxis(4);

        if (joyValx > -0.01 && joyValx < 0.01) {
            retVal = false;
        }
        if (count >= 40) {
            retVal = true;
        }
        return retVal;
    }
}
