package com.dasu.utils;

import android.text.TextUtils;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

/**
 * Created by dasu on 2018/10/10.
 * 微信公众号：dasuAndroidTv
 * blog：https://www.jianshu.com/u/bb52a2918096
 *
 */

public class MacUtils {

    /**
     * @see android.net.wifi.WifiInfo
     */
    private static final String DEFAULT_MAC = "02:00:00:00:00:00";

    private static String sMacAddress = null;

    /**
     * 获取 mac 地址
     *
     * 按照以下顺序读取 MAC 地址，成功则直接使用，失败则继续使用下个方式
     * 1. /sys/class/net/eth0/address
     * 2. busybox ifconfig eth0
     * 3. getprop ro.boot.mac
     * 4. /sys/class/net/wlan0/address
     * 5. busybox ifconfig wlan0
     * 6. NetworkInterface.getByInetAddress
     * 7. NetworkInterface.getNetworkInterfaces
     * 8. 默认
     *
     * @return
     */
    public static String getMac() {
        if (sMacAddress != null) {
            return sMacAddress;
        }

        String result = readSysFile("/sys/class/net/eth0/address");
        if (!isValidMac(result)) {
            result = callCmd("busybox ifconfig eth0", "HWaddr");
            if (!isValidMac(result)) {
                result = callCmd("getprop ro.boot.mac", "");
                if (!isValidMac(result)) {
                    String wifi = callCmd("getprop wifi.interface", "");
                    result = readSysFile("/sys/class/net/" + wifi + "/address");
                    if (!isValidMac(result)) {
                        result = callCmd("busybox ifconfig wlan0", "HWaddr");
                        if (!isValidMac(result)) {
                            result = getMacAddressByInetAddress();
                            if (!isValidMac(result)) {
                                result = getMacAddressByNetworkInterface();
                            }
                        }
                    }
                }
            }
        }
        if (TextUtils.isEmpty(result)) {
            Log.e("MacUtils", "getMac error, use default mac: 02:00:00:00:00:00");
            return DEFAULT_MAC;
        }
        sMacAddress = result;
        return result;
    }

    private static String readSysFile(String path) {
        if (!new File(path).exists()) {
            return null;
        }
        FileReader fr = null;
        BufferedReader br = null;
        StringBuilder value = null;
        try {
            String str;
            fr = new FileReader(path);
            br = new BufferedReader(fr);
            value = new StringBuilder();
            while ((str = br.readLine()) != null) {
                value.append(str);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != fr) {
                    fr.close();
                }
                if (null != br) {
                    br.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return value != null ? value.toString() : null;
    }

    private static String callCmd(String cmd, String filter) {
        String result = "";
        String line;
        InputStreamReader is = null;
        try {
            Process proc = Runtime.getRuntime().exec(cmd);
            is = new InputStreamReader(proc.getInputStream());
            BufferedReader br = new BufferedReader(is);
            while ((line = br.readLine()) != null) {
                if (!TextUtils.isEmpty(filter)) {
                    if (line.contains(filter)) {
                        result = line;
                    }
                    break;
                } else {
                    result += line;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        if (!TextUtils.isEmpty(result) && result.contains("HWaddr")) {
            result = result.substring(result.indexOf("HWaddr") + 6, result.length() - 1);
            if (result.length() > 1) {
                return result.trim();
            }
        }

        return result;
    }

    private static boolean isValidMac(String macStr) {
        if (TextUtils.isEmpty(macStr) || DEFAULT_MAC.equals(macStr)) {
            return false;
        }
        String macAddressRule = "([A-Fa-f0-9]{2}[-:]){5}[A-Fa-f0-9]{2}";
        if (macStr.matches(macAddressRule)) {
            String splitStr = null;
            if (macStr.contains(":")) {
                splitStr = ":";
            } else if (macStr.contains("-")) {
                splitStr = "-";
            }
            String[] addr = macStr.split(splitStr);
            boolean isAllZero = true;
            for (String add : addr) {
                if (!"00".equals(add)) {
                    isAllZero = false;
                    break;
                }
            }
            boolean isAllOne = true;
            for (String add : addr) {
                if (!"11".equals(add)) {
                    isAllOne = false;
                    break;
                }
            }
            if (isAllZero || isAllOne) {
                return false;
            }
            return true;
        } else {
            return false;
        }
    }

    private static boolean isAddressNotInExcepts(final String address, final String... excepts) {
        if (excepts == null || excepts.length == 0) {
            return !DEFAULT_MAC.equals(address);
        }
        for (String filter : excepts) {
            if (address.equals(filter)) {
                return false;
            }
        }
        return true;
    }

    private static String getMacAddressByNetworkInterface() {
        try {
            Enumeration<NetworkInterface> nis = NetworkInterface.getNetworkInterfaces();
            while (nis.hasMoreElements()) {
                NetworkInterface ni = nis.nextElement();
                if (ni == null || !ni.getName().equalsIgnoreCase("wlan0")) continue;
                byte[] macBytes = ni.getHardwareAddress();
                if (macBytes != null && macBytes.length > 0) {
                    StringBuilder sb = new StringBuilder();
                    for (byte b : macBytes) {
                        sb.append(String.format("%02x:", b));
                    }
                    return sb.substring(0, sb.length() - 1);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return DEFAULT_MAC;
    }

    private static String getMacAddressByInetAddress() {
        try {
            InetAddress inetAddress = getInetAddress();
            if (inetAddress != null) {
                NetworkInterface ni = NetworkInterface.getByInetAddress(inetAddress);
                if (ni != null) {
                    byte[] macBytes = ni.getHardwareAddress();
                    if (macBytes != null && macBytes.length > 0) {
                        StringBuilder sb = new StringBuilder();
                        for (byte b : macBytes) {
                            sb.append(String.format("%02x:", b));
                        }
                        return sb.substring(0, sb.length() - 1);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return DEFAULT_MAC;
    }

    private static InetAddress getInetAddress() {
        try {
            Enumeration<NetworkInterface> nis = NetworkInterface.getNetworkInterfaces();
            while (nis.hasMoreElements()) {
                NetworkInterface ni = nis.nextElement();
                // To prevent phone of xiaomi return "10.0.2.15"
                if (!ni.isUp()) continue;
                Enumeration<InetAddress> addresses = ni.getInetAddresses();
                while (addresses.hasMoreElements()) {
                    InetAddress inetAddress = addresses.nextElement();
                    if (!inetAddress.isLoopbackAddress()) {
                        String hostAddress = inetAddress.getHostAddress();
                        if (hostAddress.indexOf(':') < 0) return inetAddress;
                    }
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
        return null;
    }
}
