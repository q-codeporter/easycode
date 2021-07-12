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
      queryWrapper(queryWrapper, criteria);
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
      queryWrapper(queryWrapper, criteria);
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
        queryWrapper(queryWrapper, criteria);
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
        queryWrapper(queryWrapper, criteria);
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
      queryWrapper(queryWrapper, criteria);
    }
    return service.page(p, queryWrapper);
  }

  private void queryWrapper(QueryWrapper<T> queryWrapper, MybatisPlusEntity.CriteriaEntity criteria) {
    if ("eq".equals(criteria.getType()) && criteria.getParams() != null) {
      queryWrapper.eq(StringUtils.isNotBlank(criteria.getParams().get(0)), criteria.getColumn(),
          criteria.getParams().get(0));
    }
    if ("ne".equals(criteria.getType()) && criteria.getParams() != null) {
      queryWrapper.ne(StringUtils.isNotBlank(criteria.getParams().get(0)), criteria.getColumn(),
          criteria.getParams().get(0));
    }
    if ("gt".equals(criteria.getType()) && criteria.getParams() != null) {
      queryWrapper.gt(StringUtils.isNotBlank(criteria.getParams().get(0)), criteria.getColumn(),
          criteria.getParams().get(0));
    }
    if ("ge".equals(criteria.getType()) && criteria.getParams() != null) {
      queryWrapper.ge(StringUtils.isNotBlank(criteria.getParams().get(0)), criteria.getColumn(),
          criteria.getParams().get(0));
    }
    if ("lt".equals(criteria.getType()) && criteria.getParams() != null) {
      queryWrapper.lt(StringUtils.isNotBlank(criteria.getParams().get(0)), criteria.getColumn(),
          criteria.getParams().get(0));
    }
    if ("le".equals(criteria.getType()) && criteria.getParams() != null) {
      queryWrapper.le(StringUtils.isNotBlank(criteria.getParams().get(0)), criteria.getColumn(),
          criteria.getParams().get(0));
    }
    if ("between".equals(criteria.getType()) && criteria.getParams() != null) {
      queryWrapper.between(criteria.getParams().size() == 2, criteria.getColumn(), criteria.getParams().get(0),
          criteria.getParams().get(1));
    }
    if ("notBetween".equals(criteria.getType()) && criteria.getParams() != null) {
      queryWrapper.notBetween(criteria.getParams().size() == 2, criteria.getColumn(), criteria.getParams().get(0),
          criteria.getParams().get(1));
    }
    if ("like".equals(criteria.getType()) && criteria.getParams() != null) {
      queryWrapper.like(StringUtils.isNotBlank(criteria.getParams().get(0)), criteria.getColumn(),
          criteria.getParams().get(0));
    }
    if ("likeLeft".equals(criteria.getType()) && criteria.getParams() != null) {
      queryWrapper.likeLeft(StringUtils.isNotBlank(criteria.getParams().get(0)), criteria.getColumn(),
          criteria.getParams().get(0));
    }
    if ("likeRight".equals(criteria.getType()) && criteria.getParams() != null) {
      queryWrapper.likeRight(StringUtils.isNotBlank(criteria.getParams().get(0)), criteria.getColumn(),
          criteria.getParams().get(0));
    }
    if ("notLike".equals(criteria.getType()) && criteria.getParams() != null) {
      queryWrapper.notLike(StringUtils.isNotBlank(criteria.getParams().get(0)), criteria.getColumn(),
          criteria.getParams().get(0));
    }
  }
}
