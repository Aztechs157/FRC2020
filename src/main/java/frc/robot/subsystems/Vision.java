/*----------------------------------------------------------------------------*/
/* Copyright (c) 2019 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import java.util.ArrayList;

import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.Relay;
import edu.wpi.first.wpilibj.Servo;
import edu.wpi.first.wpilibj.I2C.Port;
import edu.wpi.first.wpilibj.Relay.Direction;
import edu.wpi.first.wpilibj.Relay.Value;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.NEO;
import frc.robot.PID_Wescott;
import frc.robot.Pixy2Controller;
import frc.robot.Pixy2Controller.Target;

public class Vision extends SubsystemBase {
    // Put methods for controlling this subsystem
    // here. Call these from Commands.
    Pixy2Controller pixy;
    Relay pixyLight;
    // Servo LRServo;
    // NEO LRControl;
    Servo UDServo;
    public Relay laser;
    public PID_Wescott pid = new PID_Wescott(0.1, 0, 0, 100, 0, 100, 0, 10, -10);
    public double UD = 0.5;

    public Vision() {
        pixy = new Pixy2Controller(Port.kOnboard, 0x55);
        // LRControl = new NEO(7, MotorType.kBrushless);
        UDServo = new Servo(2);

        laser = new Relay(1);
        pixyLight = new Relay(0);
        pixyLight.setDirection(Direction.kForward);
        laser.setDirection(Direction.kForward);
    }

    public void setHorizontal(double pos) {
        // LR = pos;
        // LRControl.set(pos);
    }

    public void setVertical(double pos) {
        UD = pos;
        UDServo.set(UD);
    }

    public Target[] getBlocks() {
        ArrayList<Target> retval = new ArrayList<Target>();
        if (pixy.read(1)) {
            retval = pixy.getCurrent();
        }
        return retval.toArray(new Target[retval.size()]);
        // return (Target[]) retval.toArray();

    }

    public void turnLight(boolean lightOn) {
        pixyLight.set((lightOn) ? Value.kForward : Value.kOff);
    }

    @Override
    public void periodic() {
        // This method will be called once per scheduler run
    }
}
