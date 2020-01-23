package frc.robot.util;

import edu.wpi.first.wpilibj.Timer;

public class SlewRate {

    private double lastRate;
    private double lastTime;
    private final double standardMaxAccel;

    public SlewRate(final double maxAccel) {
        lastTime = Timer.getFPGATimestamp();
        standardMaxAccel = maxAccel;
        lastRate = 0;
    }

    public double rateCalculate(final double desired) {
        return rateCalculate(desired, standardMaxAccel);
    }

    public double rateCalculate(final double desired, final double maxAccel) {

        final double deltaTime = Timer.getFPGATimestamp() - lastTime;
        final double desiredAccel = (desired - lastRate) / deltaTime;
        double addedRate;
        double newRate;

        if (Math.abs(desiredAccel) < maxAccel) {
            addedRate = desiredAccel * deltaTime;
            newRate = addedRate + lastRate;
        } else {
            addedRate = ((desiredAccel > 0) ? 1 : -1) * maxAccel * deltaTime;
            newRate = addedRate + lastRate;
        }
        lastTime = lastTime + deltaTime;
        lastRate = newRate;

        final double returnVal = newRate;
        return returnVal;
    }

    public double getLastTime() {
        return lastTime;
    }

    public void setLastTime(final double lastTime) {
        this.lastTime = lastTime;
    }

    public void reinit() {
        lastTime = Timer.getFPGATimestamp();
        lastRate = 0;
    }

    public double getLastRate() {
        return lastRate;
    }

    public void setLastRate(final double lastRate) {
        this.lastRate = lastRate;
    }
}
