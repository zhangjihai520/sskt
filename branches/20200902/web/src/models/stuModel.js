import * as services from '../services/stuService';

export default {
  namespace: 'stuModel',
  state: {},

  reducers: {},

  effects: {
    //课表管理-课程列表
    *GetStudentSubjectList({ payload }, { call }) {
      return yield call(services.GetStudentSubjectList, payload);
    },
    //课程管理-课程列表_我的课程，需要登录
    *GetStudentSubjectListLogin({ payload }, { call }) {
      return yield call(services.GetStudentSubjectListLogin, payload);
    },
    //报名课程
    *RegistSubject({ payload }, { call }) {
      return yield call(services.RegistSubject, payload);
    },
    //直播间初始化
    *GetSubjectVideo({ payload }, { call }) {
      return yield call(services.GetSubjectVideo, payload);
    },
    //向老师提问
    *AskQuestion({ payload }, { call }) {
      return yield call(services.AskQuestion, payload);
    },
    //向老师提问
    *GetWeiKeVideo({ payload }, { call }) {
      return yield call(services.GetWeiKeVideo, payload);
    },
    //上传附件
    *SendFile({ payload }, { call }) {
      return yield call(services.SendFile, payload);
    },
    //发送消息
    *SendMessage({ payload }, { call }) {
      return yield call(services.SendMessage, payload);
    },
    //房间禁言
    *ChangeRoomStatus({ payload }, { call }) {
      return yield call(services.ChangeRoomStatus, payload);
    },
    //用户状态
    *ChangeUserStatus({ payload }, { call }) {
      return yield call(services.ChangeUserStatus, payload);
    },
    //获取消息
    *Message({ payload }, { call }) {
      return yield call(services.Message, payload);
    },
    //获取消息
    *CurrentTime({ payload }, { call }) {
      return yield call(services.CurrentTime, payload);
    },
    //获取消息
    *GetPublicBenefitSubjectList({ payload }, { call }) {
      return yield call(services.GetPublicBenefitSubjectList, payload);
    },
  },
  subscriptions: {}
};
