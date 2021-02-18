package com.ry.sskt.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.live.model.v20161101.AddLivePullStreamInfoConfigRequest;
import com.aliyuncs.live.model.v20161101.AddLivePullStreamInfoConfigResponse;
import com.aliyuncs.live.model.v20161101.DeleteLivePullStreamInfoConfigRequest;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.ry.sskt.core.config.CommonConfig;
import com.ry.sskt.core.utils.CryptogramHelper;
import com.ry.sskt.core.utils.RedisUtil;
import com.ry.sskt.core.wsdl.NanChangShuangShikeTangServiceServiceLocator;
import com.ry.sskt.model.common.constant.CommonConst;
import com.ry.sskt.model.video.entity.CreateResult;
import com.ry.sskt.model.video.entity.QueryConfRecordResult;
import com.ry.sskt.model.video.entity.VHApiResultDevice;
import com.ry.sskt.model.video.entity.VHVideoView;
import com.ry.sskt.model.video.entity.cache.VideoPathCache;
import com.ry.sskt.model.video.entity.view.DeviceView;
import com.ry.sskt.service.ISubjectRoomService;
import com.ry.sskt.service.IVideoHardwareService;
import lombok.extern.slf4j.Slf4j;
import lombok.var;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.xml.rpc.ServiceException;
import java.rmi.RemoteException;
import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class VideoHardwareService implements IVideoHardwareService {

    @Autowired
    ISubjectRoomService subjectRoomService;

    @Override
    public List<DeviceView> GetDeviceList() throws ServiceException, RemoteException {
        NanChangShuangShikeTangServiceServiceLocator locator = new NanChangShuangShikeTangServiceServiceLocator();
        String deviceJson = locator.getNanChangShuangShikeTangService().getdeviceList();
        VHApiResultDevice devicesObj = JSONObject.parseObject(deviceJson, VHApiResultDevice.class);
        List<DeviceView> list = null;
        if (devicesObj != null && CollectionUtils.isNotEmpty(devicesObj.getDeviceList())) {
            list = devicesObj.getDeviceList().stream().filter(x -> x.getState().getIndex() != 3).collect(Collectors.toList());
        }
        return list;
    }

    @Override
    public CreateResult createConf(String subjectName, String subjectId, String sDeviceId, int typeEnum) throws RemoteException, ServiceException {
        log.info(String.format("开始创建会议，ID：%s name:%s deviceid:%s,type:%s", subjectId, subjectName, sDeviceId, typeEnum));
        NanChangShuangShikeTangServiceServiceLocator locator = new NanChangShuangShikeTangServiceServiceLocator();
        CreateResult vhresult;
        if (typeEnum == 1) {
            var serviceResult = locator.getNanChangShuangShikeTangService().createConf(subjectName, subjectId, sDeviceId);
            vhresult = JSONObject.parseObject(serviceResult, CreateResult.class);
        } else {
            var serviceResult = locator.getNanChangShuangShikeTangService().updateConfInfo(subjectName, subjectId, sDeviceId);
            vhresult = JSONObject.parseObject(serviceResult, CreateResult.class);
        }
        return vhresult;
    }

    @Override
    public List<VHVideoView> queryConfRecordFilesRequest(String sConfid) throws ServiceException, RemoteException {
        NanChangShuangShikeTangServiceServiceLocator locator = new NanChangShuangShikeTangServiceServiceLocator();
        var serviceResult = locator.getNanChangShuangShikeTangService().queryConfRecordFilesRequest(sConfid);
        var result = VideosFormat(serviceResult);
        return result;
    }

    /// <summary>
    /// 转换实例
    /// </summary>
    /// <param name="value"></param>
    /// <returns></returns>
    public List<VHVideoView> VideosFormat(String value) {
        var data = JSONObject.parseObject(value, QueryConfRecordResult.class);
        if (data == null) {
            data = new QueryConfRecordResult();
        }
        return data.getVideos();
    }

    @Override
    public int GetSessionCount() {
        return 0;
    }

    @Override
    public String GetVideoPath(String rtmpPath, int Id, boolean isSubject, int index, int typeId) {
        if (StringUtils.isBlank(rtmpPath)) {
            return StringUtils.EMPTY;
        }
        if (rtmpPath.indexOf("rtmp://") > -1 && isSubject) {
            //String newRtmpPath = rtmpPath.replace(CommonConst.AVCON_PUSH_PORT_1935,CommonConst.AVCON_PULL_PORT_1936);
            //var subjectRoomList = subjectRoomService.getSubjectRoomList(Id);
            //int regNumber = subjectRoomList.stream().mapToInt(SubjectRoomTeacherNameView::getRealRegisterNumber).sum();
            var path = GetVideoPath(Id, typeId);
            if (StringUtils.isNotBlank(path)) {
                log.info(MessageFormat.format("获取视频点播地址 取缓存 subjectId:{0}  TypeId:{1} path:{2}", Id, typeId, path));
                return path;
            }
            SaveVideoPath(Id, typeId, rtmpPath);
            return rtmpPath;
            /**if (regNumber > CommonConfig.getAliLiveThreshold()) {
             var path = GetVideoPath(Id, typeId);
             if (StringUtils.isNotBlank(path)) {
             log.info(MessageFormat.format("获取视频点播地址 取缓存 subjectId:{0}  TypeId:{1} path:{2}", Id, typeId, path));
             return path;
             }
             var createResult = CreateAliLivePath(rtmpPath);
             if (createResult) {
             Long second = LocalDateTime.now().toEpochSecond(ZoneOffset.of("+8"));
             var streamName = rtmpPath.substring(rtmpPath.lastIndexOf("/") + 1);
             var pullUrl = MessageFormat.format("rtmp://{0}/{1}/{2}", CommonConfig.getAliLiveHost(), CommonConfig.getAppName(), streamName);
             var timestamp = second + CommonConfig.getUrlAuthKeyExp();
             var md5Url = MessageFormat.format("/{0}/{1}-{2}-0-0-{3}", CommonConfig.getAppName(), streamName, timestamp, CommonConfig.getUrlAuthSecret());
             var outPath = MessageFormat.format("{0}?auth_key={1}-0-0-{2}", pullUrl, timestamp, SignedUtils.StandardMd5(md5Url));
             SaveVideoPath(Id, typeId, outPath);
             log.info(MessageFormat.format("获取视频点播地址 存缓存 subjectId:{0}  TypeId:{1} path:{2}", Id, typeId, outPath));
             return outPath;
             }
             }**/
        }
        return rtmpPath;
    }

    private String GetVideoPath(int subjectId, int typeId) {
        var cache = new VideoPathCache().setSubjectId(subjectId).setTypeId(typeId);
        RedisUtil.getObj(cache.getKey(), VideoPathCache.class);
        if (cache == null) {
            return null;
        }
        return cache.getPath();
    }

    private void SaveVideoPath(int subjectId, int typeId, String path) {
        var cache = new VideoPathCache().setSubjectId(subjectId).setTypeId(typeId).setPath(path);
        RedisUtil.setObj(cache, CommonConst.MINUTE_120);
    }

    private void DeleteCache(int subjectId, int typeId) {
        var cache = new VideoPathCache().setSubjectId(subjectId).setTypeId(typeId);
        RedisUtil.del(cache.getKey());
    }

    @Override
    public boolean CreateAliLivePath(String rtmpPath) {
        try {
            var accessKeyId = CryptogramHelper.Decrypt3DES(CommonConfig.getAccessKeyId(), CommonConfig.getConfigKey());
            var accessSecret = CryptogramHelper.Decrypt3DES(CommonConfig.getAccessSecret(), CommonConfig.getConfigKey());
            LocalDateTime now = LocalDateTime.now();
            IClientProfile profile = DefaultProfile.getProfile(CommonConfig.getRegionId(), accessKeyId, accessSecret);
            DefaultAcsClient client = new DefaultAcsClient(profile);
            String streamName = rtmpPath.substring(rtmpPath.lastIndexOf("/") + 1);
            var request = new AddLivePullStreamInfoConfigRequest();
            request.setDomainName(CommonConfig.getAliLiveHost());
            request.setAppName(CommonConfig.getAppName());
            request.setStreamName(streamName);
            request.setSourceUrl(rtmpPath);
            request.setStartTime(now.format(DateTimeFormatter.ofPattern("yyyy-MM-ddTHH:mm:ssZ")));
            request.setEndTime(now.plusHours(2).format(DateTimeFormatter.ofPattern("yyyy-MM-ddTHH:mm:ssZ")));
            AddLivePullStreamInfoConfigResponse response = client.getAcsResponse(request);
            return true;
        } catch (Exception e) {
            log.error(MessageFormat.format("添加阿里云拉流直播配置异常:拉流rtmpPath为{0}", rtmpPath), e);
        }
        return false;
    }

    @Override
    public boolean RemAliLiveConfigs(int id, String rtmpPath, int typeId) {
        if (rtmpPath.indexOf("rtmp://") > -1) {
            try {
                var accessKeyId = CryptogramHelper.Decrypt3DES(CommonConfig.getAccessKeyId(), CommonConfig.getConfigKey());
                var accessSecret = CryptogramHelper.Decrypt3DES(CommonConfig.getAccessSecret(), CommonConfig.getConfigKey());
                IClientProfile profile = DefaultProfile.getProfile(CommonConfig.getRegionId(), accessKeyId, accessSecret);
                DefaultAcsClient client = new DefaultAcsClient(profile);
                String streamName = rtmpPath.substring(rtmpPath.lastIndexOf("/") + 1);
                var request = new DeleteLivePullStreamInfoConfigRequest();
                request.setDomainName(CommonConfig.getAliLiveHost());
                request.setAppName(CommonConfig.getAppName());
                request.setStreamName(streamName);
                var response = client.getAcsResponse(request);
                DeleteCache(id, typeId);
                return true;
            } catch (Exception e) {
                log.error(MessageFormat.format("删除阿里云拉流直播配置异常:拉流rtmpPath为{0}", rtmpPath), e);
            }
        }
        return false;
    }
}