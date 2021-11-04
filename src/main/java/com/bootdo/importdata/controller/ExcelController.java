package com.bootdo.importdata.controller;

import com.bootdo.common.config.Constant;
import com.bootdo.common.controller.BaseController;
import com.bootdo.common.domain.DictDO;
import com.bootdo.common.utils.Query;
import com.bootdo.common.utils.R;
import com.bootdo.importdata.entity.ProjectItem;
import com.bootdo.importdata.entity.Result;
import com.bootdo.importdata.service.ExcelService;
import com.bootdo.importdata.utils.ExcelTool;
import com.bootdo.importdata.utils.FileUtils;
import com.bootdo.importdata.utils.PageUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;


/**
 * 接口
 */
@Controller
@RequestMapping("/common/file")
public class ExcelController extends BaseController {

    Logger logger = LoggerFactory.getLogger(ExcelController.class);

    @Autowired
    private ExcelService excelService;

    @GetMapping("")
    @ResponseBody
    public PageUtils show(@RequestParam Map<String,Object> params){
        //System.out.println("输出列表");
        Query query1 = new Query(params);
        List<ProjectItem> dictList = excelService.show(query1);
        System.out.println(dictList);
        int total = excelService.count(query1);
        PageUtils pageUtils1 = new PageUtils(dictList, total);
        return pageUtils1;
    }

    @PostMapping("/import")
    public String importProject(MultipartFile file) {
        String postfix = ExcelTool.getPostfix(file.getOriginalFilename());

        if (!"xlsx".equals(postfix) && !"xls".equals(postfix)) {
            //return Result.error("导入失败，请选择正确的文件格式支持xlsx或xls");
        }
        Result result=excelService.importProject(file);
        return "/common/file/file";
    }

    @GetMapping("/edit/{id}")
    @RequiresPermissions("common:dict:edit")
    String edit(@PathVariable("id") Long id, Model model) {
        ProjectItem dict = excelService.get(id);
        model.addAttribute("dict", dict);
        System.out.println(dict);
        return "common/file/edit";
    }

    @ResponseBody
    @RequestMapping("/update")
   // @RequiresPermissions("common:dict:edit")
    public R update(ProjectItem dict) {
        if (Constant.DEMO_ACCOUNT.equals(getUsername())) {
            return R.error(1, "演示系统不允许修改,完整体验请部署程序");
        }
        excelService.update(dict);
        return R.ok();
    }

    @GetMapping("/add")
   // @RequiresPermissions("common:dict:add")
    String add() {
        return "common/file/add";
    }

    @ResponseBody
    @PostMapping("/save")
    //@RequiresPermissions("common:dict:add")
    public R save(ProjectItem dict) {
        if (Constant.DEMO_ACCOUNT.equals(getUsername())) {
            return R.error(1, "演示系统不允许修改,完整体验请部署程序");
        }
        if (excelService.save1(dict) > 0) {
            return R.ok();
        }
        return R.error();
    }

    @GetMapping("/download")
    public String downloadFile(HttpServletRequest request, HttpServletResponse response) {
        String fileName = "template.xlsx";
        String result = FileUtils.downloadFiles(request, response, fileName);
        if (request == null) {
            return null;
        }
        return result;
    }
}
