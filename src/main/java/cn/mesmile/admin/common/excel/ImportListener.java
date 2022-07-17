//package cn.mesmile.admin.common.excel;
//
//import com.alibaba.excel.context.AnalysisContext;
//import com.alibaba.excel.event.AnalysisEventListener;
//import com.alibaba.excel.util.ListUtils;
//import lombok.Getter;
//import lombok.Setter;
//import lombok.ToString;
//
//import java.util.List;
//
///**
// * @author zb
// * @description 监听导入数据
// */
//@ToString
//@Setter
//@Getter
//public class ImportListener<T> extends AnalysisEventListener<T> {
//    /**
//     * 实际使用中可以100条，然后清理list ，方便内存回收
//     */
//    private static final int BATCH_COUNT = 100;
//    /**
//     * 缓存的数据
//     */
//    private List<T> cachedDataList = ListUtils.newArrayListWithExpectedSize(BATCH_COUNT);
//
//    /**
//     * 保存数据
//     */
//    private final ExcelImporter<T> importer;
//
//    public ImportListener(ExcelImporter<T> importer){
//        this.importer = importer;
//    }
//
//    @Override
//    public void invoke(T data, AnalysisContext analysisContext) {
//        cachedDataList.add(data);
//        if (cachedDataList.size() >= BATCH_COUNT) {
//            importer.batchSave(cachedDataList);
//            // 存储完成清理 list
//            cachedDataList = ListUtils.newArrayListWithExpectedSize(BATCH_COUNT);
//        }
//    }
//
//    /**
//     * 所有数据解析完成了 都会来调用
//     */
//    @Override
//    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
//        importer.batchSave(cachedDataList);
//        // 存储完成清理 list
//        cachedDataList = ListUtils.newArrayListWithExpectedSize(BATCH_COUNT);
//    }
//
//}
