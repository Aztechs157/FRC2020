package frc.robot.commands;

import frc.robot.PID_Wescott;
import frc.robot.Robot;
import frc.robot.SlewRate;
import frc.robot.subsystems.Drive;
import edu.wpi.first.wpilibj.Timer;

public class DriveTarget
{
    private int target;
    private double startTime;
    private double time;
    private double targetAngle;
    private boolean slewCut;
    private double encoder;
    private double drivePower;
    private PID_Wescott drivePID;
    private PID_Wescott gyroDrivePID;
    private SlewRate slewRate;
    private double leftPower;
    private double rightPower;
    private int repsAtTarget;
    private int tolerance;
    private boolean firstIteration;
    private boolean megaSlew = false;

    public DriveTarget(int target, double targetAngle, int tolerance, double time)
    {
        this.target = target;
        this.time = time;
        this.targetAngle = targetAngle;
        this.tolerance = tolerance;
        slewCut = false;
        drivePID = new PID_Wescott(0.028, 0.1, 0.000005, 10, 0, 999999, 0, 3, -3);
        gyroDrivePID = new PID_Wescott(0.03, 0, 0.000002, 999999, 0, 999999, 0, 3, -3);
        slewRate = new SlewRate(0.8);
        firstIteration = true;
    }

    public DriveTarget(int target, double targetAngle, int tolerance, double time, boolean slew)
    {
        this.target = target;
        this.time = time;
        this.targetAngle = targetAngle;
        this.slewCut = slew;
        this.tolerance = tolerance;
        //.28, .1, .000005, 10, 0, 999999, 0, 3, -3
        drivePID = new PID_Wescott(0.028, 0.1, 0.000005, 10, 0, 999999, 0, 3, -3);
        gyroDrivePID = new PID_Wescott(0.03, 0, 0.000002, 999999, 0, 999999, 0, 3, -3);
        slewRate = new SlewRate(0.5);
        slewCut = !slew;
        firstIteration = true;
    }
    public DriveTarget(int target, double targetAngle, int tolerance, double time, boolean slew, boolean megaSlew)
    {
        this.target = target;
        this.time = time;
        this.targetAngle = targetAngle;
        this.slewCut = slew;
        this.tolerance = tolerance;
        drivePID = new PID_Wescott(0.028, 0.1, 0.000005, 10, 0, 999999, 0, 3, -3);
        gyroDrivePID = new PID_Wescott(0.03, 0, 0.000002, 999999, 0, 999999, 0, 3, -3);
        slewRate = new SlewRate(0.5);
        slewCut = !slew;
        firstIteration = true;
        this.megaSlew = megaSlew;
    }

    public boolean execute() {

        if (firstIteration) {
            startTime = Timer.getFPGATimestamp();
            slewRate = new SlewRate((!megaSlew)? 1.6: 0.2);
            firstIteration = false;
            //System.out.println("New Motion");
        }
        //System.out.println("Right Encoder: " + Robot.drive.getRightEncoder());
        //System.out.println("Left Encoder: " + Robot.drive.getLeftEncoder());
        //System.out.println("Encoder: " + encoder);
        encoder = (Drive.getRightEncoder()+Drive.getLeftEncoder())/2.0;
        //System.out.println("Encoder: " + encoder);
        drivePower = drivePID.pidCalculate(target, encoder);
        
        if(!slewCut) {
            drivePower = slewRate.rateCalculate(drivePower);
        }
        if(Math.abs(drivePower)>=0.9) {
            slewCut = true;
        }

        leftPower = drivePower - gyroDrivePID.pidCalculate(targetAngle, Drive.getAngle());
        leftPower = ((leftPower > 0) ? 1 : -1) * Math.min(1, Math.abs(leftPower));

        rightPower = drivePower + gyroDrivePID.pidCalculate(targetAngle, Drive.getAngle());
        rightPower = ((rightPower > 0) ? 1 : -1) * Math.min(1, Math.abs(rightPower));

        Drive.AutoDrive(leftPower, rightPower);
        if (Math.abs(encoder - target) < tolerance)
        {
            repsAtTarget++;
            if (repsAtTarget >= 5)
            {
                return true;
            }
            else {
                return false;
            }
        }
        else if (Timer.getFPGATimestamp()-startTime>= time) {
            return true;
        }
        else
        {
            repsAtTarget = 0;
            return false;
        }
    }
}