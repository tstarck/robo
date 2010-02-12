import lejos.nxt.comm.*;
import java.io.*;

/**
 * WiiNXT is used on the NXT side to request 
 * wiimote values from the PC and to send data
 * back. Needs the proxy program running on the PC.
 * 
 * This code is based on a forum posting by 'enzomango' at this URL:
 * http://lejos.sourceforge.net/forum/viewtopic.php?p=4435&sid=1e8bc60a53450194b2a76a8ba8147c98
 * 
 * Big thanks to enzomango for the original post.
 * 
 * @author Jouko Strömmer
 */
public final class WiiNXT {
    private static WiiNXT _instance = null;


    public static final int RECVMOTION = 1;
    public static final int RECVBUTTONS = 2;
    public static final int RECVIR = 4;
    public static final int TRANSMIT = 8;
    public static final int RUMBLEON = 16;
    public static final int RUMBLEOFF = 32;
    public static final int IRON = 64;
    public static final int IROFF = 128;
    public static final int MOTIONON = 256;
    public static final int MOTIONOFF = 512;


    private static BTConnection btc = null;
    private static DataInputStream dis = null;
    private static DataOutputStream dos = null;

    // values we're interested in
    //private int _pitch, _roll, _yaw, _a, _b, _one, _two, _plus, _minus, _home, _up, _down, _left, _right, _x, _y, _z;
    private float _pitch, _roll, _yaw, _z;
    private boolean _a, _b, _one, _two, _plus, _minus, _home, _up, _down, _left, _right;
    private int _x, _y;
    private short _buttons;
    private int lastRequest;

    // user can choose which values to measure and transmit
    private boolean recvaccel = false, recvbuttons = false, recvir = false;

    // check if a bit is set (for button states)
    public boolean testbit (int b, int bit) {
        return (b & (1 << bit)) > 0;
    }

    public void setMode(int mode) {
        setMode(mode, null);
    }

    // options for setMode (those that make sense):
    // Wiimote options
    // RUMBLEON, RUMBLEOFF, IRON, IROFF, MOTIONON, MOTIONOFF
    // Transmit a chunk of data:
    // TRANSMIT
    // Set data to request when calling read():
    // RECVMOTION, RECVBUTTONS, RECVIR

    // example: setMode(RECVMOTION | RECVBUTTONS | MOTIONON);
    // ...would enable motion tracking and enable read() to get motion and button data
    public void setMode (int mode, int[] data) {
        // toggle receiving accelerometer values
        // option for read()
        boolean a = testbit(mode, 0);
        if(a != recvaccel) recvaccel = a;
        // toggle receiving button states
        // option for read()
        boolean b = testbit(mode, 1);
        if(b != recvbuttons) recvbuttons = b;
        // toggle IR tracking and receiving position values
        // option for read()
        boolean i = testbit(mode, 2);
        if(i != recvir) recvir = i;

        try {
            // above we interpret RECVXXX as a field change, so clear out the
            // actual request for data in the mode value (read() does that), 
            // and set options.
            // setMode should never request sensor values from PC itself
            mode = mode & ~7;
            dos.writeInt(mode);
            dos.flush();
            if(data != null) {
                write(data, false);
            }
        } catch (IOException e) {
            System.out.println("Error setting mode.");
        }

    }

    private WiiNXT() {
        try {
            // connect via bluetooth
            System.out.println("Wiimote: Waiting...");      
            btc = Bluetooth.waitForConnection();
            dis = btc.openDataInputStream();
            dos = btc.openDataOutputStream();
            System.out.println("Wiimote: Connected.");   
        } catch(Exception e) {
            System.out.println("Wiimote: Error!");            
        }
    }

    // use just one instance
    public synchronized static WiiNXT getInstance() {
        if (_instance == null ) {
            _instance = new WiiNXT();
        }
        return _instance;
    }

    /**
     * Read data from bluetooth. Note: the order in which these are read should
     * match the proxy program!
     */
    public synchronized void read () {
        int request = (recvaccel ? RECVMOTION : 0) | (recvbuttons ? RECVBUTTONS : 0) | (recvir ? RECVIR : 0);
        lastRequest = request;

        // if nothing to read, don't bother trying
        if(request != 0) {
            try {
                // inform PC we want to read values

                dos.writeInt(request);
                dos.flush();

                if(recvaccel) {
                    _pitch = dis.readFloat();
                    _roll = dis.readFloat();
                    _yaw = dis.readFloat();
                }
                if(recvbuttons) {
                    _buttons = dis.readShort();
                    // update button states
                    _two = testbit(_buttons, 0);
                    _one = testbit(_buttons, 1);
                    _b =  testbit(_buttons, 2);
                    _a = testbit(_buttons, 3);
                    _minus = testbit(_buttons, 4);
                    _home = testbit(_buttons, 7);
                    _left = testbit(_buttons, 8);
                    _right = testbit(_buttons, 9);
                    _down = testbit(_buttons, 10);
                    _up = testbit(_buttons, 11);
                    _plus = testbit(_buttons, 12);
                }
                if(recvir) {
                    _x = dis.readInt();
                    _y = dis.readInt();
                    _z = dis.readFloat();
                }
            } catch(Exception e) {
                System.out.println("Wiimote: Read error!");   
            }
        }
    }

    // send data and optionally the control code
    public synchronized void write (int[] data, boolean sendcontrol) {
        try {
            // inform PC we want to send values
            if(sendcontrol) {
                dos.writeInt(TRANSMIT);
                dos.flush();
            }
            // tell how many Ints
            dos.writeInt(data.length);
            dos.flush();
            // write that many Ints
            for(int x = 0; x < data.length; x++) {
                dos.writeInt(data[x]);
                dos.flush();
            }
        } catch(Exception e) {
            System.out.println("Wiimote: Write error!");   
        }
    }

    // send control code for transmitting data + the data itself
    public synchronized void write (int[] data) {
        write(data, true);
    }

    // control rumble
    public synchronized void setRumble(boolean active) {
        try {
            if(active) dos.writeInt(RUMBLEON);
            else dos.writeInt(RUMBLEOFF);
            dos.flush();
        } catch(Exception e) {
            System.out.println("Wiimote: Write error!");   
        }

    }

    // control IR features
    public synchronized void setIR(boolean active) {
        try {
            if(active) dos.writeInt(IRON);
            else dos.writeInt(IROFF);
            dos.flush();
        } catch(Exception e) {
            System.out.println("Wiimote: Write error!");   
        }

    }

    // control motion detection
    public synchronized void setMotionDetection(boolean active) {
        try {
            if(active) dos.writeInt(MOTIONON);
            else dos.writeInt(MOTIONOFF);
            dos.flush();
        } catch(Exception e) {
            System.out.println("Wiimote: Write error!");   
        }
    }

    // disconnect
    public void shutdown() throws Exception {
        dis.close();
        dos.close();
        Thread.sleep(100);
        btc.close();
    }

    // return human readable state of the wiimote as String
    public String state () {
        return "Pi: "+Math.round(_pitch)+"\nRo: "+Math.round(_roll)+"\nYa: "+Math.round(_yaw)+
        "\nA:"+(_a ? "O" : " ")+" B:"+(_b ? "O" : " ")+" +:"+(_plus ? "O" : " ")+" -:"+(_minus ? "O" : " ")+" H:"+(_home ? "O" : " ")+
        " 1:"+(_one ? "O" : " ")+" 2:"+(_two ? "O" : " ")+
        "\nU:"+(_up ? "O" : " ")+" D:"+(_down ? "O" : " ")+" L:"+(_left ? "O" : " ")+" R:"+(_right ? "O" : " ")+
        "\nX:"+_x+" Y:"+_y+" Z:"+_z; 
    }

    // getters
    public float pitch() { return _pitch; }
    public float roll() { return _roll; }
    // IR sensing needs to be enabled for this
    public float yaw() { return _yaw; }

    public boolean buttonA() { return _a; }
    public boolean buttonB() { return _b; }
    public boolean buttonOne() { return _one; }
    public boolean buttonTwo() { return _two; }
    public boolean buttonPlus() { return _plus; }
    public boolean buttonMinus() { return _minus; }
    public boolean buttonHome() { return _home; }
    public boolean buttonUp() { return _up; }
    public boolean buttonDown() {  return _down; }
    public boolean buttonLeft() { return _left; }
    public boolean buttonRight() { return _right; }
    public short buttons() { return _buttons; }

    // IR sensing needs to be enabled for these
    public int X() { return _x; }
    public int Y() { return _y; }
    public float Z() { return _z; }

    public void setLastRequest(int lastRequest) { this.lastRequest = lastRequest; }
    public int getLastRequest() { return lastRequest; }

} 
