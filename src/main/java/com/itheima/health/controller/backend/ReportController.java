package com.itheima.health.controller.backend;

import com.itheima.health.constant.MessageConstant;
import com.itheima.health.entity.Result;
import com.itheima.health.service.MemberService;
import com.itheima.health.service.ReportService;
import com.itheima.health.service.SetmealService;
import com.itheima.health.utils.DateUtils;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

// 统计报表
@RestController
@RequestMapping("/report")
public class ReportController {
    @Autowired
    private MemberService memberService;

    @Autowired
    private SetmealService setmealService;

    @Autowired
    private ReportService reportService;

    private PathMatchingResourcePatternResolver resourceLoader = new PathMatchingResourcePatternResolver();

    /**
     * 获取运营统计数据
     * @return 统一响应结果，包含运营统计数据
     * Map数据格式：
     *      reportDate              -> date     日期
     *      todayNewMember          -> number   新增会员数
     *      totalMember             -> number   总会员数
     *      thisWeekNewMember       -> number   本周新增会员数
     *      thisMonthNewMember      -> number   本月新增会员数
     *      todayOrderNumber        -> number   今日预约数
     *      todayVisitsNumber       -> number   今日到诊数
     *      thisWeekOrderNumber     -> number   本周预约数
     *      thisWeekVisitsNumber    -> number   本周到诊数
     *      thisMonthOrderNumber    -> number   本月预约数
     *      thisMonthVisitsNumber   -> number   本月到诊数
     *      hotSetmeal -> List<Map>             热门套餐    {name:'阳光爸妈升级肿瘤12项筛查（男女单人）体检套餐',setmeal_count:200,proportion:0.222}
     */
    @RequestMapping("/getBusinessReportData.do")
    public Result getBusinessReportData() {
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        try {
            // 1.调用业务层获取运营统计数据
            Map<String, Object> map = reportService.getBusinessReport();

            // 2.返回统一响应结果，包含运营统计数据
            return new Result(true, MessageConstant.GET_BUSINESS_REPORT_SUCCESS, map);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.GET_BUSINESS_REPORT_FAIL);
        }
    }

    /**
     * 导出 运营统计数据到 Excel报表
     * @return
     */
    @RequestMapping("/exportBusinessReport.do")
    public Result exportBusinessReport(HttpServletRequest request, HttpServletResponse response) {
        try {
            // 1.调用业务层获取报表数据
            Map<String, Object> map = reportService.getBusinessReport();

            // 2.获取到Map中的每个数据
            String reportDate = (String) map.get("reportDate");
            Integer todayNewMember = (Integer) map.get("todayNewMember");
            Integer totalMember = (Integer) map.get("totalMember");
            Integer thisWeekNewMember = (Integer) map.get("thisWeekNewMember");
            Integer thisMonthNewMember = (Integer) map.get("thisMonthNewMember");
            Integer todayOrderNumber = (Integer) map.get("todayOrderNumber");
            Integer todayVisitsNumber = (Integer) map.get("todayVisitsNumber");
            Integer thisWeekOrderNumber = (Integer) map.get("thisWeekOrderNumber");
            Integer thisWeekVisitsNumber = (Integer) map.get("thisWeekVisitsNumber");
            Integer thisMonthOrderNumber = (Integer) map.get("thisMonthOrderNumber");
            Integer thisMonthVisitsNumber = (Integer) map.get("thisMonthVisitsNumber");
            List<Map> hotSetmeal = (List<Map>) map.get("hotSetmeal");

            // 3.获得Excel模板文件绝对路径
            String reportExcelPath = File.separator + "static" + File.separator + "backend" + File.separator + "template" + File.separator + "report_template.xlsx";
            String reportTemplateRealPath = resourceLoader.getResource("classpath:").getFile().toString() + reportExcelPath;
            System.out.println("reportTemplateRealPath = " + reportTemplateRealPath);

            // 4.读取模板文件创建Excel表格对象
            XSSFWorkbook workbook = new XSSFWorkbook(new FileInputStream(reportTemplateRealPath));
            XSSFSheet sheetAt0 = workbook.getSheetAt(0);

            // 5.设置表格数据
            // 设置 日期
            sheetAt0.getRow(2).getCell(5).setCellValue(reportDate);
            // 设置 新增会员数
            sheetAt0.getRow(4).getCell(5).setCellValue(todayNewMember);

            // 设置 总会员数
            sheetAt0.getRow(4).getCell(7).setCellValue(totalMember);

            // 设置 本周新增会员数
            sheetAt0.getRow(5).getCell(5).setCellValue(thisWeekNewMember);

            // 设置 本月新增会员数
            sheetAt0.getRow(5).getCell(7).setCellValue(thisMonthNewMember);

            // 设置 今日预约数
            sheetAt0.getRow(7).getCell(5).setCellValue(todayOrderNumber);

            // 设置 今日到诊数
            sheetAt0.getRow(7).getCell(7).setCellValue(todayVisitsNumber);

            // 设置 本周预约数
            sheetAt0.getRow(8).getCell(5).setCellValue(thisWeekOrderNumber);

            // 设置 本周到诊数
            sheetAt0.getRow(8).getCell(7).setCellValue(thisWeekVisitsNumber);

            // 设置 本月预约数
            sheetAt0.getRow(9).getCell(5).setCellValue(thisMonthOrderNumber);

            // 设置 本月到诊数
            sheetAt0.getRow(9).getCell(7).setCellValue(thisMonthVisitsNumber);

            // 设置 热门套餐
            int rowNum = 12;
            for (Map setmeal : hotSetmeal) {
                String name = (String) setmeal.get("name");
                Long setmealCount = (Long) setmeal.get("setmeal_count");
                BigDecimal proportion = (BigDecimal) setmeal.get("proportion");
                sheetAt0.getRow(rowNum).getCell(4).setCellValue(name);
                sheetAt0.getRow(rowNum).getCell(5).setCellValue(setmealCount);
                sheetAt0.getRow(rowNum).getCell(6).setCellValue(proportion.doubleValue());
                sheetAt0.getRow(rowNum).getCell(7).setCellValue(DateUtils.getTodayString());
                rowNum++;
            }

            // 6.通过输出流进行文件下载
            response.setContentType("application/vnd.ms-excel");
            response.setHeader("content-Disposition", "attachment;filename=report.xlsx");

            ServletOutputStream out = response.getOutputStream();
            workbook.write(out);

            out.close();
            workbook.close();

            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.GET_BUSINESS_REPORT_FAIL);
        }
    }

    /**
     * 套餐占比统计
     * @return 统一响应结果，包含套餐和套餐的预约数量
     */
    @RequestMapping("/getSetmealReport.do")
    public Result getSetmealReport() {
        // 1.调用业务层获取套餐和套餐的预约数量
        List<Map> list = setmealService.findSetmealCount();

        // 2.返回统一响应结果，包含套餐和套餐的预约数量
        return new Result(true, MessageConstant.GET_SETMEAL_COUNT_REPORT_SUCCESS, list);
    }

    /**
     * 会员数量统计
     * @return 统一响应结果，包含月份和月份的会员数量
     */
    @RequestMapping("/getMemberReport.do")
    public Result getMemberReport() {
        // 1.获得当前日期之前的12个月的日期
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, -12); // 2023-06 往前12个月变为 2022-06

        // 2.转换前12个月的日期: 2022.07, 2022.08
        List<String> list = new ArrayList<>();
        for (int i = 0; i < 12; i++) {
            calendar.add(Calendar.MONTH, 1);
            list.add(new SimpleDateFormat("yyyy.MM").format(calendar.getTime()));
        }

        // 3.准备12个月份名字: ['2022.07', '2022.08', '2022.09', '2022.10']
        Map<String, Object> map = new HashMap<>();
        map.put("months", list);

        try {
            // 4.调用业务层获取12个月份的会员数据: [10, 20, 35, 46]
            List<Integer> memberCount = memberService.findMemberCountByMonth(list);
            map.put("memberCount", memberCount);

            // 5.返回统一响应结果，包含月份和月份的会员数量
            return new Result(true, MessageConstant.GET_MEMBER_NUMBER_REPORT_SUCCESS, map);
        } catch (ParseException e) {
            // 5.如果出现异常，返回统一响应结果
            return new Result(false, MessageConstant.GET_MEMBER_NUMBER_REPORT_FAIL);
        }
    }

    /**
     * 导出 运营统计数据到 PDF
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/exportBusinessReport4PDF.do")
    public Result exportBusinessReport4PDF(HttpServletRequest request, HttpServletResponse response) {
        try {
            // 1.调用业务层获取报表数据
            Map<String, Object> map = reportService.getBusinessReport();

            // 2.获取到Map中的每个数据
            String reportDate = (String) map.get("reportDate");
            Integer todayNewMember = (Integer) map.get("todayNewMember");
            Integer totalMember = (Integer) map.get("totalMember");
            Integer thisWeekNewMember = (Integer) map.get("thisWeekNewMember");
            Integer thisMonthNewMember = (Integer) map.get("thisMonthNewMember");
            Integer todayOrderNumber = (Integer) map.get("todayOrderNumber");
            Integer todayVisitsNumber = (Integer) map.get("todayVisitsNumber");
            Integer thisWeekOrderNumber = (Integer) map.get("thisWeekOrderNumber");
            Integer thisWeekVisitsNumber = (Integer) map.get("thisWeekVisitsNumber");
            Integer thisMonthOrderNumber = (Integer) map.get("thisMonthOrderNumber");
            Integer thisMonthVisitsNumber = (Integer) map.get("thisMonthVisitsNumber");
            List<Map> hotSetmeal = (List<Map>) map.get("hotSetmeal");

            // 3.准备jrxml和jasper的路径
            String template = request.getSession().getServletContext().getRealPath("static\\backend\\template");
            System.out.println("template = " + template);

            String jrxmlPath = File.separator + "static" + File.separator + "backend" + File.separator + "template" + File.separator + "health_business3.jrxml";
            String jasperPath = File.separator + "static" + File.separator + "backend" + File.separator + "template" + File.separator + "health_business3.jasper";
            String jrxmlPathRealPath = resourceLoader.getResource("classpath:").getFile().toString() + jrxmlPath;
            String jasperPathRealPath = resourceLoader.getResource("classpath:").getFile().toString() + jasperPath;
            System.out.println("jrxmlPathRealPath = " + jrxmlPathRealPath);
            System.out.println("jasperPathRealPath = " + jasperPathRealPath);

            // 4.编译模版
            JasperCompileManager.compileReportToFile(jrxmlPathRealPath, jasperPathRealPath);

            // 5.填充数据
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperPathRealPath, map, new JRBeanCollectionDataSource(hotSetmeal));

            ServletOutputStream out = response.getOutputStream();
            response.setContentType("application/pdf");
            response.setHeader("content-disposition", "attachement;filename=export.pdf");

            // 6.导出PDF
            JasperExportManager.exportReportToPdfStream(jasperPrint, out);

            return null;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("导出PDF失败!");
            return new Result(false, MessageConstant.GET_BUSINESS_REPORT_FAIL);
        }
    }
}