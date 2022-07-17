//package cn.mesmile.admin.common.excel;
//
//import cn.hutool.core.date.DateUtil;
//import cn.hutool.core.util.StrUtil;
//import cn.mesmile.admin.common.exceptions.EasyExcelException;
//import com.alibaba.excel.EasyExcel;
//import com.alibaba.excel.read.builder.ExcelReaderBuilder;
//import com.alibaba.excel.read.builder.ExcelReaderSheetBuilder;
//import com.alibaba.excel.read.listener.ReadListener;
//import com.alibaba.excel.util.DateUtils;
//
//import java.io.BufferedInputStream;
//import java.io.IOException;
//import java.io.InputStream;
//import java.net.URLEncoder;
//import java.nio.charset.StandardCharsets;
//import java.util.Date;
//import java.util.List;
//import javax.servlet.http.HttpServletResponse;
//
//import lombok.extern.slf4j.Slf4j;
//import org.apache.commons.codec.Charsets;
//import org.springframework.util.StringUtils;
//import org.springframework.web.multipart.MultipartFile;
//
///**
// * @author zb
// * @description excel 导入导出工具
// */
//@Slf4j
//public class ExcelUtil {
//
//    private ExcelUtil() {
//    }
//
//    /**
//     * 一次性读取所有数据
//     *
//     * @param excel excel 文件
//     * @param clazz 读取类的class
//     * @return 数据
//     */
//    public static <T> List<T> read(MultipartFile excel, Class<T> clazz) {
//        DataListener<T> dataListener = new DataListener<>();
//        ExcelReaderBuilder builder = getReaderBuilder(excel, dataListener, clazz);
//        builder.doReadAll();
//        return dataListener.getDataList();
//    }
//
//
//    public static <T> List<T> read(MultipartFile excel, int sheetNo, Class<T> clazz) {
//        return read(excel, sheetNo, 1, clazz);
//    }
//
//    public static <T> List<T> read(MultipartFile excel, int sheetNo, int headRowNumber, Class<T> clazz) {
//        DataListener<T> dataListener = new DataListener<>();
//        ExcelReaderBuilder builder = getReaderBuilder(excel, dataListener, clazz);
//        builder.sheet(sheetNo).headRowNumber(headRowNumber).doRead();
//        return dataListener.getDataList();
//    }
//
//    /**
//     * 读取所有数据并保存
//     *
//     * @param excel    excel 文件
//     * @param importer 导入数据
//     * @param clazz    映射类
//     */
//    public static <T> void readAndSave(MultipartFile excel, ExcelImporter<T> importer, Class<T> clazz) {
//        ImportListener<T> importListener = new ImportListener<>(importer);
//        ExcelReaderBuilder builder = getReaderBuilder(excel, importListener, clazz);
//        builder.doReadAll();
//    }
//
//
//    /**
//     * 导出文件
//     *
//     * @param response 返回体
//     * @param dataList 数据体
//     * @param clazz    映射类
//     */
//    public static <T> void export(HttpServletResponse response, List<T> dataList, Class<T> clazz) {
//        export(response, DateUtil.format(new Date(), "yyyyMMddHHmmss"), "导出数据", dataList, clazz);
//    }
//
//    /**
//     * 导出文件
//     *
//     * @param response  返回体
//     * @param fileName  文件名，不包含后缀
//     * @param sheetName sheet名称
//     * @param dataList  数据体
//     * @param clazz     映射类
//     */
//    public static <T> void export(HttpServletResponse response, String fileName, String sheetName, List<T> dataList, Class<T> clazz) {
//        try {
//            response.setContentType("application/vnd.ms-excel");
//            response.setCharacterEncoding(StandardCharsets.UTF_8.name());
//            fileName = URLEncoder.encode(fileName, StandardCharsets.UTF_8.name());
//            response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");
//            EasyExcel.write(response.getOutputStream(), clazz).sheet(sheetName).doWrite(dataList);
//        } catch (Exception e) {
//            throw new EasyExcelException("导出文件未知异常", e);
//        }
//    }
//
//    /**
//     * 构造excel reader
//     *
//     * @param excel        excel 上传文件
//     * @param readListener 去取监听器
//     * @param clazz        映射类
//     * @return reader
//     */
//    public static <T> ExcelReaderBuilder getReaderBuilder(MultipartFile excel, ReadListener<T> readListener, Class<T> clazz) {
//        String filename = excel.getOriginalFilename();
//        if (StrUtil.isEmpty(filename)) {
//            throw new EasyExcelException("请上传文件");
//        } else if (!StrUtil.endWithAnyIgnoreCase(filename, ".xls", ".xlsx")) {
//            throw new EasyExcelException("请上传正确的excel文件");
//        } else {
//            try (
//                    InputStream inputStream = new BufferedInputStream(excel.getInputStream());
//            ) {
//                return EasyExcel.read(inputStream, clazz, readListener);
//            } catch (Exception e) {
//                throw new EasyExcelException("读取文件未知异常", e);
//            }
//        }
//    }
//}
