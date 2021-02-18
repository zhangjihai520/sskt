import request from './request';

//权限验证、登录
/**
 * 登录
 */
export const loginWithPassword = (params) => request('post', '/api/Login/LoginWithPassword', params);

/**
 * 根据云平台uid获取用户Token
 * @param {Object} params  {UId:'123'}
 */
export const getTokenByUId = (params) => request('post', '/api/Dledc/DledcMobileLogin', params);

/** 
 * 获取用户信息（简）全局
 */
export const getUserBaseInfo = () => request('post', '/api/User/GetUserBaseInfo',{});

/**
 * 获取用户信息（个人中心）
 */
export const getUserInfo = () => request('post', '/api/User/GetUserInfo',{});


//公用方法

/**
 * 获取所有年级、学科
 */
export const getGradeList = () => request('post', '/api/Common/GetGradeList',{});

/**
 * 获取当前学科下所有课程
 * @param {Object} params {CourseId:0}
 */
export const getStudentSubjectListH5 = (params) => request('post', '/api/Subject/GetStudentSubjectListH5', params);

/**
 * 获取课表
 */
export const curriculumList = (params) => request('post', '/api/Subject/GetUserSubjectListH5', params);
/**
 * 搜索课表
 */
export const filtedCurriculumList = (params) => request('post', '/api/Subject/SearchStudentSubject', params);
/**
 * 搜索课表-全部
 */
export const filtedCurriculumListAll = (params) => request('post', '/api/Subject/SearchSubject', params);

/**
 * 人数统计
 */
export const getSubjectListStatistics = (params) => request('post', '/api/Subject/GetSubjectListStatistics', params);
/**
 * 学生学校占比
 */
export const getStudentSchoolShare = (params) => request('post', '/api/Subject/GetStudentSchoolShare', params);
/**
 * 测评结果
 */
export const getEvaluationResult = (params) => request('post', '/api/ExamSet/GetEvaluationResult', params);
/**
 * 获取学生列表
 */
export const getStudentList = (params) => request('post', '/api/Subject/GetStudentList', params);
/**
 * 获取学生轨迹
 */
export const getStudentStudyRecords = (params) => request('post', '/api/Student/GetStudentStudyRecords', params);
/**
 * 课堂数据
 */
export const getHotSubjectList = (params) => request('post', '/api/Subject/GetHotSubjectList', params);

// 获取题目接口
export const getQuestionDetails = (params) => request('post', '/api/ExamSet/GetExamSetPreview', params);
// 提交答案
export const submitExamSet = (params) => request('post', '/api/Student/SubmitExamSet', params);

/**
 * 【学生端】报名课程
 */
export const registSubject = (params) => request('post', '/api/Subject/RegistSubject', params);

/**
 * 【抗击疫情特殊项目】获取公益课课程列表
 */
export const GetPublicBenefitSubjectList = (params) => request('post', '/api/Subject/GetPublicBenefitSubjectList', params);

/**
 * 【抗击疫情特殊项目】获取公益课直播时间差
 */
export const CurrentTime = (params) => request('post', '/api/Common/CurrentTime', params);

/**
 * 【新增】播放统计接口
 */
export const RecordLog = (params) => request('post', '/api/Common/RecordLog', params);

/**
 * 【新增】获取课程视频信息
 */
export const GetSubjectVideo = (params) => request('post', '/api/Subject/GetSubjectVideo', params);

/**
 * 【新增】获取消息
 */
export const GetMessage = (params) => request('post', '/api/Discuss/Message', params);

/**
 * 【新增】发送消息
 */
export const SendMessage = (params) => request('post', '/api/Discuss/SendMessage', params);

/**
 * 【新增】发送文件
 */
export const SendFile = (params) => request('post', '/api/Discuss/SendFile', params);

/**
 * 【新增】直播课上课状态修改
 */
export const UpdateSubjectStudentStatus = (params) => request('post', '/api/Subject/UpdateSubjectStudentStatus', params);