package com.wujt.network;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * 域名 解析
 *
 * @author wujt
 */
public class NSLookupDemo {
    public static void main(String[] args) {
        // The URL for which IP address needs to be fetched
        String host = "www.google.com";

        try {
            // Fetch IP address by getByName()
            InetAddress[] inetAddressArray = InetAddress.getAllByName(host);

            // Print the IP address
            for (int i = 0; i < inetAddressArray.length; i++) {
                displayStuff("www.google.com #" + (i + 1), inetAddressArray[i]);
            }
        } catch (UnknownHostException e) {
            // It means the URL is invalid
            System.out.println("Invalid URL");
        }
    }

    public static void displayStuff(String whichHost, InetAddress inetAddress) {
        System.out.println("--------------------------");
        System.out.println("Which Host:" + whichHost);
        System.out.println("Canonical Host Name:" + inetAddress.getCanonicalHostName());
        System.out.println("Host Name:" + inetAddress.getHostName());
        System.out.println("Host Address:" + inetAddress.getHostAddress());
    }
}
