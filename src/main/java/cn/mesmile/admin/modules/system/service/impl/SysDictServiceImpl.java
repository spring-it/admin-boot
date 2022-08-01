package cn.mesmile.admin.modules.system.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.mesmile.admin.modules.system.entity.SysDict;
import cn.mesmile.admin.modules.system.mapper.SysDictMapper;
import cn.mesmile.admin.modules.system.service.ISysDictService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


/**
 * <p>
 * 字典 服务实现类
 * </p>
 *
 * @author zb
 */
@Service
public class SysDictServiceImpl extends ServiceImpl<SysDictMapper, SysDict> implements ISysDictService {

    @Override
    public Page<SysDict> findSysDictPage(Page<SysDict> page) {
        Page<SysDict> result = page(page);
        return result;
    }

    @Override
    public Page<SysDict> pageParentDict(Page<SysDict> page, SysDict sysDict) {
        String code = sysDict.getCode();
        String dictValue = sysDict.getDictValue();
        LambdaQueryWrapper<SysDict> wrapper = Wrappers.<SysDict>lambdaQuery()
                .like(StrUtil.isNotEmpty(code), SysDict::getCode, code)
                .like(StrUtil.isNotEmpty(dictValue), SysDict::getDictValue, dictValue)
                .eq(SysDict::getParentId, 0L)
                .orderByAsc(SysDict::getSort);
        return page(page, wrapper);
    }

    @Override
    public List<SysDict> listChildDict(SysDict sysDict) {
        Long parentId = sysDict.getParentId();
        if (parentId == null || parentId < 0){
            return new ArrayList<>();
        }
        String code = sysDict.getCode();
        String dictValue = sysDict.getDictValue();
        LambdaQueryWrapper<SysDict> wrapper = Wrappers.<SysDict>lambdaQuery()
                .like(StrUtil.isNotEmpty(code), SysDict::getCode, code)
                .like(StrUtil.isNotEmpty(dictValue), SysDict::getDictValue, dictValue)
                .eq(SysDict::getParentId, parentId)
                .orderByAsc(SysDict::getSort);
        return list(wrapper);
    }


}
