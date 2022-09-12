package io.github.gdimitriu.miniarmcontrolcenter

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.bluetooth.BluetoothSocket
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.*
import android.widget.Button
import android.widget.EditText
import android.widget.SeekBar
import kotlinx.coroutines.*
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels

private const val TAG = "MiniarmControl"

@DelicateCoroutinesApi
class MiniarmControlFragment : Fragment() {
    private val miniarmSettingsViewModel: MiniarmSettingsViewModel by activityViewModels()

    //gripper
    private lateinit var gripperBar : SeekBar
    private lateinit var currentGripper : EditText
    private var isCurrentGripperChanged : Boolean = false
    //elbow
    private lateinit var elbowBar : SeekBar
    private lateinit var currentElbow : EditText
    private var isCurrentElbowChanged : Boolean = false
    //shoulder
    private lateinit var shoulderBar : SeekBar
    private lateinit var currentShoulder : EditText
    private var isCurrentShoulderChanged : Boolean = false
    //waist
    private lateinit var waistBar : SeekBar
    private lateinit var currentWaist : EditText
    private var isCurrentWaistChanged : Boolean = false
    //send
    private lateinit var sendButton : Button
    //disconnect
    private lateinit var disconnectButton : Button
    private var isStart : Boolean = false

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.miniarm_control, container, false)
        //gripper
        gripperBar = view.findViewById(R.id.gripperBar)
        currentGripper = view.findViewById(R.id.currentGripper)
        gripperBar.max = 180
        gripperBar.progress = 90
        gripperBar.min = 0
        gripperBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, currentValue: Int, p2: Boolean) {
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                gripperBar.min = 0
                gripperBar.max = 190
                gripperBar.progress = miniarmSettingsViewModel.currentGripper.toInt()
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                if (!isCurrentGripperChanged)
                    isCurrentGripperChanged = true
                if (miniarmSettingsViewModel.isGripperChanged)
                    miniarmSettingsViewModel.isGripperChanged = false
                currentGripper.setText(seekBar!!.progress.toString())
                miniarmSettingsViewModel.currentGripper = seekBar!!.progress.toString()
                if (seekBar != null) {
                    Log.d(TAG,"Current Gripper=" + seekBar.progress.toString())
                }
            }
        })
        val gripperWatcher = object : TextWatcher {
            override fun beforeTextChanged(sequence: CharSequence?, start: Int, count: Int, after: Int) {
                //
            }

            override fun onTextChanged(sequence: CharSequence?, start: Int, before: Int, count: Int) {
                miniarmSettingsViewModel.currentGripper = sequence.toString()
                isCurrentGripperChanged = true
            }

            override fun afterTextChanged(sequence: Editable?) {
                //
                if (isCurrentGripperChanged)
                    gripperBar.progress = miniarmSettingsViewModel.currentGripper.toInt()
            }
        }
        currentGripper.setText(miniarmSettingsViewModel.currentGripper)
        currentGripper.addTextChangedListener(gripperWatcher)
        //elbow
        elbowBar = view.findViewById(R.id.elbowBar)
        currentElbow = view.findViewById(R.id.currentElbow)
        elbowBar.max = 180
        elbowBar.progress = 90
        elbowBar.min = 0
        elbowBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, currentValue: Int, p2: Boolean) {
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                elbowBar.min = 0
                elbowBar.max = 190
                elbowBar.progress = miniarmSettingsViewModel.currentElbow.toInt()
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                if (!isCurrentElbowChanged)
                    isCurrentElbowChanged = true
                if (miniarmSettingsViewModel.isElbowChanged)
                    miniarmSettingsViewModel.isElbowChanged = false
                currentElbow.setText(seekBar!!.progress.toString())
                miniarmSettingsViewModel.currentElbow = seekBar!!.progress.toString()
                if (seekBar != null) {
                    Log.d(TAG,"Current Elbow=" + seekBar.progress.toString())
                }
            }
        })
        val elbowWatcher = object : TextWatcher {
            override fun beforeTextChanged(sequence: CharSequence?, start: Int, count: Int, after: Int) {
                //
            }

            override fun onTextChanged(sequence: CharSequence?, start: Int, before: Int, count: Int) {
                miniarmSettingsViewModel.currentElbow = sequence.toString()
                isCurrentElbowChanged = true
            }

            override fun afterTextChanged(sequence: Editable?) {
                //
                if (isCurrentElbowChanged)
                    elbowBar.progress = miniarmSettingsViewModel.currentElbow.toInt()
            }
        }
        currentElbow.setText(miniarmSettingsViewModel.currentElbow)
        currentElbow.addTextChangedListener(elbowWatcher)
        //shoulder
        shoulderBar = view.findViewById(R.id.shoulderBar)
        currentShoulder = view.findViewById(R.id.currentShoulder)
        shoulderBar.max = 180
        shoulderBar.progress = 90
        shoulderBar.min = 0
        shoulderBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, currentValue: Int, p2: Boolean) {
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                shoulderBar.min = 0
                shoulderBar.max = 190
                shoulderBar.progress = miniarmSettingsViewModel.currentShoulder.toInt()
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                if (!isCurrentShoulderChanged)
                    isCurrentShoulderChanged = true
                if (miniarmSettingsViewModel.isShoulderChanged)
                    miniarmSettingsViewModel.isShoulderChanged = false
                currentShoulder.setText(seekBar!!.progress.toString())
                miniarmSettingsViewModel.currentShoulder = seekBar!!.progress.toString()
                if (seekBar != null) {
                    Log.d(TAG,"Current Shoulder=" + seekBar.progress.toString())
                }
            }
        })
        val shoulderWatcher = object : TextWatcher {
            override fun beforeTextChanged(sequence: CharSequence?, start: Int, count: Int, after: Int) {
                //
            }

            override fun onTextChanged(sequence: CharSequence?, start: Int, before: Int, count: Int) {
                miniarmSettingsViewModel.currentShoulder = sequence.toString()
                isCurrentShoulderChanged = true
            }

            override fun afterTextChanged(sequence: Editable?) {
                //
                if (isCurrentShoulderChanged)
                    shoulderBar.progress = miniarmSettingsViewModel.currentShoulder.toInt()
            }
        }
        currentShoulder.setText(miniarmSettingsViewModel.currentShoulder)
        currentShoulder.addTextChangedListener(shoulderWatcher)
        //waist
        waistBar = view.findViewById(R.id.waistBar)
        currentWaist = view.findViewById(R.id.currentWaist)
        waistBar.max = 180
        waistBar.progress = 90
        waistBar.min = 0
        waistBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, currentValue: Int, p2: Boolean) {
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                waistBar.min = 0
                waistBar.max = 190
                waistBar.progress = miniarmSettingsViewModel.currentWaist.toInt()
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                if (!isCurrentWaistChanged)
                    isCurrentWaistChanged = true
                if (miniarmSettingsViewModel.isWaistChanged)
                    miniarmSettingsViewModel.isWaistChanged = false
                currentWaist.setText(seekBar!!.progress.toString())
                miniarmSettingsViewModel.currentWaist = seekBar!!.progress.toString()
                if (seekBar != null) {
                    Log.d(TAG,"Current Waist=" + seekBar.progress.toString())
                }
            }
        })
        val waistWatcher = object : TextWatcher {
            override fun beforeTextChanged(sequence: CharSequence?, start: Int, count: Int, after: Int) {
                //
            }

            override fun onTextChanged(sequence: CharSequence?, start: Int, before: Int, count: Int) {
                miniarmSettingsViewModel.currentWaist = sequence.toString()
                isCurrentWaistChanged = true
            }

            override fun afterTextChanged(sequence: Editable?) {
                //
                if (isCurrentWaistChanged)
                    waistBar.progress = miniarmSettingsViewModel.currentWaist.toInt()
            }
        }
        currentWaist.setText(miniarmSettingsViewModel.currentWaist)
        currentWaist.addTextChangedListener(waistWatcher)
        sendButton = view.findViewById(R.id.send)
        sendButton.setOnTouchListener { view, motionEvent ->
            val event = motionEvent as MotionEvent
            if (event.actionMasked == MotionEvent.ACTION_DOWN) {
                if (isCurrentWaistChanged) {
                    if (sendOneWayCommandToMiniarm(String.format("w%s#\n",miniarmSettingsViewModel.currentWaist))) {
                        Log.d(TAG, "Waist send")
                        isCurrentWaistChanged = false
                    }
                }
                if (isCurrentShoulderChanged) {
                    if (sendOneWayCommandToMiniarm(String.format("s%s#\n",miniarmSettingsViewModel.currentShoulder))) {
                        Log.d(TAG, "Shoulder send")
                        isCurrentShoulderChanged = false
                    }
                }
                if (isCurrentElbowChanged) {
                    if (sendOneWayCommandToMiniarm(String.format("e%s#\n",miniarmSettingsViewModel.currentElbow))) {
                        Log.d(TAG, "Elbow send")
                        isCurrentElbowChanged = false
                    }
                }
                if (isCurrentGripperChanged) {
                    if (sendOneWayCommandToMiniarm(String.format("g%s#\n",miniarmSettingsViewModel.currentGripper))) {
                        Log.d(TAG, "Gripper send")
                        isCurrentGripperChanged = false
                    }
                }
            }
            return@setOnTouchListener false
        }
        disconnectButton = view.findViewById(R.id.miniarm_disconnect)
        disconnectButton.setOnTouchListener { view, motionEvent ->
            val event = motionEvent as MotionEvent
            if (event.actionMasked == MotionEvent.ACTION_DOWN) {
                miniarmSettingsViewModel.closeSockets()
            }
            return@setOnTouchListener false
        }
        return view;
    }

    private fun sendOneWayCommandToMiniarm(message : String, hasAck : Boolean = true) : Boolean = runBlocking {
        if (miniarmSettingsViewModel.connectionType == ConnectionType.BLE && validateBleSocketConnection(miniarmSettingsViewModel.bleSocket)) {
            var job = GlobalScope.launch {
                val outputStreamWriter =
                    OutputStreamWriter(miniarmSettingsViewModel.bleSocket?.getOutputStream())
                val inputStreamReader =
                    BufferedReader(InputStreamReader(miniarmSettingsViewModel.bleSocket?.getInputStream()))
                //send data
                outputStreamWriter.write(message)
                outputStreamWriter.flush()
                if (hasAck) {
                    val status = inputStreamReader.readLine()
                    Log.d(TAG,"s=$status")
                }
            }
            job.join()
            return@runBlocking true
        } else if (miniarmSettingsViewModel.connectionType == ConnectionType.NONE) {
            val builder: AlertDialog.Builder? = activity?.let {
                AlertDialog.Builder(it)
            }
            builder?.setMessage("Connect first the Miniarm !")?.setTitle("Connection failed !")
            val dialog: AlertDialog? = builder?.create()
            dialog?.show()
        }
        return@runBlocking false
    }
    
    companion object {
        fun newInstance(): MiniarmControlFragment {
            return MiniarmControlFragment()
        }
    }

    override fun onStop() {
        super.onStop()
    }

    private fun validateBleSocketConnection(socket: BluetoothSocket?): Boolean {
        if (socket == null || !socket.isConnected) {
            val builder: AlertDialog.Builder? = activity?.let {
                AlertDialog.Builder(it)
            }
            builder?.setMessage("Connect first the Miniarm !")?.setTitle("Connection failed !")
            val dialog: AlertDialog? = builder?.create()
            dialog?.show()
            return false
        }
        return true
    }
}