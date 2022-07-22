package cn.mesmile.admin.modules.system.service.impl;

import cn.mesmile.admin.common.constant.BaseServiceImpl;
import cn.mesmile.admin.modules.system.entity.SysAttach;
import cn.mesmile.admin.modules.system.mapper.SysAttachMapper;
import cn.mesmile.admin.modules.system.service.ISysAttachService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.skywalking.apm.toolkit.trace.Tag;
import org.apache.skywalking.apm.toolkit.trace.Tags;
import org.apache.skywalking.apm.toolkit.trace.Trace;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 附件表 服务实现类
 * </p>
 *
 * @author zb
 */
@Service
public class SysAttachServiceImpl extends BaseServiceImpl<SysAttachMapper, SysAttach> implements ISysAttachService {

    @Trace
    @Tags({
            @Tag(key = "param",value = "arg[0]"),
            @Tag(key = "result",value = "returnedObj")
    })
    @Override
    public Page<SysAttach> findSysAttachPage(Page<SysAttach> page) {
        Page<SysAttach> result = page(page);
        return result;
    }
}
