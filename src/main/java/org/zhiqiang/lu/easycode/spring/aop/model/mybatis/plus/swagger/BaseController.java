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
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

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

  @PostMapping("/base")
  @ApiOperation(value = "保存", notes = "公用方法", position = 1)
  public boolean save(@ApiParam(value = "实体数据", required = true) @RequestBody T entity) {
    return service.save(entity);
  }

  @PutMapping("/base/save_or_update")
  @ApiOperation(value = "保存&更新", notes = "公用方法", position = 2)
  public boolean saveOrUpdate(@ApiParam(value = "实体数据", required = true) @RequestBody T entity) {
    return service.saveOrUpdate(entity);
  }

  @PutMapping("/base")
  @ApiOperation(value = "更新", notes = "公用方法", position = 11)
  public boolean updateById(@ApiParam(value = "实体数据", required = true) @RequestBody T entity) {
    return service.updateById(entity);
  }

  @PutMapping("/base/criteria")
  @ApiOperation(value = "更新-通过条件", notes = "公用方法", position = 12)
  public boolean updateByCriterias(
      @ApiParam(value = "实体数据、查询条件", required = true) @RequestBody MybatisPlusEntity.ObjectEntity<T> object) {
    QueryWrapper<T> queryWrapper = new QueryWrapper<T>();
    for (MybatisPlusEntity.CriteriaEntity criteria : object.getCriterias()) {
      queryWrapper(queryWrapper, criteria);
    }
    return service.update(object.getEntity(), queryWrapper);
  }

  @DeleteMapping("/base/{id}")
  @ApiOperation(value = "删除", notes = "公用方法", position = 21)
  public boolean removeById(@ApiParam(value = "主键集合，隔开", required = true) @PathVariable List<String> id) {
    return service.removeByIds(id);
  }

  @GetMapping("/base/{id}")
  @ApiOperation(value = "查询", notes = "公用方法", position = 31)
  public T getById(@ApiParam(value = "主键", required = true) @PathVariable String id) {
    return service.getById(id);
  }

  @PostMapping("/base/{current}/{size}")
  @ApiOperation(value = "查询-分页", notes = "公用方法", position = 32)
  public Page<T> pageExtend(@ApiParam(value = "当前页码", required = true) @PathVariable int current,
      @ApiParam(value = "每页数量", required = true) @PathVariable int size,
      @ApiParam(value = "查询条件") @RequestBody(required = false) MybatisPlusEntity.ListEntity criterias) {
    QueryWrapper<T> queryWrapper = new QueryWrapper<T>();
    Page<T> p = new Page<T>(current, size);
    if (criterias != null) {
      for (MybatisPlusEntity.CriteriaEntity criteria : criterias.getCriterias()) {
        queryWrapper(queryWrapper, criteria);
      }
      for (OrderItem io : criterias.getOrderItems()) {
        if (StringUtils.isNotBlank(io.getColumn())) {
          if (io.isAsc()) {
            queryWrapper.orderByAsc(io.getColumn());
          } else {
            queryWrapper.orderByDesc(io.getColumn());
          }
        }
      }
    }
    return service.page(p, queryWrapper);
  }

  @PostMapping("/base/one")
  @ApiOperation(value = "查询-一条数据", notes = "公用方法", position = 33)
  public T getOneByCriterias(
      @ApiParam(value = "查询条件") @RequestBody(required = false) MybatisPlusEntity.ListEntity criterias) {
    QueryWrapper<T> queryWrapper = new QueryWrapper<T>();
    if (criterias != null) {
      for (MybatisPlusEntity.CriteriaEntity criteria : criterias.getCriterias()) {
        queryWrapper(queryWrapper, criteria);
      }
      for (OrderItem io : criterias.getOrderItems()) {
        if (StringUtils.isNotBlank(io.getColumn())) {
          if (io.isAsc()) {
            queryWrapper.orderByAsc(io.getColumn());
          } else {
            queryWrapper.orderByDesc(io.getColumn());
          }
        }
      }
    }
    return service.getOne(queryWrapper);
  }

  @PostMapping("/base/list")
  @ApiOperation(value = "查询-多条数据", notes = "公用方法", position = 34)
  public List<T> listExtend(
      @ApiParam(value = "查询条件") @RequestBody(required = false) MybatisPlusEntity.ListEntity criterias) {
    QueryWrapper<T> queryWrapper = new QueryWrapper<T>();
    if (criterias != null) {
      for (MybatisPlusEntity.CriteriaEntity criteria : criterias.getCriterias()) {
        queryWrapper(queryWrapper, criteria);
      }
      for (OrderItem io : criterias.getOrderItems()) {
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
