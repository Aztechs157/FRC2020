package frc.robot.util;

import com.revrobotics.CANSparkMax;
import com.revrobotics.jni.CANSparkMaxJNI;

import java.util.concurrent.atomic.AtomicBoolean;

import com.revrobotics.CANEncoder;
import com.revrobotics.CANError;

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

    public double getVelocity() {
        return encoder.getVelocity();
    }

    public void setPositionConversionFactor(double factor) {
        encoder.setPositionConversionFactor(factor);
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
