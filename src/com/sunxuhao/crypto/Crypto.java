package com.sunxuhao.crypto;

import cn.hutool.core.util.RandomUtil;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.LinkedList;
import java.util.List;

public class Crypto {
    public static final int MD5 = 0, SHA1 = 1, SHA256 = 2, ADD = 3;

    protected static String toHexString(byte[] bytes) {
        StringBuilder sb=new StringBuilder();
        for(byte b:bytes){
            sb.append(Integer.toHexString(0xff&b));
        }
        return sb.toString();
    }

    protected static abstract class CryptoElement {
        public abstract byte[] encrypt(byte[] bytes);
    }

    protected class MD5 extends CryptoElement {
        private MessageDigest digest;

        {
            try {
                digest = MessageDigest.getInstance("MD5");
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
        }

        @Override
        public byte[] encrypt(byte[] bytes) {
            byte[] res = digest.digest(bytes);
            digest.reset();
            return res;
        }
    }

    protected class SHA1 extends CryptoElement {
        private MessageDigest digest;

        {
            try {
                digest = MessageDigest.getInstance("SHA-1");
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
        }

        @Override
        public byte[] encrypt(byte[] bytes) {
            byte[] res = digest.digest(bytes);
            digest.reset();
            return res;
        }
    }

    protected class SHA256 extends CryptoElement {
        private MessageDigest digest;

        {
            try {
                digest = MessageDigest.getInstance("SHA-256");
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
        }

        @Override
        public byte[] encrypt(byte[] bytes) {
            byte[] res = digest.digest(bytes);
            digest.reset();
            return res;
        }
    }

    public class ADD extends CryptoElement {
        private byte[] salt = RandomUtil.randomString(32).getBytes();

        @Override
        public byte[] encrypt(byte[] bytes) {
            byte[] res = new byte[bytes.length + salt.length];
            System.arraycopy(bytes, 0, res, 0, bytes.length);
            System.arraycopy(salt, 0, res, bytes.length, salt.length);
            return res;
        }
    }

    protected List<CryptoElement> elements;

    public Crypto(List<Integer> types) {
        elements = new LinkedList<>();
        for (Integer type : types) {
            switch (type) {
                case MD5:
                    elements.add(new MD5());
                    break;
                case SHA1:
                    elements.add(new SHA1());
                    break;
                case SHA256:
                    elements.add(new SHA256());
                    break;
                case ADD:
                    elements.add(new ADD());
                    break;
                default:
                    System.out.println(type);
            }
        }
    }

    public String encrypt(byte[] bytes, int offset, int len) {
        byte[] res = new byte[len];
        System.arraycopy(bytes, offset, res, 0, len);
        for (Crypto.CryptoElement e : elements) {
            res = e.encrypt(res);
        }
        return toHexString(res);
    }

    public String encrypt(byte[] bytes, int len) {
        return encrypt(bytes, 0, len);
    }

    public String encrypt(byte[] bytes) {
        return encrypt(bytes, 0, bytes.length);
    }

    public String encrypt(String s) {
        return encrypt(s.getBytes());
    }
}
