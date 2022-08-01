package cn.mesmile.admin.modules.system.service;

import cn.mesmile.admin.modules.system.domain.vo.DictVO;
import cn.mesmile.admin.modules.system.entity.SysDict;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.List;

/**
 * <p>
 * 字典 服务类
 * </p>
 *
 * @author zb
 */
public interface ISysDictService extends IService<SysDict> {

    /**
     * 分页查找字典
     * @param page 分页信息
     * @return
     */
    Page<SysDict> findSysDictPage(Page<SysDict> page);

    /**
     * 查询所有父级列表
     * @param page 分页参数
     * @param sysDict 字典查询字段
     * @return 结果
     */
    Page<SysDict> pageParentDict(Page<SysDict> page, SysDict sysDict);

    /**
     * 获取对应子节点列表
     * @param sysDict 父节点id,以及查询参数
     * @return 结果
     */
    List<SysDict> listChildDict(SysDict sysDict);
}

