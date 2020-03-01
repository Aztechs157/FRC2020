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
// import edu.wpi.first.wpilibj.Preferences;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInWidgets;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
// import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
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
import frc.robot.commands.IntakeButton;
import frc.robot.commands.IntakeUnjam;
import frc.robot.commands.LaserFire;
import frc.robot.commands.PanicButton;
// import frc.robot.commands.SetArm;
import frc.robot.commands.ShooterControl;
import frc.robot.commands.SpinColorWheel;
import frc.robot.commands.SpinToColor;
import frc.robot.commands.TrackTarget;
// import frc.robot.commands.AutoGroup.AutoOptions;

import java.util.Map;
import static java.util.Map.entry;

/**
 * The container for the robot. Contains subsystems, OI devices, and commands.
 */
public class RobotContainer {

    public static final boolean useFlightSticks = true;

    private final Controller driveController = useFlightSticks ? new PlaneController(0, 2) : new LogitechController(0);
    private final Controller operatorController = new LogitechController(1);

    // #region Subsystems
    // private static final Conveyor conveyor = new Conveyor(driveController);
    // private static final Drive drive = new Drive(driveController);
    private final IntakeArm intakearm = new IntakeArm();
    public final Intake intake = new Intake(operatorController, intakearm);
    public final Vision vision = new Vision(intake);
    public final Turret turret = new Turret(operatorController);
    private final Kicker kicker = new Kicker(driveController, intake);
    public final Conveyor conveyor = new Conveyor(driveController, intake, kicker, intakearm);
    public final Shooter shooter = new Shooter(operatorController, kicker, conveyor, intake);
    public final Drive drive = new Drive(driveController);
    public final ColorWheel colorWheel = new ColorWheel(turret);
    // #endregion

    private enum AutoOptions {
        Minimal, DriveAndShoot, AutoDriveTurn, AutoLeft, AutoRight, AutoMid, ShootAndDriveBack
    }

    private SendableChooser<AutoOptions> autoChooser = new SendableChooser<>();

    public RobotContainer() {
        autoChooser.setDefaultOption("noShoot drive F", AutoOptions.Minimal);
        autoChooser.addOption("Shoot drive F", AutoOptions.DriveAndShoot);
        autoChooser.addOption("Shoot drive B", AutoOptions.ShootAndDriveBack);
        Shuffleboard.getTab("Driver").add("Auto Type", autoChooser).withWidget(BuiltInWidgets.kSplitButtonChooser);
        Shuffleboard.getTab("Driver").addString("Auto Selected", () -> autoChooser.getSelected().toString());
        configureButtonBindings();
    }

    /**
     * Put button controls here
     */
    private void configureButtonBindings() {
        // operatorController.A().whenPressed(new TrackTarget(turret, vision,
        // operatorController, intake));
        // operatorController.B().whenPressed(new LaserFire(true, vision));
        // operatorController.B().whenReleased(new LaserFire(false, vision));
        /*
         * driveController.Y().whenPressed(() -> { intake.zeroBallCount(); }, intake);
         */
        operatorController.RightButton().whileHeld(new ShooterControl(shooter, operatorController, intake));
        operatorController.Back().whileHeld(new Dump(intake, conveyor, kicker, shooter));
        operatorController.Y().whenPressed(new TrackTarget(turret, vision, operatorController, intake));
        operatorController.Start().whenPressed(new PanicButton(shooter, conveyor, intake));
        operatorController.X().whenPressed(new SpinColorWheel(colorWheel));
        operatorController.B().whenPressed(new SpinToColor(colorWheel));
        if (!useFlightSticks) {
            // driveController.X().whenPressed(new SpinColorWheel(colorWheel));
            // driveController.B().whenPressed(new SpinToColor(colorWheel));
            driveController.Y().whenPressed(new ColorWheelPos(colorWheel, ColorWheel.ArmPosition.Up));
            driveController.A().whenPressed(new ColorWheelPos(colorWheel, ColorWheel.ArmPosition.Down));
            driveController.RightButton().toggleWhenPressed(new IntakeButton(intake));
            driveController.Back().whileHeld(new Dump(intake, conveyor, kicker, shooter));
        } else {
            // ((PlaneController) driveController).RightStickPush().whenPressed(new
            // IntakeUnjam(intake));
            ((PlaneController) driveController).stick2button6()
                    .whenPressed(new ColorWheelPos(colorWheel, ColorWheel.ArmPosition.Up));
            ((PlaneController) driveController).stick2button7()
                    .whenPressed(new ColorWheelPos(colorWheel, ColorWheel.ArmPosition.Down));
            // ((PlaneController) driveController).stick2Button10().whenPressed(new
            // SpinColorWheel(colorWheel));
            ((PlaneController) driveController).RightButton().toggleWhenPressed(new IntakeButton(intake));
            ((PlaneController) driveController).LeftStickPush().whileHeld(new Dump(intake, conveyor, kicker, shooter));
            // driveController.Start().whenPressed(() -> {
            // intakearm.intakePID.optionSets[0].kP =
            // intakearm.pVal.getDouble(intakearm.intakePID.optionSets[0].kP);
            // intakearm.intakePID.optionSets[0].kD =
            // intakearm.dVal.getDouble(intakearm.intakePID.optionSets[0].kD);
            // });
        }
        // driveController.X().whenPressed(new SetArm(intakearm));
        // operatorController.LeftButton().whileHeld(() -> {
        // shooter.setSpeed(3300);
        // // shooter.runSpeed(1);
        // });
        // operatorController.LeftButton().whenReleased(() -> {
        // shooter.stop();
        // });

        driveController.Start().whenPressed(() -> {
            conveyor.temp = 0;
        });
    }

    private Command autoCommand = new SelectCommand(
            Map.ofEntries(entry(AutoOptions.Minimal, new AutoMinimal(drive)),
                    entry(AutoOptions.ShootAndDriveBack,
                            new AutoShootAndDrive(drive, shooter, operatorController, turret, vision, intake)),
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
