package org.zhiqiang.lu.easycode.spring.aop.model.mybatis.plus;

import java.util.List;

import com.baomidou.mybatisplus.core.metadata.OrderItem;

public class MybatisPlusEntity {
  public static class PageEntity {
   
    private Integer current = 1;

    private Integer size = 10;

    private List<OrderItem> orderItems;

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

    private List<OrderItem> orderItems;

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

    private T entity;

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

  public static class CriteriaEntity {
    private String type;

    private String column;

    private List<String> params;

    public String getType() {
      return type;
    }

    public void setType(String type) {
      this.type = type;
    }

    public String getColumn() {
      return column;
    }

    public void setColumn(String column) {
      this.column = column;
    }

    public List<String> getParams() {
      return params;
    }

    public void setParams(List<String> params) {
      this.params = params;
    }
  }
}