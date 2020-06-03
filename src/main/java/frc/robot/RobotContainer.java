/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import frc.robot.subsystems.*;
import frc.robot.util.controllers.ControllerSet;
import frc.robot.util.controllers.LogitechController;
import frc.robot.util.controllers.PlaneController;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SelectCommand;
import frc.robot.commands.AutoShootAndDrive;
import frc.robot.commands.ColorWheelPos;
import frc.robot.commands.AutoDelayShootAndDrive;
import frc.robot.commands.AutoDriveTurn;
import frc.robot.commands.AutoLeft;
import frc.robot.commands.AutoMid;
import frc.robot.commands.AutoMinimal;
import frc.robot.commands.AutoRight;
import frc.robot.commands.Dump;
import frc.robot.commands.IntakeArmToggle;
import frc.robot.commands.IntakeButton;
import frc.robot.commands.ReverseIntake;
import frc.robot.commands.PanicButton;
import frc.robot.commands.ShooterControl;
import frc.robot.commands.SpinColorWheel;
import frc.robot.commands.SpinToColor;
import frc.robot.commands.TrackTarget;

import java.util.Map;
import static java.util.Map.entry;

/**
 * The container for the robot. Contains subsystems, OI devices, and commands.
 */
public class RobotContainer {

    public static final boolean useFlightSticks = true;

    private final ControllerSet driveSet = new ControllerSet(new LogitechController(0), new PlaneController(1, 2));
    private final ControllerSet operatorSet = new ControllerSet(new LogitechController(3));

    // #region Subsystems
    private final IntakeArm intakearm = new IntakeArm();
    public final Intake intake = new Intake(operatorSet, intakearm);
    public final Vision vision = new Vision(intake);
    public final Turret turret = new Turret(operatorSet);
    private final Kicker kicker = new Kicker();
    public final Conveyor conveyor = new Conveyor(driveSet, intake, kicker, intakearm);
    public final Shooter shooter = new Shooter(kicker, conveyor, intake);
    public final Drive drive = new Drive(driveSet);
    public final ColorWheel colorWheel = new ColorWheel(turret);
    // #endregion

    private enum AutoOptions {
        Minimal, DriveAndShoot, AutoDriveTurn, AutoLeft, AutoRight, AutoMid, ShootAndDriveBack, DelayedDriveAndShoot
    }

    private final SendableChooser<AutoOptions> autoChooser = new SendableChooser<>();

    public RobotContainer() {
        autoChooser.setDefaultOption("noShoot drive F", AutoOptions.Minimal);
        autoChooser.addOption("Shoot drive F", AutoOptions.DriveAndShoot);
        autoChooser.addOption("Shoot drive B", AutoOptions.ShootAndDriveBack);
        autoChooser.addOption("Delay shoot drive F", AutoOptions.DelayedDriveAndShoot);
        Shuffleboard.getTab("Driver").add("Auto Type", autoChooser).withWidget(BuiltInWidgets.kSplitButtonChooser);
        configureButtonBindings();
    }

    /**
     * Put button controls here
     */
    private void configureButtonBindings() {
        operatorSet.useButton(LogitechController.RIGHT_BUMPER).whileHeld(new ShooterControl(shooter, intake));
        operatorSet.useButton(LogitechController.BACK).whileHeld(new Dump(intake, conveyor, kicker, shooter));
        operatorSet.useButton(LogitechController.Y).whenPressed(new TrackTarget(turret, vision, operatorSet, intake));
        operatorSet.useButton(LogitechController.START).whenPressed(new PanicButton(shooter, conveyor, intake));
        operatorSet.useButton(LogitechController.X).whenPressed(new SpinColorWheel(colorWheel));
        operatorSet.useButton(LogitechController.B).whenPressed(new SpinToColor(colorWheel));

        driveSet.useButton(LogitechController.Y, PlaneController.RIGHT_HAND_TOP_LEFT)
                .whenPressed(new ColorWheelPos(colorWheel, ColorWheel.ArmPosition.Up));
        driveSet.useButton(LogitechController.A, PlaneController.RIGHT_HAND_BOTTOM_LEFT)
                .whenPressed(new ColorWheelPos(colorWheel, ColorWheel.ArmPosition.Down));
        driveSet.useButton(LogitechController.RIGHT_BUMPER, PlaneController.LEFT_HAND_TOP_LEFT)
                .toggleWhenPressed(new IntakeButton(intake));
        driveSet.useButton(LogitechController.BACK, PlaneController.LEFT_HAND_FAR_BOTTOM_RIGHT)
                .whileHeld(new Dump(intake, conveyor, kicker, shooter));

        // TODO: Check with drive team for Logitech controls for the following two
        driveSet.useButton(LogitechController.LEFT_BUMPER, PlaneController.LEFT_HAND_MID_RIGHT)
                .toggleWhenPressed(new IntakeArmToggle(intakearm));
        driveSet.useButton(LogitechController.START, PlaneController.LEFT_HAND_BOTTOM_RIGHT)
                .whileHeld(new ReverseIntake(intake, conveyor));
    }

    private final Command autoCommand = new SelectCommand(
            Map.ofEntries(entry(AutoOptions.Minimal, new AutoMinimal(drive)),
                    entry(AutoOptions.ShootAndDriveBack,
                            new AutoShootAndDrive(drive, shooter, operatorSet, turret, vision, intake)),
                    entry(AutoOptions.DriveAndShoot,
                            new AutoShootAndDrive(drive, shooter, operatorSet, turret, vision, intake)),
                    entry(AutoOptions.AutoDriveTurn, new AutoDriveTurn(drive)),
                    entry(AutoOptions.AutoRight, new AutoRight(drive)),
                    entry(AutoOptions.AutoLeft, new AutoLeft(drive)), entry(AutoOptions.AutoMid, new AutoMid(drive)),
                    entry(AutoOptions.DelayedDriveAndShoot,
                            new AutoDelayShootAndDrive(drive, shooter, operatorSet, turret, vision, intake))),
            autoChooser::getSelected);

    /*
     * Put Autonomus command here
     */
    public Command getAutonomousCommand() {
        return autoCommand;
    }

    public String getSelectedAutoString() {
        return autoChooser.getSelected().toString();
    }
}
