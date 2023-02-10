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
import java.lang.Exception

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
    //delay
    private lateinit var currentDelay : EditText
    private var isDelayChanged : Boolean = false
    //exec
    private lateinit var execButton : Button
    //stop/start
    private lateinit var stopButton : Button
    //disconnect
    private lateinit var disconnectButton : Button
    //run forward
    private lateinit var runForwardButton : Button
    //run reverse
    private lateinit var runReverseButton : Button
    //save
    private lateinit var saveButton : Button
    //deploy
    private lateinit var deployButton : Button

    //servo settings
    private val _maxServo = 180
    private val _minServo = 0

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
        gripperBar.max = _maxServo
        gripperBar.progress = 90
        gripperBar.min = _minServo
        gripperBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, currentValue: Int, p2: Boolean) {
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                gripperBar.min = _minServo
                gripperBar.max = _maxServo
                gripperBar.progress = miniarmSettingsViewModel.currentGripper.toInt()
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                if (!isCurrentGripperChanged)
                    isCurrentGripperChanged = true
                if (miniarmSettingsViewModel.isGripperChanged)
                    miniarmSettingsViewModel.isGripperChanged = false
                currentGripper.setText(seekBar!!.progress.toString())
                miniarmSettingsViewModel.currentGripper = seekBar.progress.toString()
                Log.d(TAG,"Current Gripper=" + seekBar.progress.toString())
            }
        })
        val gripperWatcher = object : TextWatcher {
            override fun beforeTextChanged(sequence: CharSequence?, start: Int, count: Int, after: Int) {
                //
            }

            override fun onTextChanged(sequence: CharSequence?, start: Int, before: Int, count: Int) {
                if (sequence.toString().isNotEmpty() && sequence.toString().isNotBlank()) {
                    var value = sequence.toString().toInt()
                    if (value < _minServo) {
                        value = _minServo
                    }
                    if (value > _maxServo) {
                        value = _maxServo
                    }
                    miniarmSettingsViewModel.currentGripper = value.toString()
                    isCurrentGripperChanged = true
                }
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
        elbowBar.max = _maxServo
        elbowBar.progress = 90
        elbowBar.min = _minServo
        elbowBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, currentValue: Int, p2: Boolean) {
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                elbowBar.min = _minServo
                elbowBar.max = _maxServo
                elbowBar.progress = miniarmSettingsViewModel.currentElbow.toInt()
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                if (!isCurrentElbowChanged)
                    isCurrentElbowChanged = true
                if (miniarmSettingsViewModel.isElbowChanged)
                    miniarmSettingsViewModel.isElbowChanged = false
                currentElbow.setText(seekBar!!.progress.toString())
                miniarmSettingsViewModel.currentElbow = seekBar.progress.toString()
                Log.d(TAG,"Current Elbow=" + seekBar.progress.toString())
            }
        })
        val elbowWatcher = object : TextWatcher {
            override fun beforeTextChanged(sequence: CharSequence?, start: Int, count: Int, after: Int) {
                //
            }

            override fun onTextChanged(sequence: CharSequence?, start: Int, before: Int, count: Int) {
                if (sequence.toString().isNotEmpty() && sequence.toString().isNotBlank()) {
                    var value = sequence.toString().toInt()
                    if (value < _minServo) {
                        value = _minServo
                    }
                    if (value > _maxServo) {
                        value = _maxServo
                    }
                    miniarmSettingsViewModel.currentElbow = value.toString()
                    isCurrentElbowChanged = true
                }
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
        shoulderBar.max = _maxServo
        shoulderBar.progress = 90
        shoulderBar.min = _minServo
        shoulderBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, currentValue: Int, p2: Boolean) {
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                shoulderBar.min = _minServo
                shoulderBar.max = _maxServo
                shoulderBar.progress = miniarmSettingsViewModel.currentShoulder.toInt()
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                if (!isCurrentShoulderChanged)
                    isCurrentShoulderChanged = true
                if (miniarmSettingsViewModel.isShoulderChanged)
                    miniarmSettingsViewModel.isShoulderChanged = false
                currentShoulder.setText(seekBar!!.progress.toString())
                miniarmSettingsViewModel.currentShoulder = seekBar.progress.toString()
                Log.d(TAG,"Current Shoulder=" + seekBar.progress.toString())
            }
        })
        val shoulderWatcher = object : TextWatcher {
            override fun beforeTextChanged(sequence: CharSequence?, start: Int, count: Int, after: Int) {
                //
            }

            override fun onTextChanged(sequence: CharSequence?, start: Int, before: Int, count: Int) {
                if (sequence.toString().isNotEmpty() && sequence.toString().isNotBlank()) {
                    var value = sequence.toString().toInt()
                    if (value < _minServo) {
                        value = _minServo
                    }
                    if (value > _maxServo) {
                        value = _maxServo
                    }
                    miniarmSettingsViewModel.currentShoulder = value.toString()
                    isCurrentShoulderChanged = true
                }
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
        waistBar.max = _maxServo
        waistBar.progress = 90
        waistBar.min = _minServo
        waistBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, currentValue: Int, p2: Boolean) {
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                waistBar.min = _minServo
                waistBar.max = _maxServo
                waistBar.progress = miniarmSettingsViewModel.currentWaist.toInt()
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                if (!isCurrentWaistChanged)
                    isCurrentWaistChanged = true
                if (miniarmSettingsViewModel.isWaistChanged)
                    miniarmSettingsViewModel.isWaistChanged = false
                currentWaist.setText(seekBar!!.progress.toString())
                miniarmSettingsViewModel.currentWaist = seekBar.progress.toString()
                Log.d(TAG,"Current Waist=" + seekBar.progress.toString())
            }
        })
        val waistWatcher = object : TextWatcher {
            override fun beforeTextChanged(sequence: CharSequence?, start: Int, count: Int, after: Int) {
                //
            }

            override fun onTextChanged(sequence: CharSequence?, start: Int, before: Int, count: Int) {
                if (sequence.toString().isNotEmpty() && sequence.toString().isNotBlank()) {
                    var value = sequence.toString().toInt()
                    if (value < _minServo) {
                        value = _minServo
                    }
                    if (value > _maxServo) {
                        value = _maxServo
                    }
                    miniarmSettingsViewModel.currentWaist = value.toString()
                    isCurrentWaistChanged = true
                }
            }

            override fun afterTextChanged(sequence: Editable?) {
                //
                if (isCurrentWaistChanged)
                    waistBar.progress = miniarmSettingsViewModel.currentWaist.toInt()
            }
        }
        currentWaist.setText(miniarmSettingsViewModel.currentWaist)
        currentWaist.addTextChangedListener(waistWatcher)
        //delay
        currentDelay = view.findViewById(R.id.currentDelay)
        val delayWatcher = object : TextWatcher {
            override fun beforeTextChanged(sequence: CharSequence?, start: Int, count: Int, after: Int) {
                //
            }

            override fun onTextChanged(sequence: CharSequence?, start: Int, before: Int, count: Int) {
                if (sequence.toString().isNotEmpty() && sequence.toString().isNotBlank()) {
                    var value = sequence.toString().toInt()
                    if (value < 0) {
                        value = 0
                    }
                    miniarmSettingsViewModel.delay = value.toString()
                    isDelayChanged = true
                    miniarmSettingsViewModel.isDelayChanged = true
                }
            }

            override fun afterTextChanged(sequence: Editable?) {
                //
            }
        }
        currentDelay.setText(miniarmSettingsViewModel.delay)
        currentDelay.addTextChangedListener(delayWatcher)
        //exec button
        execButton = view.findViewById(R.id.miniarm_exec)
        execButton.setOnTouchListener { _, motionEvent ->
            val event = motionEvent as MotionEvent
            if (event.actionMasked == MotionEvent.ACTION_DOWN) {
                if (miniarmSettingsViewModel.isWriteToEepromAllChanged) {
                    if (!miniarmSettingsViewModel.isWriteToEepromAll) {
                        miniarmSettingsViewModel.isWriteToEepromAllChanged = false
                    } else if (sendOneWayCommandToMiniarm("Ea#\n")) {
                        Log.d(TAG, "Send write all commands to EEPROM")
                        miniarmSettingsViewModel.isWriteToEepromAllChanged = false
                    }
                }
                if (miniarmSettingsViewModel.isAutoModeChanged) {
                    var autoModeValue = ""
                    if (miniarmSettingsViewModel.isAutoLoadMode) {
                        autoModeValue = "l"
                    } else if (miniarmSettingsViewModel.isAutoLoopMode) {
                        autoModeValue = "L"
                    } else if(miniarmSettingsViewModel.isDirectMode) {
                        autoModeValue = "d"
                    } else if(miniarmSettingsViewModel.isAutoLoopDirectMode) {
                        autoModeValue = "D"
                    } else if (miniarmSettingsViewModel.isDefaultMode) {
                        autoModeValue = "c"
                    }
                    if (sendOneWayCommandToMiniarm(String.format("E%s#\n",autoModeValue))) {
                        Log.d(TAG, "Send auto mode")
                        miniarmSettingsViewModel.isAutoModeChanged = false
                    }
                }
                if (isCurrentWaistChanged) {
                    if (sendOneWayCommandToMiniarm(String.format("w%s#\n",miniarmSettingsViewModel.currentWaist))) {
                        Log.d(TAG, "Waist exec")
                        isCurrentWaistChanged = false
                    }
                }
                if (isCurrentShoulderChanged) {
                    if (sendOneWayCommandToMiniarm(String.format("s%s#\n",miniarmSettingsViewModel.currentShoulder))) {
                        Log.d(TAG, "Shoulder exec")
                        isCurrentShoulderChanged = false
                    }
                }
                if (isCurrentElbowChanged) {
                    if (sendOneWayCommandToMiniarm(String.format("e%s#\n",miniarmSettingsViewModel.currentElbow))) {
                        Log.d(TAG, "Elbow exec")
                        isCurrentElbowChanged = false
                    }
                }
                if (isCurrentGripperChanged) {
                    if (sendOneWayCommandToMiniarm(String.format("g%s#\n",miniarmSettingsViewModel.currentGripper))) {
                        Log.d(TAG, "Gripper exec")
                        isCurrentGripperChanged = false
                    }
                }
                if (isDelayChanged) {
                    if (sendOneWayCommandToMiniarm(String.format("d%s#\n",miniarmSettingsViewModel.delay))) {
                        Log.d(TAG, "Delay exec")
                        isDelayChanged = false
                        miniarmSettingsViewModel.isDelayChanged = false
                    }
                }
            }
            return@setOnTouchListener false
        }
        //stop button
        stopButton = view.findViewById(R.id.miniarm_stop)
        stopButton.setOnTouchListener { _, motionEvent ->
            val event = motionEvent as MotionEvent
            if (event.actionMasked == MotionEvent.ACTION_DOWN) {
                if (sendOneWayCommandToMiniarm("h#\n")) {
                    Log.d(TAG,"Stop/Start")
                }
            }
            return@setOnTouchListener false
        }
        //disconnect button
        disconnectButton = view.findViewById(R.id.miniarm_disconnect)
        disconnectButton.setOnTouchListener { _, motionEvent ->
            val event = motionEvent as MotionEvent
            if (event.actionMasked == MotionEvent.ACTION_DOWN) {
                miniarmSettingsViewModel.closeSockets()
            }
            return@setOnTouchListener false
        }
        //save button
        saveButton = view.findViewById(R.id.miniarm_save)
        saveButton.setOnTouchListener { _, motionEvent ->
            val event = motionEvent as MotionEvent
            if (event.actionMasked == MotionEvent.ACTION_DOWN) {
                if (miniarmSettingsViewModel.isAutoModeChanged) {
                    var autoModeValue = ""
                    if (miniarmSettingsViewModel.isAutoLoadMode) {
                        autoModeValue = "l"
                    } else if (miniarmSettingsViewModel.isAutoLoopMode) {
                        autoModeValue = "L"
                    } else if(miniarmSettingsViewModel.isDirectMode) {
                        autoModeValue = "d"
                    } else if(miniarmSettingsViewModel.isAutoLoopDirectMode) {
                        autoModeValue = "D"
                    } else if (miniarmSettingsViewModel.isDefaultMode) {
                        autoModeValue = "c"
                    }
                    if (sendOneWayCommandToMiniarm(String.format("E%s#\n",autoModeValue))) {
                        Log.d(TAG, "Send auto mode")
                        miniarmSettingsViewModel.isAutoModeChanged = false
                    }
                }
                if (isCurrentWaistChanged) {
                    if (sendOneWayCommandToMiniarm(String.format("Sw%s#\n",miniarmSettingsViewModel.currentWaist))) {
                        Log.d(TAG, "Waist saved")
                        miniarmSettingsViewModel.commands.addLast(String.format("w%s#\n",miniarmSettingsViewModel.currentWaist))
                        isCurrentWaistChanged = false
                    }
                }
                if (isCurrentShoulderChanged) {
                    if (sendOneWayCommandToMiniarm(String.format("Ss%s#\n",miniarmSettingsViewModel.currentShoulder))) {
                        Log.d(TAG, "Shoulder saved")
                        miniarmSettingsViewModel.commands.addLast(String.format("s%s#\n",miniarmSettingsViewModel.currentShoulder))
                        isCurrentShoulderChanged = false
                    }
                }
                if (isCurrentElbowChanged) {
                    if (sendOneWayCommandToMiniarm(String.format("Se%s#\n",miniarmSettingsViewModel.currentElbow))) {
                        Log.d(TAG, "Elbow saved")
                        miniarmSettingsViewModel.commands.addLast(String.format("e%s#\n",miniarmSettingsViewModel.currentElbow))
                        isCurrentElbowChanged = false
                    }
                }
                if (isCurrentGripperChanged) {
                    if (sendOneWayCommandToMiniarm(String.format("Sg%s#\n",miniarmSettingsViewModel.currentGripper))) {
                        Log.d(TAG, "Gripper saved")
                        miniarmSettingsViewModel.commands.addLast(String.format("g%s#\n",miniarmSettingsViewModel.currentGripper))
                        isCurrentGripperChanged = false
                    }
                }
                if (isDelayChanged) {
                    if (sendOneWayCommandToMiniarm(String.format("Sd%s#\n",miniarmSettingsViewModel.delay))) {
                        Log.d(TAG, "Delay saved")
                        miniarmSettingsViewModel.commands.addLast(String.format("d%s#\n",miniarmSettingsViewModel.delay))
                        isDelayChanged = false
                    }
                }
            }
            return@setOnTouchListener false
        }
        //run forward
        runForwardButton = view.findViewById(R.id.miniarm_run_forward)
        runForwardButton.setOnTouchListener { _, motionEvent ->
            val event = motionEvent as MotionEvent
            if (event.actionMasked == MotionEvent.ACTION_DOWN) {
                if (sendOneWayCommandToMiniarm(String.format("Rf#\n"))) {
                    Log.d(TAG, "Run forward")
                }
            }
            return@setOnTouchListener false
        }
        //run reverse
        runReverseButton = view.findViewById(R.id.miniarm_run_reverse)
        runReverseButton.setOnTouchListener { _, motionEvent ->
            val event = motionEvent as MotionEvent
            if (event.actionMasked == MotionEvent.ACTION_DOWN) {
                if (sendOneWayCommandToMiniarm(String.format("Rr#\n"))) {
                    Log.d(TAG, "Run reverse")
                }
            }
            return@setOnTouchListener false
        }
        //deploy button
        deployButton = view.findViewById(R.id.miniarm_deploy)
        deployButton.setOnTouchListener { _, motionEvent ->
            val event = motionEvent as MotionEvent
            if (event.actionMasked == MotionEvent.ACTION_DOWN) {
                if (miniarmSettingsViewModel.isAutoModeChanged) {
                    var autoModeValue = ""
                    if (miniarmSettingsViewModel.isAutoLoadMode) {
                        autoModeValue = "l"
                    } else if (miniarmSettingsViewModel.isAutoLoopMode) {
                        autoModeValue = "L"
                    } else if(miniarmSettingsViewModel.isDirectMode) {
                        autoModeValue = "d"
                    } else if(miniarmSettingsViewModel.isAutoLoopDirectMode) {
                        autoModeValue = "D"
                    } else if (miniarmSettingsViewModel.isDefaultMode) {
                        autoModeValue = "c"
                    }
                    if (sendOneWayCommandToMiniarm(String.format("E%s#\n",autoModeValue))) {
                        Log.d(TAG, "Send auto mode")
                        miniarmSettingsViewModel.isAutoModeChanged = false
                    }
                }
                if (sendOneWayCommandToMiniarm(String.format("C#\n"))) {
                    Log.d(TAG, "Clear command list")
                }
                for (str:String in miniarmSettingsViewModel.commands) {
                    if (sendOneWayCommandToMiniarm("S$str")) {
                        Log.d(TAG, "Save command $str")
                    }
                }
            }
            return@setOnTouchListener false
        }
        return view
    }

    private fun sendOneWayCommandToMiniarm(message : String, hasAck : Boolean = true) : Boolean = runBlocking {
        if (miniarmSettingsViewModel.connectionType == ConnectionType.BLE && validateBleSocketConnection(miniarmSettingsViewModel.bleSocket)) {
            val job = GlobalScope.launch {
                try {
                    val outputStreamWriter =
                        OutputStreamWriter(miniarmSettingsViewModel.bleSocket?.outputStream)
                    val inputStreamReader =
                        BufferedReader(InputStreamReader(miniarmSettingsViewModel.bleSocket?.inputStream))
                    //send data
                    outputStreamWriter.write(message)
                    outputStreamWriter.flush()
                    if (hasAck) {
                        val status = inputStreamReader.readLine()
                        Log.d(TAG, "s=$status")
                    }
                } catch (e : Exception) {
                    miniarmSettingsViewModel.bleSocket?.close()
                    miniarmSettingsViewModel.bleSocket = null
                    miniarmSettingsViewModel.connectionType = ConnectionType.NONE
                    Log.e(TAG, e.toString())
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