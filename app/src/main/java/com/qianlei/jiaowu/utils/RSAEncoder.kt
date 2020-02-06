package com.qianlei.jiaowu.utils

import java.math.BigInteger
import java.util.*

/**
 * RSA加密工具
 *
 * @author qianlei
 */
object RSAEncoder {
    private var n: BigInteger? = null
    private var e: BigInteger? = null
    fun rsaEncrypt(pwd: String, nStr: String, eStr: String): String {
        n = BigInteger(nStr, 16)
        e = BigInteger(eStr, 16)
        val r = rsaDoPublic(pkcs1pad2(pwd, n!!.bitLength() + 7 shr 3))
        var sp = r.toString(16)
        if (sp.length and 1 != 0) {
            sp = "0$sp"
        }
        return sp
    }

    private fun rsaDoPublic(x: BigInteger?): BigInteger {
        return x!!.modPow(e!!, n!!)
    }

    private fun pkcs1pad2(s: String, n: Int): BigInteger? {
        var num = n
        if (num < s.length + 11) {
            System.err.println("Message too long for RSAEncoder")
            return null
        }
        val ba = ByteArray(num)
        var i = s.length - 1
        while (i >= 0 && num > 0) {
            val c = s.codePointAt(i--)
            when {
                c < 128 -> {
                    ba[--num] = java.lang.Byte.valueOf(c.toString())
                }
                c < 2048 -> {
                    ba[--num] = java.lang.Byte.valueOf((c and 63 or 128).toString())
                    ba[--num] = java.lang.Byte.valueOf((c shr 6 or 192).toString())
                }
                else -> {
                    ba[--num] = java.lang.Byte.valueOf((c and 63 or 128).toString())
                    ba[--num] = java.lang.Byte.valueOf((c shr 6 and 63 or 128).toString())
                    ba[--num] = java.lang.Byte.valueOf((c shr 12 or 224).toString())
                }
            }
        }
        ba[--num] = java.lang.Byte.valueOf("0")
        val temp = ByteArray(1)
        val rdm = Random(47L)
        while (num > 2) { // random non-zero pad
            temp[0] = java.lang.Byte.valueOf("0")
            while (temp[0].toInt() == 0) {
                rdm.nextBytes(temp)
            }
            ba[--num] = temp[0]
        }
        ba[--num] = 2
        ba[--num] = 0
        return BigInteger(ba)
    }
}