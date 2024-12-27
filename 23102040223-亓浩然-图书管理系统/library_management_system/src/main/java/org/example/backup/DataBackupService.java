package org.example.backup;

import org.example.dao.BookCategoryDao;
import org.example.dao.BookDao;
import org.example.dao.impl.BookCategoryDaoImpl;
import org.example.dao.impl.BookDaoImpl;
import org.example.entity.*;
import org.example.service.BookService;
import org.example.service.impl.BookServiceImpl;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @desc 图书数据备份服务
 * 负责定期备份图书数据和数据恢复功能
 */
public class DataBackupService {
    private final BookService bookService = new BookServiceImpl();
    private final BookDao bookDao = new BookDaoImpl();
    private final BookCategoryDao categoryDao = new BookCategoryDaoImpl();
    private final String backupDir = "backup";
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    public String getBackupDir() {
        return this.backupDir;
    }
    public void startBackupScheduler() {
        // 创建备份目录
        File backupDirectory = new File(backupDir);
        if (!backupDirectory.exists()) {
            if (!backupDirectory.mkdirs()) {
                throw new RuntimeException("创建备份目录失败: " + backupDir);
            }
        }
        
        // 每24小时执行一次备份
        scheduler.scheduleAtFixedRate(this::backup, 0, 24, TimeUnit.HOURS);
    }

    public void backup() {
        try {
            List<BaseBook> books = bookService.getAllBooks();
            if (books.isEmpty()) {
                System.out.println("没有图书数据需要备份");
                return;
            }

            Map<Long, String> categoryMap = categoryDao.findAll().stream()
                    .collect(Collectors.toMap(BookCategory::getId, BookCategory::getName));
            
            String filename = backupDir + "/backup_" + 
                    LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")) + ".txt";
            
            try (PrintWriter writer = new PrintWriter(new FileWriter(filename))) {
                for (BaseBook book : books) {
                    writer.println("ID: " + book.getId());
                    writer.println("标题: " + book.getTitle());
                    writer.println("作者: " + book.getAuthor());
                    writer.println("ISBN: " + book.getIsbn());
                    writer.println("出版日期: " + book.getPublicationDate());
                    writer.println("分类ID: " + book.getCategoryId());
                    writer.println("分类名称: " + categoryMap.getOrDefault(book.getCategoryId(), "未知分类"));
                    
                    // 根据图书类型输出特定信息
                    if (book instanceof ComputerBook computerBook) {
                        writer.println("编程语言: " + computerBook.getProgrammingLanguage());
                        writer.println("技术领域: " + computerBook.getTechnicalField());
                    } else if (book instanceof LiteratureBook literatureBook) {
                        writer.println("文学流派: " + literatureBook.getGenre());
                    } else if (book instanceof HistoryBook historyBook) {
                        writer.println("历史时期: " + historyBook.getHistoricalPeriod());
                        writer.println("地理区域: " + historyBook.getGeographicalRegion());
                    } else if (book instanceof MysteryBook mysteryBook) {
                        writer.println("子类型: " + mysteryBook.getSubgenre());
                        writer.println("侦探类型: " + mysteryBook.getDetectiveType());
                    } else if (book instanceof ArtsBook artsBook) {
                        writer.println("艺术时期: " + artsBook.getArtPeriod());
                    } else if (book instanceof BusinessBook businessBook) {
                        writer.println("商业领域: " + businessBook.getBusinessField());
                    } else if (book instanceof MedicineBook medicineBook) {
                        writer.println("医学专业: " + medicineBook.getMedicalSpecialty());
                        writer.println("目标读者: " + medicineBook.getTargetAudience());
                        writer.println("研究内容: " + medicineBook.getResearchContent());
                    } else if (book instanceof RomanceBook romanceBook) {
                        writer.println("爱情类型: " + romanceBook.getRomanceType());
                        writer.println("故事背景: " + romanceBook.getSettingPeriod());
                    } else if (book instanceof ScienceBook scienceBook) {
                        writer.println("科学领域: " + scienceBook.getScientificField());
                        writer.println("研究水平: " + scienceBook.getResearchLevel());
                    } else if (book instanceof TravelBook travelBook) {
                        writer.println("目的地: " + travelBook.getDestination());
                    }
                    writer.println("-------------------");
                }
            }
        } catch (Exception e) {
            System.err.println("执行备份操作失败: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void shutdown() {
        scheduler.shutdown();
    }

    public void restoreFromBackup(String backupFile) throws Exception {
        try (BufferedReader reader = new BufferedReader(new FileReader(backupFile))) {
            BaseBook currentBook = null;
            BookCategory bookCategory = new BookCategory();

            String line;
            String title = null;    
            String author = null;   
            String isbn = null;     
            LocalDate publicationDate = null;   
            String category;        
            Long categoryId = null; 

            while ((line = reader.readLine()) != null) {
                if (line.equals("-------------------")) {
                    if (currentBook != null) {
                        try {
                            // 检查ISBN是否已存在
                            if (!bookDao.findByIsbn(isbn).isPresent()) {
                                currentBook.saveToAttribute();
                                bookDao.insert(currentBook);
                                System.out.println("成功恢复图书: " + currentBook.getTitle());
                            } else {
                                System.out.println("跳过已存在的图书: " + currentBook.getTitle() + " (ISBN: " + isbn + ")");
                            }
                        } catch (Exception e) {
                            System.err.println("恢复图书失败: " + currentBook.getTitle() + ", 原因: " + e.getMessage());
                        }
                    }
                    currentBook = null;
                    continue;
                }

                String[] parts = line.split(": ", 2);
                if (parts.length != 2) continue;

                String key = parts[0];
                String value = parts[1];

                switch (key) {
                    case "标题" -> title = value;
                    case "作者" -> author = value;
                    case "ISBN" -> isbn = value;
                    case "出版日期" -> publicationDate = LocalDate.parse(value);
                    case "分类名称" -> {
                        category = value;
                        bookCategory.setName(category);
                        // 检查分类是否已存在
                        if (!categoryDao.findByName(category).isPresent()) {
                            categoryDao.insert(bookCategory);
                            System.out.println("成功恢复分类: " + category);
                        } else {
                            System.out.println("跳过已存在的分类: " + category);
                        }
                    }
                    case "分类ID" -> {
                        categoryId = Long.parseLong(value.trim());
                        currentBook = createBookByCategory(categoryId);
                        bookCategory.setId(categoryId);
                        currentBook.setAuthor(author);
                        currentBook.setTitle(title);
                        currentBook.setIsbn(isbn);
                        currentBook.setPublicationDate(publicationDate);
                        currentBook.setCategoryId(categoryId);
                    }
                    default -> {
                        if (currentBook != null) {
                            parseBookAttribute(currentBook, line);
                        }
                    }
                }
            }

            // 处理最后一条记录
            if (currentBook != null) {
                try {
                    if (!bookDao.findByIsbn(isbn).isPresent()) {
                        currentBook.saveToAttribute();
                        bookDao.insert(currentBook);
                        System.out.println("成功恢复图书: " + currentBook.getTitle());
                    } else {
                        System.out.println("跳过已存在的图书: " + currentBook.getTitle() + " (ISBN: " + isbn + ")");
                    }
                } catch (Exception e) {
                    System.err.println("恢复图书失败: " + currentBook.getTitle() + ", 原因: " + e.getMessage());
                }
            }
        }
    }

    private BaseBook createBookByCategory(Long categoryId) {
        return switch (categoryId.intValue()) {
            case 1 -> new ScienceBook();
            case 2 -> new RomanceBook();
            case 3 -> new MysteryBook();
            case 4 -> new HistoryBook();
            case 5 -> new ComputerBook();
            case 6 -> new MedicineBook();
            case 7 -> new LiteratureBook();
            case 8 -> new TravelBook();
            case 9 -> new BusinessBook();
            case 10 -> new ArtsBook();
            default -> throw new IllegalArgumentException("未知的图书类型ID: " + categoryId);
        };
    }

    private void parseBookAttribute(BaseBook book, String line) {
        String[] parts = line.split(": ", 2);
        if (parts.length != 2) return;

        String key = parts[0];
        String value = parts[1];

        try {
            switch (key) {
                case "编程语言" -> {
                    if (book instanceof ComputerBook computerBook) {
                        computerBook.setProgrammingLanguage(value);
                    }
                }
                case "技术领域" -> {
                    if (book instanceof ComputerBook computerBook) {
                        computerBook.setTechnicalField(value);
                    }
                }
                case "文学流派" -> {
                    if (book instanceof LiteratureBook literatureBook) {
                        literatureBook.setGenre(value);
                    }
                }
                case "历史时期" -> {
                    if (book instanceof HistoryBook historyBook) {
                        historyBook.setHistoricalPeriod(value);
                    }
                }
                case "地理区域" -> {
                    if (book instanceof HistoryBook historyBook) {
                        historyBook.setGeographicalRegion(value);
                    }
                }
                case "子类型" -> {
                    if (book instanceof MysteryBook mysteryBook) {
                        mysteryBook.setSubgenre(value);
                    }
                }
                case "侦探类型" -> {
                    if (book instanceof MysteryBook mysteryBook) {
                        mysteryBook.setDetectiveType(value);
                    }
                }
                case "艺术时期" -> {
                    if (book instanceof ArtsBook artsBook) {
                        artsBook.setArtPeriod(value);
                    }
                }
                case "商业领域" -> {
                    if (book instanceof BusinessBook businessBook) {
                        businessBook.setBusinessField(value);
                    }
                }
                case "医学专业" -> {
                    if (book instanceof MedicineBook medicineBook) {
                        medicineBook.setMedicalSpecialty(value);
                    }
                }
                case "目标读者" -> {
                    if (book instanceof MedicineBook medicineBook) {
                        medicineBook.setTargetAudience(value);
                    }
                }
                case "研究内容" -> {
                    if (book instanceof MedicineBook medicineBook) {
                        medicineBook.setResearchContent(value);
                    }
                }
                case "爱情类型" -> {
                    if (book instanceof RomanceBook romanceBook) {
                        romanceBook.setRomanceType(value);
                    }
                }
                case "故事背景" -> {
                    if (book instanceof RomanceBook romanceBook) {
                        romanceBook.setSettingPeriod(value);
                    }
                }
                case "科学领域" -> {
                    if (book instanceof ScienceBook scienceBook) {
                        scienceBook.setScientificField(value);
                    }
                }
                case "研究水平" -> {
                    if (book instanceof ScienceBook scienceBook) {
                        scienceBook.setResearchLevel(value);
                    }
                }
                case "目的地" -> {
                    if (book instanceof TravelBook travelBook) {
                        travelBook.setDestination(value);
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("解析属性失败: " + key + " = " + value + ", 原因: " + e.getMessage());
        }
    }

    // 列出所有备份文件
    public List<String> listBackupFiles() {
        File dir = new File(backupDir);
        File[] files = dir.listFiles((d, name) -> name.startsWith("backup_") && name.endsWith(".txt"));
        if (files == null) return List.of();
        
        return List.of(files)
                .stream()
                .map(File::getName)
                .sorted()
                .collect(Collectors.toList());
    }
} 