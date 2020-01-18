/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.AnalogEncoder;
import edu.wpi.first.wpilibj.AnalogPotentiometer;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Servo;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.command.Subsystem;
import frc.robot.NEO;
import frc.robot.Robot;
import frc.robot.commands.ShooterControl;

/**
 * Add your docs here.
 */
public class Shooter extends Subsystem {
    public NEO LeftRight;
    public NEO UpDown;
    private int Count;

    // public Shooter() {
    // LeftRight = new Servo(0);
    // UpDown = new Talon(1);
    // testTalon = new AnalogPotentiometer(1);
    // }
    public Shooter() {
        LeftRight = new NEO(7, MotorType.kBrushless);
        // UpDown = new NEO(0, MotorType.kBrushless);
        
    }

    public void moveShooter(double Speed) {

        // if (joyValy > -0.01 && joyValy < 0.01) 
        //     UpDown.set(0.0);
        // else if (joyValy > 0) {
        //     if (UpDown.getPosition() <= 50) {
        //         UpDown.set(1 * Scale * joyValy);
        //     }
        //     else {
        //         UpDown.set(0.0);
        //     }
        // } 
        // else if (UpDown.getPosition() >= -50) {
        //     UpDown.set(1 * Scale * joyValy);
        // }
        // else {
        //     UpDown.set(0.0);
        // }


        if (Speed > -0.01 && Speed < 0.01) 
            LeftRight.set(0.0);
        else if (Speed > 0) {
            if (LeftRight.getPosition() <= 35) {
                LeftRight.set(1 * Speed);
            }
            else {
                LeftRight.set(0.0);
            }
        } 
        else if (LeftRight.getPosition() >= -25) {
            LeftRight.set(1 * Speed);
        }
        else {
            LeftRight.set(0.0);
        }
    }

    @Override
    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        // setDefaultCommand(new MySpecialCommand());
        setDefaultCommand(new ShooterControl());

    }
}
