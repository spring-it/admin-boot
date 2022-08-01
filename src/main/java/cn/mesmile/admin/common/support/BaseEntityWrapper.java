package cn.mesmile.admin.common.support;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 返回视图层所需的字段
 * @author zb
 * @Description
 * @param <E>
 * @param <V>
 */
public abstract class BaseEntityWrapper<E, V> {

    public BaseEntityWrapper() {
    }

    /**
     *  将实体类中部门字段转换为，视图层可看懂的字段，例如 性别 0 -> 女  ， 1 -> 男
     * @param entity
     * @return
     */
    public abstract V entityVO(E entity);

    public List<V> listVO(List<E> list) {
        return (List)list.stream().map(this::entityVO).collect(Collectors.toList());
    }

    public IPage<V> pageVO(IPage<E> pages) {
        List<V> records = this.listVO(pages.getRecords());
        IPage<V> pageVo = new Page(pages.getCurrent(), pages.getSize(), pages.getTotal());
        pageVo.setRecords(records);
        return pageVo;
    }
}