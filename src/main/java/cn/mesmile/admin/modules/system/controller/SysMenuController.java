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
import cn.mesmile.admin.modules.system.service.ISysMenuService;
import cn.mesmile.admin.modules.system.entity.SysMenu;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import cn.mesmile.admin.common.result.R;
import cn.mesmile.admin.common.utils.FunctionUtil;

import javax.validation.constraints.NotEmpty;

/**
 * <p>
 * 菜单权限表 前端控制器
 * </p>
 *
 * @author zb
 */
@Validated
@Api(value = "菜单权限表相关api",tags = {"菜单权限表相关api"})
@RequiredArgsConstructor
@RestController
@RequestMapping("/sys-menu")
public class SysMenuController {

    private final ISysMenuService sysMenuService;

    @ApiOperation("分页查询菜单权限表")
    @GetMapping("/get")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "当前页",name = "current",defaultValue = "1"),
            @ApiImplicitParam(value = "每页显示条数",name = "size",defaultValue = "10"),
    })
    public R<Page<SysMenu>> findSysMenuPage(@ApiIgnore Page<SysMenu> page){
        Page<SysMenu> result = sysMenuService.findSysMenuPage(page);
        return R.data(result);
    }

    @PostMapping("/save")
    @ApiOperation("新增菜单权限表")
    public R save(@Validated @RequestBody SysMenu sysMenu){
        boolean save = sysMenuService.save(sysMenu);
        return R.status(save);
    }

    @PostMapping("/update")
    @ApiOperation("修改菜单权限表")
    public R update(@Validated @RequestBody SysMenu sysMenu){
        boolean update = sysMenuService.updateById(sysMenu);
        return R.status(update);
    }


    @ApiOperation("删除菜单权限表")
    @PostMapping("/delete")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "多个id值,用英文逗号分割",name = "ids"),
    })
    public R remove(@RequestParam("ids") @NotEmpty String ids){
        boolean delete = sysMenuService.deleteLogic(FunctionUtil.strToLongList(ids));
        return R.status(delete);
    }
}
