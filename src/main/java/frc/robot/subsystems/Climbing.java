/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import edu.wpi.first.wpilibj.Counter;
import edu.wpi.first.wpilibj.Counter.Mode;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.revrobotics.CANEncoder;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import frc.robot.Constants;

public class Climbing extends SubsystemBase {
    /**
     * Creates a new Climbing.
     */
    public final CANSparkMax winch = new CANSparkMax(Constants.ClimbingConstants.winchMotorId, MotorType.kBrushless);
    public final TalonSRX arm = new TalonSRX(Constants.ClimbingConstants.armMotorId);

    public final CANEncoder winchEncoder = winch.getEncoder();
    public final Counter armEncoder = new Counter(Mode.kSemiperiod);

    public Climbing() {

        // Confirmation of Auto or Manual climbing mechanism
        // button press to posiiton arm verticly
        // winch and arm extension are manually driven

        // telescopic arm run on servo hs-788hb
        // same motor and encoder as color wheel

        // arm delivers hook to bar (spring assisted teselscoping,passivre hook
        // release), as delivery mechanism detracts
        // winch starts
        // hook stays on generator switch. and then winch pulls robot up

    }

    public void winch(/* controller and other parameters */) {
        // assumes winch motor moves at same speed which it may not
        winch.set(.5);

    }

    public void rotate() {

    }

    public void counterRotate() {

    }

    public void extendArm() {
        arm.set(ControlMode.PercentOutput, .5);

        // run motor to raise arm on button press
        // check the encoder(multiple positions{high,low of generator switch})
        // winch needs to move as arm goes up

    }

    public void retractArm() {
        arm.set(ControlMode.PercentOutput, -.5);
    }

    // run motor to lower arm on button press
    // check encoder

    @Override
    public void periodic() {
        // This method will be called once per scheduler run
    }
}
