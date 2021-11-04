package com.bootdo.importdata.utils;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;

/**
 * 解析Excel文件单元格内容
 * @Date
 */
public class ExcelTool {
    public static final String EMPTY = "";
    private static final String POINT = ".";

    /**
     * 获得path的后缀名
     *
     * @param path 文件路径
     * @return 路径的后缀名
     */
    public static String getPostfix(String path) {
        if (path == null || EMPTY.equals(path.trim())) {//输入文件名地址为null,或输入文件名为空
            return EMPTY;
        }
        if (path.contains(POINT)) {
            return path.substring(path.lastIndexOf(POINT) + 1, path.length());//返回文件格式
        }
        return EMPTY;
    }

    /**
     * 解析xls和xlsx不兼容问题
     *
     * @param Pfs
     * @param workBook
     * @param file
     * @return
     */
    public static Workbook getWorkBook(POIFSFileSystem Pfs, Workbook workBook, MultipartFile file) throws IOException {
        String filename = file.getOriginalFilename();
        if (filename.endsWith("xls")) {
            Pfs = new POIFSFileSystem(file.getInputStream());
            workBook = new HSSFWorkbook(Pfs);
            return workBook;
        } else if (filename.endsWith("xlsx")) {
            try {
                workBook = new XSSFWorkbook(file.getInputStream());
                return workBook;
            } catch (IOException e) {
                return null;
            }
        } else {
            return null;
        }
    }
}
