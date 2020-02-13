package frc.robot.util;

import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

public class NEO {
    private final CANSparkMax motor;
    private double ticks;
    private double offset;

    public NEO(final int port, final MotorType type) {
        motor = new CANSparkMax(port, type);
        ticks = 0;
        offset = 0;
    }

    public double getPosition() {
        ticks = motor.getEncoder().getPosition();
        return ticks - offset;
    }

    public void tare() {
        ticks = motor.getEncoder().getPosition();
        offset = ticks;
    }

    public void setPosition(final double position) {
        ticks = motor.getEncoder().getPosition();
        offset = ticks - position;
    }

    public double getVelocity() {
        return motor.getEncoder().getVelocity();
    }

    double count = 0;

    public void set(final double speed) {
        motor.set(speed);
        if (count++ < 50)
            return;
        count = 0;
        // System.out.println(speed);
    }

    public void setInverted(final boolean bool) {
        motor.setInverted(bool);
    }

    public void setIdleMode(final IdleMode mode) {
        motor.setIdleMode(mode);
    }

    // public void setNeutralMode(final NeutralMode mode) {
    // motor.setNeutralMode(mode);
}
