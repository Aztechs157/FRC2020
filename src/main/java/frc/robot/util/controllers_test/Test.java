package frc.robot.util.controllers_test;

class Test {

    // Define posible controller types
    private ControllerManager manager = new ControllerManager(new LogitechController(0), new DummyController(0, 1));

    public void attachBindings() {
        // How to create bindings
        manager.use(LogitechController.A, DummyController.UP).whenPressed(() -> System.out.println("Test"));
        // The above translates too:
        // when Logitech is active use it's A button
        // when Dummy is active use it's UP button

        var value = manager.getAxis(LogitechController.RIGHT_TRIGGER, DummyController.FOOBAR);

        // How to change the active controller
        manager.setActive(DummyController.class);
    }

}
