package frc.robot.util;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANEncoder;

public class NEO extends CANSparkMax {

    public final CANEncoder encoder;

    public NEO(final int port, MotorType type) {
        super(port, type);
        encoder = super.getEncoder();
    }

    // Decorator methods

    public NEO inverted() {
        super.setInverted(true);
        return this;
    }

    public NEO brushed() {
        super.setMotorType(MotorType.kBrushed);
        return this;
    }

    // Convenience methods

    public double getPosition() {
        return encoder.getPosition();
    }

    public void tare() {
        encoder.setPosition(0);
    }

    public void setBrakeMode() {
        super.setIdleMode(IdleMode.kBrake);
    }

    public void setCoastMode() {
        super.setIdleMode(IdleMode.kCoast);
    }

}
