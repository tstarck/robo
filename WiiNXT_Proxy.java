import lejos.pc.comm.*;
import java.io.*;
import java.util.Arrays;
import java.util.Hashtable;

import wiiusej.*;
import wiiusej.wiiusejevents.physicalevents.*;
import wiiusej.wiiusejevents.utils.*;
import wiiusej.wiiusejevents.wiiuseapievents.*;

/**
 * Wiimote + NXT proxy program. Relays wiimote's sensor data to
 * an NXT brick and reads back data from the NXT via bluetooth.
 * Supports up to four wiimote+NXT combinations. Runs on PC, not NXT!
 * (compile with javac, not nxjc)
 * 
 * This code is based on a forum posting by 'enzomango' at this URL:
 * http://lejos.sourceforge.net/forum/viewtopic.php?p=4435&sid=1e8bc60a53450194b2a76a8ba8147c98
 * and has been modified mostly to handle more than one wiimote/NXT per instance and to support
 * reading back data from the NXT. The code should be developed further to handle all buttons
 * etc. (easy to do) Error handling and cleanliness is pretty much neglected.
 * 
 * Big thanks to enzomango for the original post.
 * 
 * @author Jouko Strömmer
 *
 */
public class WiiNXT_Proxy implements WiimoteListener {

    /**
     * Arbitrary values to signal what the NXT wants 
     * (These are named from the point of view of the NXT!)
     */
    private static final int RECVMOTION = 1;
    private static final int RECVBUTTONS = 2;
    private static final int RECVIR = 4;
    private static final int TRANSMIT = 8;
    private static final int RUMBLEON = 16;
    private static final int RUMBLEOFF = 32;
    private static final int IRON = 64;
    private static final int IROFF = 128;
    private static final int MOTIONON = 256;
    private static final int MOTIONOFF = 512;


    /**
     * HashTable for accessing each controller using the Wiimote's ID
     * (why a hash table? When initially writing this, I wasn't sure what
     * sort of value is a Wiimote ID, and didn't care to check)
     */
    static Hashtable<Integer, Controller> motes = new Hashtable<Integer, Controller>();

    /**
     * HashTable for accessing each NXT's output buffer
     */
    static Hashtable<Integer, Integer[]> nxt = new Hashtable<Integer, Integer[]>();

    /**
     * Controller provides access to wiimote state
     * and uses a separate thread for each instance
     * to refresh the values.
     * 
     * Controller connects a single wiimote with a single NXT.
     * Therefore to read each NXT's output, Controller listens
     * for NXT to dictate whether it wants the wiimote values
     * or to transmit data to the PC.
     *
     */
    private class Controller implements Runnable {
        Wiimote mote;
        NXTComm comm;

        // variables to read values into when a wiimote event occurs
        private float pitch, roll, yaw, z;
        private boolean a, b, one, two, plus, minus, home, up, down, left, right;
        private int x, y;
        private short buttons;
        // buffer to hold data from NXT
        private int[] buffer;
        // true if there's new data from the NXT (ie. buffer
        // has not been read yet with getData()). This was just
        // used in the main loop to print out values only once
        // per refresh.
        private boolean newData = false;

        // getters/setters...
        public float getPitch() { return pitch; }
        public void setPitch(float pitch) { this.pitch = pitch; }
        public float getRoll() { return roll; }
        public void setRoll(float roll) { this.roll = roll; }
        public float getYaw() { return yaw; }
        public void setYaw(float yaw) { this.yaw = yaw; }
        public boolean getA() { return a; }
        public void setA(boolean a) { this.a = a; }
        public boolean getB() { return b; }
        public void setB(boolean b) { this.b = b; }
        public boolean getOne() { return one; }
        public void setOne(boolean one) { this.one = one; }
        public boolean getTwo() { return two; }
        public void setTwo(boolean two) { this.two = two; }
        public boolean getPlus() { return plus; }
        public void setPlus(boolean plus) { this.plus = plus; }
        public boolean getMinus() { return minus; }
        public void setMinus(boolean minus) { this.minus = minus; }
        public boolean getHome() { return home; }
        public void setHome(boolean home) { this.home = home; }
        public boolean getUp() { return up; }
        public void setUp(boolean up) { this.up = up; }
        public boolean getDown() { return down; }
        public void setDown(boolean down) { this.down = down; }
        public boolean getLeft() { return left; }
        public void setLeft(boolean left) { this.left = left; }
        public boolean getRight() { return right; }
        public void setRight(boolean right) { this.right = right; }
        public void setX(int x) { this.x = x; }
        public int getX() { return x; }
        public void setY(int y) { this.y = y; }
        public int getY() { return y; }
        public void setZ(float f) { this.z = f; }
        public float getZ() { return z; }
        public void setButtons(short buttons) { this.buttons = buttons; }
        public short getButtons() { return buttons; }

        // return data read from NXT
        public synchronized int[] getData () {
            newData = false;
            return buffer;
        }

        // return wiimote ID of this Controller
        public Integer getId () {
            return mote.getId();
        }

        // create Controller from wiimote and an NXT and
        // initialize motion/button state to 0
        public Controller (Wiimote m, NXTComm c) {
            mote = m;
            comm = c;
            pitch = roll = yaw = z = 0;
            a = b = one = two = false;
            plus = minus = home = false;
            up = down = left = right = false;
            x = y = 0;
        }

        // when invoked with Thread.start(), open input and output
        // streams and whenever values are requested by the NXT,
        // send them.
        public void run() {

            // NXT's request byte is parsed as follows:
            // bit 0 set: send acceleration values (first)
            // bit 1 set: send button states (second)
            // bit 2 set: send IR data (third)
            // bit 3 set: receive data
            // bit 4 set: start rumble
            // bit 5 set: stop rumble
            // bit 6 set: start IR tracking (off by default)
            // bit 7 set: stop IR tracking 
            // bit 8 set: start motion tracking (off by default)
            // bit 9 set: stop motion tracking 
            int request = 0;

            int numints;
            InputStream is = comm.getInputStream();
            OutputStream os = comm.getOutputStream();

            DataOutputStream dos = new DataOutputStream(os);
            DataInputStream dis = new DataInputStream(is);

            long starttime = 0;
            long endtime = 0;
            while (true) {
                try {
                    // listen for communication request from NXT
                    request = dis.readInt();
                    System.out.println("Request from NXT: "+request);
                    // NXT wants accel values
                    if((request & RECVMOTION) > 0) {
                        starttime = System.currentTimeMillis();
                        dos.writeFloat(pitch);
                        dos.flush();
                        dos.writeFloat(roll);               
                        dos.flush();
                        dos.writeFloat(yaw);               
                        dos.flush();
                        endtime = System.currentTimeMillis();
                        System.out.println("Sent accelerometer data "+pitch+" "+roll+" "+yaw+", time taken: "+(endtime-starttime)+"ms.");
                    }

                    // NXT wants button states
                    if((request & RECVBUTTONS) > 0) {
                        starttime = System.currentTimeMillis();
                        dos.writeShort(buttons);
                        dos.flush();
                        endtime = System.currentTimeMillis();
                        System.out.println("Sent button data, time taken: "+(endtime-starttime)+"ms.");
                    }

                    // NXT wants IR values
                    if((request & RECVIR) > 0) {
                        starttime = System.currentTimeMillis();
                        dos.writeInt(x);
                        dos.flush();
                        dos.writeInt(y);
                        dos.flush();
                        dos.writeFloat(z);
                        dos.flush();
                        endtime = System.currentTimeMillis();
                        System.out.println("Sent IR data, time taken: "+(endtime-starttime)+"ms.");
                    }

                    // NXT sets wiimote rumble on or off
                    if((request & RUMBLEON) > 0) mote.activateRumble();
                    if((request & RUMBLEOFF) > 0) mote.deactivateRumble();

                    // NXT sets IR tracking on or off
                    if((request & IRON) > 0) mote.activateIRTRacking();
                    if((request & IROFF) > 0) mote.deactivateIRTRacking();

                    // NXT sets motion sensing on or off
                    if((request & MOTIONON) > 0) mote.activateMotionSensing();
                    if((request & MOTIONOFF) > 0) mote.deactivateMotionSensing();

                    // NXT wants to send data, grab it
                    if((request & TRANSMIT) > 0) {
                        newData = true;
                        // number of ints to read
                        numints = dis.readInt();
                        // initialize buffer for data
                        buffer = new int[numints];
                        // read data into buffer
                        starttime = System.currentTimeMillis();
                        for(int x = 0; x < numints; x++) {
                            buffer[x] = dis.readInt();
                        }
                        endtime = System.currentTimeMillis();
                        System.out.println("Received "+numints*4+" bytes from NXT, time taken: "+(endtime-starttime)+"ms.");
                        // do something with the data here before it's gone!
                        System.out.println(""+Arrays.toString(buffer));
                    }
                } catch(Exception e) {
                    e.printStackTrace();
                    System.exit(1);
                }
            }
        }
    }

    /**
     * Connect to wiimotes
     * @param n number of wiimotes to connect to
     * @return array of connected Wiimotes
     */
    private Wiimote[] connectWiimotes(int n) {
        Wiimote[] wiimotes = WiiUseApiManager.getWiimotes(n, true, WiiUseApiManager.WIIUSE_STACK_MS);

        for(Wiimote mote : wiimotes) {
            mote.setTimeout((short)100, (short)100);
            mote.addWiiMoteEventListeners(this);
        }
        System.out.println("Connected to "+n+" Wiimotes");
        return wiimotes;
    }

    /**
     * Open BT connection to NXT brick and return an nxtComm object
     * @param nxtInfo NXT to connect to
     * @return nxtComm of the connection 
     */
    private NXTComm connectNXT (NXTInfo nxtInfo) {
        boolean opened = false;
        NXTComm nxtComm = null;  
        try {
            nxtComm = NXTCommFactory.createNXTComm(NXTCommFactory.BLUETOOTH);
        } catch (NXTCommException e) {
            System.out.println("Failed to load Bluetooth driver");
            System.exit(1);
        }
        System.out.println("Connecting to " + nxtInfo.deviceAddress);
        try {
            opened = nxtComm.open(nxtInfo);
        } catch (NXTCommException e) {
            System.out.println("Failed to open " + nxtInfo.name);
            System.exit(1);
        }
        if (!opened) {
            System.out.println("Failed to open " + nxtInfo.name);
            System.exit(1);
        }
        System.out.println("Connected to " + nxtInfo.deviceAddress);
        return nxtComm;
    }

    /**
     * Read command line arguments passed to main() and try connecting the appropriate
     * number of wiimotes with their corresponding NXT brick (hardcoded for 4 max)
     * @param args Usage: WiiNXT_Proxy address [address] [address] [address]
     * @param wiimotes array of Wiimotes. Connected to NXTs in discovery order.
     */
    private void createControllers(String[] args, Wiimote[] wiimotes) {
        if (args.length < 1) {
            System.out.println("Usage: WiiNXT_Proxy address [address] [address] [address]");
            System.exit(1);
        }
        NXTComm[] nxtComm = new NXTComm[4];
        NXTInfo[] nxtInfo = new NXTInfo[4];
        // for every argument, connect a wiimote
        for (int x = 0; x < args.length; x++) {
            nxtInfo[x] = new NXTInfo(NXTCommFactory.BLUETOOTH,"NXT",args[x]);
            nxtComm[x] = connectNXT(nxtInfo[x]);
        }

        // identify Controllers by the wiimote id (for identifying events' source)
        for (int x = 0; x < nxtComm.length; x++)
            if (nxtComm[x] != null) motes.put(wiimotes[x].getId(), new Controller(wiimotes[x], nxtComm[x]));

    }

    /**
     * Main program
     * @param args Usage: WiiNXT_Proxy address [address] [address] [address]
     */
    public static void main(String[] args) {
        System.out.println("Press 1+2 on all controllers and press ENTER");
        try {
            System.in.read();
        } catch (IOException e1) {
            System.out.println("Aborting.");
            System.exit(1);
        }
        WiiNXT_Proxy p = new WiiNXT_Proxy();
        p.createControllers(args, p.connectWiimotes(args.length));
        // if wiimotes were found
        if (motes.size() > 0) {
            // for each
            for(Controller c : motes.values()) {
                // start i/o loop in a new thread
                new Thread(c).start();
            }
        }
        System.out.println("No of motes. "+motes.size());

        // just a simple read instead of a busy loop
        try {
            System.in.read();
            // add something smart here, print sensor values for example:
            //        while(true) {
            //                    for(Controller c : motes.values()) {
            //                        System.out.print(""+c.getId()+": "+c.getPitch()+" "+c.getRoll()+" "+c.getYaw()+" "+c.getA()+" "+c.getB()+" "+c.getOne()+" "+c.getTwo());    
            //                    }
            //                    System.out.println();
            //        }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }


    /**
     * What to do when wiimote events occurs. If you want to handle
     * more buttons/motion sensing/whatever, this is the place to add code
     * that assigns them to a Controller (in addition to adding the corresponding
     * fields to Controller and adjusting WiiNXT.java running on the brick to read
     * the proper amount of data). See wiiusej docs.
     */
    public void onMotionSensingEvent(MotionSensingEvent arg0) {
        // get motion event source Controller
        Controller c = motes.get(arg0.getWiimoteId());
        if(c == null) return;
        // refresh values
        c.setPitch(arg0.getOrientation().getPitch());
        c.setRoll(arg0.getOrientation().getRoll());
        c.setYaw(arg0.getOrientation().getYaw());
    }

    public void onButtonsEvent(WiimoteButtonsEvent arg0) {
        Controller c = motes.get(arg0.getWiimoteId());
        if(c == null) return;
        // refresh values
        c.setButtons(arg0.getButtonsHeld());
        c.setA(arg0.isButtonAHeld());
        c.setB(arg0.isButtonBHeld());
        c.setOne(arg0.isButtonOneHeld());
        c.setTwo(arg0.isButtonTwoHeld());
        c.setPlus(arg0.isButtonPlusHeld());
        c.setMinus(arg0.isButtonMinusHeld());
        c.setHome(arg0.isButtonHomeHeld());
        c.setUp(arg0.isButtonUpHeld());
        c.setDown(arg0.isButtonDownHeld());
        c.setLeft(arg0.isButtonLeftHeld());
        c.setRight(arg0.isButtonRightHeld());
    }

    public void onDisconnectionEvent(DisconnectionEvent arg0) {
        Controller c = motes.get(arg0.getWiimoteId());
        if(c == null) return;
        c.mote.disconnect();
    }
    public void onIrEvent(IREvent arg0) {
        Controller c = motes.get(arg0.getWiimoteId());
        if(c == null) return;
        c.setX(arg0.getX());
        c.setY(arg0.getY());
        c.setZ(arg0.getZ());
    }
    public void onExpansionEvent(ExpansionEvent arg0) {}
    public void onStatusEvent(StatusEvent arg0) {}
    public void onNunchukInsertedEvent(NunchukInsertedEvent arg0) {}
    public void onNunchukRemovedEvent(NunchukRemovedEvent arg0) {}
    public void onClassicControllerInsertedEvent(ClassicControllerInsertedEvent arg0) {}
    public void onClassicControllerRemovedEvent(ClassicControllerRemovedEvent arg0) {}
    public void onGuitarHeroInsertedEvent(GuitarHeroInsertedEvent arg0) {}
    public void onGuitarHeroRemovedEvent(GuitarHeroRemovedEvent arg0) {}
} 
