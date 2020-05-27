package frc.robot.util.controllers_test;

public class DummyController extends ControllerType {

    public DummyController(final int port1, final int port2) {
        super(DummyController.self, port1, port2);
    }

    private static final String self = DummyController.class.getName();

    public static final ButtonDef UP = new ButtonDef(0, 1, self);
    public static final ButtonDef DOWN = new ButtonDef(0, 2, self);
    public static final ButtonDef LEFT = new ButtonDef(0, 3, self);
    public static final ButtonDef RIGHT = new ButtonDef(0, 4, self);

    public static final AxisDef FOOBAR = new AxisDef(0, 0, self);
}
