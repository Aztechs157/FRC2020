/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import java.util.ArrayList;

import com.revrobotics.CANSparkMax.IdleMode;

import edu.wpi.first.networktables.NetworkTableEntry;

// import java.util.Set;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.CommandScheduler;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the TimedRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {
    private Command autonomousCommand;
    private RobotContainer robotContainer;
    SendableChooser<String> chooser = new SendableChooser<String>();

    /**
     * This function is run when the robot is first started up and should be used
     * for any initialization code.
     */
    @Override
    public void robotInit() {
        // Instantiate our RobotContainer. This will perform all our button bindings,
        // and put our
        // autonomous chooser on the dashboard.
        robotContainer = new RobotContainer();
        chooser.addOption("forward", "forward");
        SmartDashboard.putData("Auto Mode", chooser);

        String autoSelection = "";
        ArrayList<String> trajectoryPaths = new ArrayList<String>();

        if (chooser.getSelected() == null || chooser.getSelected().isEmpty()) {
            System.out.println("dashboard is null!");
            autoSelection = "forward";
        } else {
            autoSelection = chooser.getSelected();
        }

        trajectoryPaths.add("paths/forward.wpilib.json");
        robotContainer.loadConfigs(trajectoryPaths);
    }

    /**
     * This function is called every robot packet, no matter the mode. Use this for
     * items like diagnostics that you want ran during disabled, autonomous,
     * teleoperated and test.
     *
     * <p>
     * This runs after the mode specific periodic functions, but before LiveWindow
     * and SmartDashboard integrated updating.
     */
    @Override
    public void robotPeriodic() {
        // Runs the Scheduler. This is responsible for polling buttons, adding
        // newly-scheduled
        // commands, running already-scheduled commands, removing finished or
        // interrupted commands,
        // and running subsystem periodic() methods. This must be called from the
        // robot's periodic
        // block in order for anything in the Command-based framework to work.
        CommandScheduler.getInstance().run();
    }

    /**
     * This function is called once each time the robot enters Disabled mode.
     */
    @Override
    public void disabledInit() {
        robotContainer.drive.setCoastMode();
        robotContainer.turret.leftright.setIdleMode(IdleMode.kCoast);
        robotContainer.vision.turnLight(false);
        // robotContainer.conveyor.resetStateMachine();
        // robotContainer.shooter.resetStateMachine();
        robotContainer.colorWheel.resetArmState();
        robotContainer.colorWheel.stopSpinState();
        robotContainer.shooter.stopAll();
    }

    private final NetworkTableEntry selectedAutoEntry = Shuffleboard.getTab("Driver")
            .add("Auto Selected", "No Selection Received").getEntry();
    private int selectedAutoCount = 0;

    @Override
    public void disabledPeriodic() {
        if (selectedAutoCount++ > 25) {
            selectedAutoCount = 0;
            selectedAutoEntry.setString(robotContainer.getSelectedAutoString());
        }
    }

    /**
     * This autonomous runs the autonomous command selected by your
     * {@link RobotContainer} class.
     */
    @Override
    public void autonomousInit() {
        autonomousCommand = robotContainer.getAutonomousCommand();
        robotContainer.drive.setBrakeMode();
        robotContainer.turret.leftright.setIdleMode(IdleMode.kBrake);
        robotContainer.intake.ballCountSet(3);
        if (autonomousCommand != null) {
            autonomousCommand.schedule();
        }

    }

    /**
     * This function is called periodically during autonomous.
     */
    @Override
    public void autonomousPeriodic() {
        CommandScheduler.getInstance().run();
        ArrayList<String> trajectoryPaths = new ArrayList<String>();
        trajectoryPaths.add("paths/forward.wpilib.json");
        robotContainer.loadConfigs(trajectoryPaths);

    }

    @Override
    public void teleopInit() {
        // This makes sure that the autonomous stops running when
        // teleop starts running. If you want the autonomous to
        // continue until interrupted by another command, remove
        // this line or comment it out.
        if (autonomousCommand != null) {
            autonomousCommand.cancel();
        }
        robotContainer.drive.setBrakeMode();
        robotContainer.turret.leftright.setIdleMode(IdleMode.kBrake);
    }

    /**
     * This function is called periodically during operator control.
     */
    @Override
    public void teleopPeriodic() {
    }

    @Override
    public void testInit() {
        // Cancels all running commands at the start of test mode.
        CommandScheduler.getInstance().cancelAll();
        robotContainer.drive.setBrakeMode();
        robotContainer.turret.leftright.setIdleMode(IdleMode.kBrake);
    }

    /**
     * This function is called periodically during test mode.
     */
    @Override
    public void testPeriodic() {
    }
}
