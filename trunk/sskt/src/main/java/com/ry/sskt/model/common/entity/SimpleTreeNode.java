package com.ry.sskt.model.common.entity;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.LinkedList;
import java.util.List;

@Data
@Accessors(chain = true)
public class SimpleTreeNode {
    /// <summary>
    /// 父节点Id
    /// </summary>
    @JSONField(name = "ParentNodeId")
    public int parentNodeId;

    /// <summary>
    /// 节点Id
    /// </summary>
    @JSONField(name = "NodeId")
    public int nodeId;

    /// <summary>
    /// 节点名称
    /// </summary>
    @JSONField(name = "NodeName")
    public String nodeName;

    /// <summary>
    /// 排序Id
    /// </summary>
    @JSONField(name = "OrderIndex")
    public int orderIndex;

    /// <summary>
    /// 子层节点
    /// </summary>
    @JSONField(name = "Children")
    public List<SimpleTreeNode> children;

    public SimpleTreeNode() {
        this.children = new LinkedList<>();
    }

    public SimpleTreeNode(Integer parentNodeId, int nodeId, String nodeName, int orderIndex) {
        this.parentNodeId = parentNodeId;
        this.nodeId = nodeId;
        this.nodeName = nodeName;
        this.orderIndex = orderIndex;
    }

    public SimpleTreeNode(Integer parentNodeId, int nodeId, String nodeName, int orderIndex, List<SimpleTreeNode> children) {
        this.parentNodeId = parentNodeId;
        this.nodeId = nodeId;
        this.nodeName = nodeName;
        this.orderIndex = orderIndex;
        this.children = children;
    }
}
