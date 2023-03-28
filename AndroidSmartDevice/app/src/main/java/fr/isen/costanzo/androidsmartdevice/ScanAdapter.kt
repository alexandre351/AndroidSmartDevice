package fr.isen.costanzo.androidsmartdevice

import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import fr.isen.costanzo.androidsmartdevice.databinding.ScanCellBinding


class ScanAdapter(var devices: ArrayList<BluetoothDevice>, var onDeviceClickListener: ( BluetoothDevice)-> Unit ) : RecyclerView.Adapter<ScanAdapter.ScanViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScanViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ScanCellBinding.inflate(inflater, parent, false)
        return ScanViewHolder(binding)

    }
    override fun getItemCount(): Int= devices.size

    @SuppressLint("MissingPermission")
    override fun onBindViewHolder(holder: ScanViewHolder, position: Int) {
        holder.DeviceName.text = devices[position].name.toString()
        holder.itemView.setOnClickListener{
            onDeviceClickListener(devices[position])
        }

    }


    class ScanViewHolder(binding: ScanCellBinding) : RecyclerView.ViewHolder(binding.root){
        val DeviceName = binding.DeviceName
    }
fun addDevice(device: BluetoothDevice) {
    //var shouldAddDevice = true
    devices.forEachIndexed { index, bluetoothDevice ->
        if(bluetoothDevice.address == device.address) {
            devices[index] = device
            //shouldAddDevice = false
        } else {
            devices.add(device)
        }
    }
}

}


