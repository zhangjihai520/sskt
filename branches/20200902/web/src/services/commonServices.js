import request from '../utils/request';

/**
 * 章节、年级、学科
 */
export async function GetGradeList(params) {
  return request("/api/Common/GetGradeList", {
    method: "POST",
    body: params
  });
}
/**
 * 课程状态
 */
export async function GetSubjectStatusList(params) {
  return request("/api/Common/GetSubjectStatusList", {
    method: "POST",
    body: params
  });
}

/**
 * 学生端与教师端消息列表
 * @param {*} params
 */
export async function GetMessageList(params) {
  return request("/api/TeacherEvaluate/GetMessageList", {
    method: "POST",
    body: params
  });
}

/**
 * 获取个人信息
 */
export async function GetUserInfo(params) {
  return request("/api/User/GetUserInfo", {
    method: "POST",
    body: params
  });
}

/**
 * 获取个人信息
 */
export async function GetUserBaseInfo() {
  return request("/api/User/GetUserBaseInfo", {
    method: "POST"
  });
}

/**
 * 获取未读消息数量
 */
export async function GetUnReadMessageNumber() {
  return request("/api/Student/GetUnReadMessageNumber", {
    method: "POST"
  });
}

/**
 * 双师账号密码登录
 */ 
export async function LoginByPassword(params) {
  return request("/api/Login/LoginWithPassword", {
    method: "POST",
    body: params
  });
}

/**
 * 模拟账号密码云平台登录
 */ 
export async function LoginCloudplatform(params) {
  return request("/api/Dledc/DledcConfig", {
    method: "POST",
    body: params
  });
}

/**
 * 根据code获取用户Tonken登录
 */
export async function LoginByCode(params) {
  return request("/api/Dledc/DledcLogin", {
    method: "POST",
    body: params
  });
}


/**
 * 标记已读消息
 */
export async function SignReadMessage(params) {
  return request("/api/User/SignReadMessage", {
    method: "POST",
    body: params
  });
}

/**
 * 【学生端】直播课上课状态修改
 */
export async function UpdateSubjectStudentStatus(params) {
  return request ("/api/Subject/UpdateSubjectStudentStatus", {
    method: 'POST',
    body: params
  })
}

/**
 * 获取微课状态列表
 */
export async function GetWeiKeStatusList(params) {
  return request("/api/Common/GetWeiKeStatusList", {
    method: "POST",
    body: params
  });
}

/**
 * 播放统计接口
 */
export async function RecordLog(params) {
  return request("/api/Common/RecordLog", {
    method: "POST",
    body: params
  });
}