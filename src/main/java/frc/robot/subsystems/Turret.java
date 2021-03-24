package frc.robot.subsystems;

import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import com.revrobotics.CANEncoder;
import com.revrobotics.CANSparkMax;

import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.util.PID;
import frc.robot.util.controllers.ControllerSet;
import frc.robot.commands.TurretControl;

public class Turret extends SubsystemBase {
    /**
     * Creates a new Shooter2.
     */
    public CANSparkMax leftright;
    public CANSparkMax UpDown;

    // private int Count;

    // public Shooter() {
    // lrEncoder = new Servo(0);
    // UpDown = new Talon(1);
    // testTalon = new AnalogPotentiometer(1);
    // }
    public double position = 0;
    private PID turretPID = new PID(0.0001, 0, 0, 0, 0, 0, 0, 0, 0);

    public void run(double s) {
        leftright.set(s);
    }

    public Turret(ControllerSet controller) {
        leftright = new CANSparkMax(Constants.ShooterConstants.TurretMotorID, MotorType.kBrushless);
        lrEncoder = leftright.getEncoder();
        // UpDown = new NEO(0, MotorType.kBrushless);
        setDefaultCommand(new TurretControl(this, controller));
        // Shuffleboard.getTab("Test").addNumber("TurretPos", lrEncoder::getPosition);
    }

    public CANEncoder lrEncoder;

    public void moveShooter(final double Speed) {

        // if (joyValy > -0.01 && joyValy < 0.01)
        // UpDown.set(0.0);
        // else if (joyValy > 0) {
        // if (UpDown.getPosition() <= 50) {
        // UpDown.set(1 * Scale * joyValy);
        // }
        // else {
        // UpDown.set(0.0);
        // }
        // }
        // else if (UpDown.getPosition() >= -50) {
        // UpDown.set(1 * Scale * joyValy);
        // }
        // else {
        // UpDown.set(0.0);
        // }

        if (Speed > -0.01 && Speed < 0.01) {
            leftright.set(0.0);
        } else if (Speed > 0) {
            if (lrEncoder.getPosition() <= 50) {
                leftright.set(1 * Speed);
            } else {
                leftright.set(0.0);
            }
        } else if (lrEncoder.getPosition() >= -90) {
            leftright.set(1 * Speed);
        } else {
            leftright.set(0.0);
        }
    }

    public void MoveTurret() {
        run(turretPID.pidCalculate(position, lrEncoder.getPosition()));
    }

    public void MoveTurret(double toPosition) {
        if (toPosition <= lrEncoder.getPosition()) {
            run(turretPID.pidCalculate(position, lrEncoder.getPosition()));
        } else {
            leftright.set(0);
        }
    }

    @Override
    public void periodic() {
        // This method will be called once per scheduler run
        // getBlocks();
        // System.out.println();

    }

    public double driveMap(double in, double deadZone) {
        if (in <= deadZone && in >= -deadZone) {
            return 0;
        } else if (in < deadZone) {
            return map(in, -1, -deadZone, -1, 0);
        } else {
            return map(in, deadZone, 1, 0, 1);
        }
    }

    private double map(final double x, final double in_min, final double in_max, final double out_min,
            final double out_max) {
        return (x - in_min) * (out_max - out_min) / (in_max - in_min) + out_min;
    }
}
