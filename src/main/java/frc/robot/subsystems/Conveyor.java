/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.util.NEO;
import frc.robot.util.controllers.Controller;
import frc.robot.Constants;
import frc.robot.RobotContainer;
import frc.robot.commands.ConveyerControl;
import frc.robot.subsystems.Intake;

public class Conveyor extends SubsystemBase {
    private final NEO conveyorMotor;

    private Intake intake;
    private Kicker kicker;
    private DigitalInput conveyorBottom = new DigitalInput(3);
    private Controller controller;

    /**
     * Creates a new Conveyer.
     */
    public Conveyor(Controller controller, Intake intake, Kicker kicker) {
        conveyorMotor = new NEO(Constants.ShooterConstants.conveyorMotor, MotorType.kBrushless);
        this.intake = intake;
        this.kicker = kicker;
        this.controller = controller;
        setDefaultCommand(new ConveyerControl(this, controller));
    }

    @Override
    public void periodic() {
        // This method will be called once per scheduler run
    }

    // public void shift() {
    // if (intake.get() && intake.ballCount() <= 5) {
    // intake.run();
    // run();
    // } else {
    // stop();
    // intake.stop();
    // }
    // }

    // public void test() {
    // conveyorMotor.set(controller.getRawAxis(2));
    // }

    public void run() {
        conveyorMotor.set(1.0);
    }

    public void stop() {
        conveyorMotor.set(0.0);
    }

}
