package frc.robot.util;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;

/**
 * A util class to make dealing with our logitech controllers easier
 */
public class LogitechController {

    /**
     * A enum to map ids to their buttons
     */
    private enum ButtonIDMap {
        A(1), B(2), X(3), Y(4), LeftButton(5), RightButton(6), Back(7), Start(8), LeftStickPush(9), RightStickPush(10);

        private final int value;

        private ButtonIDMap(final int value) {
            this.value = value;
        }
    }

    /** The raw underlying joystick */
    private final Joystick joystick;

    /**
     * Each index of the array corisponds to a button id
     *
     * Make sure number of nulls matches number of button ids
     */
    private final JoystickButton[] buttons = { null, null, null, null, null, null, null, null, null, null, null };

    /**
     * @param port Please just pick 0
     */
    public LogitechController(final int port) {
        joystick = new Joystick(port);
    }

    /**
     * @deprecated Use the new individual axis methods insted e.g
     *             {@link LogitechControler#getLeftStickX()} or
     *             {@link LogitechControler#getRightTrigger()}
     */
    @Deprecated
    public double getRawAxis(int axis) {
        return joystick.getRawAxis(axis);
    }

    /*
     * JavaScript code to auto generate the folowing part of the file:
     *
     * 'LeftStickX LeftStickY LeftTrigger RightTrigger RightStickX
     * RightStickY'.split(' ').map(s => 'get' + s).map((s, i) => `public double
     * ${s}() { return joystick.getRawAxis(${i}); }`).join(' ');
     */

    public double getLeftStickX() {
        return joystick.getRawAxis(0);
    }

    public double getLeftStickY() {
        return joystick.getRawAxis(1);
    }

    public double getLeftTrigger() {
        return joystick.getRawAxis(2);
    }

    public double getRightTrigger() {
        return joystick.getRawAxis(3);
    }

    public double getRightStickX() {
        return joystick.getRawAxis(4);
    }

    public double getRightStickY() {
        return joystick.getRawAxis(5);
    }

    /*
     * JavaScript code to auto generate the folowing part of the file:
     *
     * 'A B X Y LeftButton RightButton Back Start LeftStickPush RightStickPush'
     * .split(' ').map(key => `public JoystickButton ${key}() { if
     * (buttons[ButtonIDMap.${key}.value] == null) {
     * buttons[ButtonIDMap.${key}.value] = new JoystickButton(joystick,
     * ButtonIDMap.${key}.value); } return buttons[ButtonIDMap.${key}.value]; }`
     * ).join(' ');
     */

    public JoystickButton A() {
        if (buttons[ButtonIDMap.A.value] == null) {
            buttons[ButtonIDMap.A.value] = new JoystickButton(joystick, ButtonIDMap.A.value);
        }
        return buttons[ButtonIDMap.A.value];
    }

    public JoystickButton B() {
        if (buttons[ButtonIDMap.B.value] == null) {
            buttons[ButtonIDMap.B.value] = new JoystickButton(joystick, ButtonIDMap.B.value);
        }
        return buttons[ButtonIDMap.B.value];
    }

    public JoystickButton X() {
        if (buttons[ButtonIDMap.X.value] == null) {
            buttons[ButtonIDMap.X.value] = new JoystickButton(joystick, ButtonIDMap.X.value);
        }
        return buttons[ButtonIDMap.X.value];
    }

    public JoystickButton Y() {
        if (buttons[ButtonIDMap.Y.value] == null) {
            buttons[ButtonIDMap.Y.value] = new JoystickButton(joystick, ButtonIDMap.Y.value);
        }
        return buttons[ButtonIDMap.Y.value];
    }

    public JoystickButton LeftButton() {
        if (buttons[ButtonIDMap.LeftButton.value] == null) {
            buttons[ButtonIDMap.LeftButton.value] = new JoystickButton(joystick, ButtonIDMap.LeftButton.value);
        }
        return buttons[ButtonIDMap.LeftButton.value];
    }

    public JoystickButton RightButton() {
        if (buttons[ButtonIDMap.RightButton.value] == null) {
            buttons[ButtonIDMap.RightButton.value] = new JoystickButton(joystick, ButtonIDMap.RightButton.value);
        }
        return buttons[ButtonIDMap.RightButton.value];
    }

    public JoystickButton Back() {
        if (buttons[ButtonIDMap.Back.value] == null) {
            buttons[ButtonIDMap.Back.value] = new JoystickButton(joystick, ButtonIDMap.Back.value);
        }
        return buttons[ButtonIDMap.Back.value];
    }

    public JoystickButton Start() {
        if (buttons[ButtonIDMap.Start.value] == null) {
            buttons[ButtonIDMap.Start.value] = new JoystickButton(joystick, ButtonIDMap.Start.value);
        }
        return buttons[ButtonIDMap.Start.value];
    }

    public JoystickButton LeftStickPush() {
        if (buttons[ButtonIDMap.LeftStickPush.value] == null) {
            buttons[ButtonIDMap.LeftStickPush.value] = new JoystickButton(joystick, ButtonIDMap.LeftStickPush.value);
        }
        return buttons[ButtonIDMap.LeftStickPush.value];
    }

    public JoystickButton RightStickPush() {
        if (buttons[ButtonIDMap.RightStickPush.value] == null) {
            buttons[ButtonIDMap.RightStickPush.value] = new JoystickButton(joystick, ButtonIDMap.RightStickPush.value);
        }
        return buttons[ButtonIDMap.RightStickPush.value];
    }
}
