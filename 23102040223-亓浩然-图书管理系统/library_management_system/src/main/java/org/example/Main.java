package org.example;

import org.example.backup.DataBackupService;
import org.example.ui.ConsoleMenu;

/**
 * 图书管理系统的主类
 * 负责启动系统和初始化必要的服务
 */
public class Main {
    /**
     * 程序入口点
     * @param args 命令行参数
     */
    public static void main(String[] args) {
        // 初始化并启动数据备份服务
        DataBackupService backupService = new DataBackupService();
        backupService.startBackupScheduler();
        
        // 立即执行一次备份
        try {
            backupService.backup();
        } catch (Exception e) {
            System.err.println("初始备份失败: " + e.getMessage());
        }

        // 启动控制台菜单界面
        ConsoleMenu menu = new ConsoleMenu();
        menu.start();

        // 程序结束前关闭备份服务
        backupService.shutdown();
    }
}