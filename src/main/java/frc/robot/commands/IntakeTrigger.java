/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.commands;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.robot.RobotContainer;
import frc.robot.subsystems.Intake;
import frc.robot.util.LogitechController;

public class IntakeTrigger extends CommandBase {

    private final Intake intake;
    // public final ConveyerSensors sensor2;
    public final LogitechController controller;

    /**
     * Creates a new IntakeTrigger2.
     */
    public IntakeTrigger(final Intake intake, final LogitechController controller) {
        this.intake = intake;
        this.controller = controller;
        // this.sensor2 = sensor2;
        addRequirements(intake);
        // addRequirements(sensor2);
    }

    // Called every time the scheduler runs while the command is scheduled.
    @Override
    public void initialize() {
    }

    @Override
    public void execute() {
        intake.runIntake(controller);

        // if (on) {
        // intake.intake.set(0.75);
        // } else {
        // intake.intake.set(0);
        // }

    }
}
