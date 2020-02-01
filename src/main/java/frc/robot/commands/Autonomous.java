package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.Robot;
import frc.robot.subsystems.Drive;

public class Autonomous extends CommandBase {

    // DriveTarget target = new DriveTarget(2718, 0, 50, 15.0);

    private Drive drive;
    private Double drivepower;
    private double gyropower;

    /**
     * Creates a new Autonomous.
     */
    public Autonomous(Drive drive) {
        this.drive = drive;
        addRequirements(drive);
        // 81.83
        // for (int i = 0; i <= 20; i++) {
        // System.out.println("getAngle" + drive.getAngle());
        // }

        drivepower = drive.drivePID.pidCalculate(83, drive.frontLeft.getPosition());
        drivepower = drive.slew.rateCalculate(drivepower, 1125);
        gyropower = drive.gyroDrivePID.pidCalculate(0, drive.getAngle());
        // Use addRequirements() here to declare subsystem dependencies.
    }

    // Called every time the scheduler runs while the command is scheduled.
    // 2718.75
    // 75.83
    @Override
    public void execute() {
        // drive.AutoDrive(drivepower + gyropower, -drivepower - gyropower);
        drive.autoTurn(.5);
    }

    // Returns true when the command should end.
    @Override
    public boolean isFinished() {
        return drive.frontLeft.getPosition() >= 81.83;
    }

}
