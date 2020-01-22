/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import frc.robot.subsystems.Conveyer;
import frc.robot.subsystems.Drive;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.Shooter;
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

    private static final LogitechController controller = new LogitechController(0);

    // #region Subsystems
    private static final Conveyer conveyer = new Conveyer(controller);
    private static final Drive drive = new Drive(controller);
    private static final Intake intake = new Intake();
    private static final Vision vision = new Vision();
    private static final Shooter shooter = new Shooter(controller);
    // #endregion

    public RobotContainer() {
        configureButtonBindings();
    }

    /**
     * Put button controls here
     */
    private void configureButtonBindings() {
        controller.A().whenPressed(new TrackTarget(shooter, vision, controller));
        controller.B().whenPressed(new LaserFire(true, vision));
        controller.B().whenReleased(new LaserFire(false, vision));
    }

    /**
     * Put Autonomus command here
     */
    public Command getAutonomousCommand() {
        return new Autonomous();
    }
}
