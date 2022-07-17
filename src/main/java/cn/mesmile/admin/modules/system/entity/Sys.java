package cn.mesmile.admin.modules.system.entity;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.HeadRowHeight;
import lombok.Data;

/**
 * @author zb
 * @Description
 */
@ColumnWidth(value = 20)
@HeadRowHeight(value = 12)
@Data
public class Sys {

    @ExcelProperty("你好")
    private String hello;

    @ExcelProperty("世界")
    private String world;

    public Sys(){}

    public Sys(String hello,String world){
        this.hello = hello;
        this.world = world;
    }

    /**
     * 忽略这个字段
     * @ExcelIgnore
     */
}
