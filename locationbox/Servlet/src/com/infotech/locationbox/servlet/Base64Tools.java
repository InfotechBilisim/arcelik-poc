package com.infotech.locationbox.servlet;

import java.io.File;
import java.io.FileInputStream;


public class Base64Tools {
    // Mapping table from 6-bit nibbles to Base64 characters.
    private static char[] map1 = new char[64];
    static {
        int i = 0;
        for (char c = 'A'; c <= 'Z'; c++)
            map1[i++] = c;
        for (char c = 'a'; c <= 'z'; c++)
            map1[i++] = c;
        for (char c = '0'; c <= '9'; c++)
            map1[i++] = c;
        map1[i++] = '+';
        map1[i++] = '/';
    }

    // Mapping table from Base64 characters to 6-bit nibbles.
    private static byte[] map2 = new byte[128];
    static {
        for (int i = 0; i < map2.length; i++)
            map2[i] = -1;
        for (int i = 0; i < 64; i++)
            map2[map1[i]] = (byte) i;
    }

    public Base64Tools() {
    }

    //-----------------------------------------------------------------------------

    public static String encodeFile(String fname, int reclen) {
        FileInputStream inp = null;
        StringBuffer str = new StringBuffer("");

        try {
            File f = new File(fname);
            int length = (int) f.length();
            byte[] bytes = new byte[length + 1024];
            inp = new FileInputStream(f);
            length = inp.read(bytes);
            int offs = 0;
            while (true) {
                if (offs >= length)
                    break;

                if (offs + reclen > length)
                    reclen = length - offs;
                char[] chars = encode(bytes, offs, reclen);
                str.append(chars);
                str.append('\n');
                offs += reclen;
            } // while()

            return str.toString();
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            try {
                if (inp != null)
                    inp.close();
            } catch (Exception e) {
                ;
            }
        }

        return null;
    } // encodeFile()

    //-----------------------------------------------------------------------------

    public static String encodeString(String s) {
        return new String(encode(s.getBytes()));
    } // encodeString()

    //-----------------------------------------------------------------------------

    public static char[] encode(byte[] bytes) {
        return encode(bytes, 0, bytes.length);
    } // encode()

    //-----------------------------------------------------------------------------

    public static char[] encode(byte[] in, int offs, int iLen) {
        int oDataLen = (iLen * 4 + 2) / 3; // output length without padding
        int oLen = ((iLen + 2) / 3) * 4; // output length including padding
        char[] out = new char[oLen];
        int ip = 0;
        int op = 0;
        while (ip < iLen) {
            int i0 = in[offs + ip++] & 0xff;
            int i1 = ip < iLen ? in[offs + ip++] & 0xff : 0;
            int i2 = ip < iLen ? in[offs + ip++] & 0xff : 0;
            int o0 = i0 >>> 2;
            int o1 = ((i0 & 3) << 4) | (i1 >>> 4);
            int o2 = ((i1 & 0xf) << 2) | (i2 >>> 6);
            int o3 = i2 & 0x3F;
            out[op++] = map1[o0];
            out[op++] = map1[o1];
            out[op] = op < oDataLen ? map1[o2] : '=';
            op++;
            out[op] = op < oDataLen ? map1[o3] : '=';
            op++;
        } // while()
        return out;
    } // encode()

    //-----------------------------------------------------------------------------

    public static String decodeString(String s) {
        return new String(decode(s));
    } // decodeString()

    //-----------------------------------------------------------------------------

    public static byte[] decode(String s) {
        return decode(s.toCharArray());
    } // decode()

    //-----------------------------------------------------------------------------

    public static byte[] decode(char[] in) {
        int iLen = in.length;
        if (iLen % 4 != 0)
            throw new IllegalArgumentException("Length of Base64 encoded input string is not a multiple of 4.");
        while (iLen > 0 && in[iLen - 1] == '=')
            iLen--;
        int oLen = (iLen * 3) / 4;
        byte[] out = new byte[oLen];
        int ip = 0;
        int op = 0;
        while (ip < iLen) {
            int i0 = in[ip++];
            int i1 = in[ip++];
            int i2 = ip < iLen ? in[ip++] : 'A';
            int i3 = ip < iLen ? in[ip++] : 'A';
            if (i0 > 127 || i1 > 127 || i2 > 127 || i3 > 127)
                throw new IllegalArgumentException("Illegal character in Base64 encoded data.");
            int b0 = map2[i0];
            int b1 = map2[i1];
            int b2 = map2[i2];
            int b3 = map2[i3];
            if (b0 < 0 || b1 < 0 || b2 < 0 || b3 < 0)
                throw new IllegalArgumentException("Illegal character in Base64 encoded data.");
            int o0 = (b0 << 2) | (b1 >>> 4);
            int o1 = ((b1 & 0xf) << 4) | (b2 >>> 2);
            int o2 = ((b2 & 3) << 6) | b3;
            out[op++] = (byte) o0;
            if (op < oLen)
                out[op++] = (byte) o1;
            if (op < oLen)
                out[op++] = (byte) o2;
        } // while()
        return out;
    } // decode()

}
