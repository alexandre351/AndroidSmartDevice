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
import android.bluetooth.BluetoothDevice
import android.bluetooth.le.*
import android.content.*
import android.os.*
import android.provider.Settings
import android.widget.*
import java.util.*

import fr.isen.costanzo.androidsmartdevice.databinding.ActivityScanBinding
private const val PERMISSION_REQUEST_CODE =0
private const val REQUEST_LOCATION_PERMISSION = 1
class ScanActivity : AppCompatActivity() {
    private lateinit var binding: ActivityScanBinding
    private lateinit var bluetoothAdapter: BluetoothAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityScanBinding.inflate(layoutInflater)
        setContentView(binding.root)
        var scanning = true
        binding.ScanBar.isVisible = false
        binding.scanList.isVisible = false
        binding.scanList.layoutManager = LinearLayoutManager(this)
        binding.scanList.adapter = ScanAdapter(arrayListOf("Device 1", "Device 2", "Device 3"))

        val button = binding.launchBLE

        button.setOnClickListener {
            if (scanning) {
                button.setImageResource(android.R.drawable.ic_media_pause)
                binding.ScanBar.isVisible = true
                binding.scanList.isVisible = true
                val toastdisplay = Toast.makeText(
                    applicationContext, "Scanning",
                    Toast.LENGTH_SHORT
                )
                toastdisplay.show()
            } else {
                button.setImageResource(android.R.drawable.ic_media_play)
                binding.ScanBar.isVisible = false
                binding.scanList.isVisible = false
                val toastdisplay2 = Toast.makeText(
                    applicationContext, "NotScanning",
                    Toast.LENGTH_SHORT
                )
                toastdisplay2.show()


            }
            scanning = !scanning

        }
// Vérifier si la permission d'accéder à la localisation est accordée
        """   if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            // La permission est déjà accordée
            binding.launchBLE(true)
        } else {
            // Demander la permission d'accéder à la localisation
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), REQUEST_LOCATION_PERMISSION)
        }"""
    }

    private fun getAllPermissions(): Array<String> {
        return arrayOf(
            Manifest.permission.BLUETOOTH_ADMIN,
            Manifest.permission.ACCESS_FINE_LOCATION
        )
    }

    private fun scanDeviceWithPermissions() {
        if (allPermissionGranted()) {
            scanBLEDevices()
        } else {
            //request permission pour TOUTES les permissions
            requestPermissions()
        }
    }

    // Demande l'autorisation d'accès à la localisation fine (nécessaire pour scanner les appareils BLE)
    private fun requestPermissions() {
        ActivityCompat.requestPermissions(this, getAllPermissions(), PERMISSION_REQUEST_CODE)
    }

    private fun scanBLEDevices() {
        initToggleActions()
    }

    private fun initToggleActions() {
        TODO("Not yet implemented")
    }

    private fun allPermissionGranted(): Boolean {
        val allPermissions = getAllPermissions()
        val permission = Manifest.permission.ACCESS_FINE_LOCATION
        return allPermissions.all {
            // a remplacer par la vérification de chaque permission
            ContextCompat.checkSelfPermission(
                this, permission
            ) == PackageManager.PERMISSION_GRANTED
            true
        }

    }

    // Gère la réponse de l'utilisateur à la demande d'autorisation d'accès à la localisation fine
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            PERMISSION_REQUEST_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    scanBLEDevices()
                    Toast.makeText(
                        this,
                        "j'ai les couilles qui grattent.",
                        Toast.LENGTH_SHORT
                    ).show()

                } else {
                    Toast.makeText(
                        this,
                        "Permission refusée, impossible de scanner les appareils BLE.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }

    }
}