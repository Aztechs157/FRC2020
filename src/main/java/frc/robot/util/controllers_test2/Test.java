package frc.robot.util.controllers_test2;

class Test {

    // Define posible controller types
    private final ControllerSet driveSet = new ControllerSet(new LogitechController(0), new PlaneController(0, 1));
    private final ControllerSet opperSet = new ControllerSet(new LogitechController(2));

    public void attachBindings() {
        // How to create bindings
        // Make sure button order here matches type order in the set
        driveSet.useButton(LogitechController.A, PlaneController.LEFT_HAND_MID_UP)
                .whenPressed(() -> System.out.println("Test"));
        // The above translates too:
        // when Logitech is active use it's A button
        // when Plane is active use it's UP button

        final var value = driveSet.useAxis(LogitechController.RIGHT_TRIGGER_HELD,
                PlaneController.RIGHT_HAND_TRIGGER_HELD);

        // How to change the active controller
        driveSet.setActiveModelIndex(1);
    }
}
