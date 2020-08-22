package com.free.common.utils;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

@Slf4j
public class CommandUtil {

    public static String run(String command) throws IOException {
        Scanner input = null;
        String result = "";
        Process process = null;
        int exitVal;
        try {
            log.info("命令行 {} 开始执行", command);
            process = Runtime.getRuntime().exec(command);
            try {
                //等待命令执行完成
                exitVal = process.waitFor();
                if(exitVal == 0){
                    log.info("命令执行成功 {}", exitVal);
                }else{
                    log.info("命令执行失败 {}", exitVal);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            InputStream is = process.getInputStream();
            input = new Scanner(is);
            while (input.hasNextLine()) {
                result += input.nextLine() + "\n";
            }
            result = command + "\n" + result; //加上命令本身，打印出来
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            log.info("命令行 {} 结束执行", command);
            if (input != null) {
                input.close();
            }
            if (process != null) {
                process.destroy();
            }
        }
        return result;
    }

    public static String run(String[] command) throws IOException {
        Scanner input = null;
        String result = "";
        Process process = null;
        try {
            process = Runtime.getRuntime().exec(command);
            try {
                //等待命令执行完成
                process.waitFor(10, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            InputStream is = process.getInputStream();
            input = new Scanner(is);
            while (input.hasNextLine()) {
                result += input.nextLine() + "\n";
            }
            result = command + "\n" + result; //加上命令本身，打印出来
        } finally {
            if (input != null) {
                input.close();
            }
            if (process != null) {
                process.destroy();
            }
        }
        return result;
    }

}
