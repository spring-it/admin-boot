package cn.mesmile.admin.modules.system.controller;

import cn.hutool.core.lang.Dict;
import cn.mesmile.admin.modules.system.domain.vo.DictVO;
import cn.mesmile.admin.modules.system.wrapper.DictWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
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
import cn.mesmile.admin.modules.system.service.ISysDictService;
import cn.mesmile.admin.modules.system.entity.SysDict;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import cn.mesmile.admin.common.result.R;
import cn.mesmile.admin.common.utils.FunctionUtil;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * <p>
 * 字典 前端控制器
 * </p>
 *
 * @author zb
 */
@Validated
@Api(value = "字典相关api",tags = {"字典相关api"})
@RequiredArgsConstructor
@RestController
@RequestMapping("/sys-dict")
public class SysDictController {

    private final ISysDictService sysDictService;

    @ApiOperation("分页查询字典")
    @GetMapping("/get")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "当前页",name = "current",defaultValue = "1"),
            @ApiImplicitParam(value = "每页显示条数",name = "size",defaultValue = "10"),
    })
    public R<Page<SysDict>> findSysDictPage(@ApiIgnore Page<SysDict> page){
        Page<SysDict> result = sysDictService.findSysDictPage(page);
        return R.data(result);
    }

    @ApiOperation("获取所有父节点列表")
    @GetMapping("/page-parent")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "当前页",name = "current",defaultValue = "1"),
            @ApiImplicitParam(value = "每页显示条数",name = "size",defaultValue = "10"),
            @ApiImplicitParam(value = "字典编码",name = "code",defaultValue = ""),
            @ApiImplicitParam(value = "字典名称",name = "dictValue",defaultValue = ""),
    })
    public R<IPage<DictVO>> listParentDict(@ApiIgnore Page<SysDict> page,@ApiIgnore SysDict sysDict){
        Page<SysDict> sysDictPage = sysDictService.pageParentDict(page, sysDict);
        IPage<DictVO> dictVOIPage = DictWrapper.build().pageVO(sysDictPage);
        return R.data(dictVOIPage);
    }

    @ApiOperation("获取指定节点列表")
    @GetMapping("/page-child")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "父节点id",name = "parentId",defaultValue = "", required = true),
            @ApiImplicitParam(value = "字典编码",name = "code",defaultValue = ""),
            @ApiImplicitParam(value = "字典名称",name = "dictValue",defaultValue = "")
    })
    public R<List<DictVO>> listChildDict(@ApiIgnore SysDict sysDict){
        List<SysDict> sysDictList = sysDictService.listChildDict(sysDict);
        List<DictVO> dictVOList = DictWrapper.build().listVO(sysDictList);
        return R.data(dictVOList);
    }

    @PostMapping("/save")
    @ApiOperation("新增字典")
    public R save(@Validated @RequestBody SysDict sysDict){
        boolean save = sysDictService.save(sysDict);
        return R.status(save);
    }

    @PostMapping("/update")
    @ApiOperation("修改字典")
    public R update(@Validated @RequestBody SysDict sysDict){
        boolean update = sysDictService.updateById(sysDict);
        return R.status(update);
    }

    @ApiOperation("删除字典")
    @PostMapping("/delete")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "多个id值,用英文逗号分割",name = "ids"),
    })
    public R remove(@RequestParam("ids") @NotEmpty String ids){
        boolean delete = sysDictService.removeByIds(FunctionUtil.strToLongList(ids));
        return R.status(delete);
    }

}
