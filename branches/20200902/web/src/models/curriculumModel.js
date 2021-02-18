import * as services from '../services/curriculumServices';

export default {
  namespace: 'curriculumModel',
  state: {
  },

  reducers: {
  },

  effects: {

    //我的课程总数
    *GetSubjectListCount({ payload }, { call }) {
      return yield call(services.GetSubjectListCount, payload);
    },

    //课堂作业列表
    *GetExamSetList({ payload }, { call }) {
      return yield call(services.GetExamSetList, payload);
    },
    //课程列表
    *GetSubjectInfoForShow({ payload }, { call }) {
      return yield call(services.GetSubjectInfoForShow, payload);
    },
    //学生列表
    *GetStudentList({ payload }, { call }) {
      return yield call(services.GetStudentList, payload);
    },
    //获取题目列表
    *GetExamSetPreview({ payload }, { call }) {
      return yield call(services.GetExamSetPreview, payload);
    },
    *SubmitExamSet({ payload }, { call }) {
      return yield call(services.SubmitExamSet, payload);
    },
    //保存评价
    *EvaluateStudent({ payload }, { call }) {
      return yield call(services.EvaluateStudent, payload);
    },
    //布置新题地址
    *GetAddExamSetUrl({ payload }, { call }) {
      return yield call(services.GetAddExamSetUrl, payload);
    },
    //删除
    *DeleteExamSetOrFile({ payload }, { call }) {
      return yield call(services.DeleteExamSetOrFile, payload);
    },

    //上下架
    *SetExamSetOnline({ payload }, { call }) {
      return yield call(services.SetExamSetOnline, payload);
    },

    //作业统计
    *GetExamSetStatistList({ payload }, { call }) {
      return yield call(services.GetExamSetStatistList, payload);
    },
    //评测结果
    *GetEvaluationResult({ payload }, { call }) {
      return yield call(services.GetEvaluationResult, payload);
    },
    //资料列表
    *GetSubjectFileList({ payload }, { call }) {
      return yield call(services.GetSubjectFileList, payload);
    },
    //导入学生
    *SaveExamSet({ payload }, { call }) {
      return yield call(services.SaveExamSet, payload);
    },
    //导入学生
    *ImportStudent({ payload }, { call }) {
      return yield call(services.ImportStudent, payload);
    },
    //保存资料
    *SaveSubjectFile({ payload }, { call }) {
      return yield call(services.SaveSubjectFile, payload);
    },
    //学生端-我的评价列表
    *GetSubjectEvaluateList({ payload }, { call }) {
      return yield call(services.GetSubjectEvaluateList, payload);
    },
    //学生端-对我的评价列表
    *GetTeacherToStudentEvaluateList({ payload }, { call }) {
      return yield call(services.GetTeacherToStudentEvaluateList, payload);
    },
    //学生端-发布评价
    *EvaluateSubject({ payload }, { call }) {
      return yield call(services.EvaluateSubject, payload);
    },
    *GetUserSubjectList({ payload }, { call }) {
      return yield call(services.GetUserSubjectList, payload);
    },
    //获取作业基本信息
    *GetExamSetInfo({ payload }, { call }) {
      return yield call(services.GetExamSetInfo, payload);
    },
    //保存课程作业
    *SaveExamSet({ payload }, { call }) {
      return yield call(services.SaveExamSet, payload);
    },

  },
  subscriptions: {
  }
};
