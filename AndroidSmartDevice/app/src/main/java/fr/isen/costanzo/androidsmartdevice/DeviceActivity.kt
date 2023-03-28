package fr.isen.costanzo.androidsmartdevice

import android.bluetooth.BluetoothDevice
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import fr.isen.costanzo.androidsmartdevice.databinding.ActivityDeviceBinding

class DeviceActivity : AppCompatActivity() {
    private lateinit var binding : ActivityDeviceBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDeviceBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val bluetoothDevice: BluetoothDevice? = intent.getParcelableExtra("device")

    }
}