/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.util.Pixy2Controller.Target;
import frc.robot.util.controllers.Controller;
import frc.robot.subsystems.Turret;
import frc.robot.subsystems.Vision;

public class TrackTarget extends CommandBase {

    // private final double LRMUL = 0.00001;
    // private final double UDMUL = 0.004;
    private final double LRTARGET = 145;// 158
    private int importantCounter = 0;
    private int unimportantCounter = 0;
    private final Turret turret;
    private final Vision vision;
    private final Controller controller;

    /**
     * Creates a new TrackTarget2.
     */
    public TrackTarget(final Turret turret, final Vision vision, final Controller controller) {
        this.turret = turret;
        this.vision = vision;
        this.controller = controller;
        addRequirements(turret);
        addRequirements(vision);
    }

    // Called when the command is initially scheduled.
    @Override
    public void initialize() {
        // vision.turnLight(true);
        importantCounter = 0;
    }

    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute() {
        if (vision.pixyIn) {
            final Target[] targets = vision.getBlocks();
            // System.out.println("Target length" + targets.length);
            if (targets.length == 1) {
                importantCounter = 0;
                // unimportantCounter++;
                // System.out.println(vision.LR+(-(LRTARGET-targets[0].x))*LRMUL);
                turret.moveShooter(vision.pixyPID.pidCalculate(LRTARGET, targets[0].x) * 0.1);
                vision.setVertical(map(targets[0].y, 0, 207, 0.65, 0.4));
                if (unimportantCounter >= 100) {
                    System.out.println("pos: (" + targets[0].x + ", " + targets[0].y + ")");
                    System.out.println("siz: (" + targets[0].width + ", " + targets[0].height + ")");
                    unimportantCounter = 0;
                }

            } else {
                importantCounter++;
                // vision.setHorizontal(0);
            }
        } else {
            if (vision.limelight.checkTargets()) {
                importantCounter = 0;
                turret.moveShooter(vision.limeLightPID.pidCalculate(0, vision.limelight.getx()));
            } else {
                importantCounter++;

            }
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
        // vision.turnLight(false);
    }

    // Returns true when the command should end.
    @Override
    public boolean isFinished() {
        boolean retVal = true;
        double joyValx;

        joyValx = controller.getRightStickX();

        if (joyValx > -0.01 && joyValx < 0.01) {
            retVal = false;
        }
        if (importantCounter >= 40) {
            retVal = true;
        }
        return retVal;
    }
}
