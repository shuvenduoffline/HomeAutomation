package com.example.homeautomationssap;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.Toast;

import java.io.IOException;
import java.util.UUID;


public class DeviceControlActivity extends AppCompatActivity {
    static final UUID myUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    ImageButton Abt;
    ImageButton Discnt;
    ImageButton Off;
    ImageButton On;
    String address = null;
    BluetoothSocket btSocket = null;
    private boolean isBtConnected = false;
    BluetoothAdapter myBluetooth = null;
    private ProgressDialog progress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_device_control);

        this.address = getIntent().getStringExtra(ConnectDeviceActivity.EXTRA_ADDRESS);
        new ConnectBT().execute(new Void[0]);

        findViewById(R.id.btn_ledon).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                turnOnLed();
            }
        });
        findViewById(R.id.btn_ledoff).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                turnOffLed();
            }
        });

        findViewById(R.id.btn_ceillingfanon).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                turnOnFan();
            }
        });

        findViewById(R.id.btn_ceillingfanoff).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                turnOffFan();
            }
        });

    }


    private class ConnectBT extends AsyncTask<Void, Void, Void> {
        private boolean ConnectSuccess;

        private ConnectBT() {
            this.ConnectSuccess = true;
        }

//        /* synthetic */ ConnectBT(DeviceControlActivity x0, AnonymousClass1 x1) {
//            this();
//        }

        /* Access modifiers changed, original: protected */
        public void onPreExecute() {
            DeviceControlActivity.this.progress = ProgressDialog.show(DeviceControlActivity.this, "Connecting...", "Please wait!!!");
        }

        /* Access modifiers changed, original: protected|varargs */
        public Void doInBackground(Void... devices) {
            try {
                if (DeviceControlActivity.this.btSocket == null || !DeviceControlActivity.this.isBtConnected) {
                    DeviceControlActivity.this.myBluetooth = BluetoothAdapter.getDefaultAdapter();
                    BluetoothDevice dispositivo = DeviceControlActivity.this.myBluetooth.getRemoteDevice(DeviceControlActivity.this.address);
                    DeviceControlActivity.this.btSocket = dispositivo.createInsecureRfcommSocketToServiceRecord(DeviceControlActivity.myUUID);
                    BluetoothAdapter.getDefaultAdapter().cancelDiscovery();
                    DeviceControlActivity.this.btSocket.connect();
                }
            } catch (IOException e) {
                this.ConnectSuccess = false;
            }
            return null;
        }

        /* Access modifiers changed, original: protected */
        public void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (this.ConnectSuccess) {
                DeviceControlActivity.this.msg("Connected.");
                DeviceControlActivity.this.isBtConnected = true;
            } else {
                DeviceControlActivity.this.msg("Connection Failed. Is it a SPP Bluetooth? Try again.");
                DeviceControlActivity.this.finish();
            }
            DeviceControlActivity.this.progress.dismiss();
        }
    }

    private void msg(String s) {
        Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
    }

    private void turnOffLed() {
        if (this.btSocket != null) {
            try {
                this.btSocket.getOutputStream().write("0".toString().getBytes());
            } catch (IOException e) {
                msg("Error");
                finish();
            }
        }
    }

    private void turnOnLed() {
        if (this.btSocket != null) {
            try {
                this.btSocket.getOutputStream().write("1".toString().getBytes());
            } catch (IOException e) {
                msg("Error");
                finish();
            }
        }
    }
    private void turnOffFan() {
        if (this.btSocket != null) {
            try {
                this.btSocket.getOutputStream().write("3".toString().getBytes());
            } catch (IOException e) {
                msg("Error");
                finish();
            }
        }
    }
    private void turnOnFan() {
        if (this.btSocket != null) {
            try {
                this.btSocket.getOutputStream().write("2".toString().getBytes());
            } catch (IOException e) {
                msg("Error");
                finish();
            }
        }
    }

}
