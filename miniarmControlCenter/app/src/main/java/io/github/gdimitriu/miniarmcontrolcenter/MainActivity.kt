package io.github.gdimitriu.miniarmcontrolcenter

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem

private const val TAG = "MiniarmControlMain"
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val currentFragment = supportFragmentManager.findFragmentById(R.id.fragment_container)

        if (currentFragment == null) {
            val fragment = MiniarmControlFragment.newInstance()
            supportFragmentManager
                .beginTransaction()
                .add(R.id.fragment_container, fragment)
                .commit()
        }
    }

    /* create the menu */
    override fun onCreateOptionsMenu(menu: Menu) :Boolean {
        super.onCreateOptionsMenu(menu)
        menuInflater.inflate(R.menu.activity_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        Log.d(TAG, "MiniarmConnectionFragment")
        return when (item.itemId) {
            R.id.miniarm_connection_ble -> {
                val fragment =
                    MiniarmConnectionBleFragment.newInstance()
                supportFragmentManager.beginTransaction().replace(R.id.fragment_container, fragment)
                    .addToBackStack(null).commit()
                true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }
}