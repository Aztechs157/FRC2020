package frc.robot.subsystems;

import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.commands.IntakeTrigger;
import frc.robot.util.LogitechController;
import frc.robot.util.NEO;

public class Intake extends SubsystemBase {
    // public final NEO IntakeRight;
    /**
     * Creates a new Intake.
     */
    public NEO intake;

    public Intake(LogitechController controller) {
        intake = new NEO(Constants.ShooterConstants.Intake, MotorType.kBrushless);
        setDefaultCommand(new IntakeTrigger(this, controller));
    }

    // public void runIntake(final double speed) {
    // intake.set(speed);
    // }

    @Override
    public void periodic() {
        // This method will be called once per scheduler run
    }
}
