package frc.robot.subsystems;

import java.util.ArrayList;

import edu.wpi.first.wpilibj.Relay;
import edu.wpi.first.wpilibj.Servo;
import edu.wpi.first.wpilibj.I2C.Port;
import edu.wpi.first.wpilibj.Relay.Direction;
import edu.wpi.first.wpilibj.Relay.Value;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.util.PID_Wescott;
import frc.robot.util.Pixy2Controller;
import frc.robot.util.Pixy2Controller.Target;

public class Vision extends SubsystemBase {

    public final Relay laser;
    public final PID_Wescott pid = new PID_Wescott(0.1, 0, 0, 100, 0, 100, 0, 10, -10);

    private final Pixy2Controller pixy;
    private final Relay pixyLight;
    private final Servo UDServo;
    private double UD = 0.5;

    public Vision() {
        pixy = new Pixy2Controller(Port.kOnboard, 0x55);
        UDServo = new Servo(2);
        laser = new Relay(1);
        pixyLight = new Relay(0);
        pixyLight.setDirection(Direction.kForward);
        laser.setDirection(Direction.kForward);
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
        pixyLight.set((lightOn) ? Value.kForward : Value.kOff);
    }
}
