package com.infor.alexm.docprocessor;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class SampleCompare {
    public static boolean execute(File file1, File file2) throws IOException {
        boolean result = false;
        long start = System.nanoTime();
        BufferedInputStream fis1 = new BufferedInputStream(new FileInputStream(file1));
        BufferedInputStream fis2 = new BufferedInputStream(new FileInputStream(file2));
        int b1 = 0, b2 = 0, pos = 1;
        while (b1 != -1 && b2 != -1) {
            if (b1 != b2) {
                System.out.println("Files differ at position " + pos);
            }
            pos++;
            b1 = fis1.read();
            b2 = fis2.read();
        }
        if (b1 != b2) {
            System.out.println("Files have different length");
        } else {
            System.out.println("Files are identical.");
            result = true;
        }
        fis1.close();
        fis2.close();
        long end = System.nanoTime();
        System.out.print("Execution time: " + (end - start) / 1000000 + "ms");
        return result;
    }
}
