package com.dengzii.plugin.adb

import com.intellij.ide.util.PropertiesComponent

/**
 * <pre>
 * author : dengzi
 * e-mail : denua@foxmail.com
 * github : https://github.com/MrDenua
 * time   : 2019/10/10
 * desc   :
 * </pre>
 */

object Config {

    private val TAG = Config::class.java.simpleName
    private const val KEY_DEVICES = "com.dengzii.plugin.adb.devices"
    private const val KEY_DIALOG_CONFIG = "com.dengzii.plugin.adb.config.dialog"
    private const val KEY_ABD = "com.dengzii.plugin.adb.config.adb"

    fun clear() {
        PropertiesComponent.getInstance().unsetValue(KEY_DEVICES)
    }

    fun loadAdbPath(): String {
        val pro = PropertiesComponent.getInstance()
        return pro.getValue(KEY_ABD) ?: "adb"
    }

    fun saveAdbPath(path: String) {
        PropertiesComponent.getInstance().setValue(KEY_ABD, path)
    }

    fun loadDevices(): List<Device> {

        val pro = PropertiesComponent.getInstance()
        val deviceList = pro.getValues(KEY_DEVICES)
        val devices = ArrayList<Device>()
        deviceList?.forEach {
            val device = Device.fromSerialString(it)
            if (device != null) {
                devices.add(device)
            }
        }
        XLog.d("${TAG}.loadConfigDevice", devices.toString())
        return devices
    }

    fun saveDevice(devices: List<Device>) {

        XLog.d("$TAG.saveDevice", devices.toString())
        try {
            val pro = PropertiesComponent.getInstance()
            val serialList = devices.map { it.toSerialString() }.toTypedArray()
            pro.setValues(KEY_DEVICES, serialList)
        } catch (e: Throwable) {
            XLog.e("$TAG.saveDevice", e)
            PropertiesComponent.getInstance().unsetValue(KEY_DEVICES)
        }
    }

    fun saveDialogConfig(dialogConfig: DialogConfig) {
        PropertiesComponent.getInstance().setValue(KEY_DIALOG_CONFIG, dialogConfig.toSerialString())
    }

    fun loadDialogConfig(): DialogConfig {
        return try {
            DialogConfig.fromSerialString(PropertiesComponent.getInstance().getValue(KEY_DIALOG_CONFIG, ""))
        } catch (t: Throwable) {
            XLog.e("$TAG.saveDevice", t)
            PropertiesComponent.getInstance().unsetValue(KEY_DIALOG_CONFIG)
            DialogConfig()
        }
    }
}

