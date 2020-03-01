/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.Constants;
import frc.robot.commands.IntakeArmControl;
import frc.robot.util.NEO;
import frc.robot.util.PID;

public class IntakeArm extends SubsystemBase {

    private final NEO intakeArmMotor;
    // public NetworkTableEntry pVal, dVal;
    public double position = 0;
    public PID intakePID = new PID(0.03, 0, 0.003, 0, 0, 0, 0, 0, 0);
    public final double outPos = 27;// 38

    /**
     * Creates a new IntakeArm.
     */
    public IntakeArm() {
        intakeArmMotor = new NEO(Constants.ShooterConstants.intakeArmMotorID, MotorType.kBrushless);
        intakeArmMotor.inverted();
        intakeArmMotor.setBrakeMode();
        setDefaultCommand(new IntakeArmControl(this));
        // pVal = Shuffleboard.getTab("Test").add("P Val but really cool",
        // intakePID.optionSets[0].kP).getEntry();
        // dVal = Shuffleboard.getTab("Test").add("D Val but cool",
        // intakePID.optionSets[0].kP).getEntry();
    }

    public void run(double speed) {
        intakeArmMotor.set(speed);
    }

    public void MoveArm() {
        run(intakePID.pidCalculate(position, intakeArmMotor.getPosition()));
    }

    @Override
    public void periodic() {
        // This method will be called once per scheduler run
    }
}
