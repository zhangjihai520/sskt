import request from '../utils/request';

/**
 * 课表热门数据
 */
export async function GetHotSubjectList(params) {
  return request("/api/Subject/GetHotSubjectList", {
    method: "POST",
    body: params
  });
}

/**
 * 获取学生学校占数比
 */
export async function GetStudentSchoolShare(params) {
  return request("/api/Subject/GetStudentSchoolShare", {
    method: "POST",
    body: params
  });
}

/**
 * 获取课程人数详情图表
 */
export async function GetSubjectListStatistics(params) {
  return request("/api/Subject/GetSubjectListStatistics", {
    method: "POST",
    body: params
  });
}

/**
 * 点播课程统计
 */
export async function GetPublicBenefitStatistics(params) {
  return request("/api/Subject/GetPublicBenefitStatistics", {
    method: "POST",
    body: params
  });
}

