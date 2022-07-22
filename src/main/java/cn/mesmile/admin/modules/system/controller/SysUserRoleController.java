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
import cn.mesmile.admin.modules.system.service.ISysUserRoleService;
import cn.mesmile.admin.modules.system.entity.SysUserRole;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import cn.mesmile.admin.common.result.R;
import cn.mesmile.admin.common.utils.FunctionUtil;

import javax.validation.constraints.NotEmpty;

/**
 * <p>
 * 用户和角色关联 前端控制器
 * </p>
 *
 * @author zb
 */
@Validated
@Api(value = "用户和角色关联相关api",tags = {"用户和角色关联相关api"})
@RequiredArgsConstructor
@RestController
@RequestMapping("/sys-user-role")
public class SysUserRoleController {

    private final ISysUserRoleService sysUserRoleService;

    @ApiOperation("分页查询用户和角色关联")
    @GetMapping("/get")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "当前页",name = "current",defaultValue = "1"),
            @ApiImplicitParam(value = "每页显示条数",name = "size",defaultValue = "10"),
    })
    public R<Page<SysUserRole>> findSysUserRolePage(@ApiIgnore Page<SysUserRole> page){
        Page<SysUserRole> result = sysUserRoleService.findSysUserRolePage(page);
        return R.data(result);
    }

    @PostMapping("/save")
    @ApiOperation("新增用户和角色关联")
    public R save(@RequestBody SysUserRole sysUserRole){
        boolean save = sysUserRoleService.save(sysUserRole);
        return R.status(save);
    }

    @PostMapping("/update")
    @ApiOperation("修改用户和角色关联")
    public R update(@RequestBody SysUserRole sysUserRole){
        boolean update = sysUserRoleService.updateById(sysUserRole);
        return R.status(update);
    }

    @ApiOperation("删除用户和角色关联")
    @PostMapping("/delete")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "多个id值,用英文逗号分割",name = "ids"),
    })
    public R remove(@RequestParam("ids") @NotEmpty String ids){
        boolean delete = sysUserRoleService.removeByIds(FunctionUtil.strToLongList(ids));
        return R.status(delete);
    }

}
