/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import frc.robot.subsystems.*;
import frc.robot.util.controllers.Controller;
import frc.robot.util.controllers.LogitechController;
import frc.robot.util.controllers.PlaneController;
import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SelectCommand;
import frc.robot.commands.AutoShootAndDrive;
import frc.robot.commands.ColorWheelPos;
import frc.robot.commands.AutoDriveTurn;
import frc.robot.commands.AutoLeft;
import frc.robot.commands.AutoMid;
import frc.robot.commands.AutoMinimal;
import frc.robot.commands.AutoRight;
import frc.robot.commands.Dump;
import frc.robot.commands.LaserFire;
import frc.robot.commands.SetArm;
import frc.robot.commands.ShooterControl;
import frc.robot.commands.SpinColorWheel;
import frc.robot.commands.TrackTarget;
import frc.robot.commands.AutoGroup.AutoOptions;

import java.util.Map;
import static java.util.Map.entry;

/**
 * The container for the robot. Contains subsystems, OI devices, and commands.
 */
public class RobotContainer {

    private final Controller driveController = Preferences.getInstance().getBoolean("usePlaneController", false)
            ? new PlaneController(0, 2)
            : new LogitechController(0);
    private final Controller operatorController = new LogitechController(1);

    // #region Subsystems
    // private static final Conveyor conveyor = new Conveyor(driveController);
    // private static final Drive drive = new Drive(driveController);
    private final IntakeArm intakearm = new IntakeArm();
    public final Intake intake = new Intake(driveController, intakearm);
    public final Vision vision = new Vision();
    private final Turret turret = new Turret(operatorController);
    private final Kicker kicker = new Kicker(driveController, intake);
    private final Conveyor conveyor = new Conveyor(driveController, intake, kicker, intakearm);
    private final Shooter shooter = new Shooter(operatorController, kicker, conveyor, intake);
    public final Drive drive = new Drive(driveController);
    private final ColorWheel colorWheel = new ColorWheel(turret);
    // #endregion

    private enum AutoOptions {
        Minimal, DriveAndShoot, AutoDriveTurn, AutoLeft, AutoRight, AutoMid
    }

    private SendableChooser<AutoOptions> autoChooser = new SendableChooser<>();

    public RobotContainer() {
        autoChooser.addOption("Minimal", AutoOptions.Minimal);
        autoChooser.addOption("Drive and Shoot", AutoOptions.DriveAndShoot);
        autoChooser.addOption("Auto Turn", AutoOptions.AutoDriveTurn);
        autoChooser.addOption("Auto Left", AutoOptions.AutoLeft);
        autoChooser.setDefaultOption("Auto Right", AutoOptions.AutoRight);
        autoChooser.addOption("Auto Mid", AutoOptions.AutoMid);
        Shuffleboard.getTab("SmartDashboard").add("Auto Type", autoChooser)
                .withWidget(BuiltInWidgets.kSplitButtonChooser);
        configureButtonBindings();

    }

    /**
     * Put button controls here
     */
    private void configureButtonBindings() {
        operatorController.A().whenPressed(new TrackTarget(turret, vision, operatorController, intake));
        operatorController.B().whenPressed(new LaserFire(true, vision));
        operatorController.B().whenReleased(new LaserFire(false, vision));
        /*
         * driveController.Y().whenPressed(() -> { intake.zeroBallCount(); }, intake);
         */
        operatorController.RightButton().whileHeld(new ShooterControl(shooter, operatorController, intake));
        operatorController.X().whileHeld(new Dump(intake, conveyor, kicker));
        operatorController.Y().whenPressed(new TrackTarget(turret, vision, operatorController, intake));
        operatorController.LeftButton().whenPressed(new SpinColorWheel(colorWheel));
        driveController.RightButton().whenPressed(new ColorWheelPos(colorWheel));
        // driveController.X().whenPressed(new SetArm(intakearm));
        // operatorController.LeftButton().whileHeld(() -> {
        // shooter.setSpeed(3300);
        // // shooter.runSpeed(1);
        // });
        // operatorController.LeftButton().whenReleased(() -> {
        // shooter.stop();
        // });

        driveController.Back().whenPressed(() -> {
            conveyor.conveyorPID.optionSets[0].kP = conveyor.pVal.getDouble(conveyor.conveyorPID.optionSets[0].kP)
                    / 1000000;
            conveyor.conveyorPID.optionSets[0].kD = conveyor.dVal.getDouble(conveyor.conveyorPID.optionSets[0].kD)
                    / 1000000;
            conveyor.currentSpeed = 0;
        });

        driveController.Start().whenPressed(() -> {
            conveyor.temp = 0;
        });
    }

    private Command autoCommand = new SelectCommand(
            Map.ofEntries(entry(AutoOptions.Minimal, new AutoMinimal(drive)),
                    entry(AutoOptions.DriveAndShoot,
                            new AutoShootAndDrive(drive, shooter, operatorController, turret, vision, intake)),
                    entry(AutoOptions.AutoDriveTurn, new AutoDriveTurn(drive)),
                    entry(AutoOptions.AutoRight, new AutoRight(drive)),
                    entry(AutoOptions.AutoLeft, new AutoLeft(drive)), entry(AutoOptions.AutoMid, new AutoMid(drive))),
            autoChooser::getSelected);

    /*
     * Put Autonomus command here
     */
    public Command getAutonomousCommand() {
        return autoCommand;
    }
}
