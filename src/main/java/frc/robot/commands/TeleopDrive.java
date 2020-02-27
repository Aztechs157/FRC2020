/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.subsystems.Drive;

public class TeleopDrive extends CommandBase {
    private Drive drive;
    private final String TANK = "TANK";
    private final String ARCADE_DUAL = "ARCADE_DUAL";
    private final String ARCADE_SOLO = "ARCADE_SOLO";
    private final SendableChooser<String> chooser = new SendableChooser<>();

    /**
     * Creates a new TeleopDrive.
     */
    public TeleopDrive(final Drive drive) {
        this.drive = drive;
        addRequirements(drive);
        chooser.addOption("Tank Drive", TANK);
        chooser.addOption("Arcade Drive", ARCADE_DUAL);
        chooser.addOption("Arcade (Single Stick)", ARCADE_SOLO);
        chooser.setDefaultOption("Default", ARCADE_DUAL);
        SmartDashboard.putData("Drive Switch", chooser);
    }

    // Called when the command is initially scheduled.
    @Override
    public void initialize() {

    }

    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void execute() {
        var selected = chooser.getSelected();
        if (selected == null) {
            selected = TANK;
        }
        switch (selected) {
        case ARCADE_DUAL:
            drive.arcadedrive(false);
            break;
        case ARCADE_SOLO:
            drive.arcadedrive(true);
            break;
        case TANK:
        default:
            drive.tankdrive();
            break;
        }
    }

    // Called once the command ends or is interrupted.
    @Override
    public void end(final boolean interrupted) {
    }

    // Returns true when the command should end.
    @Override
    public boolean isFinished() {
        return false;
    }
}
