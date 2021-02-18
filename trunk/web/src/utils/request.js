import fetch from 'dva/fetch';
import { message } from "antd";

/**
 * 全局错误码提示信息
 * @type {{2001: string}}
 */
const errorStatusMessage = {
  2001: "代码异常",
  2002: "数据错误",
  2003: "数据校验异常",
  2004: "特殊字符", //【/:~*?!@#$+<>{}|`&'"%;\】
  2005: "存在敏感字符",
  2006: "权限校验异常"
};

/**
 * 转为json格式
 * @param {*} response 返回的body数据
 */
function parseJSON(response) {
  return response.json();
}

/**
 * 验证返回的http状态
 * @param {*} response 返回
 */
function checkStatus(response) {
  if (response.status >= 200 && response.status < 300) {
    return response;
  }
  const error = new Error(response.statusText);
  error.response = response;
  throw error;
}

/**
 * 公用请求方法
 * @param  {string} url       The URL we want to request
 * @param  {object} [options] The options we want to pass to "fetch"
 * @return {object}           An object containing either "data" or "err"
 */
export default function request(url, options) {
  // credentials: 'include' ；因为默认是omit, 即不发送cookies;
  let defaultOptions = {
    credentials: "include"
  };
  //合并设置、参数
  const newOptions = { ...defaultOptions, ...options };
  //post默认为json格式
  if (newOptions.method.toUpperCase() === "POST") {
    if (newOptions.file) {
      newOptions.body = newOptions.file;
      newOptions.headers = [];
    } else {
      newOptions.headers = {
        "Content-Type": "application/json; charset=utf-8",
        ...newOptions.headers
      };
      if (newOptions.headers["Content-Type"] == "application/json; charset=utf-8") {
        newOptions.body = JSON.stringify(newOptions.body);
      }
    }
  }
  //添加线上、线下标识
  const { commonModel: { CurUserSystem = 1,FromTypeId = 1 } } = window.g_app._store.getState();
  if (newOptions.mode !== 'cors') 
  {
    newOptions.headers["CurUserSystem"] = CurUserSystem;
    newOptions.headers["FromTypeId"] = FromTypeId;
  }
  return fetch(url, newOptions)
    .then(checkStatus)
    .then(parseJSON)
    .then(data => {
      //兼容第三方的返回数据格式
      if(data && data.nResult){
        return data;
      }
      if (data.ResultType !== 1) {
        if (data.Message === "2004") {
          message.error("当前操作不允许包含特殊字符");
        }
        if (data.Message === "2005") {
          message.error("存在敏感字符");
        }
        throw new Error(`请求错误:"${errorStatusMessage[data.Message]}"`);
      }
      return data.ReturnEntity;
    })
    .catch(err => { throw err });
}
