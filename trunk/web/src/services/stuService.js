import request from '../utils/request';

export function query() {
  return request('/api/users');
}

/**
 * 课表管理-课程列表
 */
export async function GetStudentSubjectList(params) {
  return request("/api/Subject/GetStudentSubjectList", {
    method: "POST",
    body: params
  });
}

/**
 * 课程管理-课程列表_我的课程，需要登录
 */
export async function GetStudentSubjectListLogin(params) {
  return request("/api/Subject/GetStudentSubjectListLogin", {
    method: "POST",
    body: params
  });
}

/**
 * 报名课程
 */
export async function RegistSubject(params) {
  return request("/api/Subject/RegistSubject", {
    method: "POST",
    body: params
  });
}

/**
 * 直播间初始化
 */
export async function GetSubjectVideo(params) {
  return request("/api/Subject/GetSubjectVideo", {
    method: "POST",
    body: params
  });
}

/**
 * 向老师提问
 */
export async function AskQuestion(params) {
  return request("/api/Student/AskQuestion", {
    method: "POST",
    body: params
  });
}

/**
 * 微课视频播放
 */
export async function GetWeiKeVideo(params) {
  return request("/api/WeiKe/GetWeiKeVideo", {
    method: "POST",
    body: params
  });
}

/**
 * 上传附件
 */
export async function SendFile(params) {
  return request("/api/Discuss/SendFile", {
    method: "POST",
    file: params,
  });
}

/**
 * 发送消息
 */
export async function SendMessage(params) {
  return request("/api/Discuss/SendMessage", {
    method: "POST",
    body: params
  });
}

/**
 * 发送消息
 */
export async function RemoveMessage(params) {
  return request("/api/Discuss/RemoveMessage", {
    method: "POST",
    body: params
  });
}
export async function ChangeRoomStatus(params) {
  return request("/api/Discuss/ChangeRoomStatus", {
    method: "POST",
    body: params
  });
}

export async function ChangeUserStatus(params) {
  return request("/api/Discuss/ChangeUserStatus", {
    method: "POST",
    body: params
  });
}

export async function ChangeExamSetStatus(params) {
  return request("/api/Discuss/ChangeExamSetStatus", {
    method: "POST",
    body: params
  });
}


/**
 * 获取消息
 */
export async function Message(params) {
  return request("/api/Discuss/Message", {
    method: "POST",
    body: params
  });
}

/**
 * 获取公益课日期筛选条件
 */
export async function CurrentTime(params) {
  return request("/api/Common/CurrentTime", {
    method: "POST",
    body: params
  });
}

/**
 * 获取公益课日期筛选条件
 */
export async function GetPublicBenefitSubjectList(params) {
  return request("/api/Subject/GetPublicBenefitSubjectList", {
    method: "POST",
    body: params
  });
}