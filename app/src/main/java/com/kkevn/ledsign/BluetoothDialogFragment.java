package com.kkevn.ledsign;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.kkevn.ledsign.ui.bluetooth.ConnectThread;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class BluetoothDialogFragment extends DialogFragment {

    //ArrayList selectedItem;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        //selectedItem = new ArrayList();

        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        //builder.setMessage(R.string.dialog_bt_profile)
        builder.setTitle(R.string.app_name)
                .setSingleChoiceItems(MainActivity.mBTArrayAdapter, 1 /*new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1)*/, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // The 'which' argument contains the index position of the selected item
                        //selectedItem.add(which);
                        String item = MainActivity.mBTArrayAdapter.getItem(which).toString();
                        final String address = item.substring(item.length() - 17);

                        Toast.makeText(getContext(), address, Toast.LENGTH_SHORT).show();
                    }
                })
                /*.setAdapter(MainActivity.mBTArrayAdapter, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // The 'which' argument contains the index position of the selected item
                        //selectedItem.add(which);
                        String item = MainActivity.mBTArrayAdapter.getItem(which).toString();
                        final String address = item.substring(item.length() - 17);

                        Toast.makeText(getContext(), address, Toast.LENGTH_SHORT).show();
                    }
                })*/
                /*.setItems(, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // The 'which' argument contains the index position
                        // of the selected item
                    }
                }*/
                .setPositiveButton(R.string.dialog_scan, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // scan devices
                        MainActivity.pairedDevicesList();
                    }
                })
                .setNegativeButton(R.string.dialog_discard, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // discover new device

                    }
                });
        // Create the AlertDialog object and return it
        return builder.create();
    }
}
