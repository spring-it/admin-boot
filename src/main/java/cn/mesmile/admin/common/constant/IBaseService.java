package cn.mesmile.admin.common.constant;

import com.baomidou.mybatisplus.extension.service.IService;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author zb
 * @Description 父类
 */
public interface IBaseService<T> extends IService<T> {

    /**
     * 批量逻辑删除
     * @param ids
     * @return
     */
    boolean deleteLogic(@NotNull List<Long> ids);

    /***
     * 批量改变状态
     * @param ids
     * @param status
     * @return
     */
    boolean changeStatus(@NotNull List<Long> ids, String status);

}