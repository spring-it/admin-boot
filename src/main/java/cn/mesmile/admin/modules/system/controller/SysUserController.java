package cn.mesmile.admin.modules.system.controller;

import cn.mesmile.admin.common.result.R;
import cn.mesmile.admin.common.utils.FunctionUtil;
import cn.mesmile.admin.modules.system.entity.SysUser;
import cn.mesmile.admin.modules.system.service.ISysUserService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.constraints.NotEmpty;

/**
 * <p>
 * 用户信息 前端控制器
 * </p>
 *
 * @author zb
 */
@Validated
@Api(value = "用户信息相关api",tags = {"用户信息相关api"})
@RequiredArgsConstructor
@RestController
@RequestMapping("/sys-user")
public class SysUserController {

    private final ISysUserService sysUserService;

    @PreAuthorize("hasAuthority('system:user:list')")
    @ApiOperation("分页查询用户信息")
    @GetMapping("/get")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "当前页",name = "current",defaultValue = "1"),
            @ApiImplicitParam(value = "每页显示条数",name = "size",defaultValue = "10"),
    })
    public R<Page<SysUser>> findSysUserPage(@ApiIgnore Page<SysUser> page){
        Page<SysUser> result = sysUserService.findSysUserPage(page);
        return R.data(result);
    }

    @PostMapping("/save")
    @ApiOperation("新增用户信息")
    public R save(@RequestBody SysUser sysUser){
        boolean save = sysUserService.save(sysUser);
        return R.status(save);
    }

    @PostMapping("/update")
    @ApiOperation("修改用户信息")
    public R update(@RequestBody SysUser sysUser){
        boolean update = sysUserService.updateById(sysUser);
        return R.status(update);
    }


    @ApiOperation("删除用户信息")
    @PostMapping("/delete")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "多个id值,用英文逗号分割",name = "ids"),
    })
    public R remove(@RequestParam("ids") @NotEmpty String ids){
        boolean delete = sysUserService.deleteLogic(FunctionUtil.strToLongList(ids));
        return R.status(delete);
    }
}
