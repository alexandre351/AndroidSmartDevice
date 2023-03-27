package fr.isen.costanzo.androidsmartdevice

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.floatingactionbutton.FloatingActionButton
import androidx.recyclerview.widget.RecyclerView
import fr.isen.costanzo.androidsmartdevice.databinding.ActivityScanBinding

class ScanActivity : AppCompatActivity() {
    private lateinit var binding : ActivityScanBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityScanBinding.inflate(layoutInflater)
        setContentView(binding.root)
        var scanning = true
        binding.ScanBar.isVisible = false
        binding.scanList.isVisible = false
        binding.scanList.layoutManager = LinearLayoutManager(this)
        binding.scanList.adapter = ScanAdapter(arrayListOf("Device 1","Device 2","Device 3"))

        val button = binding.launchBLE
        button.setOnClickListener {
            if (scanning) {
                button.setImageResource(android.R.drawable.ic_media_pause)
                binding.ScanBar.isVisible = true
                binding.scanList.isVisible = true
            }
            else {
                button.setImageResource(android.R.drawable.ic_media_play)
                binding.ScanBar.isVisible = false
                binding.scanList.isVisible = false


            }
            scanning = !scanning
        }


    }
}