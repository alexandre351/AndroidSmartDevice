package fr.isen.costanzo.androidsmartdevice
import android.bluetooth.BluetoothAdapter
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.*
import java.util.*


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val button: Button = findViewById(R.id.mainAction)
        button.setOnClickListener(View.OnClickListener {
            val intent = Intent(this, ScanActivity::class.java)
            startActivity(intent)
        })
        val bluetoothAdapter: BluetoothAdapter? = BluetoothAdapter.getDefaultAdapter()

        if (bluetoothAdapter == null) {
            // Le Bluetooth n'est pas disponible sur cet appareil
        } else {
            // Le Bluetooth est disponible sur cet appareil
            if (bluetoothAdapter.isEnabled) {
                val toast_val = Toast.makeText(applicationContext,"appareil prêt à l'utilisation",Toast.LENGTH_SHORT)
                toast_val.show()
            } else {
                val toast = Toast.makeText(applicationContext, "Le bluetooth n'est pas actif, veuillez l'activer", Toast.LENGTH_LONG)
                toast.show()
            }
        }
    }


}