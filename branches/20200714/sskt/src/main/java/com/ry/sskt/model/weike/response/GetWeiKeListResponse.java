package com.ry.sskt.model.weike.response;

import com.ry.sskt.core.utils.UrlUtil;
import com.ry.sskt.model.common.entity.Course;
import com.ry.sskt.model.common.entity.Grade;
import com.ry.sskt.model.common.entity.IdName;
import com.ry.sskt.model.subject.entity.WeiKe;
import com.ry.sskt.model.video.entity.VHVideoView;
import com.ry.sskt.model.weike.entity.GetWeiKeListResponseItem;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.LinkedList;
import java.util.List;

/**
 * 读取微课列表
 */
@Data
@Accessors(chain = true)
public class GetWeiKeListResponse {

    /// <summary>
    /// 总数
    /// </summary>
    private int TotalCount;

    /// <summary>
    /// 列表
    /// </summary>
    public List<GetWeiKeListResponseItem> WeiKeList;

    public GetWeiKeListResponse() {
        WeiKeList = new LinkedList<>();
    }

    /// <summary>
    /// 构建
    /// </summary>
    /// <param name="total"></param>
    /// <param name="weikes"></param>
    /// <param name="bookVersions"></param>
    /// <param name="courseMappings"></param>
    /// <param name="sections"></param>
    /// <param name="courses"></param>
    /// <param name="grades"></param>
    /// <returns></returns>
    public GetWeiKeListResponse(int total, List<WeiKe> weikes, List<IdName> bookVersions, List<IdName> courseMappings, List<IdName> sections, List<Course> courses, List<Grade> grades) {

        TotalCount = total;
        List<GetWeiKeListResponseItem> list = new LinkedList<>();
        weikes.forEach(m -> {
            list.add(new GetWeiKeListResponseItem(m, bookVersions, courseMappings, sections, courses, grades));
        });
        WeiKeList = list;
    }
}
