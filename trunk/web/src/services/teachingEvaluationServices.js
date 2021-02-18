import request from '../utils/request';


  /**
   * 获取所有可评价课程
   */
  export async function getAllSubjectEvaluate(params) {
    return request("/api/TeacherEvaluate/GetAllSubjectEvaluate", {
      method: "POST",
      body: params
    })
  }

  /**
   * 主讲获取课程评价
   */
  export async function getSubjectEvaluate(params) {
    return request("/api/TeacherEvaluate/GetSubjectEvaluate", {
      method: "POST",
      body: params
    })
  }

  /**
   * 消息管理列表
   */
  export async function getMessageList(params) {
    return request("/api/TeacherEvaluate/GetMessageList", {
      method: "POST",
      body: params
    });
  }

  /**
   * 回复消息
   */
  export async function replayMessage(params) {
    return request("/api/TeacherEvaluate/ReplayMessage", {
      method: "POST",
      body: params
    });
  }

  /**
   * 助教 - 课程评价列表
   */
  export async function getSubjectEvaluateList(params) {
    return request("/api/TeacherEvaluate/GetSubjectEvaluateList", {
      method: "POST",
      body: params
    });
  }


    /**
   * 助教 -评价课程
   */
  export async function evaluateSubject(params) {
    return request("/api/TeacherEvaluate/EvaluateSubject", {
      method: "POST",
      body: params
    });
  }
