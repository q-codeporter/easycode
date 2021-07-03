package org.zhiqiang.lu.easycode.spring.aop.model.mybatis.plus.swagger;

import java.util.List;

import com.baomidou.mybatisplus.core.metadata.OrderItem;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

public class MybatisPlusEntity {
  public static class PageEntity {
    @ApiModelProperty(value = "页码")
    private Integer current = 1;

    @ApiModelProperty(value = "数量")
    private Integer size = 10;

    @ApiModelProperty(value = "排序方式")
    private List<OrderItem> orderItems;

    @ApiModelProperty(value = "查询条件")
    private List<CriteriaEntity> criterias;

    public Integer getCurrent() {
      return current;
    }

    public void setCurrent(Integer current) {
      this.current = current;
    }

    public Integer getSize() {
      return size;
    }

    public void setSize(Integer size) {
      this.size = size;
    }

    public List<OrderItem> getOrderItems() {
      return orderItems;
    }

    public void setOrderItems(List<OrderItem> orderItems) {
      this.orderItems = orderItems;
    }

    public List<CriteriaEntity> getCriterias() {
      return criterias;
    }

    public void setCriterias(List<CriteriaEntity> criterias) {
      this.criterias = criterias;
    }
  }

  public static class ListEntity {

    @ApiModelProperty(value = "排序方式")
    private List<OrderItem> orderItems;

    @ApiModelProperty(value = "查询条件")
    private List<CriteriaEntity> criterias;

    public List<OrderItem> getOrderItems() {
      return orderItems;
    }

    public void setOrderItems(List<OrderItem> orderItems) {
      this.orderItems = orderItems;
    }

    public List<CriteriaEntity> getCriterias() {
      return criterias;
    }

    public void setCriterias(List<CriteriaEntity> criterias) {
      this.criterias = criterias;
    }
  }

  public static class ObjectEntity<T> {

    @ApiModelProperty(value = "实体数据")
    private T entity;

    @ApiModelProperty(value = "查询条件")
    private List<CriteriaEntity> criterias;

    public T getEntity() {
      return entity;
    }

    public void setEntity(T entity) {
      this.entity = entity;
    }

    public List<CriteriaEntity> getCriterias() {
      return criterias;
    }

    public void setCriterias(List<CriteriaEntity> criterias) {
      this.criterias = criterias;
    }
  }

  @ApiModel(value = "查询条件", description = "查询条件")
  public static class CriteriaEntity {
    @ApiModelProperty(value = "sql查询条件：name like {0}")
    private String sql;

    @ApiModelProperty(value = "sql注入的值：['%小明%']")
    private List<String> params;

    public String getSql() {
      return sql;
    }

    public void setSql(String sql) {
      this.sql = sql;
    }

    public List<String> getParams() {
      return params;
    }

    public void setParams(List<String> params) {
      this.params = params;
    }
  }
}