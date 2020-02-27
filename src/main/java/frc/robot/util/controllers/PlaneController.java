package frc.robot.util.controllers;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj2.command.button.JoystickButton;

/**
 * A util class to make dealing with our logitech controllers easier
 */
public class PlaneController implements Controller {

    /**
     * A enum to map ids to their buttons
     */
    private enum ButtonIDMap {
        // A(1), B(2), X(3), Y(4), LeftButton(5), RightButton(6), Back(7), Start(8),
        // LeftStickPush(9), RightStickPush(10);
        Trigger(1), MidDown(2), MidUp(3), MidLeft(4), MidRight(5), TopLeft(6), BottomLeft(7), FarBottomLeft(8),
        FarBottomRight(9), BottomRight(10), TopRight(11);

        private final int value;

        private ButtonIDMap(final int value) {
            this.value = value;
        }
    }

    /** The raw underlying joysticks */
    private final Joystick joystickRight;
    private final Joystick joystickLeft;

    /**
     * Each index of the array corisponds to a button id
     *
     * Make sure number of nulls matches number of button ids
     */
    private final JoystickButton[] buttons = { null, null, null, null, null, null, null, null, null, null, null, null };

    /**
     * @param rightPort Please just pick 0
     */
    public PlaneController(final int rightPort, final int leftPort) {
        joystickRight = new Joystick(rightPort);
        joystickLeft = new Joystick(leftPort);
    }

    /**
     * @deprecated Use the new individual axis methods insted e.g
     *             {@link LogitechController#getLeftStickX()} or
     *             {@link LogitechController#getRightTrigger()}
     */
    @Deprecated
    public double getRawAxis(final int axis) {
        return joystickRight.getRawAxis(axis);
    }

    /*
     * JavaScript code to auto generate the folowing part of the file:
     *
     * 'LeftStickX LeftStickY LeftTrigger RightTrigger RightStickX
     * RightStickY'.split(' ').map(s => 'get' + s).map((s, i) => `public double
     * ${s}() { return joystick.getRawAxis(${i}); }`).join(' ');
     */

    public double getLeftStickX() {
        return joystickLeft.getRawAxis(0);
    }

    public double getLeftStickY() {
        return joystickLeft.getRawAxis(1);
    }

    public double getLeftTrigger() {
        return joystickLeft.getRawAxis(3);
    }

    public double getRightTrigger() {
        return joystickRight.getRawAxis(3);
    }

    public double getRightStickX() {
        return joystickRight.getRawAxis(0);
    }

    public double getRightStickY() {
        return joystickRight.getRawAxis(1);
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
        if (buttons[ButtonIDMap.MidUp.value] == null) {
            buttons[ButtonIDMap.MidUp.value] = new JoystickButton(joystickRight, ButtonIDMap.MidUp.value);
        }
        return buttons[ButtonIDMap.MidUp.value];
    }

    public JoystickButton B() {
        if (buttons[ButtonIDMap.MidDown.value] == null) {
            buttons[ButtonIDMap.MidDown.value] = new JoystickButton(joystickRight, ButtonIDMap.MidDown.value);
        }
        return buttons[ButtonIDMap.MidDown.value];
    }

    public JoystickButton X() {
        if (buttons[ButtonIDMap.MidLeft.value] == null) {
            buttons[ButtonIDMap.MidLeft.value] = new JoystickButton(joystickRight, ButtonIDMap.MidLeft.value);
        }
        return buttons[ButtonIDMap.MidLeft.value];
    }

    public JoystickButton Y() {
        if (buttons[ButtonIDMap.MidRight.value] == null) {
            buttons[ButtonIDMap.MidRight.value] = new JoystickButton(joystickRight, ButtonIDMap.MidRight.value);
        }
        return buttons[ButtonIDMap.MidRight.value];
    }

    public JoystickButton LeftButton() {
        if (buttons[ButtonIDMap.TopLeft.value] == null) {
            buttons[ButtonIDMap.TopLeft.value] = new JoystickButton(joystickRight, ButtonIDMap.TopLeft.value);
        }
        return buttons[ButtonIDMap.TopLeft.value];
    }

    public JoystickButton RightButton() {
        if (buttons[ButtonIDMap.TopRight.value] == null) {
            buttons[ButtonIDMap.TopRight.value] = new JoystickButton(joystickRight, ButtonIDMap.TopRight.value);
        }
        return buttons[ButtonIDMap.TopRight.value];
    }

    public JoystickButton Back() {
        if (buttons[ButtonIDMap.FarBottomLeft.value] == null) {
            buttons[ButtonIDMap.FarBottomLeft.value] = new JoystickButton(joystickRight,
                    ButtonIDMap.FarBottomLeft.value);
        }
        return buttons[ButtonIDMap.FarBottomLeft.value];
    }

    public JoystickButton Start() {
        if (buttons[ButtonIDMap.FarBottomRight.value] == null) {
            buttons[ButtonIDMap.FarBottomRight.value] = new JoystickButton(joystickRight,
                    ButtonIDMap.FarBottomRight.value);
        }
        return buttons[ButtonIDMap.FarBottomRight.value];
    }

    public JoystickButton LeftStickPush() {
        if (buttons[ButtonIDMap.BottomLeft.value] == null) {
            buttons[ButtonIDMap.BottomLeft.value] = new JoystickButton(joystickRight, ButtonIDMap.BottomLeft.value);
        }
        return buttons[ButtonIDMap.BottomLeft.value];
    }

    public JoystickButton RightStickPush() {
        if (buttons[ButtonIDMap.BottomRight.value] == null) {
            buttons[ButtonIDMap.BottomRight.value] = new JoystickButton(joystickRight, ButtonIDMap.BottomRight.value);
        }
        return buttons[ButtonIDMap.BottomRight.value];
    }

    public JoystickButton stick2button6() {
        return new JoystickButton(joystickLeft, ButtonIDMap.TopLeft.value);
    }

    public JoystickButton stick2button7() {
        return new JoystickButton(joystickLeft, ButtonIDMap.BottomLeft.value);
    }

    public JoystickButton stick2Button10() {
        return new JoystickButton(joystickLeft, ButtonIDMap.BottomRight.value);
    }
}
