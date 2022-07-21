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
import cn.mesmile.admin.modules.system.service.SysUserService;
import cn.mesmile.admin.modules.system.entity.SysUser;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import cn.mesmile.admin.common.result.R;
import java.util.List;

/**
 * <p>
 * 用户信息表 前端控制器
 * </p>
 *
 * @author zb
 */
@Api(value = "用户信息表相关api",tags = {"用户信息表相关api"})
@RequiredArgsConstructor
@RestController
@RequestMapping("/sys-user")
public class SysUserController {

    private final SysUserService sysUserService;

    @ApiOperation("分页查询用户信息表")
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
    @ApiOperation("新增用户信息表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "sysUser", value = "sysUser对象的json数据")
    })
    public R save(@RequestBody SysUser sysUser){
        boolean save = sysUserService.save(sysUser);
        return R.status(save);
    }

    @PostMapping("/update")
    @ApiOperation("修改用户信息表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "sysUser", value = "sysUser对象的json数据")
    })
    public R update(@RequestBody SysUser sysUser){
        boolean update = sysUserService.updateById(sysUser);
        return R.status(update);
    }

    @ApiOperation("删除用户信息表")
    @PostMapping("/delete")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "多个id值",name = "ids"),
    })
    public R updateCoinType(@RequestBody List<String> ids){
        boolean delete = sysUserService.removeByIds(ids);
        return R.data(delete);
    }
}
