package io.github.nikodc.commons;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

public class NetworkUtils {

    public static String getIpAddress() throws SocketException {
        Enumeration<NetworkInterface> ifcs = NetworkInterface.getNetworkInterfaces();
        while (ifcs.hasMoreElements()) {
            NetworkInterface ifc = ifcs.nextElement();
            Enumeration<InetAddress> addresses = ifc.getInetAddresses();

            while (addresses.hasMoreElements()) {
                InetAddress addr = addresses.nextElement();
                if (addr instanceof Inet4Address && !addr.isLoopbackAddress()) {
                    return toString(addr);
                }
            }
        }

        return null;
    }

    private static String toString(InetAddress addr) {
        return addr.toString().substring(1);
    }

}
