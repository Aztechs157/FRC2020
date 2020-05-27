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
import frc.robot.Constants;

public class Climbing extends SubsystemBase {
    /**
     * Creates a new Climbing.
     */
    public final TalonSRX winch = new TalonSRX(Constants.ClimbingConstants.winchMotorId);
    public final TalonSRX arm = new TalonSRX(Constants.ClimbingConstants.armMotorId);

    public final Counter Wencoder = new Counter(Mode.kSemiperiod);

    public final Counter Aencoder = new Counter(Mode.kSemiperiod);

    public Climbing() {

        // Confirmation of Auto or Manual climbing mechanism
        // button press to posiiton arm verticly

        // telescopic arm run on servo hs-788hb
        // samre motor and encoder as color wheel

        // arm delivers hook to bar (spring assisted teselscoping,passivre hook
        // release), as delivery mechanism detracts
        // winch starts
        // hook stays on generator switch. and then winch pulls robot up

    }

    public void climb(/* controller and other parameters */) {
        // assumes winch motor moves at same speed which it may not
        winch.set(ControlMode.Follower, Constants.ClimbingConstants.armMotorId);

    }

    public void raiseArm() {
        arm.set(ControlMode.PercentOutput, .5);

        // run motor to raise arm on button press
        // check the encoder(multiple positions{high,low of generator switch})
        // winch needs to move as arm goes up

    }

    public void lowerArm() {
        arm.set(ControlMode.PercentOutput, -.5);
    }

    // run motor to lower arm on button press
    // check encoder

    @Override
    public void periodic() {
        // This method will be called once per scheduler run
    }
}
