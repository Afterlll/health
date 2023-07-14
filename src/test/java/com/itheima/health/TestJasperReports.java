package com.itheima.health;

import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TestJasperReports {
    @Test
    public void test01() throws JRException {
        String jrxmPath = "D:\\pdf\\demo.jrxml";
        String jasperPath = "D:\\pdf\\demo.jasper";

        // 编译模板
        JasperCompileManager.compileReportToFile(jrxmPath, jasperPath);

        // 构造数据, 参数的Map泛型必须是<String, Object>
        Map<String, Object> parameters = new HashMap();
        parameters.put("reportDate", "2023-06-07");
        parameters.put("company", "ptg");

        List<Map> list = new ArrayList<>();
        Map<String, String> data1 = new HashMap<>();
        data1.put("name", "zhang");
        data1.put("address", "guangzhou");
        data1.put("email", "zhangsan@163.com");

        Map<String, String> data2 = new HashMap<>();
        data2.put("name", "lisi");
        data2.put("address", "dongguan");
        data2.put("email", "lisi@163.com");

        list.add(data1);
        list.add(data2);

        // 填充数据
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperPath, parameters, new JRBeanCollectionDataSource(list));

        // 输出到文件
        JasperExportManager.exportReportToPdfFile(jasperPrint, "D:\\pdf\\demo.pdf");
    }

    // JDBC数据源方式填充数据
    @Test
    public void testReportJDBC() throws ClassNotFoundException, SQLException, JRException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/health", "root", "123");

        String jrxmlPath = "D:\\pdf\\demo2.jrxml";
        String jasperPath = "D:\\pdf\\demo1.jasper";

        // 编译模板
        JasperCompileManager.compileReportToFile(jrxmlPath, jasperPath);
        Map parameters = new HashMap();
        parameters.put("company", "广州黑马");

        // 填充数据: 使用JDBC数据源方式填充
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperPath, parameters, connection);

        // 输出文件
        JasperExportManager.exportReportToPdfFile(jasperPrint, "D:\\pdf\\demo01.pdf");
    }

    // JavaBean数据源方式填充数据
    @Test
    public void testReportJavaBean() throws ClassNotFoundException, SQLException, JRException {
        String jrxmlPath = "D:\\pdf\\demo1.jrxml";
        String jasperPath = "D:\\MyFileTest\\pdf\\demo2.jasper";

        // 编译模板
        JasperCompileManager.compileReportToFile(jrxmlPath, jasperPath);
        Map parameters = new HashMap();
        parameters.put("company", "22广州黑马JavaBean");

        // 构造数据
        Map paramters = new HashMap();
        paramters.put("company","传智播客");

        List<Map> list = new ArrayList();
        Map map1 = new HashMap();
        map1.put("tName","入职体检套餐");
        map1.put("tCode","RZTJ");
        map1.put("tAge","18-60");
        map1.put("tPrice","500");

        Map map2 = new HashMap();
        map2.put("tName","阳光爸妈老年健康体检");
        map2.put("tCode","YGBM");
        map2.put("tAge","55-60");
        map2.put("tPrice","500");
        list.add(map1);
        list.add(map2);

        // 填充数据: 使用JavaBean数据源方式填充
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperPath, parameters, new JRBeanCollectionDataSource(list));

        // 输出文件
        JasperExportManager.exportReportToPdfFile(jasperPrint, "D:\\MyFileTest\\pdf\\demo02.pdf");
    }
}