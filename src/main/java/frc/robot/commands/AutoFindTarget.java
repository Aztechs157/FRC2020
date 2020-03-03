/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
// import frc.robot.subsystems.Intake;
import frc.robot.subsystems.Turret;
import frc.robot.subsystems.Vision;

public class AutoFindTarget extends CommandBase {
    private Turret turret;
    private Vision vision;

    /**
     * Creates a new AutoFindtTarget.
     */
    public AutoFindTarget(Turret turret, Vision vision) {
        this.turret = turret;
        this.vision = vision;
        // Use addRequirements() here to declare subsystem dependencies.
    }

    // Called when the command is initially scheduled.
    @Override
    public void initialize() {
        vision.turnLight(true);
    }

    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute() {
        turret.moveShooter(-0.2);

    }

    // Called once the command ends or is interrupted.
    @Override
    public void end(boolean interrupted) {
        turret.LeftRight.set(0);
    }

    // Returns true when the command should end.
    @Override
    public boolean isFinished() {
        return vision.limelight.checkTargets();
    }
}
