import request from '../utils/request';

export function query() {
  return request('/api/Subject');
}

/**
 * 课程数量列表
 */
export async function GetSubjectListCount(params) {
  return request("/api/Subject/GetSubjectListCount", {
    method: "POST",
    body: params
  });

}

/**
 * 课堂作业列表
 */
export async function GetExamSetList(params) {
  return request("/api/ExamSet/GetExamSetList", {
    method: "POST",
    body: params
  });
}

/**
 * 课程列表
 */
export async function GetSubjectInfoForShow(params) {
  return request("/api/Subject/GetSubjectInfoForShow", {
    method: "POST",
    body: params
  });
}
/**
* 学生列表
*/
export async function GetStudentList(params) {
  return request("/api/Subject/GetStudentList", {
    method: "POST",
    body: params
  });
}

/**
 * 获取题目or作业
 */
export async function GetExamSetPreview(params){
  return request("/api/ExamSet/GetExamSetPreview", {
    method: "POST",
    body: params
  });
}

/**
 *  提交答案
 */
export async function SubmitExamSet(params){
  return request("/api/Student/SubmitExamSet", {
    method: "POST",
    body: params
  });
}

/**
* 保存评价
*/
export async function EvaluateStudent(params) {
  return request("/api/Teacher/EvaluateStudent", {
    method: "POST",
    body: params
  });
}
/**
* 获取布置新题地址
*/
  export async function GetAddExamSetUrl(params) {
    return request("/api/ExamSet/GetAddExamSetUrl", {
    method: "POST",
    body: params
  });
}
/**
* 删除操作
*/
export async function DeleteExamSetOrFile(params) {
  return request("/api/Subject/DeleteExamSetOrFile", {
    method: "POST",
    body: params
  });
}
/**
* 上下架操作
*/
export async function SetExamSetOnline(params) {
  return request("/api/ExamSet/SetExamSetOnline", {
    method: "POST",
    body: params
  });
}
/**
* 作业统计
*/
export async function GetExamSetStatistList(params) {
  return request("/api/ExamSet/GetExamSetStatistList", {
    method: "POST",
    body: params
  });
}

/**
 * 评测结果
 */
export async function GetEvaluationResult(params) {
  return request("/api/ExamSet/GetEvaluationResult", {
    method: "POST",
    body: params
  });
}

/**
 * 随堂作业统计结果
 */
export async function GetAnswersStatistics(params) {
  return request("/api/ExamSet/GetAnswersStatistics", {
    method: "POST",
    body: params
  });
}
/**
 * 资料列表
 */
export async function GetSubjectFileList(params) {
  return request("/api/Subject/GetSubjectFileList", {
    method: "POST",
    body: params
  });
}
/**
 * 保存资料
 */
export async function SaveSubjectFile(params) {
  return request("/api/Subject/SaveSubjectFile", {
    method: "POST",
    body: params
  });
}
/**
 * 获取作业基本信息
 */
export async function GetExamSetInfo(params) {
  return request("/api/ExamSet/GetExamSetInfo", {
    method: "POST",
    body: params
  });
}

/**
 * 保存课程作业
 */
export async function SaveExamSet(params) {
  return request("/api/ExamSet/SaveExamSet", {
    method: "POST",
    body: params
  });
}

/**
 * 导入学生
 */
export async function ImportStudent(params) {
  return request("/api/Teacher/ImportStudent", {
    method: "POST",
    file: params,
  });
}

/**
 * 学生端-我的评价列表
 */
export async function GetSubjectEvaluateList(params) {
  return request("/api/Student/GetSubjectEvaluateList", {
    method: "POST",
    body: params
  });
}

/**
 * 学生端-对我的评价列表
 */
export async function GetTeacherToStudentEvaluateList(params) {
  return request("/api/Student/GetTeacherToStudentEvaluateList", {
    method: "POST",
    body: params
  });
}

/**
 * 学生端-发布评价
 */
export async function EvaluateSubject(params) {
  return request("/api/Student/EvaluateSubject", {
    method: "POST",
    body: params
  });
}

/**
 * 学生端-课表管理
 */
export async function GetUserSubjectList(params){
  return request("/api/Subject/GetUserSubjectListH5", {
    method: "POST",
    body: params
  });
}

// 获取视频地址和房间列表接口
export async function GetSubjectVideo(params){
  return request("/api/SuperHelperTeacher/GetSubjectVideo", {
    method: "POST",
    body: params
  });
}

//变更房间状态
export async function ChangeRoomStatus(params){
  return request("/api/SuperHelperTeacher/ChangeRoomStatus", {
    method: "POST",
    body: params
  });
}

//超级助教根据房间号获取信息
export async function GetMessageBySubjectRoomId(params){
  return request("/api/SuperHelperTeacher/GetMessageBySubjectRoomId", {
    method: "POST",
    body: params
  });
}

 // 发送消息
 export async function SendMessage(params){
  return request("/api/SuperHelperTeacher/SendMessage", {
    method: "POST",
    body: params
  });
}

// 变更用户状态
export async function ChangeUserStatus(params){
  return request("/api/SuperHelperTeacher/ChangeUserStatus", {
    method: "POST",
    body: params
  });
}

// 发送文件
export async function SendFile(params){
  return request("/api/SuperHelperTeacher/SendFile", {
    method: "POST",
    file: params
  });
}

// 撤回消息
export async function RemoveMessage(params){
  return request("/api/SuperHelperTeacher/RemoveMessage", {
    method: "POST",
    body: params
  });
}