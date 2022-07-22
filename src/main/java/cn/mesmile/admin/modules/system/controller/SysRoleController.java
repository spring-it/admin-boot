package cn.mesmile.admin.modules.system.controller;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.GetMapping;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import springfox.documentation.annotations.ApiIgnore;
import cn.mesmile.admin.modules.system.service.ISysRoleService;
import cn.mesmile.admin.modules.system.entity.SysRole;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import cn.mesmile.admin.common.result.R;
import cn.mesmile.admin.common.utils.FunctionUtil;

import javax.validation.constraints.NotEmpty;

/**
 * <p>
 * 角色 前端控制器
 * </p>
 *
 * @author zb
 */
@Validated
@Api(value = "角色相关api",tags = {"角色相关api"})
@RequiredArgsConstructor
@RestController
@RequestMapping("/sys-role")
public class SysRoleController {

    private final ISysRoleService sysRoleService;

    @ApiOperation("分页查询角色")
    @GetMapping("/get")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "当前页",name = "current",defaultValue = "1"),
            @ApiImplicitParam(value = "每页显示条数",name = "size",defaultValue = "10"),
    })
    public R<Page<SysRole>> findSysRolePage(@ApiIgnore Page<SysRole> page){
        Page<SysRole> result = sysRoleService.findSysRolePage(page);
        return R.data(result);
    }

    @PostMapping("/save")
    @ApiOperation("新增角色")
    public R save(@RequestBody SysRole sysRole){
        boolean save = sysRoleService.save(sysRole);
        return R.status(save);
    }

    @PostMapping("/update")
    @ApiOperation("修改角色")
    public R update(@RequestBody SysRole sysRole){
        boolean update = sysRoleService.updateById(sysRole);
        return R.status(update);
    }

    @ApiOperation("删除角色")
    @PostMapping("/delete")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "多个id值,用英文逗号分割",name = "ids"),
    })
    public R remove(@RequestParam("ids") @NotEmpty String ids){
        boolean delete = sysRoleService.removeByIds(FunctionUtil.strToLongList(ids));
        return R.status(delete);
    }

}
