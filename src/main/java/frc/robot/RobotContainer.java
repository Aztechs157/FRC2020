/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.XboxController;
import frc.robot.subsystems.Conveyer;
import frc.robot.subsystems.Drive;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.Vision;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import frc.robot.commands.Autonomous;
import frc.robot.commands.LaserFire;
import frc.robot.commands.RunIntake;
import frc.robot.commands.TrackTarget;

/**
 * This class is where the bulk of the robot should be declared. Since
 * Command-based is a "declarative" paradigm, very little robot logic should
 * actually be handled in the {@link Robot} periodic methods (other than the
 * scheduler calls). Instead, the structure of the robot (including subsystems,
 * commands, and button mappings) should be declared here.
 */
public class RobotContainer {

    // #region Subsystems
    private static Conveyer conveyer = new Conveyer();
    private static Drive drive = new Drive();
    private static Intake intake = new Intake();
    private static Vision vision = new Vision();
    private static Shooter shooter = new Shooter();
    // #endregion

    public final static Joystick joystick = new Joystick(5);
    public JoystickButton x = new JoystickButton(joystick, 3);
    public JoystickButton b = new JoystickButton(joystick, 2);
    public JoystickButton y = new JoystickButton(joystick, 4);
    public JoystickButton a = new JoystickButton(joystick, 1);
    public JoystickButton a2;
    public JoystickButton b2;
    public JoystickButton x2;
    public JoystickButton y2;

    /**
     * The container for the robot. Contains subsystems, OI devices, and commands.
     */
    public RobotContainer() {
        configureButtonBindings();
    }

    /**
     * Put button controls here
     */
    private void configureButtonBindings() {
        a.whenPressed(new TrackTarget(shooter, vision, joystick));
        b.whenPressed(new LaserFire(true, vision));
        b.whenReleased(new LaserFire(false, vision));
    }

    /**
     * Use this to pass the autonomous command to the main {@link Robot} class.
     *
     * @return the command to run in autonomous
     */
    public Command getAutonomousCommand() {

        // An ExampleCommand will run in autonomous
        return new Autonomous();
    }
}
