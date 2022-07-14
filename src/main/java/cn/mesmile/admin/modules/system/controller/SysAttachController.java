package cn.mesmile.admin.modules.system.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.GetMapping;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import springfox.documentation.annotations.ApiIgnore;
import cn.mesmile.admin.modules.system.service.SysAttachService;
import cn.mesmile.admin.modules.system.entity.SysAttach;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import cn.mesmile.admin.common.result.R;
import java.util.List;

/**
 * <p>
 * 附件 前端控制器
 * </p>
 *
 * @author zb
 */
@Api(value = "附件相关api",tags = {"附件相关api"})
@RequiredArgsConstructor
@RestController
@RequestMapping("/sys-attach")
public class SysAttachController {

    private final SysAttachService sysAttachService;

    @ApiOperation("分页查询附件")
    @GetMapping("/get")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "当前页",name = "current",defaultValue = "1"),
            @ApiImplicitParam(value = "每页显示条数",name = "size",defaultValue = "10"),
    })
    public R<Page<SysAttach>> findSysAttachPage(@ApiIgnore Page<SysAttach> page){
        Page<SysAttach> result = sysAttachService.findSysAttachPage(page);
        return R.data(result);
    }

    @PostMapping("/save")
    @ApiOperation("新增附件")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "sysAttach", value = "sysAttach对象的json数据")
    })
    public R save(@RequestBody SysAttach sysAttach){
        boolean save = sysAttachService.save(sysAttach);
        return R.status(save);
    }

    @PostMapping("/update")
    @ApiOperation("修改附件")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "sysAttach", value = "sysAttach对象的json数据")
    })
    public R update(@RequestBody SysAttach sysAttach){
        boolean update = sysAttachService.updateById(sysAttach);
        return R.status(update);
    }

    @ApiOperation("删除附件")
    @PostMapping("/delete")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "多个id值",name = "ids"),
    })
    public R updateCoinType(@RequestBody List<String> ids){
        boolean delete = sysAttachService.removeByIds(ids);
        return R.data(delete);
    }
}
