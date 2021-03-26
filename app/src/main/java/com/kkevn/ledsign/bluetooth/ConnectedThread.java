/**
 * ConnectedThread is the thread that attempts to manage a connection with an already connected
 * Bluetooth device.
 *
 * @source https://developer.android.com/guide/topics/connectivity/bluetooth#ManageAConnection
 *
 * @author Kevin Kowalski
 */

package com.kkevn.ledsign.bluetooth;

import android.bluetooth.BluetoothSocket;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.kkevn.ledsign.MainActivity;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class ConnectedThread extends Thread {

    // declare relevant variables
    private final BluetoothSocket mmSocket;
    private final InputStream mmInStream;
    private final OutputStream mmOutStream;
    private byte[] mmBuffer;
    private Handler mHandler;
    private int writeDelay = 1000;

    /**
     * Constructor for this ConnectedThread.
     *
     * @param {BluetoothSocket} socket: Bluetooth socket where connection is already established.
     * @param {Handler} handler: Handler to communicate with UI thread.
     */
    public ConnectedThread(BluetoothSocket socket, Handler handler) {

        // initialize this thread's variables
        mmSocket = socket;
        mHandler = handler;
        InputStream tmpIn = null;
        OutputStream tmpOut = null;

        // attempt to get the input stream
        try {
            tmpIn = socket.getInputStream();
        } catch (IOException e) {

            // log the error
            Log.e(this.getClass().getSimpleName(), "Error occurred when creating input stream", e);
        }

        // attempt to get the output stream
        try {
            tmpOut = socket.getOutputStream();
        } catch (IOException e) {

            // log the error
            Log.e(this.getClass().getSimpleName(), "Error occurred when creating output stream", e);
        }

        // assign the streams once successful
        mmInStream = tmpIn;
        mmOutStream = tmpOut;
    }

    /**
     * Contains the behavior of this thread during its lifespan.
     */
    public void run() {

        // allocate space for data to send and declare bytes to read
        mmBuffer = new byte[1024];
        int numBytes;

        // keep listening to the InputStream until an exception occurs
        while (true) {
            try {

                // read from the InputStream
                numBytes = mmInStream.read(mmBuffer);

                // send the obtained bytes to the UI activity
                Message readMsg = mHandler.obtainMessage(MainActivity.MESSAGE_READ, numBytes, -1, mmBuffer);
                readMsg.sendToTarget();

            } catch (IOException e) {

                // log the error
                Log.d(this.getClass().getSimpleName(), "Input stream was disconnected", e);

                // inform UI connection was lost, remove Bluetooth status as connected
                Message msg = mHandler.obtainMessage(MainActivity.CONNECTING_STATUS, -1, -1);
                msg.sendToTarget();
                break;
            }
        }
    }

    /**
     * Sends data to the remote device.
     *
     * @param {byte[]} bytes: Data to be sent over.
     */
    public void write(byte[] bytes) {
        try {

            // artificial delay to prevent too much incoming data to Arduino
            try {
                this.sleep(writeDelay);
            } catch (InterruptedException e) {
                Log.e(this.getClass().getSimpleName(), "Error occurred when sleeping", e);
            }

            // write the data
            mmOutStream.write(bytes);

            // share the sent message with the UI activity
            Message writtenMsg = mHandler.obtainMessage(MainActivity.MESSAGE_WRITE, -1, -1, mmBuffer);
            writtenMsg.sendToTarget();

        } catch (IOException e) {

            // log the error
            Log.e(this.getClass().getSimpleName(), "Error occurred when sending data", e);

            // send a failure message back to the activity
            Message writeErrorMsg = mHandler.obtainMessage(MainActivity.MESSAGE_WRITE, 0, -1);
            mHandler.sendMessage(writeErrorMsg);
        }
    }

    /**
     * Closes the client socket and causes the thread to finish.
     */
    public void cancel() {
        try {
            mmSocket.close();
        } catch (IOException e) {
            Log.e(this.getClass().getSimpleName(), "Could not close the connect socket", e);
        }
    }
}