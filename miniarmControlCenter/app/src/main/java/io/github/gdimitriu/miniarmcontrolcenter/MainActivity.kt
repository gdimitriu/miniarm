package io.github.gdimitriu.miniarmcontrolcenter

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.DocumentsContract
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.net.toUri
import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader

private const val TAG = "MiniarmControlMain"
class MainActivity : AppCompatActivity() {
    private val miniarmSettingsViewModel: MiniarmSettingsViewModel by viewModels()

    private val isExternalStorageReadOnly: Boolean get() {
        val extStorageState = Environment.getExternalStorageState()
        return Environment.MEDIA_MOUNTED_READ_ONLY == extStorageState
    }
    private val isExternalStorageAvailable: Boolean get() {
        val extStorageState = Environment.getExternalStorageState()
        return Environment.MEDIA_MOUNTED == extStorageState
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, resultData: Intent?) {
        super.onActivityResult(requestCode, resultCode, resultData)
        if (requestCode == 2 && resultCode == Activity.RESULT_OK) {
            // The result data contains a URI for the document or directory that
            // the user selected.
            resultData?.data?.also { uri ->
                // Perform operations on the document using its URI.
                try {
                    val fileOutPutStream = contentResolver.openOutputStream(uri)
                    for (str:String in miniarmSettingsViewModel.commands) {
                        fileOutPutStream?.write(str.toByteArray())
                        fileOutPutStream?.write("\n".toByteArray())
                    }
                    fileOutPutStream?.close()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            // The result data contains a URI for the document or directory that
            // the user selected.
            miniarmSettingsViewModel.commands.clear()
            resultData?.data?.also { uri ->
                // Perform operations on the document using its URI.
                try {
                    val fileInputStream = contentResolver.openInputStream(uri)
                    var inputStreamReader: InputStreamReader = InputStreamReader(fileInputStream)
                    val bufferedReader: BufferedReader = BufferedReader(inputStreamReader)
                    var text: String? = bufferedReader.readLine()
                    while (text != null) {
                        miniarmSettingsViewModel.commands.addLast(text)
                        text = bufferedReader.readLine()
                    }
                    fileInputStream?.close()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

    private fun openFile(pickerInitialUri: Uri, requestCode: Int) {
        var typeOp = android.content.Intent.ACTION_CREATE_DOCUMENT
        if (requestCode == 1) {
            typeOp = android.content.Intent.ACTION_OPEN_DOCUMENT
        }
        var intent = Intent(typeOp).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = "application/*"
            putExtra(DocumentsContract.EXTRA_INITIAL_URI, pickerInitialUri)
        }
        startActivityForResult(intent, requestCode)
    }

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
            R.id.miniarm_save -> {
                val myExternalFile = File(Environment.getExternalStoragePublicDirectory("Downloads/Miniarm"),"deploy.dat")
                openFile(myExternalFile!!.toUri(),2)
                true
            }
            R.id.miniarm_load -> {
                val myExternalFile = File(Environment.getExternalStoragePublicDirectory("Downloads/Miniarm"),"deploy.dat")
                openFile(myExternalFile!!.toUri(),1)
                true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }
}