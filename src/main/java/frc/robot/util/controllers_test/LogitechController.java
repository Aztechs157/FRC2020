package frc.robot.util.controllers_test;

public class LogitechController extends ControllerType {

    public LogitechController(final int port) {
        super(LogitechController.self, port);
    }

    private static final String self = LogitechController.class.getName();

    public static final ButtonDef A = new ButtonDef(0, 1, self);
    public static final ButtonDef B = new ButtonDef(0, 2, self);
    public static final ButtonDef X = new ButtonDef(0, 3, self);
    public static final ButtonDef Y = new ButtonDef(0, 4, self);

    public static final AxisDef RIGHT_TRIGGER = new AxisDef(0, 0, self);
}
