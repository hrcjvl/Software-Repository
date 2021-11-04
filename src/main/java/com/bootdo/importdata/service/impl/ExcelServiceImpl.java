package com.bootdo.importdata.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.bootdo.importdata.entity.ProjectItem;
import com.bootdo.importdata.entity.Result;
import com.bootdo.importdata.dao.ExcelMapper;
import com.bootdo.importdata.service.ExcelService;
import com.bootdo.importdata.utils.ExcelTool;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class ExcelServiceImpl extends ServiceImpl<ExcelMapper, ProjectItem> implements ExcelService {

    private NumberFormat numberFormat = null;
    @Autowired
    private ExcelMapper excelMapper;

    @Override
    public int count(Map<String, Object> map) {
        return excelMapper.count(map);
    }

    @Override
    public List<ProjectItem> show(Map<String,Object> map){
        return excelMapper.show(map);
    }

    @Override
    public ProjectItem get(Long id){return excelMapper.get(id);}

    @Override
    public int update(ProjectItem dict){
        return excelMapper.update(dict);
    }

    @Override
    public int save1(ProjectItem dict){return excelMapper.save1(dict);}

    @Override
    public Result importProject(MultipartFile file) {
        // 解析Excel数据+
        System.out.println("读取数据");
        Result r = readDataFromExcel(file);
        List<ProjectItem> items = (List) r.getData();
        if (items == null || items.size() <= 0) {
            return Result.error("没有数据！！！");
        }

        //查询之前是否存在项目清单项
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("is_deleted", 0);
        //System.out.println("48");
       /* List<ProjectItem> beforeItems = baseMapper.selectList(wrapper);
        System.out.println("50");
        //如果存在，判断两个集合中是否有相同的项目序号
        if (beforeItems != null && beforeItems.size() > 0) {
            List<String> beforeOrderNumber = beforeItems.stream().map(ProjectItem::getOrderNumber).collect(Collectors.toList());
            List<String> afterOrderNumber = items.stream().map(ProjectItem::getOrderNumber).collect(Collectors.toList());

            for (String vo : beforeOrderNumber) {
                if (afterOrderNumber.contains(vo)) {
                    return Result.error(vo + "：该项目序号已经存在");
                }
            }
        }
*/
        // 如果没有序号相等，则插入数据表格中的数据，然后重新读取
        for (ProjectItem item : items) {
            // 保存数据
            int insert = baseMapper.insertProjectItem(item.getFailure_number(), item.getTime_to_fail());
            if (insert <= 0) {
                return Result.error("导入失败");
            }
        }
        return Result.success("导入成功");
    }

    /**
     * 解析Excel数据
     *
     * @param file 文件
     * @return
     */
    public Result readDataFromExcel(MultipartFile file) {
        POIFSFileSystem pfs = null;
        System.out.println("读取数据");
        Workbook workbook = null;
        try {
            // 解析xls和xlsx不兼容问题
            workbook = ExcelTool.getWorkBook(null, null, file);
        } catch (IOException e) {
            e.printStackTrace();
            return Result.error("模板保存异常。");
        }
        if (workbook == null) {
            return Result.error("请使用模板上传文件");
        }
        // 判断有记录的列数
        if (workbook.getSheetAt(0).getRow(0).getPhysicalNumberOfCells() != 2) {
            return Result.error("请使用类型所对应的模板");
        }

        numberFormat = NumberFormat.getNumberInstance();
        //System.out.println("读取数据");
        List<ProjectItem> list = new ArrayList<>();
        // 获取表格第一个sheet的内容
        Sheet sheetAt = workbook.getSheetAt(0);
        // 获得sheet总行数
        int lastRowNum = sheetAt.getLastRowNum();
        if (lastRowNum < 1) {
            //System.out.println("读取数据\n");
            //System.out.println("读取数据");
            return Result.error("数据错误");
        }
        System.out.println(lastRowNum);
        for (int i = 1; i <= lastRowNum; i++) {// 开始读取,不读取表头所以从第二行开始
            // 获取每一行
            System.out.println("有执行");
            Row row = sheetAt.getRow(i);
            // 行为空不读取
            if (row == null)//行空
                continue;
            Cell cell = row.getCell(0);
            //列为空不读取
            if (cell == null || StringUtils.isEmpty(convertData(cell)))
                continue;

            // 创建对象封装行数据
            ProjectItem projectItem = new ProjectItem();
            // 创建一个集合根据下标来确定每个单元格对应对象的什么属性
            List<String> rowList = new ArrayList<>();
            //添加数据
            for (int j = 0; j < 2; j++) {
                Cell cellOne = row.getCell(j);
                try {
                    String item = convertData(cellOne);
                    rowList.add(item);
                } catch (Exception e) {
                    System.out.println("-------------------Err-----------------------");
                    System.out.println(i + "行" + j + "列数据转换出现异常");
                    rowList.add("");
                }
            }
            //规避行数数据后几行为空
            if (rowList.size() < 2) {
                for (int k = 0; k < 2 - rowList.size(); k++) {
                    rowList.add("");
                }
            }
            // 添加数据
            //System.out.println("数据输出");
            //System.out.println(rowList.get(0));
            //System.out.println(rowList.get(1));
            projectItem.setFailure_number(rowList.get(0).trim());
            projectItem.setTime_to_fail(rowList.get(1).trim());
            list.add(projectItem);
        }
        return Result.success("解析成功", list);
    }

    /**
     * 表格数据转换
     *
     * @param cell
     * @return
     */
    public String convertData(Cell cell) {
        String str = "";

        switch (cell.getCellTypeEnum()) {
            case NUMERIC:
                //判断是否是整数
                str = numberFormat.format(cell.getNumericCellValue());
                break;
            case STRING:
                str = cell.getStringCellValue();
                break;
            case _NONE:
                str = "";
                break;
            case BLANK:
                str = "";
                break;
            case FORMULA:
                try {
                    str = String.valueOf(cell.getNumericCellValue());
                } catch (IllegalArgumentException e) {
                    str = String.valueOf(cell.getRichStringCellValue());
                }
                break;
            default:
                str = "";
        }
        return str;
    }
}

