package com.byteofcuriosity.joystick;

import android.util.Log;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class MulticastPublisher extends Thread {
    private DatagramSocket socket;
    private InetAddress group;
    private byte[] buf;
    public volatile int speed = 0;

    @Override
    public void run() {
        super.run();
        int oldspeed = 0;

        while(true) {
            if( speed == oldspeed) continue;
            try {
                multicast((oldspeed = speed) + "");
                Log.d("multicast", "multicasted!");
            } catch (IOException e) {
                Log.e("messagebus", e.getMessage());
            }
        }
    }

    private void multicast(String multicastMessage) throws IOException {
        socket = new DatagramSocket();
        group = InetAddress.getByName("192.168.86.161");
        buf = multicastMessage.getBytes();

        DatagramPacket packet
                = new DatagramPacket(buf, buf.length, group, 4446);
        socket.send(packet);
        socket.close();
    }
}
