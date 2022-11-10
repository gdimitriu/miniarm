package io.github.gdimitriu.miniarmcontrolcenter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.google.android.material.switchmaterial.SwitchMaterial

class MiniarmEepromFragment: Fragment() {
    private val miniarmSettingsViewModel: MiniarmSettingsViewModel by activityViewModels()

    private lateinit var autoModeGroup : RadioGroup

    private lateinit var writeAllSwitch : SwitchMaterial

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.miniarm_eeprom, container, false)
        writeAllSwitch = view.findViewById(R.id.write_memory_to_eeprom)
        writeAllSwitch.setOnCheckedChangeListener {
                _, isChecked -> miniarmSettingsViewModel.isWriteToEepromAll = isChecked
            miniarmSettingsViewModel.isWriteToEepromAllChanged = true
        }
        autoModeGroup = view.findViewById(R.id.auto_mode)
        if (miniarmSettingsViewModel.isAutoLoadMode) {
            autoModeGroup.check(R.id.auto_load)
        } else if (miniarmSettingsViewModel.isAutoLoopMode) {
            autoModeGroup.check(R.id.auto_loop)
        } else if (miniarmSettingsViewModel.isAutoLoopDirectMode) {
            autoModeGroup.check(R.id.auto_loop_direct)
        } else if (miniarmSettingsViewModel.isDirectMode) {
            autoModeGroup.check(R.id.direct_mode)
        } else if(miniarmSettingsViewModel.isDefaultMode) {
            autoModeGroup.check(R.id.auto_default)
        }

        autoModeGroup.setOnCheckedChangeListener { view, checkedId ->
            val radio: RadioButton = view.findViewById(checkedId)
            when (radio.id) {
                R.id.auto_load -> {
                    miniarmSettingsViewModel.isAutoLoadMode = true
                    miniarmSettingsViewModel.isAutoLoopMode = false
                    miniarmSettingsViewModel.isAutoLoopDirectMode = false
                    miniarmSettingsViewModel.isDirectMode = false
                    miniarmSettingsViewModel.isDefaultMode = false
                    miniarmSettingsViewModel.isAutoModeChanged = true
                }
                R.id.direct_mode -> {
                    miniarmSettingsViewModel.isAutoLoadMode = false
                    miniarmSettingsViewModel.isAutoLoopMode = false
                    miniarmSettingsViewModel.isAutoLoopDirectMode = false
                    miniarmSettingsViewModel.isDirectMode = true
                    miniarmSettingsViewModel.isDefaultMode = false
                    miniarmSettingsViewModel.isAutoModeChanged = true
                }
                R.id.auto_loop -> {
                    miniarmSettingsViewModel.isAutoLoadMode = false
                    miniarmSettingsViewModel.isAutoLoopMode = true
                    miniarmSettingsViewModel.isAutoLoopDirectMode = false
                    miniarmSettingsViewModel.isDirectMode = false
                    miniarmSettingsViewModel.isDefaultMode = false
                    miniarmSettingsViewModel.isAutoModeChanged = true
                }
                R.id.auto_loop_direct -> {
                    miniarmSettingsViewModel.isAutoLoadMode = false
                    miniarmSettingsViewModel.isAutoLoopMode = false
                    miniarmSettingsViewModel.isAutoLoopDirectMode = true
                    miniarmSettingsViewModel.isDirectMode = false
                    miniarmSettingsViewModel.isDefaultMode = false
                    miniarmSettingsViewModel.isAutoModeChanged = true
                }
                R.id.auto_default -> {
                    miniarmSettingsViewModel.isAutoLoadMode = false
                    miniarmSettingsViewModel.isAutoLoopMode = false
                    miniarmSettingsViewModel.isAutoLoopDirectMode = false
                    miniarmSettingsViewModel.isDirectMode = false
                    miniarmSettingsViewModel.isDefaultMode = true
                    miniarmSettingsViewModel.isAutoModeChanged = true
                }
            }
        }
        return view
    }

    companion object {
        fun newInstance(): MiniarmEepromFragment {
            return MiniarmEepromFragment()
        }
    }
}