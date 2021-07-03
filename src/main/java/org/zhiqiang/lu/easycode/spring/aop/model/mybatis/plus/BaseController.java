package org.zhiqiang.lu.easycode.spring.aop.model.mybatis.plus;

import java.util.List;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @Author Q
 * @Description 实现泛型父类
 * @Date 2021/6/26 10:49
 */
public class BaseController<S extends IService<T>, T> {

  @Autowired
  @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
  private S service;

  public S getBaseService() {
    return this.service;
  }

  @PostMapping("/save")
  public boolean save(@RequestBody T entity) {
    return service.save(entity);
  }

  @PostMapping("/save_or_update")
  public boolean saveOrUpdate(@RequestBody T entity) {
    return service.saveOrUpdate(entity);
  }

  @PostMapping("/update_by_id")
  public boolean updateById(@RequestBody T entity) {
    return service.updateById(entity);
  }

  @PostMapping("/update_by_criterias")
  public boolean updateByCriterias(@RequestBody MybatisPlusEntity.ObjectEntity<T> object) {
    QueryWrapper<T> queryWrapper = new QueryWrapper<T>();
    for (MybatisPlusEntity.CriteriaEntity criteria : object.getCriterias()) {
      queryWrapper.apply(StringUtils.isNotBlank(criteria.getSql()), criteria.getSql(), criteria.getParams());
    }
    return service.update(object.getEntity(), queryWrapper);
  }

  @PostMapping("/remove_by_id")
  public boolean removeById(@RequestParam String id) {
    return service.removeById(id);
  }

  @PostMapping("/remove_by_ids")
  public boolean removeByIds(@RequestBody List<String> ids) {
    return service.removeByIds(ids);
  }

  @PostMapping("/remove_by_criterias")
  public boolean removeByCriterias(List<MybatisPlusEntity.CriteriaEntity> criterias) {
    QueryWrapper<T> queryWrapper = new QueryWrapper<T>();
    for (MybatisPlusEntity.CriteriaEntity criteria : criterias) {
      queryWrapper.apply(StringUtils.isNotBlank(criteria.getSql()), criteria.getSql(), criteria.getParams());
    }
    return service.remove(queryWrapper);
  }

  @GetMapping("/get_by_id")
  public T getById(@RequestParam String id) {
    return service.getById(id);
  }

  @PostMapping("/get_one_by_criterias")
  public T getOneByCriterias(List<MybatisPlusEntity.CriteriaEntity> criterias) {
    QueryWrapper<T> queryWrapper = new QueryWrapper<T>();
    if (criterias != null) {
      for (MybatisPlusEntity.CriteriaEntity criteria : criterias) {
        queryWrapper.apply(StringUtils.isNotBlank(criteria.getSql()), criteria.getSql(), criteria.getParams());
      }
    }
    return service.getOne(queryWrapper);
  }

  @GetMapping("/list")
  public List<T> list() {
    return service.list();
  }

  @PostMapping("/list_extend")
  public List<T> listExtend(MybatisPlusEntity.ListEntity list) {
    QueryWrapper<T> queryWrapper = new QueryWrapper<T>();
    if (list != null) {
      for (MybatisPlusEntity.CriteriaEntity criteria : list.getCriterias()) {
        queryWrapper.apply(StringUtils.isNotBlank(criteria.getSql()), criteria.getSql(), criteria.getParams());
      }
      for (OrderItem io : list.getOrderItems()) {
        if (StringUtils.isNotBlank(io.getColumn())) {
          if (io.isAsc()) {
            queryWrapper.orderByAsc(io.getColumn());
          } else {
            queryWrapper.orderByDesc(io.getColumn());
          }
        }
      }
    }
    return service.list(queryWrapper);
  }

  @GetMapping("/page")
  public Page<T> page(@RequestParam Integer current, @RequestParam Integer size) {
    Page<T> page = new Page<T>(current, size);
    return service.page(page);
  }

  @PostMapping("/page_extend")
  public Page<T> pageExtend(@RequestBody MybatisPlusEntity.PageEntity page) {
    QueryWrapper<T> queryWrapper = new QueryWrapper<T>();
    Page<T> p = new Page<T>(page.getCurrent(), page.getSize());
    p.setOrders(page.getOrderItems());
    for (MybatisPlusEntity.CriteriaEntity criteria : page.getCriterias()) {
      queryWrapper.apply(StringUtils.isNotBlank(criteria.getSql()), criteria.getSql(), criteria.getParams());
    }
    return service.page(p, queryWrapper);
  }
}
