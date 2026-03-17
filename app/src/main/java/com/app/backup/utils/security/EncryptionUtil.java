package com.app.backup.utils.security;

import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;
import android.util.Base64;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.KeyStore;
import java.security.MessageDigest;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class EncryptionUtil {

    private static final String TAG = "EncryptionUtil";
    private static final String ANDROID_KEYSTORE = "AndroidKeyStore";
    private static final String KEY_ALIAS = "BackupVaultKey";
    private static final String AES_MODE = "AES/GCM/NoPadding";
    private static final int GCM_IV_LENGTH = 12;
    private static final int GCM_TAG_LENGTH = 128;
    private static final String ALGORITHM = "AES";

    @Inject
    public EncryptionUtil() {}

    /**
     * Generate or retrieve AES key from Android Keystore
     */
    public SecretKey getOrCreateKey() throws Exception {
        KeyStore keyStore = KeyStore.getInstance(ANDROID_KEYSTORE);
        keyStore.load(null);

        if (!keyStore.containsAlias(KEY_ALIAS)) {
            KeyGenerator keyGenerator = KeyGenerator.getInstance(
                    KeyProperties.KEY_ALGORITHM_AES, ANDROID_KEYSTORE);
            keyGenerator.init(new KeyGenParameterSpec.Builder(
                    KEY_ALIAS,
                    KeyProperties.PURPOSE_ENCRYPT | KeyProperties.PURPOSE_DECRYPT)
                    .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
                    .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
                    .setKeySize(256)
                    .setUserAuthenticationRequired(false)
                    .build());
            keyGenerator.generateKey();
        }

        return (SecretKey) keyStore.getKey(KEY_ALIAS, null);
    }

    /**
     * Encrypt a file and write to output path
     */
    public boolean encryptFile(File inputFile, File outputFile) {
        try {
            SecretKey key = getOrCreateKey();
            Cipher cipher = Cipher.getInstance(AES_MODE);
            cipher.init(Cipher.ENCRYPT_MODE, key);
            byte[] iv = cipher.getIV();

            try (FileInputStream fis = new FileInputStream(inputFile);
                 FileOutputStream fos = new FileOutputStream(outputFile)) {

                // Write IV length and IV first
                fos.write(iv.length);
                fos.write(iv);

                try (CipherOutputStream cos = new CipherOutputStream(fos, cipher)) {
                    byte[] buffer = new byte[8192];
                    int bytesRead;
                    while ((bytesRead = fis.read(buffer)) != -1) {
                        cos.write(buffer, 0, bytesRead);
                    }
                }
            }
            return true;
        } catch (Exception e) {
            Log.e(TAG, "Encryption failed", e);
            return false;
        }
    }

    /**
     * Decrypt a file and write to output path
     */
    public boolean decryptFile(File inputFile, File outputFile) {
        try {
            SecretKey key = getOrCreateKey();

            try (FileInputStream fis = new FileInputStream(inputFile);
                 FileOutputStream fos = new FileOutputStream(outputFile)) {

                // Read IV
                int ivLength = fis.read();
                byte[] iv = new byte[ivLength];
                int read = fis.read(iv);
                if (read != ivLength) {
                    throw new IOException("Could not read IV");
                }

                Cipher cipher = Cipher.getInstance(AES_MODE);
                GCMParameterSpec parameterSpec = new GCMParameterSpec(GCM_TAG_LENGTH, iv);
                cipher.init(Cipher.DECRYPT_MODE, key, parameterSpec);

                try (CipherInputStream cis = new CipherInputStream(fis, cipher)) {
                    byte[] buffer = new byte[8192];
                    int bytesRead;
                    while ((bytesRead = cis.read(buffer)) != -1) {
                        fos.write(buffer, 0, bytesRead);
                    }
                }
            }
            return true;
        } catch (Exception e) {
            Log.e(TAG, "Decryption failed", e);
            return false;
        }
    }

    /**
     * Encrypt bytes directly
     */
    public byte[] encryptBytes(byte[] data) throws Exception {
        SecretKey key = getOrCreateKey();
        Cipher cipher = Cipher.getInstance(AES_MODE);
        cipher.init(Cipher.ENCRYPT_MODE, key);
        byte[] iv = cipher.getIV();
        byte[] encrypted = cipher.doFinal(data);

        // Prepend IV to encrypted data
        byte[] result = new byte[1 + iv.length + encrypted.length];
        result[0] = (byte) iv.length;
        System.arraycopy(iv, 0, result, 1, iv.length);
        System.arraycopy(encrypted, 0, result, 1 + iv.length, encrypted.length);
        return result;
    }

    /**
     * Decrypt bytes directly
     */
    public byte[] decryptBytes(byte[] data) throws Exception {
        int ivLength = data[0] & 0xFF;
        byte[] iv = new byte[ivLength];
        System.arraycopy(data, 1, iv, 0, ivLength);

        byte[] encrypted = new byte[data.length - 1 - ivLength];
        System.arraycopy(data, 1 + ivLength, encrypted, 0, encrypted.length);

        SecretKey key = getOrCreateKey();
        Cipher cipher = Cipher.getInstance(AES_MODE);
        GCMParameterSpec parameterSpec = new GCMParameterSpec(GCM_TAG_LENGTH, iv);
        cipher.init(Cipher.DECRYPT_MODE, key, parameterSpec);
        return cipher.doFinal(encrypted);
    }

    /**
     * Generate SHA-256 checksum of a file
     */
    public String computeChecksum(File file) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            try (FileInputStream fis = new FileInputStream(file)) {
                byte[] buffer = new byte[8192];
                int bytesRead;
                while ((bytesRead = fis.read(buffer)) != -1) {
                    digest.update(buffer, 0, bytesRead);
                }
            }
            byte[] hash = digest.digest();
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (Exception e) {
            Log.e(TAG, "Checksum computation failed", e);
            return null;
        }
    }

    /**
     * Verify file checksum
     */
    public boolean verifyChecksum(File file, String expectedChecksum) {
        String actual = computeChecksum(file);
        return actual != null && actual.equals(expectedChecksum);
    }

    /**
     * Generate a random encryption password (for password-based backups)
     */
    public String generateSecurePassword() {
        SecureRandom random = new SecureRandom();
        byte[] bytes = new byte[32];
        random.nextBytes(bytes);
        return Base64.encodeToString(bytes, Base64.URL_SAFE | Base64.NO_WRAP);
    }

    /**
     * Derive key from password using PBKDF2
     */
    public SecretKey deriveKeyFromPassword(String password, byte[] salt) throws Exception {
        javax.crypto.SecretKeyFactory factory = javax.crypto.SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        javax.crypto.spec.PBEKeySpec spec = new javax.crypto.spec.PBEKeySpec(
                password.toCharArray(), salt, 65536, 256);
        SecretKey tmp = factory.generateSecret(spec);
        return new SecretKeySpec(tmp.getEncoded(), ALGORITHM);
    }
}
