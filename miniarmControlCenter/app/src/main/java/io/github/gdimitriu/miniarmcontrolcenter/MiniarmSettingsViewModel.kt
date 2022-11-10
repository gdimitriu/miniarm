package io.github.gdimitriu.miniarmcontrolcenter

import android.bluetooth.BluetoothSocket
import android.util.Log
import androidx.lifecycle.ViewModel

private const val TAG = "MiniarmSettingsViewModel"
class MiniarmSettingsViewModel : ViewModel()  {
    var isChanged : Boolean

    var bleSocket: BluetoothSocket?

    var connectionType : ConnectionType

    var currentGripper : String
    var isGripperChanged : Boolean

    var currentElbow : String
    var isElbowChanged : Boolean

    var currentShoulder : String
    var isShoulderChanged : Boolean

    var currentWaist : String
    var isWaistChanged : Boolean

    var delay : String
    var isDelayChanged : Boolean

    var commands : ArrayDeque<String>

    var isAutoLoadMode : Boolean

    var isDirectMode : Boolean

    var isAutoLoopMode: Boolean

    var isAutoLoopDirectMode : Boolean

    var isDefaultMode : Boolean

    var isAutoModeChanged : Boolean

    var isWriteToEepromAll : Boolean

    var isWriteToEepromAllChanged : Boolean

    init {
        Log.d(TAG, "Initialized the model view")
        isChanged = true
        connectionType = ConnectionType.NONE
        bleSocket = null
        currentGripper = "90"
        isGripperChanged = true
        currentElbow = "90"
        isElbowChanged = true
        currentShoulder = "90"
        isShoulderChanged = true
        currentWaist = "90"
        isWaistChanged = true
        delay = "0"
        isDelayChanged = true
        commands = ArrayDeque<String>()
        isAutoLoadMode = false
        isDirectMode = false
        isAutoLoopMode = false
        isAutoLoopDirectMode = false
        isDefaultMode = true
        isAutoModeChanged = true
        isWriteToEepromAll = false
        isWriteToEepromAllChanged = false
    }

    override fun onCleared() {
        Log.d(TAG,"Clearing and close the socket.")
        super.onCleared()
        bleSocket?.close()
        commands.clear()
    }

    fun closeSockets() {
        bleSocket?.close()
        connectionType = ConnectionType.NONE
    }
}