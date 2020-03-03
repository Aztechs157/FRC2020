/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.Shooter;

public class ShooterControl extends CommandBase {

    /**
     * Creates a new ShooterControl.
     */
    private Shooter shooter;
    private Intake intake;

    public ShooterControl(Shooter shooter, Intake intake) {
        this.shooter = shooter;
        this.intake = intake;
        addRequirements(this.shooter);
        // Use addRequirements() here to declare subsystem dependencies.
    }

    // Called when the command is initially scheduled.
    @Override
    public void initialize() {
        shooter.resetStateMachine();
    }

    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute() {
        // System.out.println("Running: " + shooter.shooter.getPosition());
        // System.out.println("ShooterControl.Execute");
        // shooter.SingleAction();
        shooter.automatic();

    }

    // Called once the command ends or is interrupted.
    @Override
    public void end(boolean interrupted) {
        shooter.stopAll();
    }

    // Returns true when the command should end.
    @Override
    public boolean isFinished() {
        return intake.ballCount() == 0;
    }
}
