/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot.util;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import edu.wpi.cscore.CvSource;
import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.wpilibj.I2C;

/**
 * Add your docs here.
 */
public class Pixy2Controller {
    public final byte[] GETBLOCKS = { (byte) 174, (byte) 193, (byte) 32, (byte) 2, (byte) 0, (byte) 0 };
    public final byte[] GETVERSION = { (byte) 174, (byte) 193, (byte) 14, (byte) 0 };
    public final byte[] GETRESOLUTION = { (byte) 174, (byte) 193, (byte) 12, (byte) 1, (byte) 0 };
    public final byte[] SETBRIGHTNESS = { (byte) 174, (byte) 193, (byte) 16, (byte) 1, (byte) 0 };
    public final byte[] SETLED = { (byte) 174, (byte) 193, (byte) 20, (byte) 3, (byte) 0, (byte) 0, (byte) 0 };
    public final byte[] SETLAMP = { (byte) 174, (byte) 193, (byte) 22, (byte) 2, (byte) 0, (byte) 0 };
    public final byte[] GETFPS = { (byte) 174, (byte) 193, (byte) 24, (byte) 0 };
    public final byte[] GETPIXEL = { (byte) 174, (byte) 193, (byte) 112, (byte) 5, (byte) 0, (byte) 0, (byte) 0,
            (byte) 0, (byte) 0 };
    private byte[] packetHead = new byte[6];
    private final I2C pixy;
    public Target[] current = { new Target() };
    private boolean serverStarted = false;
    private CvSource server;
    private int frameCount = 0;
    private int maxcount;
    public int selectedIndex = -1;

    public Pixy2Controller(final I2C.Port port, final int address) {
        this.pixy = new I2C(port, address);
    }

    public boolean badReadInput(final int[] signatures, final int maxBlocks) {
        boolean retval = false;
        if (maxBlocks > 255 || maxBlocks < 0 || signatures.length > 8 || signatures.length <= 0) {
            retval = true;
        }
        for (final int i : signatures) {
            if (i < 1 || i > 8) {
                retval = false;
            }
        }
        return retval;
    }

    public ArrayList<Target> getCurrent() {
        final ArrayList<Target> returnArr = new ArrayList<Target>();
        for (final Target target : current) {
            returnArr.add(target);
        }
        return returnArr;
    }

    public Byte mask(final int[] signatures) {
        byte retval = 0;
        for (final int i : signatures) {
            retval |= 1 << i - 1;
        }
        return retval;
    }

    public boolean read(final int signature) {
        return read(new int[] { signature }, 255);
    }

    public boolean read(final int[] signature) {
        return read(signature, 255);
    }

    public boolean read(final int signature, final int maxBlocks) {
        return read(new int[] { signature }, maxBlocks);
    }

    public boolean read(final int[] signatures, final int maxBlocks) {
        if (badReadInput(signatures, maxBlocks)) {
            return false;
        }

        // System.out.println(serverStarted);
        final Byte sigmap = mask(signatures);
        final byte[] writeBytes = GETBLOCKS;
        writeBytes[4] = sigmap;
        writeBytes[5] = (byte) maxBlocks;
        pixy.writeBulk(writeBytes);
        packetHead = new byte[6];
        pixy.readOnly(packetHead, 6);
        // System.out.println("test " + packetHead[0]);
        if (packetHead[3] > 1) {

            // System.out.println(serverStarted);
            final byte[] packetBody = new byte[packetHead[3]];
            pixy.readOnly(packetBody, packetHead[3]);
            final Target[] retval = new Target[packetBody.length / 14];
            for (int i = 0; i < packetBody.length; i += 14) {
                retval[i / 14] = new Target();
                retval[i / 14].sig = convertToShort(packetBody[i + 0], packetBody[i + 1]);
                retval[i / 14].x = convertToShort(packetBody[i + 2], packetBody[i + 3]);
                retval[i / 14].y = convertToShort(packetBody[i + 4], packetBody[i + 5]);
                retval[i / 14].width = convertToShort(packetBody[i + 6], packetBody[i + 7]);
                retval[i / 14].height = convertToShort(packetBody[i + 8], packetBody[i + 9]);
                retval[i / 14].angle = convertToShort(packetBody[i + 10], packetBody[i + 11]);
            }
            this.current = retval;
            // System.out.println(serverStarted);
            if (serverStarted) {
                drawPicture(retval);
            }
            return true;
        } else {
            return false;
        }

    }

    private void drawPicture(Target[] targets) {
        frameCount++;

        if (frameCount >= maxcount) {
            Arrays.sort(targets);
            frameCount = 0;
            Mat image = new Mat(new Size(315, 207), CvType.CV_8UC3);
            image.setTo(new Scalar(255, 255, 255));
            // System.out.println(targets.length);

            for (int i = 0; i < targets.length; i++) {
                Point a = new Point(targets[i].x - (targets[i].width / 2), targets[i].y - (targets[i].height / 2));
                Point b = new Point(targets[i].x + (targets[i].width / 2), targets[i].y + (targets[i].height / 2));
                if (i == selectedIndex) {
                    Imgproc.rectangle(image, a, b, new Scalar(0, 0, 255), 1);
                } else {
                    Imgproc.rectangle(image, a, b, new Scalar(100, 100, 255), 2);
                }
            }
            server.putFrame(image);
        }
    }

    public int unsign(final byte n) {
        return n < 0 ? 256 + n : n;
    }

    private int convertToShort(final byte a, final byte b) {
        return (unsign(b) << 8) | unsign(a);
    }

    public Version getVersion() {
        final byte[] packet = readPacket(GETVERSION);
        final Version retval = new Version();
        retval.hardware = convertToShort(packet[6], packet[7]);
        retval.firmwareVersion = packet[8] + "." + packet[9];
        retval.firmwareBuild = convertToShort(packet[10], packet[11]);
        final byte[] string = new byte[packet.length - 11];
        for (int i = 11; i < packet.length; i++) {
            string[i - 11] = packet[i];
        }
        retval.firmwareType = new String(string, StandardCharsets.US_ASCII);
        return retval;
    }

    public int[] getResolution() {
        final int[] retval = new int[] { 0, 0 };
        final byte[] packet = readPacket(GETRESOLUTION);
        retval[0] = convertToShort(packet[6], packet[7]);
        retval[1] = convertToShort(packet[8], packet[9]);
        return retval;
    }

    public boolean checkSetBrightnessInput(final int brightness) {
        return !(brightness < 0 || brightness > 255);
    }

    public boolean setBrightness(final double percent) {
        final int intPercent = (int) Math.floor(percent * (double) 255);
        return setBrightness(intPercent);
    }

    public boolean setBrightness(final int brightness) {
        final boolean retval = checkSetBrightnessInput(brightness);
        if (!retval)
            return retval;
        final byte[] sendPacket = SETBRIGHTNESS;
        sendPacket[4] = (byte) brightness;
        final byte[] packet = readPacket(sendPacket);
        return packet[6] == 0 || packet[0] == packet[1];
    }

    public boolean setLED(final int r, final int g, final int b) {
        final boolean retval = checkSetLEDInput(r, g, b);
        if (!retval)
            return retval;
        final byte[] sendPacket = SETLED;
        sendPacket[4] = (byte) r;
        sendPacket[5] = (byte) g;
        sendPacket[6] = (byte) b;
        final byte[] packet = readPacket(sendPacket);
        return packet[6] == 0 || packet[0] == packet[1];
    }

    private boolean checkSetLEDInput(final int r, final int g, final int b) {
        return !(r < 0 || r > 255 || g < 0 || g > 255 || b < 0 || b > 255);
    }

    public boolean setLamps(final boolean upper, final boolean lower) {
        final byte[] sendPacket = SETLAMP;
        sendPacket[4] = boolToByte(upper);
        sendPacket[5] = boolToByte(lower);
        final byte[] packet = readPacket(sendPacket);
        return packet[6] == 0 || packet[0] == packet[1];
    }

    private byte boolToByte(final boolean bool) {
        return (bool) ? (byte) 1 : (byte) 0;
    }

    public int getFPS() {
        final byte[] packet = readPacket(GETFPS);
        return packet[6];
    }

    public byte[] readPacket(final byte[] data) {
        pixy.writeBulk(data);
        final byte[] packet1 = new byte[6];
        pixy.readOnly(packet1, 6);
        final byte[] packet2 = new byte[packet1[3]];
        pixy.readOnly(packet2, packet1[3]);
        final byte[] retval = new byte[packet1.length + packet2.length];
        int checksum = 0;
        for (int i = 0; i < packet2.length; i++) {
            checksum += packet2[i];
        }
        checksum = checksum % 0xffff;
        final boolean correct = checksum == convertToShort(packet1[4], packet1[5]);
        if (correct) {
            for (int i = 0; i < packet1.length; i++) {
                retval[i] = packet1[i];
            }
            for (int i = 0; i < packet2.length; i++) {
                retval[i + packet1.length] = packet2[i];
            }
        }
        return retval;
    }

    /**
     * creates a VideoSource with the name "Pixy Output" on the camera server at a
     * framerate of 50 fps, which will be used to display all of the pixy blocks
     * read to the driverstation
     */
    public void AddCameraServer() {
        addCameraServer("Pixy Output", 50);
    }

    /**
     * creates a VideoSource with the name "Pixy Output" at a framerate of the
     * specified value, which will be used to display all of the pixy blocks read to
     * the driverstation
     *
     * @param framerate
     */
    public void AddCameraServer(int framerate) {
        addCameraServer("Pixy Output", framerate);
    }

    /**
     * creates a VideoSource with a specified name on the camera server at a
     * framerate of 50 fps, which will be used to display all of the pixy blocks
     * read to the driverstation
     *
     * @param name - the name to use for the camera server
     */
    public void addCameraServer(String name) {
        addCameraServer(name, 50);
    }

    /**
     * creates a VideoSource with a specified name on the camera server at the
     * specified framerate, which will be used to display all of the pixy blocks
     * read to the driverstation
     *
     *
     * @param name
     * @param framerate
     */
    public void addCameraServer(String name, int framerate) {
        this.maxcount = (framerate < 50) ? 50 / framerate : 1;
        server = CameraServer.getInstance().putVideo(name, 315, 207);
        serverStarted = true;
    }

    public void disableServer() {
        serverStarted = false;
    }

    public class Version {
        public int hardware;
        public String firmwareVersion;
        public int firmwareBuild;
        public String firmwareType;
    }

    public static class Target implements Comparable<Target> {
        public double x;
        public double y;
        public int sig;
        public double width;
        public double height;
        public int angle;
        // public boolean unread = true;
        public int checkSum;
        public boolean checkCorrect;

        @Override
        public int compareTo(Target o) {
            // TODO Auto-generated method stub
            return ((Double) (this.x * this.y)).compareTo((Double) (o.x * o.y));
        }

    }
}
