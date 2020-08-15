package frc.robot;

/**
 * RoboMath
 */
public class RoboMath {

    // public static double toInches(double ticks) {
    // return ticks * Const.TICKS_TO_INCHES_RATIO;
    // }
    // TODO this conversion is most certainly incorrect.
    public static double ticksToMeters(double ticks) {
        return ticks * (2 * Math.PI * .0762) / 7;
    }
}
