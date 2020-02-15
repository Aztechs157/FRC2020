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
import frc.robot.util.controllers.Controller;
import frc.robot.util.controllers.LogitechController;
import frc.robot.util.controllers.PlaneController;
import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.commands.AutoGroup;
import frc.robot.commands.LaserFire;
import frc.robot.commands.ShooterControl;
import frc.robot.commands.TrackTarget;

/**
 * The container for the robot. Contains subsystems, OI devices, and commands.
 */
public class RobotContainer {

    private final Controller driveController = Preferences.getInstance().getBoolean("usePlaneController", false)
            ? new PlaneController(0, 2)
            : new LogitechController(0);
    private final Controller operatorController = new LogitechController(1);

    // #region Subsystems
    // private static final Conveyor conveyor = new Conveyor(driveController);
    // private static final Drive drive = new Drive(driveController);
    private final Intake intake = new Intake(driveController);
    private final Vision vision = new Vision();
    private final Turret turret = new Turret(operatorController);
    private final Kicker kicker = new Kicker(driveController, intake);
    private final Conveyor conveyor = new Conveyor(driveController, intake, kicker);
    private final Shooter shooter = new Shooter(operatorController, kicker, conveyor);
    public final Drive drive = new Drive(driveController);
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
        driveController.Y().whenPressed(() -> {
            intake.zeroBallCount();
        }, intake);

        operatorController.RightButton().whenPressed(new ShooterControl(shooter, operatorController));
        driveController.Back().and(driveController.Start()).whenActive(() -> drive.isArcade = !drive.isArcade, drive);
    }

    /**
     * Put Autonomus command here
     */
    public Command getAutonomousCommand() {
        return new AutoGroup(drive);
    }
}
