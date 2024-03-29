package org.zhiqiang.lu.easycode.spring.aop.model.mybatis.plus.swagger;

import java.util.List;

import com.baomidou.mybatisplus.core.metadata.OrderItem;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

public class MybatisPlusEntity {

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
    @ApiModelProperty(value = "查询类型：eq、ne、gt、ge、lt、le、between、notBetween、like、likeLeft、likeRight、notLike")
    private String type;

    @ApiModelProperty(value = "匹配的的列名：user_name")
    private String column;

    @ApiModelProperty(value = "注入的值：['小明']")
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