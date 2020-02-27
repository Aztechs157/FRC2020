/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.subsystems.Drive;
import frc.robot.subsystems.Intake;
// import frc.robot.subsystems.IntakeArm;
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.Turret;
import frc.robot.subsystems.Vision;
import frc.robot.util.controllers.Controller;

public class AutoShootAndDrive extends SequentialCommandGroup {
    /**
     * Creates a new AutoDriveAndShoot.
     */
    public AutoShootAndDrive(Drive drive, Shooter shooter, Controller controller, Turret turret, Vision vision,
            Intake intake) {

        ShooterControl shoot = new ShooterControl(shooter, controller, intake);
        DriveForward commandForward = new DriveForward(1, true, drive);
        TrackTarget trackTarget = new TrackTarget(turret, vision, controller, intake);
        addCommands(new AutoFindTarget(turret, vision));

        addCommands(shoot.alongWith(trackTarget));
        addCommands(new NewWaitCommand(25));
        commandForward.drivepower = .1;
        addCommands(commandForward);

        // System.out.println("DriveAndShoot");

    }

}
