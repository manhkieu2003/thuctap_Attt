package org.example.Mahoa;

import java.util.Scanner;

public class VigenereDemo {
    // Bảng mã 97 ký tự ASCII từ SPACE (32) đến '~' (126)
    public static final String CHARSET = " !\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~";

    // Hàm mã hóa Vigenère
    public static String encrypt(String text, String key) {
        StringBuilder encryptedText = new StringBuilder();
        int charsetSize = CHARSET.length();

        for (int i = 0, j = 0; i < text.length(); i++) {
            char currentChar = text.charAt(i);
            int charIndex = CHARSET.indexOf(currentChar);

            if (charIndex == -1) { // Nếu ký tự không nằm trong bảng mã
                encryptedText.append(currentChar); // Giữ nguyên ký tự
                continue;
            }

            char keyChar = key.charAt(j % key.length());
            int keyIndex = CHARSET.indexOf(keyChar);

            int newIndex = (charIndex + keyIndex) % charsetSize; // Dịch theo khóa
            encryptedText.append(CHARSET.charAt(newIndex));

            j++; // Chỉ tăng j khi ký tự thuộc bảng mã
        }

        return encryptedText.toString();
    }

    // Hàm giải mã Vigenère
    public static String decrypt(String encryptedText, String key) {
        StringBuilder decryptedText = new StringBuilder();
        int charsetSize = CHARSET.length();

        for (int i = 0, j = 0; i < encryptedText.length(); i++) {
            char currentChar = encryptedText.charAt(i);
            int charIndex = CHARSET.indexOf(currentChar);

            if (charIndex == -1) { // Nếu ký tự không nằm trong bảng mã
                decryptedText.append(currentChar); // Giữ nguyên ký tự
                continue;
            }

            char keyChar = key.charAt(j % key.length());
            int keyIndex = CHARSET.indexOf(keyChar);

            int newIndex = (charIndex - keyIndex + charsetSize) % charsetSize; // Dịch ngược theo khóa
            decryptedText.append(CHARSET.charAt(newIndex));

            j++; // Chỉ tăng j khi ký tự thuộc bảng mã
        }

        return decryptedText.toString();
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Nhập vào văn bản và khóa
        System.out.print("Nhập văn bản cần mã hóa: ");
        String text = scanner.nextLine();

        System.out.print("Nhập khóa: ");
        String key = scanner.nextLine();

        // Mã hóa văn bản
        String encryptedText = encrypt(text, key);
        System.out.println("Văn bản sau khi mã hóa: " + encryptedText);

        // Giải mã văn bản
        String decryptedText = decrypt(encryptedText, key);
        System.out.println("Văn bản sau khi giải mã: " + decryptedText);

        scanner.close();
    }
}

