package fr.isen.costanzo.androidsmartdevice

import android.bluetooth.BluetoothAdapter
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.floatingactionbutton.FloatingActionButton
import androidx.recyclerview.widget.RecyclerView
import android.Manifest
import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.bluetooth.le.*
import android.content.*
import android.os.*
import android.provider.Settings
import android.util.Log
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import java.util.*

import fr.isen.costanzo.androidsmartdevice.databinding.ActivityScanBinding
import kotlin.collections.ArrayList

private const val PERMISSION_REQUEST_CODE =0
private const val REQUEST_LOCATION_PERMISSION = 1
class ScanActivity : AppCompatActivity() {
    private lateinit var binding: ActivityScanBinding

    private var Devices: ArrayList<BLE> = ArrayList()
    private val bluetoothAdapter: BluetoothAdapter by
    lazy(LazyThreadSafetyMode.NONE) {
        val bluetoothManager =
            getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        bluetoothManager.adapter
    }

    private val handler = Handler(Looper.getMainLooper())
    private var mscanning = false


    private val leDeviceList = ArrayList<BluetoothDevice>()

    // Stops scanning after 10 seconds.
    private val SCAN_PERIOD: Long = 10000
    val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            //permissions.containsValue(false)
            if (permissions.all { it.value }) {
                scanBLEDevices()
            }
        }

    // Device scan callback.
    private val leScanCallback1: ScanCallback = object : ScanCallback() {
        override fun onScanResult(callbackType: Int, result: ScanResult) {
            super.onScanResult(callbackType, result)
            requestPermissions()
            var ble = BLE()
            if (ActivityCompat.checkSelfPermission(
                    this@ScanActivity,
                    Manifest.permission.BLUETOOTH_CONNECT
                ) != PackageManager.PERMISSION_GRANTED
            ) {

                return
            }
            ble.name = result.device.name
            ble.address = result.device.address
            Devices.add(ble)
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityScanBinding.inflate(layoutInflater)
        setContentView(binding.root)
        var scanning = true
        binding.ScanBar.isVisible = false
        binding.scanList.isVisible = false
        bluetoothAdapter?.bluetoothLeScanner
        binding.scanList.layoutManager = LinearLayoutManager(this)
        binding.scanList.adapter = ScanAdapter(leDeviceList) {
            val intent = Intent(this, DeviceActivity::class.java)
            intent.putExtra("device", it)
            startActivity(intent)
        }
        scanDeviceWithPermissions()

        /*val button = binding.launchBLE
        if (bluetoothAdapter?.isEnabled == true) {
            scanDeviceWithPermissions()
        } else {

        }$
         */

    }

    private fun scanDeviceWithPermissions() {
        if (allPermissionGranted()) {
            initToggleActions()
            //scanBLEDevices()
        } else {

            requestPermissionLauncher.launch(getAllPermissions())
        }
    }

    private fun requestPermissions() {
        ActivityCompat.requestPermissions(this, getAllPermissions(), PERMISSION_REQUEST_CODE)
    }

    @SuppressLint("MissingPermission")
    private fun scanBLEDevices() {
        if (!mscanning) {
            handler.postDelayed({
                mscanning = false
                bluetoothAdapter.bluetoothLeScanner.stopScan(leScanCallback1)
            }, SCAN_PERIOD)
            mscanning = true
            bluetoothAdapter.bluetoothLeScanner.startScan(leScanCallback1)
        } else {
            mscanning = false
            bluetoothAdapter.bluetoothLeScanner.stopScan(leScanCallback1)
        }

    }


    private fun allPermissionGranted(): Boolean {
        val allPermissions = getAllPermissions()
        return allPermissions.all {
            ContextCompat.checkSelfPermission(this, it) == PackageManager.PERMISSION_GRANTED

        }

    }

    private fun initToggleActions() {
        binding.launchBLE.setOnClickListener {
            if (!mscanning) {
                binding.launchBLE.setImageResource(android.R.drawable.ic_media_pause)
                binding.ScanBar.isVisible = true
                binding.scanList.isVisible = true
                scanBLEDevices()
                val toastdisplay = Toast.makeText(
                    applicationContext, "Scanning",
                    Toast.LENGTH_SHORT
                )
                toastdisplay.show()
            } else {
                binding.launchBLE.setImageResource(android.R.drawable.ic_media_play)
                binding.ScanBar.isVisible = false
                binding.scanList.isVisible = false
                scanBLEDevices()
                val toastdisplay2 = Toast.makeText(
                    applicationContext, "NotScanning",
                    Toast.LENGTH_SHORT
                )
                toastdisplay2.show()


            }
            //mscanning = !mscanning

        }
    }

    private fun getAllPermissions(): Array<String> {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.BLUETOOTH_SCAN,
                Manifest.permission.BLUETOOTH_CONNECT,
            )
        } else {
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        }
    }


    class BLE {
        lateinit var name: String
        lateinit var address: String
    }

    //Fonction scan Bluetooth corrigée
   /* @SuppressLint("MissingPermission")
    private fun scanBLEDevice() {
        if (!mscanning) { // Stops scanning after a pre-defined scan period.
            handler.postDelayed({
                mscanning = false
                bluetoothAdapter?.bluetoothLeScanner?.stopScan(leScanCallback)
                initToggleActions()
            }, SCAN_PERIOD)
            mscanning = true
            bluetoothAdapter?.bluetoothLeScanner?.stopScan(leScanCallback)
        } else {
            mscanning = false
            bluetoothAdapter?.bluetoothLeScanner?.stopScan(leScanCallback)
        }
        initToggleActions()
    }*/

    @SuppressLint("MissingPermission")
    override fun onStop() {
        super.onStop()
        if (bluetoothAdapter?.isEnabled == true && allPermissionGranted()) {
            mscanning = false
            bluetoothAdapter?.bluetoothLeScanner?.stopScan(leScanCallback)
        }
    }

    //Device scan callback
    private val leScanCallback: ScanCallback = object : ScanCallback() {
        @SuppressLint("MissingPermission")
        override fun onScanResult(callbackType: Int, result: ScanResult?) {
            super.onScanResult(callbackType, result)
            if (result != null) {
                (binding.scanList.adapter as? ScanAdapter)?.addDevice(result.device)
            }
            binding.scanList.adapter?.notifyDataSetChanged()
        }
    }

    companion object {
        private val SCAN_PERIOD: Long = 10000
    }

}


