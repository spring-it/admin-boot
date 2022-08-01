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
import cn.mesmile.admin.modules.system.service.ISysRoleMenuService;
import cn.mesmile.admin.modules.system.entity.SysRoleMenu;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import cn.mesmile.admin.common.result.R;
import cn.mesmile.admin.common.utils.FunctionUtil;

import javax.validation.constraints.NotEmpty;

/**
 * <p>
 * 角色和菜单关联 前端控制器
 * </p>
 *
 * @author zb
 */
@Validated
@Api(value = "角色和菜单关联相关api",tags = {"角色和菜单关联相关api"})
@RequiredArgsConstructor
@RestController
@RequestMapping("/sys-role-menu")
public class SysRoleMenuController {

    private final ISysRoleMenuService sysRoleMenuService;

    @ApiOperation("分页查询角色和菜单关联")
    @GetMapping("/get")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "当前页",name = "current",defaultValue = "1"),
            @ApiImplicitParam(value = "每页显示条数",name = "size",defaultValue = "10"),
    })
    public R<Page<SysRoleMenu>> findSysRoleMenuPage(@ApiIgnore Page<SysRoleMenu> page){
        Page<SysRoleMenu> result = sysRoleMenuService.findSysRoleMenuPage(page);
        return R.data(result);
    }

    @PostMapping("/save")
    @ApiOperation("新增角色和菜单关联")
    public R save(@Validated @RequestBody SysRoleMenu sysRoleMenu){
        boolean save = sysRoleMenuService.save(sysRoleMenu);
        return R.status(save);
    }

    @PostMapping("/update")
    @ApiOperation("修改角色和菜单关联")
    public R update(@Validated @RequestBody SysRoleMenu sysRoleMenu){
        boolean update = sysRoleMenuService.updateById(sysRoleMenu);
        return R.status(update);
    }

    @ApiOperation("删除角色和菜单关联")
    @PostMapping("/delete")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "多个id值,用英文逗号分割",name = "ids"),
    })
    public R remove(@RequestParam("ids") @NotEmpty String ids){
        boolean delete = sysRoleMenuService.removeByIds(FunctionUtil.strToLongList(ids));
        return R.status(delete);
    }

}
