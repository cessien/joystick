package com.byteofcuriosity.joystick;

import android.util.Log;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;

public class MulticastReceiver extends Thread {
    protected MulticastSocket socket = null;
    protected byte[] buf = new byte[256];

    public void run() {
        try {
            socket = new MulticastSocket(4446);
        } catch (IOException e) {
            Log.e("multicast", e.getLocalizedMessage());
        }
        InetAddress group = null;
        try {
            group = InetAddress.getByName("230.0.0.0");
        } catch (UnknownHostException e) {
            Log.e("multicast", e.getLocalizedMessage());
        }
        try {
            socket.joinGroup(group);
        } catch (IOException e) {
            Log.e("multicast", e.getLocalizedMessage());
        }
        while (true) {
            DatagramPacket packet = new DatagramPacket(buf, buf.length);
            try {
                socket.receive(packet);
            } catch (IOException e) {
                Log.e("multicast", e.getLocalizedMessage());
            }
            String received = new String(
                    packet.getData(), 0, packet.getLength());
            if ("end".equals(received)) {
                break;
            }
        }
        try {
            socket.leaveGroup(group);
        } catch (IOException e) {
            Log.e("multicast", e.getLocalizedMessage());
        }
        socket.close();
    }
}
