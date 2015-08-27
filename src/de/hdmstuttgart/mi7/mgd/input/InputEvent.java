package de.hdmstuttgart.mi7.mgd.input;

/**
 * Created by florianporada on 26.08.15.
 */
public class InputEvent {
    public enum InputDevice {
        NONE,
        KEYBOARD,
        TOUCHSCREEN,
        ACCELEROMETER,
        GYROSCOPE,
        ROTATION
    }

    public enum InputAction {
        NONE,
        DOWN,
        UP,
        MOVE,
        UPDATE,
    }

    private InputDevice inputDevice;
    private InputAction inputAction;
    private float time;
    private int keycode;
    private float values[] = new float[4];

    public InputDevice getInputDevice() {
        return inputDevice;
    }

    public InputAction getInputAction() {
        return inputAction;
    }

    public float getTime() {
        return time;
    }

    public int getKeycode() {
        return keycode;
    }

    public float[] getValues() {
        return values;
    }

    public void set(InputDevice inputDevice, InputAction inputAction, float time, int keycode, float f0, float f1, float f2, float f3){
        this.inputDevice = inputDevice;
        this.inputAction = inputAction;
        this.time = time;
        this.keycode = keycode;
        this.values[0] = f0;
        this.values[1] = f1;
        this.values[2] = f2;
        this.values[3] = f3;

    }
}
