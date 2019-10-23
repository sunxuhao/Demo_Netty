package com.sunxuhao;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Runtime rt = Runtime.getRuntime();
        Scanner sc=new Scanner(System.in);
        byte[] bytes=new byte[200*1024*1024];
        while (true){
            sc.nextLine();
            sop(rt.maxMemory() / 1024 / 1024);
            sop(rt.freeMemory() / 1024 / 1024);
            sop(rt.totalMemory() / 1024 / 1024);
        }
    }

    public static void sop(Object obj) {
        System.out.println(obj);
    }
}
