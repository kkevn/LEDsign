/**
 * ConnectThread is the thread that attempts to establish a connection with another Bluetooth
 * device as a client.
 *
 * @source https://developer.android.com/guide/topics/connectivity/bluetooth#ConnectAsAClient
 *
 * @author Kevin Kowalski
 */

package com.kkevn.ledsign.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.Handler;
import android.util.Log;

import com.kkevn.ledsign.MainActivity;

import java.io.IOException;

public class ConnectThread extends Thread {

    // declare relevant variables
    private final BluetoothSocket mmSocket;
    private final BluetoothDevice mmDevice;
    private BluetoothAdapter bluetoothAdapter;
    private Handler mHandler;
    private boolean connectionFailed = false;

    /**
     * Constructor for this ConnectThread.
     *
     * @param {BluetoothDevice} device: Target device to establish connection with.
     * @param {BluetoothAdapter} adapter: Default Bluetooth adapter of this device.
     * @param {Handler} handler: Handler to communicate with UI thread.
     */
    public ConnectThread(BluetoothDevice device, BluetoothAdapter adapter, Handler handler) {

        // use a temporary object that is later assigned to mmSocket because mmSocket is final
        BluetoothSocket tmp = null;

        // initialize this thread's variables
        mmDevice = device;
        bluetoothAdapter = adapter;
        mHandler = handler;

        // attempt the socket connection
        try {

            // get a BluetoothSocket to connect with the given BluetoothDevice with app's UUID
            tmp = mmDevice.createRfcommSocketToServiceRecord(MainActivity.BTMODULEUUID);

        } catch (IOException e) {

            // flag connection as failed and log the error
            connectionFailed = true;
            Log.e(this.getClass().getSimpleName(), "Socket's create() method failed", e);
        }

        // successful connection, assign the socket
        mmSocket = tmp;

        // inform UI connection is attempted, enable the indefinite progress bar
        mHandler.obtainMessage(MainActivity.CONNECTING_STATUS, 0, -1).sendToTarget();
    }

    /**
     * Contains the behavior of this thread during its lifespan.
     */
    public void run() {

        // cancel discovery because it otherwise slows down the connection
        bluetoothAdapter.cancelDiscovery();

        // attempt the device connection
        try {

            // connect to the remote device through the socket
            mmSocket.connect();

        } catch (IOException connectException) {

            // unable to connect, close socket and return
            try {

                // flag connection as failed
                connectionFailed = true;

                // close the socket
                mmSocket.close();

                // inform UI connection attempt finished, disable the indefinite progress bar
                mHandler.obtainMessage(MainActivity.CONNECTING_STATUS, -1, -1).sendToTarget();

            } catch (IOException closeException) {

                // log the error
                Log.e(this.getClass().getSimpleName(), "Could not close the client socket", closeException);
            }

            return;
        }

        // connection attempt succeeded
        if (connectionFailed == false) {

            // let UI create a thread to manage the connection
            MainActivity.manageMyConnectedSocket(mmSocket);

            // inform UI connection attempt finished, disable the indefinite progress bar
            mHandler.obtainMessage(MainActivity.CONNECTING_STATUS, 1, -1, mmDevice.getName()).sendToTarget();
        }
    }

    /**
     * Closes the client socket and causes the thread to finish.
     */
    public void cancel() {
        try {
            mmSocket.close();
        } catch (IOException e) {
            Log.e(this.getClass().getSimpleName(), "Could not close the client socket", e);
        }
    }
}