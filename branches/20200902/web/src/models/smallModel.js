import * as services from '../services/smallServices';

export default {
	namespace: 'smallnModel',
	state: {
	},

	reducers: {
	},

	effects: {

    //在线微课列表
    *GetWeiKeList({ payload }, { call }) {
      return yield call(services.GetWeiKeList, payload);
    },
    //修改微课状态(上架/下架/删除)
    *UpdateWeiKeStatus({ payload }, { call }) {
      return yield call(services.UpdateWeiKeStatus, payload);
    },
    //获取教材版本
    *GetBookVersionList({ payload }, { call }) {
      return yield call(services.GetBookVersionList, payload);
    },
    //根据教材获取课本列表
    *GetCourseMappingList({ payload }, { call }) {
      return yield call(services.GetCourseMappingList, payload);
    },
    //根据课本获取章节列表
    *GetChapterSectionList({ payload }, { call }) {
      return yield call(services.GetChapterSectionList, payload);
    },
    //添加/编辑微课
    *SetWeiKeInfo({ payload }, { call }) {
      return yield call(services.SetWeiKeInfo, payload);
    },
    //获取微课详情
    *GetWeiKeInfo({ payload }, { call }) {
      return yield call(services.GetWeiKeInfo, payload);
    },
    //获取硬件的视频房间列表
    *GetDeviceList({ payload }, { call }) {
      return yield call(services.GetDeviceList, payload);
    },
    //获取授权码
    *GetAuthCode({ payload }, { call }) {
      return yield call(services.GetAuthCode, payload);
    },
    //创建/修改 视频直播
    *CreateVideoLive({ payload }, { call }) {
      return yield call(services.CreateVideoLive, payload);
    },
    //获取上传文件第三方token
    *getUploadToken({ payload }, { call }) {
      return yield call(services.getUploadToken, payload);
    },
	},
	subscriptions: {
	}
};
