package org.example.Mahoa;

import java.util.Scanner;

public class MaHoaHill {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int n;

        // Nhập kích thước ma trận
        while (true) {
            System.out.print("Nhập kích thước ma trận (n x n): ");
            n = scanner.nextInt();
            if (n > 1) break;
            else System.out.println("Kích thước không hợp lệ. Vui lòng nhập lại.");
        }

        // Nhập ma trận khóa với kiểm tra khả nghịch
        int[][] a;
        while (true) {
            a = new int[n][n];
            System.out.println("Nhập ma trận khóa (n x n): ");
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    System.out.printf("a[%d,%d] = ", i, j);
                    a[i][j] = scanner.nextInt();
                }
            }

            // Kiểm tra tính khả nghịch
            if (isInvertible(a, n)) {
                break;
            } else {
                System.out.println("Ma trận không khả nghịch. Vui lòng nhập lại.");
            }
        }

        // Nhập văn bản cần mã hóa và kiểm tra văn bản
        scanner.nextLine(); // Consume newline left-over
        String banro;
        while (true) {
            System.out.println("Nhập văn bản cần mã hóa (97 ký tự có thể): ");
            banro = scanner.nextLine();
            if (isValidText(banro)) {
                break;
            } else {
                System.out.println("Văn bản không hợp lệ. Vui lòng chỉ nhập các ký tự hợp lệ.");
            }
        }

        // Thêm padding nếu cần
        if (banro.length() % n != 0) {
            banro = paddingText(banro, n);
        }

        // Mã hóa
        String banma = encrypt(banro, a, n);
        System.out.println("Văn bản đã mã hóa: " + banma);

        // Giải mã
        int[][] adao = inverseMatrix(a, n);
        String decryptedText = decrypt(banma, adao, n);
        System.out.println("Văn bản đã giải mã: " + decryptedText);

        scanner.close();
    }

    // Kiểm tra ma trận khả nghịch
    static boolean isInvertible(int[][] matrix, int n) {
        int det = determinant(matrix, n) % 97;
        return det != 0 && modInverse(det, 97) != 1;
    }

    // Kiểm tra văn bản hợp lệ
    static boolean isValidText(String text) {
        for (char c : text.toCharArray()) {
            if (c < 32 || c > 128) return false; // Chỉ nhận các ký tự ASCII từ 32 đến 128
        }
        return true;
    }

    // Phương thức mã hóa văn bản
    static String encrypt(String plaintext, int[][] keyMatrix, int n) {
        int[] vector = new int[n];
        StringBuilder encryptedText = new StringBuilder();

        for (int i = 0; i < plaintext.length(); i += n) {
            for (int j = 0; j < n; j++) {
                vector[j] = plaintext.charAt(i + j) - 32; // Chuyển ký tự về khoảng 0-96
            }

            for (int x = 0; x < n; x++) {
                int sum = 0;
                for (int y = 0; y < n; y++) {
                    sum += keyMatrix[x][y] * vector[y];
                }
                encryptedText.append((char) ((sum % 97) + 32)); // Chuyển về ký tự ASCII
            }
        }
        return encryptedText.toString();
    }

    // Phương thức giải mã văn bản
    static String decrypt(String ciphertext, int[][] inverseKeyMatrix, int n) {
        int[] vector = new int[n];
        StringBuilder decryptedText = new StringBuilder();

        for (int i = 0; i < ciphertext.length(); i += n) {
            for (int j = 0; j < n; j++) {
                vector[j] = ciphertext.charAt(i + j) - 32; // Chuyển ký tự về khoảng 0-96
            }

            for (int x = 0; x < n; x++) {
                int sum = 0;
                for (int y = 0; y < n; y++) {
                    sum += inverseKeyMatrix[x][y] * vector[y];
                }
                sum = (sum % 97 + 97) % 97; // Điều chỉnh để sum luôn dương
                decryptedText.append((char) (sum + 32)); // Chuyển về ký tự ASCII
            }
        }
        return decryptedText.toString();
    }

    // Tính ma trận nghịch đảo trong modulo 97
    static int[][] inverseMatrix(int[][] matrix, int n) {
        int[][] inverseMatrix = new int[n][n];
        int det = determinant(matrix, n);
        int detInverse = modInverse(det % 97, 97);

        int[][] adjMatrix = adjugateMatrix(matrix, n);

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                inverseMatrix[i][j] = (adjMatrix[i][j] * detInverse) % 97;
                if (inverseMatrix[i][j] < 0)
                    inverseMatrix[i][j] += 97;
            }
        }

        return inverseMatrix;
    }

    // Tính định thức của ma trận
    static int determinant(int[][] a, int n) {
        if (n == 1) return a[0][0];
        if (n == 2) return (a[0][0] * a[1][1] - a[0][1] * a[1][0]);

        int det = 0;
        int sign = 1;
        int[][] subMatrix = new int[n][n];

        for (int x = 0; x < n; x++) {
            getSubMatrix(a, subMatrix, 0, x, n);
            det += sign * a[0][x] * determinant(subMatrix, n - 1);
            sign = -sign;
        }

        return det;
    }

    // Lấy ma trận con
    static void getSubMatrix(int[][] matrix, int[][] subMatrix, int p, int q, int n) {
        int i = 0, j = 0;
        for (int row = 0; row < n; row++) {
            for (int col = 0; col < n; col++) {
                if (row != p && col != q) {
                    subMatrix[i][j++] = matrix[row][col];
                    if (j == n - 1) {
                        j = 0;
                        i++;
                    }
                }
            }
        }
    }

    // Tính ma trận liên hợp
    static int[][] adjugateMatrix(int[][] matrix, int n) {
        int[][] adjMatrix = new int[n][n];
        if (n == 1) {
            adjMatrix[0][0] = 1;
            return adjMatrix;
        }

        int sign;
        int[][] subMatrix = new int[n][n];

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                getSubMatrix(matrix, subMatrix, i, j, n);
                sign = ((i + j) % 2 == 0) ? 1 : -1;
                adjMatrix[j][i] = sign * determinant(subMatrix, n - 1);
            }
        }
        return adjMatrix;
    }

    // Tính nghịch đảo modulo
    static int modInverse(int a, int m) {
        a = a % m;
        for (int x = 1; x < m; x++) {
            if ((a * x) % m == 1)
                return x;
        }
        return 1;
    }

    // Thêm padding cho văn bản nếu cần
    static String paddingText(String text, int n) {
        int paddingSize = n - (text.length() % n);
        return text + "X".repeat(paddingSize); // Thêm 'X' vào văn bản
    }
}