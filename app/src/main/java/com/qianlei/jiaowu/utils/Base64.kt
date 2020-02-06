package com.qianlei.jiaowu.utils

/**
 * Base64工具类
 *
 * @author qianlei
 */
object Base64 {
    private const val B64MAP = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/"
    private const val B64PAD = '='
    private const val HEX_CODE = "0123456789abcdef"
    /**
     * 获取对应16进制字符
     *
     * @param a 0-15之间的数
     * @return 对应的16进制字符
     */
    private fun int2char(a: Int): Char {
        return HEX_CODE[a]
    }

    /**
     * Base64转16进制
     *
     * @param s base64编码
     * @return 16进制的编码
     */
    fun b64toHex(s: String): String {
        val ret = StringBuilder()
        var k = 0
        var slop = 0
        for (i in s.indices) {
            if (s[i] == B64PAD) {
                break
            }
            val v = B64MAP.indexOf(s[i])
            if (v < 0) {
                continue
            }
            when (k) {
                0 -> {
                    ret.append(int2char(v shr 2))
                    slop = v and 3
                    k = 1
                }
                1 -> {
                    ret.append(int2char(slop shl 2 or (v shr 4)))
                    slop = v and 0xf
                    k = 2
                }
                2 -> {
                    ret.append(int2char(slop))
                    ret.append(int2char(v shr 2))
                    slop = v and 3
                    k = 3
                }
                else -> {
                    ret.append(int2char(slop shl 2 or (v shr 4)))
                    ret.append(int2char(v and 0xf))
                    k = 0
                }
            }
        }
        if (k == 1) {
            ret.append(int2char(slop shl 2))
        }
        return ret.toString()
    }

    /**
     * 16进制转Base64
     *
     * @param h 16进制数
     * @return base64编码
     */
    fun hex2b64(h: String?): String {
        var c: Int
        val ret = StringBuilder()
        var i = 0
        while (i + 3 <= h!!.length) {
            c = h.substring(i, i + 3).toInt(16)
            ret.append(B64MAP[c shr 6])
            ret.append(B64MAP[c and 63])
            i += 3
        }
        if (i + 1 == h.length) {
            c = h.substring(i, i + 1).toInt(16)
            ret.append(B64MAP[c shl 2])
        } else if (i + 2 == h.length) {
            c = h.substring(i, i + 2).toInt(16)
            ret.append(B64MAP[c shr 2])
            ret.append(B64MAP[c and 3 shl 4])
        }
        while (ret.length and 3 > 0) {
            ret.append(B64PAD)
        }
        return ret.toString()
    }
}