package com.kkevn.ledsign.ui.bluetooth;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.util.Log;

import java.io.IOException;
import java.util.UUID;

public class ConnectThread extends Thread {
    private final BluetoothSocket mmSocket;
    private final BluetoothDevice mmDevice;

    public ConnectThread(BluetoothDevice device) {
        BluetoothSocket tmp = null;

        mmDevice = device;

        try {
            tmp = device.createRfcommSocketToServiceRecord(UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"));
        } catch (IOException ioe) {
            Log.e("ConnectThread", "error initializing bluetooth socket", ioe);
        }

        mmSocket = tmp;
    }

    public void run() {
        //bluetoothAdapter.cancelDiscovery();

        try {
            mmSocket.connect();
        } catch (IOException connectException) {

            try {
                mmSocket.close();
            } catch (IOException closeException) {
                Log.e("ConnectThread", "error connecting bluetooth socket", closeException);
            }
            return;
        }
        //manageMyConnectedSocket(mmSocket);
    }

    public void cancel() {
        try {
            mmSocket.close();
        } catch (IOException ioe) {
            Log.e("ConnectThread", "error closing bluetooth socket", ioe);
        }
    }
}
