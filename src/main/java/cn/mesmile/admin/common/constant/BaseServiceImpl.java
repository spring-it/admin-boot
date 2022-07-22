package cn.mesmile.admin.common.constant;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author zb
 * @Description
 */
@Validated
public class BaseServiceImpl<M extends BaseMapper<T>, T extends BaseEntity> extends ServiceImpl<M, T> implements IBaseService<T> {

    @Transactional(rollbackFor = {Exception.class})
    @Override
    public boolean deleteLogic(@NotNull List<Long> ids) {
        return removeByIds(ids);
    }

    @Transactional(rollbackFor = {Exception.class})
    @Override
    public boolean changeStatus(@NotNull List<Long> ids, String status) {
        Class<T> tClass = this.currentModelClass();
        T t = BeanUtils.instantiateClass(tClass);

        return false;
    }

}
