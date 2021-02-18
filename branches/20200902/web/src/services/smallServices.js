import request from '../utils/request';
import { queryParseJson } from '../utils/utils';

export function query() {
  return request('/api/users');
}

/**
 * 在线微课列表
 */
export async function GetWeiKeList(params) {
  return request("/api/WeiKe/GetWeiKeList", {
    method: "POST",
    body: params
  });
}
/**
 * 修改微课状态(上架/下架/删除)
 */
export async function UpdateWeiKeStatus(params) {
  return request("/api/WeiKe/UpdateWeiKeStatus", {
    method: "POST",
    body: params
  });
}
/**
  * 获取教材版本
  */
export async function GetBookVersionList(params) {
  return request("/api/Common/GetBookVersionList", {
    method: "POST",
    body: params
  });
}
/**
  * 根据教材获取课本列表
  */
export async function GetCourseMappingList(params) {
  return request("/api/Common/GetCourseMappingList", {
    method: "POST",
    body: params
  });
}
/**
 * 根据课本获取章节列表
 */
export async function GetChapterSectionList(params) {
  return request("/api/Common/GetChapterSectionList", {
    method: "POST",
    body: params
  });
}

/**
 * 添加/编辑微课
 */
export async function SetWeiKeInfo(params) {
  return request("/api/WeiKe/SetWeiKeInfo", {
    method: "POST",
    body: params
  });
}

/**
 * 获取微课详情
 */
export async function GetWeiKeInfo(params) {
  return request("/api/WeiKe/GetWeiKeInfo", {
    method: "POST",
    body: params
  });
}

/**
 * 获取硬件的视频房间列表
 */
export async function GetDeviceList(params) {
  return request("/api/Subject/GetDeviceList", {
    method: "POST",
    body: params
  });
}

export async function GetAuthCode(params) {
  return request("/api/Subject/GetAuthCode", {
    method: "POST",
    body: params
  });
}

/**
 * 创建/修改 视频直播
 */
export async function CreateVideoLive(params) {
  return request("/api/Subject/CreateVideoLive", {
    method: "POST",
    body: params
  });
}

/**
 * 获取上传文件第三方token
 */
export async function getUploadToken(params) {
  return request(SYSTEM_CONFIG.upload.getTokenUrl, {
    mode: 'cors',
    credentials: 'include',
    method: "POST",
    headers: {
      'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8'
    },
    body: queryParseJson(params)
  });
}