package com.xjrsoft.module.excel.controller;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.xjrsoft.common.page.PageOutput;
import com.xjrsoft.common.result.Response;
import com.xjrsoft.core.tool.utils.BeanUtil;
import com.xjrsoft.core.tool.utils.ExcelExportUtil;
import com.xjrsoft.core.tool.utils.WebUtil;
import com.xjrsoft.module.excel.dto.ExcelExportDto;
import com.xjrsoft.module.excel.dto.GetListExcelExportDto;
import com.xjrsoft.module.excel.entity.XjrExcelExport;
import com.xjrsoft.module.excel.service.IXjrExcelExportService;
import com.xjrsoft.module.excel.vo.ExcelExportVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.apache.commons.lang.StringUtils;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;


/**
 * @Author:光华科技-软件研发部
 * @Date:2020/11/10
 * @Description:excel导出控制器
 */
@AllArgsConstructor
@RestController
@RequestMapping("/excel-export")
@Api(value = "/excel-export",tags = "excel导出模块")
public class XjrExcelExportController {

    private IXjrExcelExportService excelExportService;

    /**
     * @Author:光华科技-软件研发部
     * @Date:2020/11/10
     * @Param:[dto, fModuleId]
     * @return:com.xjrsoft.common.result.Response
     * @Description:获取excel导出分页数据
     */
    @GetMapping()
    @ApiOperation(value = "获取excel导出分页数据")
    public Response<PageOutput<ExcelExportVo>> queryExcelExport(GetListExcelExportDto dto) {
        return Response.ok(excelExportService.getPageData(dto));
    }

    /**
     * @Author:光华科技-软件研发部
     * @Date:2020/11/10
     * @Param:[]
     * @return:voide
     * @Description:获取导出配置详情
     */
    @GetMapping("/{id}")
    @ApiOperation(value = "获取导出配置详情")
    public Response toUpdatePage(@PathVariable String id) {
        XjrExcelExport xjrExcelExport = excelExportService.getById(id);
        if (xjrExcelExport != null) {
            return Response.ok(BeanUtil.copy(xjrExcelExport, ExcelExportVo.class));
        } else {
            return Response.notOk();
        }
    }

    /**
     * @Author:光华科技-软件研发部
     * @Date:2020/11/10
     * @Param:[xjrExcelExport]
     * @return:com.xjrsoft.common.result.Response
     * @Description:excel导出按钮的新增
     */
    @PostMapping()
    @ApiOperation(value = "excel导出按钮的新增")
    public Response save(@RequestBody ExcelExportDto dto) {
        return Response.status(excelExportService.saveExcelExport(dto));
    }

    /**
     * @Author:光华科技-软件研发部
     * @Date:2020/11/10
     * @Param:[id]
     * @return:com.xjrsoft.common.result.Response
     * @Description:更新
     */
    @PutMapping("/{id}")
    @ApiOperation(value = "更新")
    public Response update(@PathVariable String id, @RequestBody ExcelExportDto dto) {
        return Response.status(excelExportService.updateExcelExport(id, dto));
    }

    /**
     * @Author:光华科技-软件研发部
     * @Date:2020/11/10
     * @Param:[id]
     * @return:com.xjrsoft.common.result.Response
     * @Description:删除
     */
    @DeleteMapping("/{id}")
    @ApiOperation(value = "删除")
    public Response deleteExcelExport(@PathVariable String id) {
        return Response.status(excelExportService.removeById(id));
    }

    /**
     * @Author:光华科技-软件研发部
     * @Date:2020/11/10
     * @Param:[id]
     * @return:com.xjrsoft.common.result.Response
     * @Description:启动
     */
    @PatchMapping("/start/{id}")
    @ApiOperation(value = "启动")
    public Response start(@PathVariable String id) {
        XjrExcelExport xjrExcelExport = new XjrExcelExport();
        xjrExcelExport.setEnabledMark(1);
        return Response.status(excelExportService.updateById(xjrExcelExport));
    }

    /**
     * @Author:光华科技-软件研发部
     * @Date:2020/11/10
     * @Param:[id]
     * @return:com.xjrsoft.common.result.Response
     * @Description:停止
     */
    @PatchMapping("/stop/{id}")
    @ApiOperation(value = "停止")
    public Response stop(@PathVariable String id) {
        XjrExcelExport xjrExcelExport = new XjrExcelExport();
        xjrExcelExport.setEnabledMark(0);
        return Response.status(excelExportService.updateById(xjrExcelExport));
    }

    /**
     * @Author:光华科技-软件研发部
     * @Date:2020/11/11
     * @Param:[titleDataList:标题列数据, dataList:导出数据List]
     * @return:com.xjrsoft.common.result.Response
     * @Description:导出excel文件
     */
    @SneakyThrows
    @PostMapping("/export-file")
    @ApiOperation(value = "导出excel文件")
    public Response exportExcel(String titleDataList, String dataList) {
//        Map<String, String> titleData = (Map<String, String>) JSON.parse(titleDataList);
//        if (StringUtils.isNotBlank(dataList)) {
//            ArrayList<Map<String, Object>> list = JSON.parseObject(dataList, new TypeReference<ArrayList<Map<String, Object>>>() {
//            });
//            File file = new File(ExcelExportUtil.getFilePath());
//            return Response.ok(ExcelExportUtil.saveFile(titleData, list, file));
//        }
        return Response.status(false);
    }

    /**
     * @Author:光华科技-软件研发部
     * @Date:2020/11/11
     * @Param:[url:请求地址url, urlParam:请求url携带的参数, titleHeader:excel列名]
     * @return:void
     * @Description:根据传过来的方法名和参数 请求接口获取数据生成excel
     */
    @SneakyThrows
    @PostMapping("/export-file-by")
    @ApiOperation(value = "根据传过来的方法名和参数 请求接口获取数据生成excel")
    public void exportExcelBy(String url, String urlParam, String titleHeader) {
//        String urlParamStr = urlParam.replace(" ", "&");
//        String requestUrl = urlParamStr + "?" + urlParam;
        String requestUrl = url + "?" + urlParam;
        // 标题 String 转 Map
        Map<String, String> titleData = (Map<String, String>) JSON.parse(titleHeader);
        // 请求url获取数据
        String dataStr = HttpUtil.get(requestUrl);
        // 解析数据
//        Map<String, Object> parse = (Map<String, Object>) JSON.parse(dataStr);
////        List<Map<String, Object>> rowsList = (List<Map<String, Object>>) parse.get("rows");
//        Map<String, Object> data = (Map<String, Object> )parse.get("data");
//        List<Map<String, Object>> rowsList = (List<Map<String, Object>>) data.get("Rows");
        List<Map> rowsList = JSONArray.parseArray(dataStr,Map.class);
        // 生成excel
        File file = new File(ExcelExportUtil.getFilePath());
        file = ExcelExportUtil.saveFile(titleData, rowsList, file);
        WebUtil.writeFileToResponse(file.getName(), file);
//        return Response.ok(file);
    }



    /**
     * 通用文件下载
     *
     * @param filePath 文件所在全路径 (复制文件路径在浏览器可访问)
     * @param response
     * @throws Exception
     */
    @RequestMapping(value = "/download")
    public HttpServletResponse download(HttpServletResponse response) throws Exception {
       String  filePath = "C:\\WorkSpace\\Stig-itss\\temp\\1668160666277.xlsx";
            try {
                // 要下载的文件的全路径名
//                String  = "D:\\test\\canon\\upload\\file\\202009031027490_河北省政务服务网办事指南.doc";
                File file = new File(filePath);
                // 获取文件名
                String filename = file.getName().toString();
                //通过流把文件内容写入到客户端
                InputStream fis = new BufferedInputStream(new FileInputStream(filePath));
                byte[] buffer = new byte[fis.available()];
                fis.read(buffer);
                fis.close();
                // 清空response
                response.reset();
                // 设置response的Header
                response.addHeader("Content-Disposition", "attachment;filename=" + new String(filename.getBytes(), "ISO-8859-1"));
                response.addHeader("Content-Length", "" + file.length());
                OutputStream toClient = new BufferedOutputStream(response.getOutputStream());
                response.setContentType("application/octet-stream");
                toClient.write(buffer);
                toClient.flush();
                toClient.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            return response;
        }
    }


