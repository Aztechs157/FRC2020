/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

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
    private I2C pixy;
    public Target[] current = { new Target() };

    public Pixy2Controller(I2C.Port port, int address) {
        this.pixy = new I2C(port, address);
    }

    public boolean badReadInput(int[] signatures, int maxBlocks) {
        boolean retval = false;
        if (maxBlocks > 255 || maxBlocks < 0 || signatures.length > 8 || signatures.length <= 0) {
            retval = true;
        }
        for (int i : signatures) {
            if (i < 1 || i > 8) {
                retval = false;
            }
        }
        return retval;
    }

    public ArrayList<Target> getCurrent() {
        ArrayList<Target> returnArr = new ArrayList<Target>();
        for (Target target : current) {
            returnArr.add(target);
        }
        return returnArr;
    }

    public Byte mask(int[] signatures) {
        byte retval = 0;
        for (int i : signatures) {
            retval |= 1 << i - 1;
        }
        return retval;
    }

    public boolean read(int signature) {
        return read(new int[] { signature }, 255);
    }

    public boolean read(int[] signature) {
        return read(signature, 255);
    }

    public boolean read(int signature, int maxBlocks) {
        return read(new int[] { signature }, maxBlocks);
    }

    public boolean read(int[] signatures, int maxBlocks) {
        if (badReadInput(signatures, maxBlocks)) {
            return false;
        }
        Byte sigmap = mask(signatures);
        byte[] writeBytes = GETBLOCKS;
        writeBytes[4] = sigmap;
        writeBytes[5] = (byte) maxBlocks;
        pixy.writeBulk(writeBytes);
        packetHead = new byte[6];
        pixy.readOnly(packetHead, 6);
        if (packetHead[3] > 1) {
            byte[] packetBody = new byte[packetHead[3]];
            pixy.readOnly(packetBody, packetHead[3]);
            Target[] retval = new Target[packetBody.length / 14];
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
            return true;
        } else {
            return false;
        }

    }

    public int unsign(byte n) {
        return n < 0 ? 256 + n : n;
    }

    private int convertToShort(byte a, byte b) {
        return (unsign(b) << 8) | unsign(a);
    }

    public Version getVersion() {
        byte[] packet = readPacket(GETVERSION);
        Version retval = new Version();
        retval.hardware = convertToShort(packet[6], packet[7]);
        retval.firmwareVersion = packet[8] + "." + packet[9];
        retval.firmwareBuild = convertToShort(packet[10], packet[11]);
        byte[] string = new byte[packet.length - 11];
        for (int i = 11; i < packet.length; i++) {
            string[i - 11] = packet[i];
        }
        retval.firmwareType = new String(string, StandardCharsets.US_ASCII);
        return retval;
    }

    public int[] getResolution() {
        int[] retval = new int[] { 0, 0 };
        byte[] packet = readPacket(GETRESOLUTION);
        retval[0] = convertToShort(packet[6], packet[7]);
        retval[1] = convertToShort(packet[8], packet[9]);
        return retval;
    }

    public boolean checkSetBrightnessInput(int brightness) {
        return !(brightness < 0 || brightness > 255);
    }

    public boolean setBrightness(double percent) {
        int intPercent = (int) Math.floor(percent * (double) 255);
        return setBrightness(intPercent);
    }

    public boolean setBrightness(int brightness) {
        boolean retval = checkSetBrightnessInput(brightness);
        if (!retval)
            return retval;
        byte[] sendPacket = SETBRIGHTNESS;
        sendPacket[4] = (byte) brightness;
        byte[] packet = readPacket(sendPacket);
        return packet[6] == 0 || packet[0] == packet[1];
    }

    public boolean setLED(int r, int g, int b) {
        boolean retval = checkSetLEDInput(r, g, b);
        if (!retval)
            return retval;
        byte[] sendPacket = SETLED;
        sendPacket[4] = (byte) r;
        sendPacket[5] = (byte) g;
        sendPacket[6] = (byte) b;
        byte[] packet = readPacket(sendPacket);
        return packet[6] == 0 || packet[0] == packet[1];
    }

    private boolean checkSetLEDInput(int r, int g, int b) {
        return !(r < 0 || r > 255 || g < 0 || g > 255 || b < 0 || b > 255);
    }

    public boolean setLamps(boolean upper, boolean lower) {
        byte[] sendPacket = SETLAMP;
        sendPacket[4] = boolToByte(upper);
        sendPacket[5] = boolToByte(lower);
        byte[] packet = readPacket(sendPacket);
        return packet[6] == 0 || packet[0] == packet[1];
    }

    private byte boolToByte(boolean bool) {
        return (bool) ? (byte) 1 : (byte) 0;
    }

    public int getFPS() {
        byte[] packet = readPacket(GETFPS);
        return packet[6];
    }

    public byte[] readPacket(byte[] data) {
        pixy.writeBulk(data);
        byte[] packet1 = new byte[6];
        pixy.readOnly(packet1, 6);
        byte[] packet2 = new byte[packet1[3]];
        pixy.readOnly(packet2, packet1[3]);
        byte[] retval = new byte[packet1.length + packet2.length];
        int checksum = 0;
        for (int i = 0; i < packet2.length; i++) {
            checksum += packet2[i];
        }
        checksum = checksum % 0xffff;
        boolean correct = checksum == convertToShort(packet1[4], packet1[5]);
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

    public class Version {
        public int hardware;
        public String firmwareVersion;
        public int firmwareBuild;
        public String firmwareType;
    }

    public class Target {
        public double x;
        public double y;
        public int sig;
        public double width;
        public double height;
        public int angle;
        public boolean unread = true;
        public int checkSum;
        public boolean checkCorrect;
    }
}
