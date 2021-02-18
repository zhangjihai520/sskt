import request from '../utils/request';

export function query() {
  return request('/api/users');
}

/**
  * 修改个人信息
  */
export async function EditStudentInfo(params) {
  return request("/api/Student/EditStudentInfo", {
    method: "POST",
    body: params
  });
}

/**
* 修改密码
*/
export async function ChangePassword(params) {
  return request("/api/User/ChangePassword", {
    method: "POST",
    body: params
  });
}

/**
* 修改手机
*/
export async function ChangePhoneNum(params) {
  return request("/api/User/ChangePhoneNum", {
    method: "POST",
    body: params
  });
}

/**
* 修改邮箱
*/
export async function ChangeEmail(params) {
  return request("/api/User/ChangeEmail", {
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