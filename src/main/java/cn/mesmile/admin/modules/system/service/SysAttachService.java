package cn.mesmile.admin.modules.system.service;

import cn.mesmile.admin.modules.system.entity.SysAttach;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 附件表 服务类
 * </p>
 *
 * @author zb
 */
public interface SysAttachService extends IService<SysAttach> {

    /**
     * 分页查找附件表
     * @param page 分页信息
     * @return
     */
    Page<SysAttach> findSysAttachPage(Page<SysAttach> page);
}

