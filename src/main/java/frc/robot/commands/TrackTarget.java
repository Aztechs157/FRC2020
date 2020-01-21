/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import edu.wpi.first.wpilibj2.command.Subsystem;
import frc.robot.RobotContainer;
import frc.robot.util.Pixy2Controller.Target;
import frc.robot.subsystems.Shooter;

public class TrackTarget extends CommandBase {
    double map(double x, double in_min, double in_max, double out_min, double out_max) {
        return (x - in_min) * (out_max - out_min) / (in_max - in_min) + out_min;
    }

    // private final double LRMUL = 0.00001;
    // private final double UDMUL = 0.004;
    private final double LRTARGET = 158;
    int count = 0;
    private final Shooter shooter;

    /**
     * Creates a new TrackTarget2.
     */
    public TrackTarget(Shooter shooter) {
        this.shooter = shooter;
        // Use addRequirements() here to declare subsystem dependencies.
        addRequirements(RobotContainer.vision);// (Subsystem)
        addRequirements(shooter);
    }

    // Called when the command is initially scheduled.
    @Override
    public void initialize() {
        RobotContainer.vision.turnLight(true);
        count = 0;
    }

    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute() {
        Target[] targets = RobotContainer.vision.getBlocks();
        System.out.println(targets.length);
        if (targets.length == 1) {
            count = 0;
            // System.out.println(RobotContainer.vision.LR+(-(LRTARGET-targets[0].x))*LRMUL);
            shooter.moveShooter(RobotContainer.vision.pid.pidCalculate(LRTARGET, targets[0].x) * 0.1);
            RobotContainer.vision.setVertical(map(targets[0].y, 0, 207, 0.65, 0.4));

        } else {
            count++;
            // RobotContainer.vision.setHorizontal(0);
        }
    }

    // Called once the command ends or is interrupted.
    @Override
    public void end(boolean interrupted) {

        RobotContainer.vision.setHorizontal(0);
        RobotContainer.vision.turnLight(false);
    }

    // Returns true when the command should end.
    @Override
    public boolean isFinished() {
        boolean retVal = true;
        double joyValx;

        joyValx = RobotContainer.joystick.getRawAxis(4);

        if (joyValx > -0.01 && joyValx < 0.01) {
            retVal = false;
        }
        if (count >= 40) {
            retVal = true;
        }
        return retVal;
    }
}
