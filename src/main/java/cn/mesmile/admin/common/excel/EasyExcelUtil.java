package cn.mesmile.admin.common.excel;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.ContentType;
import cn.mesmile.admin.common.exceptions.EasyExcelException;
import cn.mesmile.admin.common.exceptions.ServiceException;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.read.builder.ExcelReaderBuilder;
import com.alibaba.excel.read.listener.PageReadListener;
import com.alibaba.excel.read.listener.ReadListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;
import java.util.function.Consumer;

/**
 * @author zb
 * @description excel 导入导出工具
 */
@Slf4j
public class EasyExcelUtil {

    private EasyExcelUtil() {
    }

    /**
     * 一次性读取所有数据
     *
     * @param excel excel 文件
     * @param clazz 读取类的class
     * @return 数据
     */
    public static <T> List<T> read(MultipartFile excel, Class<T> clazz) {
        uploadCheck(excel);
        try (
                InputStream inputStream = new BufferedInputStream(excel.getInputStream());
        ) {
            return EasyExcel.read(inputStream).sheet().head(clazz).doReadSync();
        } catch (Exception e) {
            throw new EasyExcelException("读取文件未知异常", e);
        }
    }


    /**
     * 一次性读取所有数据
     *
     * @param excel   excel 文件
     * @param sheetNo 读取第几个表格
     * @param clazz   读取类的class
     * @return 数据
     */
    public static <T> List<T> read(MultipartFile excel, int sheetNo, Class<T> clazz) {
        return read(excel, sheetNo, 1, clazz);
    }

    /**
     * 一次性读取所有数据
     *
     * @param excel         excel 文件
     * @param sheetNo       读取第几个表格
     * @param headRowNumber 标题行在第几行
     * @param clazz         读取类的class
     * @return 数据
     */
    public static <T> List<T> read(MultipartFile excel, int sheetNo, int headRowNumber, Class<T> clazz) {
        uploadCheck(excel);
        try (
                InputStream inputStream = new BufferedInputStream(excel.getInputStream());
        ) {
            return EasyExcel.read(inputStream).sheet(sheetNo).headRowNumber(headRowNumber).head(clazz).doReadSync();
        } catch (Exception e) {
            throw new EasyExcelException("读取文件未知异常", e);
        }
    }

    /**
     * 读取所有数据并保存，默认每次读取 100 条数据
     *
     * @param excel    excel 文件
     * @param consumer 执行保存动作
     * @param clazz    映射类
     */
    public static <T> void readAndSave(MultipartFile excel, Consumer<List<T>> consumer, Class<T> clazz) {
        uploadCheck(excel);
        try (
                InputStream inputStream = new BufferedInputStream(excel.getInputStream());
        ) {
            // 默认每次读取 100 条数据
            EasyExcel.read(inputStream, clazz, new PageReadListener<T>(consumer)).sheet().headRowNumber(1).doRead();
        } catch (Exception e) {
            throw new EasyExcelException("读取文件未知异常", e);
        }
    }

    /**
     * 检查上传文件
     *
     * @param excel 上传文件
     */
    private static void uploadCheck(MultipartFile excel) {
        String filename = excel.getOriginalFilename();
        if (StrUtil.isEmpty(filename)) {
            throw new EasyExcelException("请上传文件");
        } else if (!StrUtil.endWithAnyIgnoreCase(filename, ".xls", ".xlsx")) {
            throw new EasyExcelException("请上传正确的excel文件");
        }
    }


    /**
     * 导出文件
     *
     * @param response 返回体
     * @param dataList 数据体
     * @param clazz    映射类
     */
    public static <T> void export(HttpServletResponse response, List<T> dataList, Class<T> clazz) {
        export(response, DateUtil.format(new Date(), "yyyyMMddHHmmss"), "导出数据", dataList, clazz);
    }


    /**
     * 导出文件
     *
     * @param response 返回体
     * @param fileName 文件名，不包含后缀
     * @param dataList 数据体
     * @param clazz    映射类
     */
    public static <T> void export(HttpServletResponse response, String fileName, List<T> dataList, Class<T> clazz) {
        export(response, fileName, "Sheet1", dataList, clazz);
    }

    /**
     * 导出文件
     *
     * @param response  返回体
     * @param fileName  文件名，不包含后缀
     * @param sheetName sheet名称
     * @param dataList  数据体
     * @param clazz     映射类
     */
    public static <T> void export(HttpServletResponse response, String fileName, String sheetName, List<T> dataList, Class<T> clazz) {
        try {
            response.setContentType("application/vnd.ms-excel");
            response.setCharacterEncoding(StandardCharsets.UTF_8.name());
            fileName = URLEncoder.encode(fileName, StandardCharsets.UTF_8.name());
            response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");
            EasyExcel.write(response.getOutputStream(), clazz).sheet(sheetName).doWrite(dataList);
        } catch (Exception e) {
            throw new EasyExcelException("导出文件未知异常", e);
        }
    }

    /**
     * 导出文件并且添加水印
     *
     * @param response  返回体
     * @param fileName  文件名，不包含后缀
     * @param dataList  数据体
     * @param clazz     映射类
     * @param watermark 水印内容
     */
    public static <T> void exportAndWatermark(HttpServletResponse response, String fileName,
                                              List<T> dataList, Class<T> clazz,String watermark) {
        exportAndWatermark(response,fileName,"Sheet1",dataList,clazz, watermark);
    }

    /**
     * 导出文件并且添加水印
     *
     * @param response  返回体
     * @param fileName  文件名，不包含后缀
     * @param sheetName sheet名称
     * @param dataList  数据体
     * @param clazz     映射类
     * @param watermark 水印内容
     */
    public static <T> void exportAndWatermark(HttpServletResponse response, String fileName, String sheetName,
                                              List<T> dataList, Class<T> clazz,String watermark) {
        if (StrUtil.isEmpty(watermark)){
            throw new ServiceException("水印内容不允许为空");
        }
        try {
            response.setContentType("application/vnd.ms-excel");
            response.setCharacterEncoding(StandardCharsets.UTF_8.name());
            fileName = URLEncoder.encode(fileName, StandardCharsets.UTF_8.name());
            response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");
            EasyExcel.write(response.getOutputStream(), clazz)
                    // 注意，此项配置不能少
                    .inMemory(true)
                    .registerWriteHandler(new WaterMarkHandler(watermark))
                    .sheet(sheetName).doWrite(dataList);
        } catch (Exception e) {
            throw new EasyExcelException("导出文件未知异常", e);
        }
    }

}
