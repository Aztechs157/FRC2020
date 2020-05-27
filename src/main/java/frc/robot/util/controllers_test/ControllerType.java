package frc.robot.util.controllers_test;

import edu.wpi.first.wpilibj.Joystick;
import java.util.HashMap;

public class ControllerType {

    // A class representing a button definition
    public static class ButtonDef {
        public final int joystickId;
        public final int buttonId;
        public final String controllerType;

        public ButtonDef(final int joystickId, final int buttonId, final String controllerType) {
            this.joystickId = joystickId;
            this.buttonId = buttonId;
            this.controllerType = controllerType;
        }
    }

    // A class representing a axis definition
    public static class AxisDef {
        public final int joystickId;
        public final int axisId;
        public final String controllerType;

        public AxisDef(final int joystickId, final int axisId, final String controllerType) {
            this.joystickId = joystickId;
            this.axisId = axisId;
            this.controllerType = controllerType;
        }
    }

    // Used to identify the type of controller later
    public final String controllerType;

    // Maps def ids -> joystick id
    private final int[] joystickIds;
    // Stores the created Joystick objects by def id
    private final HashMap<Integer, Joystick> joystickCache = new HashMap<>();

    public ControllerType(final String controllerType, final int... joystickIds) {
        this.controllerType = controllerType;
        this.joystickIds = joystickIds;
    }

    // Helper method for getButton and getAxis
    // Get a joystick or create one if it doesn't exist
    private Joystick getJoystick(final int joystickId) {
        if (!joystickCache.containsKey(joystickId)) {
            joystickCache.put(joystickId, new Joystick(joystickIds[joystickId]));
        }
        return joystickCache.get(joystickId);
    }

    public boolean getButton(final ButtonDef def) {
        return getJoystick(def.joystickId).getRawButton(def.buttonId);
    }

    public double getAxis(final AxisDef def) {
        return getJoystick(def.joystickId).getRawAxis(def.axisId);
    }
}
