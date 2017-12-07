package com.doing.flat.coffee.ble;

import android.annotation.TargetApi;
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;

import java.util.UUID;

@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
public class BLEService extends Service {

    public final static String ACTION_DATA_CHANGE = "com.example.bluetooth.le.ACTION_DATA_CHANGE";
    public final static String ACTION_RSSI_READ = "com.example.bluetooth.le.ACTION_RSSI_READ";
    public final static String ACTION_STATE_CONNECTED = "com.example.bluetooth.le.ACTION_STATE_CONNECTED";
    public final static String ACTION_STATE_DISCONNECTED = "com.example.bluetooth.le.ACTION_STATE_DISCONNECTED";
    public final static String ACTION_WRITE_OVER = "com.example.bluetooth.le.ACTION_WRITE_OVER";
    public final static String ACTION_READ_OVER = "com.example.bluetooth.le.ACTION_READ_OVER";
    public final static String ACTION_READ_Descriptor_OVER = "com.example.bluetooth.le.ACTION_READ_Descriptor_OVER";
    public final static String ACTION_WRITE_Descriptor_OVER = "com.example.bluetooth.le.ACTION_WRITE_Descriptor_OVER";
    public final static String ACTION_ServicesDiscovered_OVER = "com.example.bluetooth.le.ACTION_ServicesDiscovered_OVER";
    public final static String ACTION_OVER = "com.example.bluetooth.le.ACTION_OVER";

    public static final UUID SERVIE_UUID = UUID
            .fromString("0000ffe0-0000-1000-8000-00805f9b34fb");
//    public static final UUID SERVIE_UUID = UUID
//            .fromString("0000ffb0-0000-1000-8000-00805f9b34fb");
    public static final UUID SERVIE_UUID_1 = UUID
            .fromString("0000ffe1-0000-1000-8000-00805f9b34fb");
    public static final UUID SERVIE_UUID_2 = UUID
            .fromString("0000ffe1-0000-1000-8000-00805f9b34fb");
//    public static final UUID SERVIE_UUID_1 = UUID
//            .fromString("0000ffb2-0000-1000-8000-00805f9b34fb");
//    public static final UUID SERVIE_UUID_2 = UUID
//            .fromString("0000ffb2-0000-1000-8000-00805f9b34fb");

    public BluetoothManager mBluetoothManager;
    public BluetoothAdapter mBluetoothAdapter;
    public BluetoothGatt mBluetoothGatt;
    private boolean connect_flag = false;

    private BluetoothGattCallback mGattCallback = new BluetoothGattCallback() {

        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status,
                                            int newState) {
            super.onConnectionStateChange(gatt, status, newState);
            System.out.println("mGattCallback-mGattCallback_onConnectionStateChange=" + newState);
            if (newState == BluetoothProfile.STATE_CONNECTED) { // ���ӳɹ�
                System.out.println("CONNECTED");
                connect_flag = true;
                mBluetoothGatt.discoverServices();
                broadcastUpdate(ACTION_STATE_CONNECTED);

            } else if (newState == BluetoothProfile.STATE_DISCONNECTED) { // �Ͽ�����
                System.out.println("UNCONNECTED");
                connect_flag = false;
                broadcastUpdate(ACTION_STATE_DISCONNECTED);
            }

        }

        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            super.onServicesDiscovered(gatt, status);
            System.out.println("mGattCallback-mGattCallback_onServicesDiscovered=" + status);
            if (status == BluetoothGatt.GATT_SUCCESS) {
                System.out.println("mGattCallback-onServicesDiscovered");
                broadcastUpdate(ACTION_ServicesDiscovered_OVER, status);
            } else {
                System.out.println("mGattCallback-ACTION_OVER");
                broadcastUpdate(ACTION_OVER, status);
            }
        }

        @Override
        public void onDescriptorRead(BluetoothGatt gatt,
                                     BluetoothGattDescriptor descriptor, int status) {
            super.onDescriptorRead(gatt, descriptor, status);
            System.out.println("mGattCallback-mGattCallback_onDescriptorRead=" + status);
            broadcastUpdate(ACTION_READ_Descriptor_OVER, status);
        }

        @Override
        public void onCharacteristicRead(BluetoothGatt gatt,
                                         BluetoothGattCharacteristic characteristic, int status) {
            super.onCharacteristicRead(gatt, characteristic, status);
            if (status == BluetoothGatt.GATT_SUCCESS) {
                System.out.println("mGattCallback-mGattCallback_onCharacteristicRead=" + status);
                broadcastUpdate(ACTION_READ_OVER, characteristic.getValue());
            } else {
                System.out.println("mGattCallback-ACTION_OVER");
                broadcastUpdate(ACTION_OVER, status);
            }
        }

        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt,
                                            BluetoothGattCharacteristic characteristic) {
            super.onCharacteristicChanged(gatt, characteristic);
            System.out.println("mGattCallback-mGattCallback_onCharacteristicChanged=" + characteristic.getValue());
            broadcastUpdate(ACTION_DATA_CHANGE, characteristic.getValue());
        }


        @Override
        public void onCharacteristicWrite(BluetoothGatt gatt,
                                          BluetoothGattCharacteristic characteristic, int status) {
            super.onCharacteristicWrite(gatt, characteristic, status);
            System.out.println("mGattCallback-mGattCallback_onCharacteristicWrite=" + status + "  characteristic=" + characteristic.getValue());
            broadcastUpdate(ACTION_WRITE_OVER, characteristic.getValue());
        }

    };

    public class LocalBinder extends Binder {
        public BLEService getService() {
            return BLEService.this;
        }
    }

    private final IBinder mBinder = new LocalBinder();

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        disConectBle();
    }

    @Override
    public boolean onUnbind(Intent intent) {
        disConectBle();
        return super.onUnbind(intent);
    }

    // ��ʼ��BLE
    public boolean initBle() {
        mBluetoothManager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);

        if (null == mBluetoothManager) {
            return false;
        }

        mBluetoothAdapter = mBluetoothManager.getAdapter();
        if (null == mBluetoothAdapter) {
            return false;
        }

//		if (!mBluetoothAdapter.isEnabled()) {
//			mBluetoothAdapter.enable();
//		}

        return true;
    }

    // ɨ��
    public void scanBle(BluetoothAdapter.LeScanCallback callback) {
        if (callback != null)
            mBluetoothAdapter.startLeScan(callback);
    }

    // ֹͣɨ��
    public void stopscanBle(BluetoothAdapter.LeScanCallback callback) {
        mBluetoothAdapter.stopLeScan(callback);
    }

    // ��������
    public boolean conectBle(BluetoothDevice mBluetoothDevice) {
        disConectBle();

        BluetoothDevice device_tmp = mBluetoothAdapter.getRemoteDevice(mBluetoothDevice.getAddress());
        if (device_tmp == null) {
            System.out.println("device ������");
            return false;
        }

        mBluetoothGatt = device_tmp.connectGatt(getApplicationContext(), false,
                mGattCallback);
        return true;
    }

    // �ر�����
    public void disConectBle() {
        if (mBluetoothGatt != null) {
            mBluetoothGatt.disconnect();
            mBluetoothGatt.close();
            mBluetoothGatt = null;
            connect_flag = false;
        }
    }

    // �ر�����
    public void close() {
        if (mBluetoothGatt != null) {
            mBluetoothGatt.close();
            mBluetoothGatt = null;
            connect_flag = false;
        }
    }

    // ����Ƿ�����
    public boolean isConnected() {
        return connect_flag;
    }

    // ���͹㲥��Ϣ
    private void broadcastUpdate(final String action) {
        final Intent intent = new Intent(action);
        sendBroadcast(intent);
    }

    // ���͹㲥��Ϣ
    private void broadcastUpdate(final String action, int value) {
        final Intent intent = new Intent(action);
        intent.putExtra("value", value);
        sendBroadcast(intent);
    }

    // ���͹㲥��Ϣ
    private void broadcastUpdate(final String action, byte value[]) {
        final Intent intent = new Intent(action);
        intent.putExtra("value", value);
        sendBroadcast(intent);
    }
}
