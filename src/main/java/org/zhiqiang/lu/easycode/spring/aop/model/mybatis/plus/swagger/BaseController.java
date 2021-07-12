package org.zhiqiang.lu.easycode.spring.aop.model.mybatis.plus.swagger;

import java.util.List;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

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
  @ApiOperation(value = "保存", notes = "公用方法")
  public boolean save(@ApiParam(value = "实体数据", required = true) @RequestBody T entity) {
    return service.save(entity);
  }

  @PostMapping("/save_or_update")
  @ApiOperation(value = "保存或更新", notes = "公用方法")
  public boolean saveOrUpdate(@ApiParam(value = "实体数据", required = true) @RequestBody T entity) {
    return service.saveOrUpdate(entity);
  }

  @PostMapping("/update_by_id")
  @ApiOperation(value = "通过主键更新", notes = "公用方法")
  public boolean updateById(@ApiParam(value = "实体数据", required = true) @RequestBody T entity) {
    return service.updateById(entity);
  }

  @PostMapping("/update_by_criterias")
  @ApiOperation(value = "通过条件更新", notes = "公用方法")
  public boolean updateByCriterias(
      @ApiParam(value = "实体数据", required = true) @RequestBody MybatisPlusEntity.ObjectEntity<T> object) {
    QueryWrapper<T> queryWrapper = new QueryWrapper<T>();
    for (MybatisPlusEntity.CriteriaEntity criteria : object.getCriterias()) {
      queryWrapper(queryWrapper, criteria);
    }
    return service.update(object.getEntity(), queryWrapper);
  }

  @PostMapping("/remove_by_id")
  @ApiOperation(value = "通过主键删除", notes = "公用方法")
  public boolean removeById(@ApiParam(value = "主键", required = true) @RequestParam String id) {
    return service.removeById(id);
  }

  @PostMapping("/remove_by_ids")
  @ApiOperation(value = "通过多主键删除", notes = "公用方法")
  public boolean removeByIds(@ApiParam(value = "主键集合", required = true) @RequestBody List<String> ids) {
    return service.removeByIds(ids);
  }

  @PostMapping("/remove_by_criterias")
  @ApiOperation(value = "通过条件删除", notes = "公用方法")
  public boolean removeByCriterias(
      @ApiParam(value = "查询条件") @RequestBody(required = false) List<MybatisPlusEntity.CriteriaEntity> criterias) {
    QueryWrapper<T> queryWrapper = new QueryWrapper<T>();
    for (MybatisPlusEntity.CriteriaEntity criteria : criterias) {
      queryWrapper(queryWrapper, criteria);
    }
    return service.remove(queryWrapper);
  }

  @GetMapping("/get_by_id")
  @ApiOperation(value = "通过主键查询", notes = "公用方法")
  public T getById(@ApiParam(value = "主键", required = true) @RequestParam String id) {
    return service.getById(id);
  }

  @PostMapping("/get_one_by_criterias")
  @ApiOperation(value = "通过条件返回一条信息", notes = "公用方法")
  public T getOneByCriterias(
      @ApiParam(value = "查询条件") @RequestBody(required = false) List<MybatisPlusEntity.CriteriaEntity> criterias) {
    QueryWrapper<T> queryWrapper = new QueryWrapper<T>();
    if (criterias != null) {
      for (MybatisPlusEntity.CriteriaEntity criteria : criterias) {
        queryWrapper(queryWrapper, criteria);
      }
    }
    return service.getOne(queryWrapper);
  }

  @GetMapping("/list")
  @ApiOperation(value = "查询全部信息", notes = "公用方法")
  public List<T> list() {
    return service.list();
  }

  @PostMapping("/list_extend")
  @ApiOperation(value = "查询全部信息_扩展", notes = "公用方法")
  public List<T> listExtend(
      @ApiParam(value = "查询条件数据") @RequestBody(required = false) MybatisPlusEntity.ListEntity list) {
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
  @ApiOperation(value = "查询分页信息", notes = "公用方法")
  public Page<T> page(@ApiParam(value = "页码", required = true) @RequestParam Integer current,
      @ApiParam(value = "数量", required = true) @RequestParam Integer size) {
    Page<T> page = new Page<T>(current, size);
    return service.page(page);
  }

  @PostMapping("/page_extend")
  @ApiOperation(value = "查询分页信息扩展", notes = "公用方法")
  public Page<T> pageExtend(
      @ApiParam(value = "查询分页条件数据", required = true) @RequestBody MybatisPlusEntity.PageEntity page) {
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
