package com.cctvhub.app.onvif

import android.content.Context
import android.net.wifi.WifiManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.DatagramPacket
import java.net.InetAddress
import java.net.MulticastSocket
import java.util.UUID

data class DiscoveredDevice(val ipAddress: String)

object OnvifDiscovery {

    private const val MULTICAST_ADDR = "239.255.255.250"
    private const val PORT = 3702

    suspend fun discoverDevices(context: Context, timeoutMs: Int = 4000): List<DiscoveredDevice> =
        withContext(Dispatchers.IO) {
            val results = mutableListOf<DiscoveredDevice>()
            val wifi = context.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
            val multicastLock = wifi.createMulticastLock("cctvhub_onvif")
            multicastLock.acquire()
            try {
                val socket = MulticastSocket()
                socket.soTimeout = timeoutMs
                val group = InetAddress.getByName(MULTICAST_ADDR)

                val probe = buildProbeMessage()
                socket.send(DatagramPacket(probe.toByteArray(), probe.length, group, PORT))

                val buffer = ByteArray(8192)
                val endTime = System.currentTimeMillis() + timeoutMs
                while (System.currentTimeMillis() < endTime) {
                    try {
                        val response = DatagramPacket(buffer, buffer.size)
                        socket.receive(response)
                        val ip = response.address.hostAddress
                        if (ip != null && !results.any { it.ipAddress == ip }) {
                            results.add(DiscoveredDevice(ip))
                        }
                    } catch (e: Exception) {
                        break
                    }
                }
                socket.close()
            } finally {
                multicastLock.release()
            }
            results
        }

    private fun buildProbeMessage(): String {
        val msgId = UUID.randomUUID()
        return """<?xml version="1.0" encoding="UTF-8"?>
        <e:Envelope xmlns:e="http://www.w3.org/2003/05/soap-envelope"
            xmlns:w="http://schemas.xmlsoap.org/ws/2004/08/addressing"
            xmlns:d="http://schemas.xmlsoap.org/ws/2005/04/discovery"
            xmlns:dn="http://www.onvif.org/ver10/network/wsdl">
            <e:Header>
                <w:MessageID>uuid:$msgId</w:MessageID>
                <w:To e:mustUnderstand="1">urn:schemas-xmlsoap-org:ws:2005:04:discovery</w:To>
                <w:Action a:mustUnderstand="1">http://schemas.xmlsoap.org/ws/2005/04/discovery/Probe</w:Action>
            </e:Header>
            <e:Body>
                <d:Probe><d:Types>dn:NetworkVideoTransmitter</d:Types></d:Probe>
            </e:Body>
        </e:Envelope>""".trimIndent()
    }
}
