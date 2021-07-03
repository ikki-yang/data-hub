package com.issac.studio.app.util;

public class OSInfo {
    private static final String OS = System.getProperty("os.name").toLowerCase();

    private static final OSInfo _instance = new OSInfo();

    private Platform platform;

    private OSInfo(){}

    public static boolean isLinux(){
        return OS.contains("linux");
    }

    public static boolean isMacOS(){
        return OS.contains("mac") &&OS.indexOf("os")>0&& !OS.contains("x");
    }

    public static boolean isMacOSX(){
        return OS.contains("mac") &&OS.indexOf("os")>0&&OS.indexOf("x")>0;
    }

    public static boolean isWindows(){
        return OS.contains("windows");
    }

    public static boolean isOS2(){
        return OS.contains("os/2");
    }

    public static boolean isSolaris(){
        return OS.contains("solaris");
    }

    public static boolean isSunOS(){
        return OS.contains("sunos");
    }

    public static boolean isMPEiX(){
        return OS.contains("mpe/ix");
    }

    public static boolean isHPUX(){
        return OS.contains("hp-ux");
    }

    public static boolean isAix(){
        return OS.contains("aix");
    }

    public static boolean isOS390(){
        return OS.contains("os/390");
    }

    public static boolean isFreeBSD(){
        return OS.contains("freebsd");
    }

    public static boolean isIrix(){
        return OS.contains("irix");
    }

    public static boolean isDigitalUnix(){
        return OS.contains("digital") &&OS.indexOf("unix")>0;
    }

    public static boolean isNetWare(){
        return OS.contains("netware");
    }

    public static boolean isOSF1(){
        return OS.contains("osf1");
    }

    public static boolean isOpenVMS(){
        return OS.contains("openvms");
    }

    /**
     * 获取操作系统名字
     * @return 操作系统名
     */
    public static Platform getOSname(){
        if(isAix()){
            _instance.platform = Platform.AIX;
        }else if (isDigitalUnix()) {
            _instance.platform = Platform.Digital_Unix;
        }else if (isFreeBSD()) {
            _instance.platform = Platform.FreeBSD;
        }else if (isHPUX()) {
            _instance.platform = Platform.HP_UX;
        }else if (isIrix()) {
            _instance.platform = Platform.Irix;
        }else if (isLinux()) {
            _instance.platform = Platform.Linux;
        }else if (isMacOS()) {
            _instance.platform = Platform.Mac_OS;
        }else if (isMacOSX()) {
            _instance.platform = Platform.Mac_OS_X;
        }else if (isMPEiX()) {
            _instance.platform = Platform.MPEiX;
        }else if (isNetWare()) {
            _instance.platform = Platform.NetWare_411;
        }else if (isOpenVMS()) {
            _instance.platform = Platform.OpenVMS;
        }else if (isOS2()) {
            _instance.platform = Platform.OS2;
        }else if (isOS390()) {
            _instance.platform = Platform.OS390;
        }else if (isOSF1()) {
            _instance.platform = Platform.OSF1;
        }else if (isSolaris()) {
            _instance.platform = Platform.Solaris;
        }else if (isSunOS()) {
            _instance.platform = Platform.SunOS;
        }else if (isWindows()) {
            _instance.platform = Platform.Windows;
        }else{
            _instance.platform = Platform.Others;
        }
        return _instance.platform;
    }


    /**
     * 简单使用
     * @param args : args
     */
    public static void main(String[] args) {
        System.out.println(OSInfo.getOSname());// 获取系统类型
        System.out.println(OSInfo.isWindows());// 判断是否为windows系统
    }
}
