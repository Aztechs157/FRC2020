package frc.robot;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;


public class NEO {
    private CANSparkMax motor;
    private double ticks;
    private double offset;
	public NEO (int port, MotorType type) {
        motor = new CANSparkMax(port, type);
        ticks = 0;
        offset = 0;
    }
    public double getPosition() {
        ticks = motor.getEncoder().getPosition();
        return ticks-offset;
    }
    public void tare() {
        ticks = motor.getEncoder().getPosition();
        offset = ticks;
    }
    public void setPosition(double position) {
        ticks = motor.getEncoder().getPosition();
        offset = ticks - position;
    }
    public double getVelocity() {
        return motor.getEncoder().getVelocity();
    }
    public void set(double speed) {
        motor.set(speed);
    }
    public void setInverted(boolean bool) {
        motor.setInverted(bool);
    }
    public void setIdleMode(IdleMode mode){
        motor.setIdleMode(mode);
    }


}