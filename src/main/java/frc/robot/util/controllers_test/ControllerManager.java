package frc.robot.util.controllers_test;

import edu.wpi.first.wpilibj2.command.button.Button;
import java.util.HashMap;
import frc.robot.util.controllers_test.ControllerType.ButtonDef;
import frc.robot.util.controllers_test.ControllerType.AxisDef;

public class ControllerManager {

    // Maps string names of controller types to actual instances of ControllerType
    private final HashMap<String, ControllerType> singletonMap = new HashMap<>();
    // The currently active controller type
    private String activeControllerType;

    public ControllerManager(final ControllerType... controllers) {
        // It doesn't make sense to make a manager with no controller
        // The following construtor code makes use of this assumsion
        if (controllers.length < 1) {
            throw new IllegalArgumentException("ControllerManager must have at least one controller!");
        }
        // The first controller in the list gets active by default
        this.activeControllerType = controllers[0].controllerType;
        // Populate the singletonMap
        for (var controller : controllers) {
            singletonMap.put(controller.controllerType, controller);
        }
    }

    public Button use(final ButtonDef... defs) {
        // Return a wpi Button so we can use them like normal button defs
        return new Button(() -> {
            for (var def : defs) {
                // If it's not active skip to the next
                if (def.controllerType != activeControllerType) {
                    continue;
                }
                // "Safety net" If it's false we try other buttons before giving up
                if (singletonMap.get(activeControllerType).getButton(def)) {
                    return true;
                }
            }
            // Default to false
            return false;
        });
    }

    public double getAxis(final AxisDef... defs) {
        for (var def : defs) {
            // If it's the active one return it immediately
            // It doesn't make sense for axises to have the same safety net as buttons do
            if (def.controllerType == activeControllerType) {
                return singletonMap.get(activeControllerType).getAxis(def);
            }
        }
        // Default to 0
        return 0;
    }

    // Set the active controller type with a class or a raw string
    public void setActive(Class<?> controllerType) {
        setActive(controllerType.getName());
    }

    public void setActive(String controllerType) {
        this.activeControllerType = controllerType;
    }
}
