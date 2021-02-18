package com.ry.sskt.model.common.entity;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import lombok.experimental.Accessors;
import org.apache.commons.lang.StringUtils;

import java.util.LinkedList;
import java.util.List;

@Data
@Accessors(chain = true)
public class ChapterSection {
    @JSONField(name = "ParentNodeId")
    private int parentNodeId;
    @JSONField(name = "NodeId")
    private int nodeId;
    @JSONField(name = "NodeName")
    private String nodeName;
    @JSONField(name = "OrderIndex")
    private int orderIndex;
    @JSONField(name = "Children")
    private List<ChapterSection> children;
    @JSONField(name = "CourseMappingId")
    private int courseMappingId;

    public ChapterSection() {
        nodeName = StringUtils.EMPTY;
    }

    /// <summary>
    /// 返回所有值,无树结构
    /// </summary>
    /// <returns></returns>
    public static List<ChapterSection> GetAllSections(List<ChapterSection> sections) {
        List result = new LinkedList<ChapterSection>();
        sections.forEach(m -> {
            result.add(m);
            result.addAll(m.getChildren());
        });
        return result;
    }
}
