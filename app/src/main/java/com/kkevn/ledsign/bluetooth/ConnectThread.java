package com.kkevn.ledsign.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.Handler;
import android.util.Log;

import com.kkevn.ledsign.MainActivity;

import java.io.IOException;

public class ConnectThread extends Thread {
    private final BluetoothSocket mmSocket;
    private final BluetoothDevice mmDevice;

    BluetoothAdapter bluetoothAdapter;
    Handler mHandler;
    boolean fail = false;

    public ConnectThread(BluetoothDevice device, BluetoothAdapter adapter, Handler handler) {
        // Use a temporary object that is later assigned to mmSocket
        // because mmSocket is final.
        BluetoothSocket tmp = null;
        mmDevice = device;

        bluetoothAdapter = adapter;

        mHandler = handler;

        try {
            // Get a BluetoothSocket to connect with the given BluetoothDevice.
            // MY_UUID is the app's UUID string, also used in the server code.
            tmp = mmDevice.createRfcommSocketToServiceRecord(MainActivity.BTMODULEUUID);
        } catch (IOException e) {
            fail = true;
            Log.e(this.getClass().getSimpleName(), "Socket's create() method failed", e);
        }
        mmSocket = tmp;

        mHandler.obtainMessage(MainActivity.CONNECTING_STATUS, 0, -1)
                .sendToTarget();
    }

    public void run() {
        // Cancel discovery because it otherwise slows down the connection.
        bluetoothAdapter.cancelDiscovery();

        try {
            // Connect to the remote device through the socket. This call blocks
            // until it succeeds or throws an exception.
            mmSocket.connect();
        } catch (IOException connectException) {
            // Unable to connect; close the socket and return.
            try {
                fail = true;
                mmSocket.close();
                mHandler.obtainMessage(MainActivity.CONNECTING_STATUS, -1, -1)
                        .sendToTarget();
            } catch (IOException closeException) {
                Log.e(this.getClass().getSimpleName(), "Could not close the client socket", closeException);
            }
            return;
        }

        // The connection attempt succeeded. Perform work associated with
        // the connection in a separate thread.
        if (fail == false) {
            MainActivity.manageMyConnectedSocket(mmSocket);

            mHandler.obtainMessage(MainActivity.CONNECTING_STATUS, 1, -1, mmDevice.getName())
                    .sendToTarget();
        }
    }

    // Closes the client socket and causes the thread to finish.
    public void cancel() {
        try {
            mmSocket.close();
        } catch (IOException e) {
            Log.e(this.getClass().getSimpleName(), "Could not close the client socket", e);
        }
    }
}
