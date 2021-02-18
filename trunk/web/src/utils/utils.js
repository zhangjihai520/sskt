import moment from 'moment';
import _ from 'lodash';
let cIServerTime;

/**
 * 同步服务器时间（每次请求都会重置当前的全局日期时间）
 * window对象挂载了serverYear serverDate
 * serverTime 本地实时计算
 */
export function asyncServerTime(dateTime) {
  let dt = moment(dateTime);

  window.serverYear = dt.format('YYYY');
  window.serverDate = dt.format('YYYY-MM-DD');

  cIServerTime && window.clearInterval(cIServerTime);

  let oTime = `${dt.hour()}:${dt.minute()}:${dt.second()}`;
  let ts = oTime.split(':', 3);
  let tnums = [parseInt(ts[0]), parseInt(ts[1]), parseInt(ts[2])];

  window.serverTime = showNewTime(tnums[0], tnums[1], tnums[2]);

  cIServerTime = window.setInterval(function() {
    tnums = getNextTimeNumber(tnums[0], tnums[1], tnums[2]);
    window.serverTime = showNewTime(tnums[0], tnums[1], tnums[2]);
  }, 1000);

  function showNewTime(h, m, s) {
    let timeStr =
      ('0' + h.toString()).substr(-2) + ':' + ('0' + m.toString()).substr(-2) + ':' + ('0' + s.toString()).substr(-2);

    return timeStr;
  }

  function getNextTimeNumber(h, m, s) {
    if (++s === 60) {
      s = 0;
    }

    if (s === 0) {
      if (++m === 60) {
        m = 0;
      }
    }

    if (m === 0 && s === 0) {
      if (++h === 24) {
        h = 0;
      }
    }

    return [h, m, s];
  }
}
export function fixedZero(val) {
  return val * 1 < 10 ? `0${val}` : val;
}
/**
 * 获取某种类型时间 本周和本月
 * @param {获取} type
 */
export function getTimeDistance(type) {
  const now = new Date();
  const oneDay = 1000 * 60 * 60 * 24;

  if (type === 'week') {
    let day = now.getDay();
    now.setHours(0);
    now.setMinutes(0);
    now.setSeconds(0);

    if (day === 0) {
      day = 6;
    } else {
      day -= 1;
    }

    const beginTime = now.getTime() - day * oneDay;

    return [moment(beginTime), moment(beginTime + (7 * oneDay - 1000))];
  }

  if (type === 'month') {
    const year = now.getFullYear();
    const month = now.getMonth();
    const nextDate = moment(now).add(1, 'months');
    const nextYear = nextDate.year();
    const nextMonth = nextDate.month();

    return [
      moment(`${year}-${fixedZero(month + 1)}-01 00:00:00`),
      moment(moment(`${nextYear}-${fixedZero(nextMonth + 1)}-01 00:00:00`).valueOf() - 1000)
    ];
  }

  const year = now.getFullYear();
  return [moment(`${year}-01-01 00:00:00`), moment(`${year}-12-31 23:59:59`)];
}

/**
 * 将Json参数转化为Url参数格式
 * @param param
 */
function parseParam(param, urlParam = '?') {
  const paramKeys = Object.keys(param);
  paramKeys &&
    paramKeys.map(p => {
      urlParam += `${p}=${param[p] !== null && param[p] !== undefined ? param[p] : ''}&`;
    });
  return urlParam;
}

/**
 * 将JSON转为Query形式字符串
 * @param {*} json
 */
export function queryParseJson(json) {
  return parseParam(json, '');
}

/**
 * 通过iframe下载文件
 * @param url 请求路径
 * @param params 请求参数
 */
export function downloadByIframe(url, params) {
  if (!url) return;
  let frameNode = document.getElementById('frameForDownload');
  if (frameNode) {
    frameNode.parentNode.removeChild(frameNode);
  }
  frameNode = document.createElement('iframe');
  frameNode.id = 'frameForDownload';
  if (params) {
    frameNode.src = url + parseParam(params);
  } else {
    frameNode.src = url;
  }
  frameNode.style.display = 'none';
  document.body.appendChild(frameNode);
}
/**
 * form 下载
 */
export function downloadByForm(url, params) {
  if (!url) return;
  let formNode = document.getElementById('formForDownload');
  if (formNode) {
    formNode.parentNode.removeChild(frameNode);
  }
  formNode = document.createElement('form');
  formNode.id = 'formForDownload';
  formNode.style.display = 'none';
  formNode.method = 'post';
  formNode.action = url;
  document.body.appendChild(formNode);
  for (var key in params) {
    let inputNode;
    if (Array.isArray(params[key])) {
      params[key].map((r, i) => {
        inputNode = document.createElement('input');
        inputNode.type = 'text';
        inputNode.name = key + `[${i}]`;
        inputNode.value = r;
        formNode.appendChild(inputNode);
      });
    } else {
      inputNode = document.createElement('input');
      inputNode.type = 'text';
      inputNode.name = key;
      inputNode.value = params[key];
      formNode.appendChild(inputNode);
    }
  }
  formNode.submit();
  document.body.removeChild(formNode);
}

/**
 * 特殊字符校验
 * @param {*} str
 */
export function isValidSpecialCharacter(str) {
  return /[\/\\:~*?!@#$+<>{}|`&'"%;]/gim.test(str);
}

/**
 * 删除数组指定元素，返回新数组
 * @param {*} arr 源数组
 * @param {*} val 删除指定元素值
 */
export function removeArrItemByValue(arr, val) {
  for (let i = 0; i < arr.length; i++) {
    if (arr[i] === val) {
      arr.splice(i, 1);
      break;
    }
  }
  return arr;
}

/**
 * 获取浏览器地址参数
 * @param url 地址
 * @param queryName 获取具体参数名称
 * @returns {*} 参数不传返回所有的，参数名称统一小写
 */
export function getUrlQuery(url, queryName) {
  let paraString = url.substring(url.indexOf('?') + 1, url.length).split('&');
  let paraObj = {};
  for (let i = 0; i < paraString.length; i++) {
    let j = paraString[i];
    paraObj[j.substring(0, j.indexOf('=')).toLowerCase()] = j.substring(j.indexOf('=') + 1, j.length);
  }
  if (queryName) {
    let returnValue = paraObj[queryName.toLowerCase()];
    if (typeof returnValue === 'undefined') {
      return '';
    } else {
      return returnValue;
    }
  } else {
    return paraObj;
  }
}

/**
 * 获取字符串长度，字节数
 * @param str
 * @returns {Number}
 */
export function getByteLength(str) {
  let len = 0;
  for (let i = 0; i < str.length; i++) {
    if (str[i].match(/[^\x00-\xff]/gi) !== null) len += 2;
    else len += 1;
  }
  return len;
}

/**
 * 截取字符串，以字符计算
 * @param str
 * @param length
 * @returns {string}
 */
export function subStringByte(str, length) {
  let result = '';
  let len = 0;
  for (let i = 0; i < str.length; i++) {
    let c = str.charCodeAt(i);
    if (c > 127 || c === 94) {
      len += 2;
    } else {
      len++;
    }
    if (len > length) {
      break;
    } else {
      result += String.fromCharCode(str.charCodeAt(i));
    }
  }
  return result;
}

/**
 * 定位光标并将光标移到最后
 * @param focusInput
 */
export function inputOnFocus(focusInput) {
  focusInput.focus();
  let length = focusInput.value.length;
  if (focusInput.setSelectionRange) {
    setTimeout(() => focusInput.setSelectionRange(length, length), 100);
  } else if (focusInput.createTextRange) {
    let range = focusInput.createTextRange();
    range.collapse(true);
    range.moveStart('character', length);
    range.select();
  }
}

/**
 *获取元素的纵坐标（相对于窗口）
 *@param e
 */
export function getTop(e) {
  var offset = e.offsetTop;
  if (e.offsetParent != null) offset += getTop(e.offsetParent);
  return offset;
}

/**
 * 过滤非本站点课程
 */
export function filterCourse(courseList) {
  _.remove(courseList, function(course) {
    return checkCourse(course.Name);
  });
}

/**
 * 判断是否是非本站点课程
 */
export function checkCourse(shortCode) {
  return (
    shortCode == 'tydl' ||
    shortCode == 'hswhjy' ||
    shortCode == 'ysxs' ||
    shortCode == 'jkjy' ||
    shortCode == 'skq' ||
    shortCode == 'mnlx' ||
    shortCode == 'zzzy'
  );
}

/**
 * 验证flash是否启用
 * @return {[type]} [description]
 */
export function flashChecker() {
  var hasFlash = 0; //是否安装了flash
  var flashVersion = 0; //flash版本
  var isValid = 0; //是否过期

  //IE浏览器
  if ('ActiveXObject' in window) {
    try {
      var swf = new ActiveXObject('ShockwaveFlash.ShockwaveFlash');
      hasFlash = 1;
      isValid = 1;
      VSwf = swf.GetVariable('$version');
      flashVersion = parseInt(VSwf.split(' ')[1].split(',')[0]);
    } catch (e) {}
    //非IE浏览器
  } else {
    try {
      if (navigator.plugins && navigator.plugins.length > 0) {
        var swf = navigator.plugins['Shockwave Flash'];
        if (swf) {
          hasFlash = 1;
          isValid = 1;
          var words = swf.description.split(' ');
          for (var i = 0; i < words.length; ++i) {
            if (isNaN(parseInt(words[i]))) continue;
            flashVersion = parseInt(words[i]);
          }
          if (swf.filename && swf.filename == 'internal-not-yet-present') {
            //过期
            isValid = 0;
          }
        }
      }
    } catch (e) {}
  }
  return { flash: hasFlash, version: flashVersion, vaild: isValid };
}
