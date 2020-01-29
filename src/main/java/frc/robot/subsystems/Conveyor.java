package frc.robot.subsystems;

import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.util.LogitechController;
import frc.robot.util.NEO;

public class Conveyor extends SubsystemBase {

    private final NEO conveyerMotor = new NEO(Constants.ShooterConstants.ConveyerMotorID, MotorType.kBrushless);
    private final LogitechController controller;

    /**
     * Creates a new Conveyer.
     */
    public Conveyor(final LogitechController controller) {
        this.controller = controller;
    }

    @Override
    public void periodic() {
        // System.out.println(controller.getRawAxis(2));
        conveyerMotor.set(controller.getRawAxis(2));
    }
}
