package com.LawnWatering;

import javax.swing.JFrame;

public class WindowManager {
    public static void waitUntilClosed(JFrame window) {
        while (window.isVisible()) {
            try {
                System.out.println("I'm waiting...");
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                // Ignore
            }
        }
    }
}
