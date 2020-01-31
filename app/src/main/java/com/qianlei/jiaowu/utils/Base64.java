package com.qianlei.jiaowu.utils;

import static java.lang.Integer.parseInt;

/**
 * Base64工具类
 *
 * @author qianlei
 */
public class Base64 {

    private static final String B64MAP = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/";
    private static final char B64PAD = '=';
    private static final String HEX_CODE = "0123456789abcdef";

    /**
     * 获取对应16进制字符
     *
     * @param a 0-15之间的数
     * @return 对应的16进制字符
     */
    private static char int2char(int a) {
        return HEX_CODE.charAt(a);
    }

    /**
     * Base64转16进制
     *
     * @param s base64编码
     * @return 16进制的编码
     */
    public static String b64tohex(String s) {
        StringBuilder ret = new StringBuilder();
        int k = 0;
        int slop = 0;
        for (int i = 0; i < s.length(); ++i) {
            if (s.charAt(i) == B64PAD) {
                break;
            }
            int v = B64MAP.indexOf(s.charAt(i));
            if (v < 0) {
                continue;
            }
            if (k == 0) {
                ret.append(int2char(v >> 2));
                slop = v & 3;
                k = 1;
            } else if (k == 1) {
                ret.append(int2char((slop << 2) | (v >> 4)));
                slop = v & 0xf;
                k = 2;
            } else if (k == 2) {
                ret.append(int2char(slop));
                ret.append(int2char(v >> 2));
                slop = v & 3;
                k = 3;
            } else {
                ret.append(int2char((slop << 2) | (v >> 4)));
                ret.append(int2char(v & 0xf));
                k = 0;
            }
        }
        if (k == 1) {
            ret.append(int2char(slop << 2));
        }
        return ret.toString();
    }

    /**
     * 16进制转Base64
     *
     * @param h 16进制数
     * @return base64编码
     */
    public static String hex2b64(String h) {
        int i, c;
        StringBuilder ret = new StringBuilder();
        for (i = 0; i + 3 <= h.length(); i += 3) {
            c = parseInt(h.substring(i, i + 3), 16);
            ret.append(B64MAP.charAt(c >> 6));
            ret.append(B64MAP.charAt(c & 63));
        }
        if (i + 1 == h.length()) {
            c = parseInt(h.substring(i, i + 1), 16);
            ret.append(B64MAP.charAt(c << 2));
        } else if (i + 2 == h.length()) {
            c = parseInt(h.substring(i, i + 2), 16);
            ret.append(B64MAP.charAt(c >> 2));
            ret.append(B64MAP.charAt((c & 3) << 4));
        }
        while ((ret.length() & 3) > 0) {
            ret.append(B64PAD);
        }
        return ret.toString();
    }
}
