package org.example.ui;

import org.example.backup.DataBackupService;
import org.example.entity.*;
import org.example.service.AuthService;
import org.example.service.BookService;
import org.example.service.impl.AuthServiceImpl;
import org.example.service.impl.BookServiceImpl;

import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

public class ConsoleMenu {
    private final Scanner scanner = new Scanner(System.in);
    private final AuthService authService = new AuthServiceImpl();
    private final BookService bookService = new BookServiceImpl();

    private final DataBackupService backupService = new DataBackupService();
    private User currentUser = null;

    public void start() {
        while (true) {
            displayMenu();
            int choice;
            try {
                choice = scanner.nextInt();
                scanner.nextLine(); // 清除换行符
            } catch (Exception e) {
                System.out.println("无效的选择，请重新输入您的选择！");
                scanner.nextLine(); // 清除错误输入
                continue;
            }

            // 未登录用户只能执行1,2,8这几个选项
            if (currentUser == null && !(choice == 1 || choice == 2 || choice == 8)) {
                System.out.println("请先登录后再进行操作！");
                continue;
            }

            switch (choice) {
                case 1 -> {
                    if (currentUser != null) {
                        System.out.println("您已登录，请先退出登录！");
                    } else {
                        register();
                    }
                }
                case 2 -> {
                    if (currentUser != null) {
                        System.out.println("您已登录，用户名：" + currentUser.getUsername());
                    } else {
                        login();
                    }
                }
                case 3 -> viewBooks();
                case 4 -> searchBooks();
                case 5 -> {
                    if (isAdmin()) {
                        addBook();
                    } else {
                        System.out.println("权限不足，仅管理员可执行此操作！");
                    }
                }
                case 6 -> {
                    if (isAdmin()) {
                        deleteBook();
                    } else {
                        System.out.println("权限不足，仅管理员可执行此操作！");
                    }
                }
                case 7 -> {
                    if (currentUser == null) {
                        System.out.println("您尚未登录！");
                    } else {
                        logout();
                    }
                }
                case 8 -> {
                    System.out.println("感谢使用图书管理系统，再见！");
                    return;
                }
                case 9 -> {
                    if (isAdmin()) {
                        restoreFromBackup();
                    } else {
                        System.out.println("权限不足，仅管理员可执行此操作！");
                    }
                }
                default -> System.out.println("无效的选择，请重新输入您的选择！");
            }
        }
    }

    private void displayMenu() {
        System.out.println("\n=== 欢迎来到图书管理系统 ===");
        if (currentUser != null) {
            System.out.println("当前用户：" + currentUser.getUsername() + 
                    " (" + (currentUser.getRole() == UserRole.admin ? "管理员" : "普通用户") + ")");
        }
        
        // 显示基本选项
        System.out.println("1. 注册");
        System.out.println("2. 登录");

        boolean showBackupMenu = false;
        // 只有登录后才显示的功能选项
        if (currentUser != null) {
            System.out.println("3. 查看图书");
            System.out.println("4. 搜索图书");
            // 管理员特有功能
            if (currentUser.getRole() == UserRole.admin) {
                System.out.println("5. 添加图书（仅管理员）");
                System.out.println("6. 删除图书（仅管理员）");
                showBackupMenu = true;

            }
            System.out.println("7. 退出登录");
        }
        
        System.out.println("8. 退出图书管理系统");
        if (showBackupMenu) {
            System.out.println("9. 从备份恢复数据（仅管理员）");
        }
        System.out.print("请输入您的选择: ");
    }

    private boolean isAdmin() {
        return currentUser != null && currentUser.getRole() == UserRole.admin;
    }

    // 具体的操作方法实现...
    private void register() {
        System.out.println("\n=== 用户注册 ===");
        System.out.print("请输入用户名: ");
        String username = scanner.nextLine();
        System.out.print("请输入密码: ");
        String password = scanner.nextLine();
        
        System.out.println("请选择用户角色：");
        System.out.println("1. 普通用户(user)");
        System.out.println("2. 管理员(admin)");
        System.out.print("请输入选择(1-2): ");
        int roleChoice = scanner.nextInt();
        scanner.nextLine(); // 清除换行符
        
        UserRole role;
        try {
            if (roleChoice == 1) {
                role = UserRole.user;
            } else if (roleChoice == 2) {
                role = UserRole.admin;
            } else {
                throw new IllegalArgumentException("无效的角色选择！");
            }
            
            authService.register(username, password, role);
            System.out.println("注册成功！");
            // 注册成功后自动登录并显示完整菜单
            currentUser = authService.login(username, password);
        } catch (Exception e) {
            System.out.println("注册失败：" + e.getMessage());
        }
    }

    private void login() {
        System.out.println("\n=== 用户登录 ===");
        System.out.print("请输入用户名: ");
        String username = scanner.nextLine();
        System.out.print("请输入密码: ");
        String password = scanner.nextLine();
        
        try {
            currentUser = authService.login(username, password);
            System.out.println("登录成功！欢迎用户 " + currentUser.getUsername());
        } catch (Exception e) {
            System.out.println("登录失败：" + e.getMessage());
        }
    }

    private void logout() {
        if (currentUser != null) {
            currentUser = null;
            System.out.println("已成功退出登录！");
        } else {
            System.out.println("您尚未登录！");
        }
    }

    // 其他菜单方法的实现...

    private void viewBooks() {
        System.out.println("\n=== 图书列表 ===");
        List<BaseBook> books = bookService.getAllBooks();
        if (books.isEmpty()) {
            System.out.println("暂无图书信息");
            return;
        }
        
        for (BaseBook book : books) {
            book.displayInfo();
        }
    }

    private void searchBooks() {
        System.out.println("\n=== 搜索图书 ===");
        System.out.print("请输入搜索关键词（书名，作者，isbn）: ");
        String keyword = scanner.nextLine();
        
        List<BaseBook> books = bookService.searchBooks(keyword);
        if (books.isEmpty()) {
            System.out.println("未找到匹配的图书");
            return;
        }
        
        System.out.println("\n搜索结果：");
        for (BaseBook book : books) {
            book.displayInfo();
        }
    }

    private void addBook() {
        System.out.println("\n=== 添加图书 ===");
        System.out.println("请选择图书类型：");
        System.out.println("1. 科学类");
        System.out.println("2. 爱情类");
        System.out.println("3. 悬疑类");
        System.out.println("4. 历史类");
        System.out.println("5. 计算机类");
        System.out.println("6. 医学类");
        System.out.println("7. 文学类");
        System.out.println("8. 旅行类");
        System.out.println("9. 商业类");
        System.out.println("10. 艺术类");
        System.out.print("请输入选择(1-10): ");
        
        int type = scanner.nextInt();
        scanner.nextLine(); // 清除换行符
        
        BaseBook book;
        switch (type) {
            case 1 -> book = new ScienceBook();
            case 2 -> book = new RomanceBook();
            case 3 -> book = new MysteryBook();
            case 4 -> book = new HistoryBook();
            case 5 -> book = new ComputerBook();
            case 6 -> book = new MedicineBook();
            case 7 -> book = new LiteratureBook();
            case 8 -> book = new TravelBook();
            case 9 -> book = new BusinessBook();
            case 10 -> book = new ArtsBook();
            default -> {
                System.out.println("无效的图书类型！");
                return;
            }
        }
        
        // 输入基本信息
        System.out.print("请输入书名: ");
        book.setTitle(scanner.nextLine());
        
        System.out.print("请输入作者: ");
        book.setAuthor(scanner.nextLine());
        
        System.out.print("请输入ISBN: ");
        book.setIsbn(scanner.nextLine());
        
        System.out.print("请输入出版日期(yyyy-MM-dd): ");
        book.setPublicationDate(LocalDate.parse(scanner.nextLine()));
        
        book.setCategoryId((long) type);
        
        // 输入特定类型的信息
        if (book instanceof ComputerBook computerBook) {
            System.out.print("请输入编程语言: ");
            computerBook.setProgrammingLanguage(scanner.nextLine());
            System.out.print("请输入技术领域: ");
            computerBook.setTechnicalField(scanner.nextLine());
        } else if (book instanceof LiteratureBook literatureBook) {
            System.out.print("请输入文学流派: ");
            literatureBook.setGenre(scanner.nextLine());

        } else if (book instanceof HistoryBook historyBook) {
            System.out.print("请输入历史时期: ");
            historyBook.setHistoricalPeriod(scanner.nextLine());
            System.out.print("请输入地理区域: ");
            historyBook.setGeographicalRegion(scanner.nextLine());
        } else if (book instanceof MysteryBook mysteryBook) {
            System.out.print("请输入子类型: ");
            mysteryBook.setSubgenre(scanner.nextLine());
            System.out.print("请输入侦探类型: ");
            mysteryBook.setDetectiveType(scanner.nextLine());
        } else if (book instanceof ArtsBook artsBook) {
            System.out.print("请输入艺术时期: ");
            artsBook.setArtPeriod(scanner.nextLine());
        } else if (book instanceof BusinessBook businessBook) {
            System.out.print("请输入商业领域: ");
            businessBook.setBusinessField(scanner.nextLine());
        } else if (book instanceof MedicineBook medicineBook) {
            System.out.print("请输入医学领域: ");
            System.out.print("请输入实践等级: ");
        } else if (book instanceof RomanceBook romanceBook) {
            System.out.print("请输入爱情类型: ");
            romanceBook.setRomanceType(scanner.nextLine());
            System.out.print("请输入故事背景时期: ");
            romanceBook.setSettingPeriod(scanner.nextLine());
        } else if (book instanceof ScienceBook scienceBook) {
            System.out.print("请输入科学领域: ");
            scienceBook.setScientificField(scanner.nextLine());
            System.out.print("请输入研究水平: ");
            scienceBook.setResearchLevel(scanner.nextLine());
        } else if (book instanceof TravelBook travelBook) {
            System.out.print("请输入目的地: ");
            travelBook.setDestination(scanner.nextLine());
        }
        
        try {
            bookService.addBook(book);
            System.out.println("图书添加成功！");
        } catch (Exception e) {
            System.out.println("图书添加失败：" + e.getMessage());
        }
    }

    private void deleteBook() {
        System.out.println("\n=== 删除图书 ===");
        System.out.print("请输入要删除的图书ISBN: ");
        String isbn = scanner.nextLine();
        
        try {
            bookService.deleteByIsbn(isbn);
            System.out.println("图书删除成功！");
        } catch (Exception e) {
            System.out.println("图书删除失败：" + e.getMessage());
        }
    }

    private void restoreFromBackup() {
        System.out.println("\n=== 从备份恢复数据 ===");
        List<String> backupFiles = backupService.listBackupFiles();
        
        if (backupFiles.isEmpty()) {
            System.out.println("没有找到备份文件！");
            return;
        }

        System.out.println("可用的备份文件：");
        for (int i = 0; i < backupFiles.size(); i++) {
            System.out.println((i + 1) + ". " + backupFiles.get(i));
        }

        System.out.print("请选择要恢复的备份文件编号(1-" + backupFiles.size() + "): ");
        try {
            int choice = scanner.nextInt();
            scanner.nextLine(); // 清除换行符
            
            if (choice < 1 || choice > backupFiles.size()) {
                System.out.println("无效的选择！");
                return;
            }

            String selectedFile = backupService.getBackupDir() + "/" + backupFiles.get(choice - 1);
            System.out.print("确定要从备份文件恢复数据吗？这将覆盖现有数据(y/n): ");
            String confirm = scanner.nextLine();
            
            if ("y".equalsIgnoreCase(confirm)) {
                backupService.restoreFromBackup(selectedFile);
                System.out.println("数据恢复成功！");
            } else {
                System.out.println("已取消恢复操作。");
            }
        } catch (Exception e) {
            System.out.println("恢复失败：" + e.getMessage());
        }
    }
} 