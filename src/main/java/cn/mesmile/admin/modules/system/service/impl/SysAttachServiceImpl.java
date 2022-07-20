package cn.mesmile.admin.modules.system.service.impl;

import cn.mesmile.admin.modules.system.entity.SysAttach;
import cn.mesmile.admin.modules.system.mapper.SysAttachMapper;
import cn.mesmile.admin.modules.system.service.SysAttachService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.skywalking.apm.toolkit.trace.Tag;
import org.apache.skywalking.apm.toolkit.trace.Tags;
import org.apache.skywalking.apm.toolkit.trace.Trace;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * 附件表 服务实现类
 * </p>
 *
 * @author zb
 */
@Service
@Transactional(rollbackFor = Exception.class,propagation = Propagation.SUPPORTS,readOnly = true)
public class SysAttachServiceImpl extends ServiceImpl<SysAttachMapper, SysAttach> implements SysAttachService {

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
