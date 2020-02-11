/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import frc.robot.subsystems.Conveyor;
import frc.robot.subsystems.Drive;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.Kicker;
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.Turret;
import frc.robot.subsystems.Vision;
import frc.robot.util.LogitechController;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.commands.Autonomous;
import frc.robot.commands.LaserFire;
import frc.robot.commands.TrackTarget;

/**
 * The container for the robot. Contains subsystems, OI devices, and commands.
 */
public class RobotContainer {

    private final LogitechController driveController = new LogitechController(0);
    private final LogitechController operatorController = new LogitechController(1);

    // #region Subsystems
    // private static final Conveyor conveyor = new Conveyor(driveController);
    // private static final Drive drive = new Drive(driveController);
    private final Intake intake = new Intake(driveController);
    private final Vision vision = new Vision();
    private final Turret turret = new Turret(operatorController);
    private final Shooter shooter = new Shooter(operatorController);
    private final Kicker kicker = new Kicker(driveController);
    private final Conveyor conveyor = new Conveyor(driveController, intake, kicker);
    private final Drive drive = new Drive(driveController);
    // #endregion

    // comments

    public RobotContainer() {
        configureButtonBindings();
    }

    /**
     * Put button controls here
     */
    private void configureButtonBindings() {
        operatorController.A().whenPressed(new TrackTarget(turret, vision, operatorController));
        operatorController.B().whenPressed(new LaserFire(true, vision));
        operatorController.B().whenReleased(new LaserFire(false, vision));
    }

    /**
     * Put Autonomus command here
     */
    public Command getAutonomousCommand() {
        return new Autonomous(drive);
    }
}
