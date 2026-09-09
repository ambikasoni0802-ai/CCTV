package com.cctvhub.app.data.repository

import android.content.Context
import com.cctvhub.app.data.model.Camera
import com.cctvhub.app.data.model.CameraStatus
import org.json.JSONArray
import org.json.JSONObject

object CameraRepository {

    private const val PREFS = "cctvhub_prefs"
    private const val KEY_CAMERAS = "cameras_json"

    fun loadCameras(context: Context): List<Camera> {
        val prefs = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
        val json = prefs.getString(KEY_CAMERAS, null) ?: return emptyList()
        val arr = JSONArray(json)
        val list = mutableListOf<Camera>()
        for (i in 0 until arr.length()) {
            val o = arr.getJSONObject(i)
            list.add(
                Camera(
                    id = o.getString("id"),
                    name = o.getString("name"),
                    ipAddress = o.getString("ipAddress"),
                    port = o.optInt("port", 554),
                    username = o.optString("username", ""),
                    password = o.optString("password", ""),
                    rtspPath = o.optString("rtspPath", "/stream1"),
                    status = CameraStatus.ONLINE
                )
            )
        }
        return list
    }

    fun saveCamera(context: Context, camera: Camera) {
        val current = loadCameras(context).toMutableList()
        current.add(camera)
        persist(context, current)
    }

    private fun persist(context: Context, cameras: List<Camera>) {
        val arr = JSONArray()
        cameras.forEach { c ->
            val o = JSONObject()
            o.put("id", c.id)
            o.put("name", c.name)
            o.put("ipAddress", c.ipAddress)
            o.put("port", c.port)
            o.put("username", c.username)
            o.put("password", c.password)
            o.put("rtspPath", c.rtspPath)
            arr.put(o)
        }
        val prefs = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
        prefs.edit().putString(KEY_CAMERAS, arr.toString()).apply()
    }
}
