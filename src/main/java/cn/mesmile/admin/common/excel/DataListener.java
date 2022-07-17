//package cn.mesmile.admin.common.excel;
//
//import com.alibaba.excel.context.AnalysisContext;
//import com.alibaba.excel.event.AnalysisEventListener;
//import lombok.Getter;
//import lombok.Setter;
//
//import java.util.ArrayList;
//import java.util.List;
//
///**
// *
// * @author zb
// * @description
// */
//@Getter
//@Setter
//public class DataListener<T> extends AnalysisEventListener<T> {
//
//    private final List<T> dataList = new ArrayList<>();
//
//    public DataListener() {
//    }
//
//    @Override
//    public void invoke(T data, AnalysisContext analysisContext) {
//        this.dataList.add(data);
//    }
//
//    @Override
//    public void doAfterAllAnalysed(AnalysisContext analysisContext) {
//    }
//
//}