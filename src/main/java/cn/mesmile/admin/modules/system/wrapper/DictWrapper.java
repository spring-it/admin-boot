package cn.mesmile.admin.modules.system.wrapper;

import cn.hutool.core.bean.BeanUtil;
import cn.mesmile.admin.common.support.BaseEntityWrapper;
import cn.mesmile.admin.modules.system.domain.vo.DictVO;
import cn.mesmile.admin.modules.system.entity.SysDict;

import java.util.Objects;

/**
 * @author zb
 * @Description
 */
public class DictWrapper extends BaseEntityWrapper<SysDict , DictVO> {

    public static DictWrapper build(){
        return new DictWrapper();
    }

    @Override
    public DictVO entityVO(SysDict entity) {
        DictVO dict = BeanUtil.copyProperties(entity, DictVO.class);
        DictVO dictVO = Objects.requireNonNull(dict);
        // todo 其他转换
        return dictVO;
    }
}
