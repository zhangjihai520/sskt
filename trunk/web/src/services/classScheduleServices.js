import request from "../utils/request";

//课表管理接口

/**
 * 课表管理列表
 */
export async function GetSubjectList(params) {
	return request("/api/Subject/GetSubjectList", {
		method: "POST",
		body: params
	});
}
/**
 * 下架/上架/删除课程
 */
export async function UpdateSubjectStatus(params) {
	return request("/api/Subject/UpdateSubjectStatus", {
		method: "POST",
		body: params
	});
}

/**
 * 添加/编辑课程
 */
export async function EditSubject(params) {
	return request("/api/Subject/EditSubject", {
		method: "POST",
		body: params
	});
}
/**
 * 获取课程详情
 */
export async function GetSubjectInfo(params) {
	return request("/api/Subject/GetSubjectInfo", {
		method: "POST",
		body: params
	});
}

/**
 * 教师首页获取当天课程通知
 */
export async function GetCurrentSubiject() {
	return request("/api/Subject/GetCurrentSubjectName", {
		method: "POST"
	});
}
/**
 * 模糊搜索所有教师名称
 */
export async function SearchTeacherInfo(params) {
	return request("/api/Teacher/SearchTeacherInfo", {
		method: "POST",
		body: params
	});
}

/**
 * 获取助教列表
 */
export async function GetSubjectRoomList(params) {
	return request("/api/Subject/GetSubjectRoomList", {
		method: "POST",
		body: params
	});
}

/**
 * 获取学生列表
 */
export async function GetGroupByStudentList(params) {
	return request("/api/Subject/GetGroupByStudentList", {
		method: "POST",
		body: params
	});
}

/**
 * 学生分组
 */
export async function SaveGroupByStudentList(params) {
	return request("/api/Subject/SaveGroupByStudentList", {
		method: "POST",
		body: params
	});
}



// 获取视频地址和房间列表接口
// export async function SaveGroupByStudentList(params) {
// 	return request("/api/Subject/SaveGroupByStudentList", {
// 		method: "POST",
// 		body: params
// 	});
// }