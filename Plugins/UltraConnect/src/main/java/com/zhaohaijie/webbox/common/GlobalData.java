package com.zhaohaijie.webbox.common;

import com.sun.tools.corba.se.idl.InvalidArgument;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang3.StringUtils;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.Charset;
import java.util.Calendar;
import java.util.Locale;

public class GlobalData {
    public static final String PRIMARY_SERVER_URL_INDEX = "wb.plugin.ultraconnect.primary.server.url.index";
    public static final String SERIAL_NUMBER = "wb.plugin.ultraconnect.serial.number";
    public static final String PASSCODE = "wb.plugin.ultraconnect.passcode";
    private static final byte[] MASTER_KEY = new byte[]{65, -113, -70, -114, 77, -74, -58, 10, 25, 11, -106, 29, 42, 14, 106, 7, 3, -85, -34, 70, -128, 111, 21, -32, 94, 114, -121, 1, -15, -50, -67, -116, 1, -61, 72, -106, 30, -41, -1, 49, 84, 70, -1, -34, -62, -57, 100, -19};

    public static int LAUNCH_IMAGE_TIME_TO_DISPLAY = 3000;
    private static final Locale[] langValues_EU = new Locale[]{new Locale("en"), new Locale("da"), new Locale("de"), new Locale("el"), new Locale("es"), new Locale("fr"), new Locale("fr", "BE"), new Locale("it"), new Locale("nl"), new Locale("nl", "BE"), new Locale("pt")};
    private static final Locale[] langValues_US = new Locale[]{new Locale("en"), new Locale("es"), new Locale("fr", "CA"), new Locale("pt")};
    public static final String[] notifym_url_array = new String[]{"https://notifym.ultraconnect.com", "https://webauth-qa1.zerowire.com", "https://xrelay-dv1a.zerowire.com"};
    public static final String[] xserver_url_array = new String[]{"https://webauth-a.ultraconnect.com", "https://webauth-qa1.zerowire.com", "https://xrelay-dv1a.zerowire.com"};
    private String appName = "ultraconnect";
    private int appVersionCode = 44;
    private String appVersionName = "1.11.11";
    private String appType = "android-ultraconnect";
    private CharSequence[] choiceList;
    private boolean editMode = false;
    private boolean isInBackground = false;
    private Locale[] langValues;
    private boolean openSesame = false;
    private boolean serialNumberSiteDescriptionPrefix = false;

    public String getAppName() {
        return this.appName;
    }

    public int getAppVersionCode() {
        return this.appVersionCode;
    }

    public String getAppVersionName() {
        return this.appVersionName;
    }

    public String getAppType() {
        return this.appType;
    }

    private static class GlobalDataHolder {
        private static GlobalData singletonGlobalData = new GlobalData();
    }

    public static GlobalData getGlobalDataInstance() {
        return GlobalDataHolder.singletonGlobalData;
    }

    public String toLanguageTag() {
        return "en";
    }

    public String getPrimaryServerUrl() {
        return xserver_url_array[getPrimaryServerUrlIndex()];
    }

    public static String getDateString() {
        Calendar now = Calendar.getInstance();
        return "" + now.get(1) + String.format("%02d", now.get(2) + 1) +
                String.format("%02d", now.get(5)) + "T" + String.format("%02d", now.get(11)) +
                String.format("%02d", now.get(12)) + String.format("%02d", now.get(13)) + "Z";
    }

    private int getPrimaryServerUrlIndex() {
        int index = Integer.getInteger(PRIMARY_SERVER_URL_INDEX, 0);

        if (index >= 0 && index < xserver_url_array.length) {
            return index;
        }

        return 0;
    }

    public String getSerialNumber() throws InvalidArgument {
        return getStringProperty(SERIAL_NUMBER);
    }

    public String getPasscode() throws InvalidArgument {
        return getStringProperty(PASSCODE);
    }

    private String getStringProperty(String propName) throws InvalidArgument {
        final String propValue = System.getProperty(propName);

        if (StringUtils.isEmpty(propValue)) {
            throw new InvalidArgument(propName + " can't empty");
        }

        return propValue;
    }

    public static String getSignature(String dateString, String rlayUrl, String serialNumber) {
        String signature;
        try {
            Charset charset = Charset.forName("US-ASCII");
            Mac mac = Mac.getInstance("HmacSHA256");
            SecretKeySpec secretKeySpec = new SecretKeySpec(MASTER_KEY, "HmacSHA256");
            mac.init(secretKeySpec);
            byte[] macBytes = mac.doFinal(charset.encode(dateString).array());
            secretKeySpec = new SecretKeySpec(macBytes, "HmacSHA256");
            mac.init(secretKeySpec);
            macBytes = mac.doFinal(charset.encode(rlayUrl).array());
            SecretKeySpec var8 = new SecretKeySpec(macBytes, "HmacSHA256");
            mac.init(var8);
            signature = Hex.encodeHexString(mac.doFinal(charset.encode(serialNumber).array()));
        } catch (Exception var6) {
            var6.printStackTrace();
            signature = null;
        }

        return signature;
    }
}
