package com.kkevn.ledsign.ui.bluetooth;

import android.bluetooth.BluetoothSocket;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

public class ConnectedThread extends Thread {

    private final BluetoothSocket socket;
    private final InputStream inStream;
    private final OutputStream outStream;
    //private byte[] buffer;
    public static final int RESPONSE_MESSAGE = 10;
    Handler uih;

    public ConnectedThread(BluetoothSocket socket, Handler uih) {
        this.socket = socket;
        this.uih = uih;
        InputStream tmpIn = null;
        OutputStream tmpOut = null;

        try {
            tmpIn = socket.getInputStream();
            tmpOut = socket.getOutputStream();
        } catch (IOException ioe) {
            Log.e("ConnectedThread", "error initializing in/output streams", ioe);
        }

        this.inStream = tmpIn;
        this.outStream = tmpOut;

        try {
            outStream.flush();
        } catch (IOException e) {
            return;
        }
    }

    public void run() {
        //buffer = new byte[1024]; // buffer store for stream
        //int bytes; // bytes returned from read()

        BufferedReader br = new BufferedReader(new InputStreamReader(this.inStream));

        while (true) {
            try {
                /*bytes = this.inStream.available();

                if (bytes != 0) {
                    SystemClock.sleep(100); // pause and wait for remaining data

                    bytes = this.inStream.available(); // how many bytes ready to be read
                    bytes = this.inStream.read(buffer, 0, bytes); // record how many bytes read
                    BluetoothFragment.mHandler.obtainMessage(BluetoothFragment.MESSAGE_READ, bytes, -1, buffer).sendToTarget(); // send obtained bytes to UI
                }*/

                String resp = br.readLine();

                Message msg = new Message();
                msg.what = RESPONSE_MESSAGE;
                msg.obj = resp;
                uih.sendMessage(msg);

                //bytes = this.inStream.read(buffer);

                //BluetoothFragment.mHandler.obtainMessage(BluetoothFragment.MESSAGE_READ, bytes, -1, buffer).sendToTarget(); // send obtained bytes to UI
            } catch (IOException ioe) {
                Log.e("ConnectedThread", "error reading incoming bytes", ioe);
                break;
            }
        }
    }

    public void write(/*String input*/byte[] bytes) {
        //byte[] bytes = input.getBytes();
        try {
            this.outStream.write(bytes);
            //BluetoothFragment.mHandler.obtainMessage(BluetoothFragment.MESSAGE_WRITE, -1, -1, buffer).sendToTarget();
        } catch (IOException ioe) {
            Log.e("ConnectedThread", "error writing outgoing bytes", ioe);
        }
    }

    public void cancel() {
        try {
            this.socket.close();
        } catch (IOException ioe) {
            Log.e("ConnectedThread", "error closing socket", ioe);
        }
    }
}