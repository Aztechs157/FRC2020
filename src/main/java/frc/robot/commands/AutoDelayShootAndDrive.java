/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.subsystems.Drive;
import frc.robot.subsystems.Intake;
// import frc.robot.subsystems.IntakeArm;
import frc.robot.subsystems.Shooter;
import frc.robot.subsystems.Turret;
import frc.robot.subsystems.Vision;
import frc.robot.util.controllers.Controller;

public class AutoDelayShootAndDrive extends SequentialCommandGroup {
    /**
     * Creates a new AutoDriveAndShoot.
     */
    public AutoDelayShootAndDrive(Drive drive, Shooter shooter, Controller controller, Turret turret, Vision vision,
            Intake intake) {

        ShooterControl shoot = new ShooterControl(shooter, controller, intake);
        DriveForward commandForward = new DriveForward(10, true, drive, .1);
        TrackTarget trackTarget = new TrackTarget(turret, vision, controller, intake);
        addCommands(race(new WaitCommand(10), sequence(new AutoFindTarget(turret, vision), new WaitCommand(3),
                shoot.alongWith(trackTarget), new WaitCommand(.5))));
        addCommands(commandForward);

        // System.out.println("DriveAndShoot");

    }

}
