package cn.mesmile.admin.modules.system.controller;

import cn.mesmile.admin.common.result.R;
import cn.mesmile.admin.common.utils.FunctionUtil;
import cn.mesmile.admin.modules.system.entity.SysAttach;
import cn.mesmile.admin.modules.system.service.ISysAttachService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.constraints.NotEmpty;

/**
 * <p>
 * 附件 前端控制器
 * </p>
 *
 * @author zb
 */
@Validated
@Api(value = "附件相关api",tags = {"附件相关api"})
@RequiredArgsConstructor
@RestController
@RequestMapping("/sys-attach")
public class SysAttachController {

    private final ISysAttachService sysAttachService;

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
    public R save(@RequestBody @Validated SysAttach sysAttach){
        boolean save = sysAttachService.save(sysAttach);
        return R.status(save);
    }

    @PostMapping("/update")
    @ApiOperation("修改附件")
    public R update(@RequestBody @Validated SysAttach sysAttach){
        boolean update = sysAttachService.updateById(sysAttach);
        return R.status(update);
    }

    @ApiOperation("删除附件")
    @PostMapping("/delete")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "一个或多个id值,用英文逗号分割",name = "ids"),
    })
    public R remove(@RequestParam("ids") @NotEmpty String ids){
        boolean delete = sysAttachService.removeByIds(FunctionUtil.strToLongList(ids));
        return R.status(delete);
    }
}
