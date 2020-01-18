/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

// import java.util.ResourceBundle.Control;

// import edu.wpi.first.wpilibj.AnalogGyro;
// import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.XboxController;
import frc.robot.subsystems.Conveyer;
// import edu.wpi.first.wpilibj.CounterBase.EncodingType;
import frc.robot.subsystems.Drive;
import frc.robot.subsystems.Intake;
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.Vision;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;
import frc.robot.Constants;
import frc.robot.commands.Autonomous;
import frc.robot.commands.LaserFire;
import frc.robot.commands.RunIntake;
import frc.robot.commands.TrackTarget;

/**
 * This class is where the bulk of the robot should be declared.  Since Command-based is a
 * "declarative" paradigm, very little robot logic should actually be handled in the {@link Robot}
 * periodic methods (other than the scheduler calls).  Instead, the structure of the robot
 * (including subsystems, commands, and button mappings) should be declared here.
 */
public class RobotContainer {
  // The robot's subsystems and commands are defined here...
  public static Conveyer conveyer = new Conveyer();
  public static Drive  drive = new Drive();
  public static Intake intake = new Intake();
  public static Command runIntakeCommand = new RunIntake();
  public static Vision vision = new Vision();
  public static Shooter shooter = new Shooter(); 
  
  public final static Joystick joystick = new Joystick(5);
  public static Joystick controller1 = new Joystick(3);
  public static Joystick controller2 = new Joystick(2);
  public JoystickButton x = new JoystickButton(controller2, 3);
  public JoystickButton b = new JoystickButton(controller2, 2);
  public JoystickButton y = new JoystickButton(controller2, 4);
  public JoystickButton a = new JoystickButton(controller2, 1);
  public JoystickButton a2;
  public JoystickButton b2;
  public JoystickButton x2;
  public JoystickButton y2;
  private final JoystickButton Joystickbutton = new JoystickButton(joystick, Constants.OIConstants.Ybutton);

  /**
   * The container for the robot.  Contains subsystems, OI devices, and commands.
   */
  public RobotContainer() {
    // Configure the button bindings

    configureButtonBindings();
  }

  /**
   * Use this method to define your button->command mappings.  Buttons can be created by
   * instantiating a {@link GenericHID} or one of its subclasses ({@link
   * edu.wpi.first.wpilibj.Joystick} or {@link XboxController}), and then passing it to a
   * {@link edu.wpi.first.wpilibj2.command.button.JoystickButton}.
   */
  private void configureButtonBindings() {
    Joystickbutton.whileHeld(runIntakeCommand);
    Joystickbutton.whenReleased(() -> RobotContainer.intake.IntakeRight.set(0.0));
    
    a.whenPressed((Command) new TrackTarget());
    b.whenPressed((Command) new LaserFire(true));
    b.whenReleased((Command) new LaserFire(false));
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

