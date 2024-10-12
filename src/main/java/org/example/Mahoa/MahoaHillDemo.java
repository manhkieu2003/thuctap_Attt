package org.example.Mahoa;

import java.util.Arrays;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.HashMap;

public class MahoaHillDemo {
    static int MOD = 97;
    static int size;
    static int[][] matrix = null;
    static int[][] inverseMatrix;
    static String plaintext;
    static String ciphertext;
    private static HashMap<String, String> dic1;
    private static HashMap<String, String> dic2;
    static Scanner sc = new Scanner(System.in);

    // Hàm nhập ma trận khóa
    public static void nhapkhoa() {
        // Kích thước của ma trận (n x n)
        do {
            size = NhapSolonhon1("Nhập kích thước của ma trận khóa: ");
        } while (size <= 1);

        // Nhập ma trận khóa
        matrix = InputMatrixKey(size);
        // In ma trận khóa
        PrintMatrix(matrix, size, "khóa");
        // In định thức của ma trận khóa
        PrintDeterminant(matrix, MOD, "ma trận khóa");

        // Tìm ma trận nghịch đảo
        inverseMatrix = InverseMatrixMod(matrix, MOD);

        // Kiểm tra ma trận khả nghịch
        if (!IsInvertible(matrix, size) || !IsInvertible(inverseMatrix, size)) {
            System.out.println("Ma trận không khả nghịch, vui lòng nhập lại.\n");
            nhapkhoa();
        } else {
            // In ma trận khóa
            PrintMatrix(inverseMatrix, size, "nghịch đảo");
            // In định thức của ma trận nghịch đảo
            PrintDeterminant(inverseMatrix, MOD, "ma trận nghịch đảo");
        }
    }

    // Kiểm tra số nhập
    static int NhapSolonhon1(String message) {
        int result;
        boolean isValid;

        do {
            System.out.print(message);
            String input = sc.nextLine();
            isValid = isNumeric(input);
            result = isValid ? Integer.parseInt(input) : -1;

            if (!isValid || result <= 1) {
            }

        } while (!isValid || result <= 1);

        return result;
    }

    static boolean isNumeric(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    // Hàm nhập ma trận khóa từ người dùng
    static int[][] InputMatrixKey(int size) {
        int[][] matrix = new int[size][size];
        System.out.println("Nhập các phần tử cho ma trận khóa:");
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                matrix[i][j] = NhapSo("Phần tử [" + i + ", " + j + "]: ");
            }
        }
        return matrix;
    }

    static int NhapSo(String message) {
        int result;
        boolean isValid;

        do {
            System.out.print(message);
            String input = sc.nextLine();
            isValid = isNumeric(input);
            result = isValid ? Integer.parseInt(input) : -1;

            if (!isValid || result < 0) {
                System.out.println("Giá trị không hợp lệ. Vui lòng nhập một số nguyên dương.\n");
            }

        } while (!isValid || result < 0);

        return result;
    }

    // Hàm in ma trận
    static void PrintMatrix(int[][] matrix1, int size, String matrixName) {
        System.out.println("\nMa trận " + matrixName + ":");
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                System.out.print(matrix1[i][j] + "\t");
            }
            System.out.println();
        }
        System.out.println();
    }

    // Hàm in định thức của ma trận
    static void PrintDeterminant(int[][] matrix1, int mod, String matrixName) {
        int det = DeterminantMod(matrix1, mod);
        System.out.println("Định thức của " + matrixName + ": " + det + "\n");
    }

    // Hàm kiểm tra ma trận khả nghịch
    static boolean IsInvertible(int[][] matrix1, int n) {
        int det = DeterminantMod(matrix1, MOD);
        det = Mod(det, 97);
        return det != 0;
    }

    // Hàm tính mod, xử lý số âm
    static int Mod(int a, int mod) {
        return (a % mod + mod) % mod;
    }

    // Hàm tìm modular inverse của một số a theo mod
    static int ModInverse(int a, int mod) {
        a = a % mod;
        for (int x = 1; x < mod; x++) {
            if ((a * x) % mod == 1) return x;
        }
        return 1;
    }

    // Hàm tính định thức của ma trận mod 97
    static int DeterminantMod(int[][] matrix1, int mod) {
        int n = matrix1.length;
        if (n == 2) {
            return ((matrix1[0][0] * matrix1[1][1] - matrix1[0][1] * matrix1[1][0]) % mod + mod) % mod;
        }

        int det = 0;
        for (int col = 0; col < n; col++) {
            int sign = (col % 2 == 0) ? 1 : -1;
            det = (det + sign * matrix1[0][col] * DeterminantMod(Minor(matrix1, 0, col), mod)) % mod;
        }

        return (det + mod) % mod;
    }

    // Hàm lấy ma trận con khi loại bỏ hàng row và cột col
    static int[][] Minor(int[][] matrix, int row, int col) {
        int n = matrix.length;
        int[][] result = new int[n - 1][n - 1];
        int r = 0, c = 0;

        for (int i = 0; i < n; i++) {
            if (i == row) continue;
            c = 0;
            for (int j = 0; j < n; j++) {
                if (j == col) continue;
                result[r][c] = matrix[i][j];
                c++;
            }
            r++;
        }

        return result;
    }

    // Hàm tính ma trận nghịch đảo mod 97
    static int[][] InverseMatrixMod(int[][] matrix, int mod) {
        int n = matrix.length;
        int det = DeterminantMod(matrix, mod);
        int detInverse = ModInverse(det, mod);

        int[][] adjugate = new int[n][n];

        // Tính ma trận phụ hợp (adjugate)
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                int sign = ((i + j) % 2 == 0) ? 1 : -1;
                adjugate[j][i] = (sign * DeterminantMod(Minor(matrix, i, j), mod)) % mod;
                adjugate[j][i] = (adjugate[j][i] + mod) % mod;
            }
        }

        // Tính ma trận nghịch đảo
        int[][] inverse = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                inverse[i][j] = (adjugate[i][j] * detInverse) % mod;
            }
        }

        return inverse;
    }

    static String[] text = {
            "a","b","c","d","e","f","g","h","i","j","k","l","m","n","o","p","q","r","s","t","u","v",
            "w","x","y","z","A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V",
            "W","X","Y","Z"," ","~","`","!","@","#","$","%","^","&","*","(",")","-","_","+","=","|","\\","\"","\n","\t",
            "{","[","]","}",":",";","'","<",",",".",">","?","/","0","1","2","3","4","5","6","7","8","9"
    };

    // Hàm kiểm tra văn bản
    public static String CheckValidInput() {
        while (true) {
            System.out.print("Nhập văn bản: ");
            String input = sc.nextLine();

            boolean isValid = true;
            for (char c : input.toCharArray()) {
                if (!new String(Arrays.toString(text)).contains(String.valueOf(c))) {
                    isValid = false;
                    break;
                }
            }

            if (isValid) {
                return input;
            } else {
                System.out.println("Văn bản không hợp lệ. Vui lòng nhập lại.\n");
            }
        }
    }

    // Hàm kiểm tra văn bản có thể mã hóa theo kích thước ma trận
    public static String CheckValidPlaintext() {
        while (true) {
            plaintext = CheckValidInput();
            while (plaintext.length() % size != 0) {
                System.out.println("Độ dài văn bản không hợp lệ. Số ký tự trong văn bản phải chia hết cho kích thước ma trận.\n");
                plaintext = CheckValidInput();
            }
            return plaintext;
        }
    }

    public static void main(String[] args) {
        dic1 = new HashMap<>();
        dic2 = new HashMap<>();

        // Khởi tạo bảng mã cho str
        for (int i = 0; i < text.length; i++) {
            dic1.put(text[i], String.valueOf(i));
            dic2.put(String.valueOf(i), text[i]);
        }

        nhapkhoa(); // Nhập ma trận khóa

        // Mã hóa
        System.out.println("MÃ HÓA:");
        CheckValidPlaintext();

        // Thực hiện mã hóa
        ArrayList<String> result = Encrypt(plaintext, matrix);
        ciphertext = String.join("", result);
        System.out.println("Văn bản đã mã hóa: " + ciphertext);

        // Giải mã
        System.out.println("GIẢI MÃ:");
        CheckValidPlaintext();

        // Thực hiện giải mã
        ArrayList<String> decrypted = Decrypt(ciphertext, inverseMatrix);
        String plaintextDecrypted = String.join("", decrypted);
        System.out.println("Văn bản đã giải mã: " + plaintextDecrypted);
    }

    // Hàm mã hóa Hill Cipher
    static ArrayList<String> Encrypt(String text, int[][] matrix) {
        ArrayList<String> result = new ArrayList<>();
        int n = text.length() / size;

        for (int i = 0; i < n; i++) {
            int[] vec = new int[size];
            for (int j = 0; j < size; j++) {
                vec[j] = Integer.parseInt(dic1.get(String.valueOf(text.charAt(i * size + j))));
            }

            for (int j = 0; j < size; j++) {
                int sum = 0;
                for (int k = 0; k < size; k++) {
                    sum += matrix[j][k] * vec[k];
                }
                result.add(dic2.get(String.valueOf(sum % MOD)));
            }
        }

        return result;
    }

    // Hàm giải mã Hill Cipher
    static ArrayList<String> Decrypt(String text, int[][] matrix) {
        return Encrypt(text, matrix);
    }
}