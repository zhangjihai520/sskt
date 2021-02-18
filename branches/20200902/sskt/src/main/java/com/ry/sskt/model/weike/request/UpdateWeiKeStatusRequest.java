package com.ry.sskt.model.weike.request;

import com.alibaba.fastjson.JSONArray;
import com.ry.sskt.core.utils.UrlUtil;
import com.ry.sskt.model.common.request.RequestBase;
import com.ry.sskt.model.subject.entity.WeiKe;
import com.ry.sskt.model.video.entity.VHVideoView;
import lombok.Data;
import lombok.experimental.Accessors;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

/**
 * 修改微课状态
 */
@Data
@Accessors(chain = true)
public class UpdateWeiKeStatusRequest extends RequestBase {
    /// <summary>
    /// 0删除，1待上架、2待审核、3已上架、4已驳回
    /// </summary>
    private int StatusFlag;

    /// <summary>
    /// 微课的Id
    /// </summary>
    private List<String> UpdateIds;
}
