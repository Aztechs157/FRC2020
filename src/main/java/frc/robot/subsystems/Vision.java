/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018-2019 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.subsystems;

import java.util.ArrayList;

import edu.wpi.first.wpilibj.Relay;
import edu.wpi.first.wpilibj.Servo;
import edu.wpi.first.wpilibj.I2C.Port;
import edu.wpi.first.wpilibj.Relay.Direction;
import edu.wpi.first.wpilibj.Relay.Value;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.RobotContainer;
import frc.robot.commands.LEDControl;
import frc.robot.util.LimeLight;
import frc.robot.util.PID;
import frc.robot.util.Pixy2Controller;
import frc.robot.util.Pixy2Controller.Target;

public class Vision extends SubsystemBase {

    public final Relay laser;
    public final PID pixyPID = new PID(0.1, 0, 0, 100, 0, 100, 0, 10, -10);
    public final PID limeLightPID = new PID(0.03, 0, 0, 100, 0, 100, 0, 10, -10);

    private final Pixy2Controller pixy;
    public final LimeLight limelight;
    private final Relay pixyLight;
    private final Servo UDServo;
    private double UD = 0.5;
    public final boolean pixyIn = false; // if false, limelight code will run instead

    public Vision(Intake intake) {

        setDefaultCommand(new LEDControl(intake, this));
        limelight = new LimeLight();
        pixy = new Pixy2Controller(Port.kOnboard, 0x55);
        UDServo = new Servo(2);
        laser = new Relay(1);
        pixyLight = new Relay(0);
        // Shuffleboard.getTab("Test").addNumber("x", limelight::getx);
        // Shuffleboard.getTab("Test").addNumber("y", () -> {
        // return limelight.gety();
        // });
        pixyLight.setDirection(Direction.kForward);
        laser.setDirection(Direction.kForward);
        pixy.AddCameraServer(10);
    }

    public boolean checkLED() {
        return limelight.checkLED();
    }

    /**
     * finds the current accuracy of a shot, based on the distance to the target
     *
     * @return the distance from the current position to the currect position
     */
    public double checkAcc() {
        if (!pixyIn) {
            return limelight.getx();
        } else {
            return 9999;
        }
    }

    public void setHorizontal(final double pos) {
        // LR = pos;
        // LRControl.set(pos);
    }

    public void setVertical(final double pos) {
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

    public void turnLight(final boolean lightOn) {
        if (lightOn) {
            limelight.LEDon();
        } else {
            limelight.LEDoff();
        }
    }

    @Override
    public void periodic() {
        // This method will be called once per scheduler run
        // getBlocks();
        // System.out.println("hello world");
        // SmartDashboard.putNumber("acc", checkAcc());

    }

    public boolean chechTargets() {
        return limelight.checkTargets();
    }

}
