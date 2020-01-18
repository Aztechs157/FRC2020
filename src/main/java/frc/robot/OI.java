/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.JoystickButton;
import frc.robot.commands.CheckVision;
import frc.robot.commands.LaserFire;
import frc.robot.commands.TrackTarget;
import frc.robot.commands.VisionPrintout;

/**
 * This class is the glue that binds the controls on the physical operator
 * interface to the commands and command groups that allow control of the robot.
 */
public class OI {
  //// CREATING BUTTONS
  // One type of button is a joystick button which is any button on a
  //// joystick.
  // You create one by telling it which joystick it's on and which button
  // number it is.
  // Joystick stick = new Joystick(port);
  // Button button = new JoystickButton(stick, buttonNumber);

  // There are a few additional built in buttons you can use. Additionally,
  // by subclassing Button you can create custom triggers and bind those to
  // commands the same as any other Button.

  //// TRIGGERING COMMANDS WITH BUTTONS
  // Once you have a button, it's trivial to bind it to a button in one of
  // three ways:

  // Start the command when the button is pressed and let it run the command
  // until it is finished as determined by it's isFinished method.
  // button.whenPressed(new ExampleCommand());

  // Run the command while the button is being held down and interrupt it once
  // the button is released.
  // button.whileHeld(new ExampleCommand());

  // Start the command when the button is released and let it run the command
  // until it is finished as determined by it's isFinished method.
  // button.whenReleased(new ExampleCommand());
  public Joystick controller1;
  public Joystick controller2;
  public JoystickButton x;
  public JoystickButton b;
  public JoystickButton y;
  public JoystickButton a;
  public JoystickButton a2;
  public JoystickButton b2;
  public JoystickButton x2;
  public JoystickButton y2;

  public OI (){
    //controller1 = new Joystick(3);
    controller2 = new Joystick(2);
    a = new JoystickButton(controller2, 1);
    b = new JoystickButton(controller2, 2);
    x = new JoystickButton(controller2, 3);
    y = new JoystickButton(controller2, 4); 
    a.whenPressed(new TrackTarget());
    b.whenPressed(new LaserFire(true));
    b.whenReleased(new LaserFire(false));
  }
}
