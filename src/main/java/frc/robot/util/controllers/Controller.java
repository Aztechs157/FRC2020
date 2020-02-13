package frc.robot.util.controllers;

import edu.wpi.first.wpilibj2.command.button.JoystickButton;

public interface Controller {

    public JoystickButton A();

    public JoystickButton B();

    public JoystickButton X();

    public JoystickButton Y();

    public JoystickButton LeftButton();

    public JoystickButton RightButton();

    public JoystickButton Back();

    public JoystickButton Start();

    public JoystickButton LeftStickPush();

    public JoystickButton RightStickPush();

    /**
     * @deprecated Use the new individual axis methods insted e.g
     *             {@link LogitechController#getLeftStickX()} or
     *             {@link LogitechController#getRightTrigger()}
     */
    @Deprecated
    public double getRawAxis(int axis);

    public double getLeftStickX();

    public double getLeftStickY();

    public double getLeftTrigger();

    public double getRightTrigger();

    public double getRightStickX();

    public double getRightStickY();
}
