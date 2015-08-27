package de.hdmstuttgart.mi7.mgd.input;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.View.OnTouchListener;

import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * Created by florianporada on 26.08.15.
 */
public class InputSystem implements OnKeyListener, OnTouchListener, SensorEventListener {
    private Queue<InputEvent> inputQueue;
    private Queue<InputEvent> inputPool;

    public InputSystem(View view){
        int maxInputEvents = 128;
        this.inputQueue = new ArrayBlockingQueue<InputEvent>(maxInputEvents);
        this.inputPool = new ArrayBlockingQueue<InputEvent>(maxInputEvents);
        for (int i = 0; i < maxInputEvents; ++i)
        {
            inputPool.add(new InputEvent());
        }

        view.setFocusable(true);
        view.setFocusableInTouchMode(true);
        view.setOnKeyListener(this);
        view.setOnTouchListener(this);

        Context context = view.getContext();
        SensorManager sensorManager = (SensorManager)context.getSystemService(Context.SENSOR_SERVICE);

        Sensor accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        if (accelerometer != null)
            sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_GAME);

        Sensor gyroscope = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        if (gyroscope != null)
            sensorManager.registerListener(this, gyroscope, SensorManager.SENSOR_DELAY_GAME);

        Sensor rotation = sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);
        if (rotation != null)
            sensorManager.registerListener(this, rotation, SensorManager.SENSOR_DELAY_GAME);
    }


    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        InputEvent.InputDevice inputDevice = InputEvent.InputDevice.KEYBOARD;
        InputEvent.InputAction inputAction = InputEvent.InputAction.NONE;
        float time = event.getEventTime();

        switch (event.getAction()){
            case KeyEvent.ACTION_DOWN:
                inputAction = InputEvent.InputAction.DOWN;
                break;
            case KeyEvent.ACTION_UP:
                inputAction = InputEvent.InputAction.UP;
                break;

            default:
                return false;
        }
        InputEvent inputEvent = inputPool.poll();
        if(inputEvent == null)
            return false;

        inputEvent.set(inputDevice, inputAction, time, keyCode, 0, 0, 0, 0);
        inputQueue.add(inputEvent);

        return true;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        InputEvent.InputDevice inputDevice = InputEvent.InputDevice.TOUCHSCREEN;
        InputEvent.InputAction inputAction = InputEvent.InputAction.NONE;
        float time = event.getEventTime();
        float x = event.getX();
        float y = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                inputAction = InputEvent.InputAction.DOWN;
                break;

            case MotionEvent.ACTION_UP:
                inputAction = InputEvent.InputAction.UP;
                break;

            case MotionEvent.ACTION_MOVE:
                inputAction = InputEvent.InputAction.MOVE;
                break;

            default:
                return false;
        }

        InputEvent inputEvent = inputPool.poll();
        if (inputEvent == null)
            return false;

        inputEvent.set(inputDevice, inputAction, time, 0, x, y, 0, 0);
        inputQueue.add(inputEvent);

        return true;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        InputEvent.InputDevice inputDevice = InputEvent.InputDevice.TOUCHSCREEN;
        InputEvent.InputAction inputAction = InputEvent.InputAction.NONE;
        float time = event.timestamp / 1000.0f;
        float v0 = 0, v1 = 0, v2 = 0, v3 = 0;

        switch (event.sensor.getType()) {
            case Sensor.TYPE_ACCELEROMETER:
                inputDevice = InputEvent.InputDevice.ACCELEROMETER;
                v0 = event.values[0];
                v1 = event.values[1];
                v2 = event.values[2];
                break;

            case Sensor.TYPE_GYROSCOPE:
                inputDevice = InputEvent.InputDevice.GYROSCOPE;
                v0 = event.values[0];
                v1 = event.values[1];
                v2 = event.values[2];
                break;

            case Sensor.TYPE_ROTATION_VECTOR:
                inputDevice = InputEvent.InputDevice.ROTATION;
                v0 = event.values[0];
                v1 = event.values[1];
                v2 = event.values[2];
                if (event.values.length > 3) {
                    v3 = event.values[3];
                } else {
                    // Viertes Element berechnen, falls nicht vorhanden.
                    // Siehe auch: Android SensorEvent Dokumentation:
                    // http://developer.android.com/reference/android/hardware/SensorEvent.html
                    v3 = (float) Math.cos(Math.asin(Math.sqrt(v0 * v0 + v1 * v1 + v2 * v2)));
                }
                break;

            default:
                return;
        }

        InputEvent inputEvent = inputPool.poll();
        if (inputEvent == null)
            return;

        inputEvent.set(inputDevice, inputAction, time, 0, v0, v1, v2, v3);
        inputQueue.add(inputEvent);


    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    public InputEvent peekEvent() {
        return inputQueue.peek();
    }

    public void popEvent() {
        InputEvent inputEvent = inputQueue.poll();
        if (inputEvent == null)
            return;

        inputPool.add(inputEvent);
    }
}
