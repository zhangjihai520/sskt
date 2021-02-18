package com.ry.sskt.service;

import com.ry.sskt.model.video.entity.CreateResult;
import com.ry.sskt.model.video.entity.VHVideoView;
import com.ry.sskt.model.video.entity.view.DeviceView;

import javax.xml.rpc.ServiceException;
import java.rmi.RemoteException;
import java.util.List;

/**
 * 和硬件对接BLL
 */
public interface IVideoHardwareService {
    /// <summary>
    /// 获取视频房间列表
    /// </summary>
    /// <returns></returns>
    List<DeviceView> GetDeviceList() throws ServiceException, RemoteException;

    /// <summary>
    /// 创建会议接口
    /// </summary>
    /// <param name="subjectName">课堂名称</param>
    /// <param name="subjectId">课堂Id 加密 格式 sub_{id}</param>
    /// <param name="sDeviceId">设备ID</param>
    /// <param name="typeEnum">1创建 2修改</param>
    /// <returns></returns>
    CreateResult createConf(String subjectName, String subjectId, String sDeviceId, int typeEnum) throws ServiceException, RemoteException;

    /// <summary>
    /// 根据课堂ID获取录播地址接口
    /// </summary>
    /// <param name="sConfid"></param>
    /// <returns></returns>
    List<VHVideoView> queryConfRecordFilesRequest(String sConfid) throws ServiceException, RemoteException;

    /// <summary>
    /// 获取硬件的链接数量
    /// </summary>
    /// <returns></returns>
    int GetSessionCount();

    /// <summary>
    /// 获取视频点播地址
    /// </summary>
    /// <param name="rtmpPath">视频点播地址</param>
    /// <param name="Id">课程ID或者微课ID</param>
    /// <param name="isSubject">是否是课程 true是 false不是</param>
    /// <param name="index">线路里面的第几块</param>
    /// <param name="typeId">1是老师线路视频 2 是PPt线路</param>
    /// <returns></returns>
    String GetVideoPath(String rtmpPath, int Id, boolean isSubject, int index, int typeId);

    /// <summary>
    /// 添加阿里云拉流直播配置
    /// </summary>
    /// <param name="rtmpPath"></param>
    /// <returns></returns>
    boolean CreateAliLivePath(String rtmpPath) throws Exception;

    /// <summary>
    /// 删除阿里云直播拉流配置
    /// </summary>
    /// <param name="id"></param>
    /// <param name="rtmpPath"></param>
    /// <param name="typeId"></param>
    boolean RemAliLiveConfigs(int id, String rtmpPath, int typeId);
}
