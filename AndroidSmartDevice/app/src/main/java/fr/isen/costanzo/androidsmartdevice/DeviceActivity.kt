package fr.isen.costanzo.androidsmartdevice

import android.annotation.SuppressLint
import android.bluetooth.*
import android.content.Context
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import fr.isen.costanzo.androidsmartdevice.databinding.ActivityDeviceBinding
import java.util.*

class DeviceActivity : AppCompatActivity() {
    private lateinit var binding : ActivityDeviceBinding

    private val bluetoothAdapter: BluetoothAdapter by
    lazy(LazyThreadSafetyMode.NONE) {
        val bluetoothManager =
            getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        bluetoothManager.adapter
    }

    var bluetoothGatt: BluetoothGatt? = null

    private val serviceUUID = UUID.fromString("0000feed-cc7a-482a-984a-7f2ed5b3e58f")
    private val characteristicLedUUID = UUID.fromString("0000abcd-8e22-4541-9d4c-21edae82ed19")
    private var ledBleueOn = false
    private var ledVerteOn = false
    private var ledRougeOn = false
    @SuppressLint("MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDeviceBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val name = intent.getStringExtra("name")
        val address = intent.getStringExtra("address")
        binding.Title.text=name
        connect(address!!)

        binding.ledbleu.setOnClickListener {
            val service = bluetoothGatt?.getService(serviceUUID)
            val characteristic = service?.getCharacteristic(characteristicLedUUID)
            if(ledBleueOn) {
                characteristic?.value = byteArrayOf(0x00)
                bluetoothGatt?.writeCharacteristic(characteristic)
            } else {

                characteristic?.value = byteArrayOf(0x01)
                bluetoothGatt?.writeCharacteristic(characteristic)
                ledVerteOn = false
                ledRougeOn = false
            }
            ledBleueOn = !ledBleueOn
        }
        binding.ledverte.setOnClickListener {
            val service = bluetoothGatt?.getService(serviceUUID)
            val characteristic = service?.getCharacteristic(characteristicLedUUID)
            if(ledVerteOn) {
                characteristic?.value = byteArrayOf(0x00)
                bluetoothGatt?.writeCharacteristic(characteristic)
            } else {

                characteristic?.value = byteArrayOf(0x02)
                bluetoothGatt?.writeCharacteristic(characteristic)
                ledBleueOn = false
                ledRougeOn = false
            }
            ledVerteOn = !ledVerteOn
        }
        binding.ledrouge.setOnClickListener {
            val service = bluetoothGatt?.getService(serviceUUID)
            val characteristic = service?.getCharacteristic(characteristicLedUUID)
            if(ledRougeOn) {
                characteristic?.value = byteArrayOf(0x00)
                bluetoothGatt?.writeCharacteristic(characteristic)
            } else {

                characteristic?.value = byteArrayOf(0x03)
                bluetoothGatt?.writeCharacteristic(characteristic)
                ledVerteOn = false
                ledBleueOn = false
            }
            ledRougeOn = !ledRougeOn
        }

    }

    @SuppressLint("MissingPermission")
    fun connect(address: String): Boolean {
        bluetoothAdapter?.let { adapter ->
            try {
                val device = adapter.getRemoteDevice(address)
                // connect to the GATT server on the device
                bluetoothGatt = device.connectGatt(this, false, bluetoothGattCallback)
                return true
            } catch (exception: IllegalArgumentException) {
                return false
            }
        } ?: run {
            return false
        }
    }

    private val bluetoothGattCallback: BluetoothGattCallback = object : BluetoothGattCallback() {
        @SuppressLint("MissingPermission")
        override fun onConnectionStateChange(gatt: BluetoothGatt, status: Int, newState: Int) {
            if (newState == BluetoothProfile.STATE_CONNECTED) {
                // successfully connected to the GATT Server
                bluetoothGatt?.discoverServices()

            } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                // disconnected from the GATT Server

            }
        }
    }
}