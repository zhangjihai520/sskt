import router from 'umi/router';
import { message } from 'antd';
import { roleEnum } from 'config/enum';
import { routerRedux } from 'dva/router';

//权限验证
export function Authorization(roleId) {
  const {
    commonModel: { UserBaseInfo = {} }
  } = window.g_app._store.getState();
  const { dispatch } = window.g_app._store;
  if (UserBaseInfo) {
    check(UserBaseInfo);
  } else {
    dispatch({ type: 'commonModel/getUserBaseInfoAsync' }).then(user => {
      check(user);
    });
  }

  function check(user) {
    if (user.UserRoles.indexOf(roleId) === -1) {
      message.warn('您没有该页面权限');
      let userRoles = (user.UserRoles.split(',') || []).map(n => n * 1);
      goIndex(userRoles[0]); //默认跳转第一个角色
    }
  }
}

/**
 * 获取用户信息，判断角色
 * callback: 角色的回调处理
 */
export function getUserInfo(callback) {
  const { dispatch } = window.g_app._store;
  dispatch({ type: 'commonModel/getUserBaseInfo', payload: { IsRest: true } }).then(user => {
    if (user && user.UserRoles) {
      let userRoles = (user.UserRoles.split(',') || []).map(n => n * 1); //字符串逗号分隔id转成数组数字类型

      if (userRoles.length > 0) {
        callback && callback(userRoles);
      } else {
        message.info('暂不支持该角色');
      }
    } else {
      message.error('用户没有权限');
    }
  });
}

/**
 * 根据当前角色跳转
 * @param {*} role 角色
 */
export function goIndex(role) {
  const { dispatch } = window.g_app._store;
  const {
    commonModel: { TeaBasePath, AdminBasePath, StudentBasePath }
  } = window.g_app._store.getState();
  switch (role) {
    case roleEnum.student:
      dispatch(routerRedux.push(`${StudentBasePath}/MinLesson`));
      break;
    case roleEnum.teacher:
      dispatch(routerRedux.push(`${TeaBasePath}`));
      break;
    case roleEnum.admin:
      dispatch(routerRedux.push(`${AdminBasePath}`));
      break;
    default:
      message.info('暂不支持该角色');
      break;
  }
}

/** 退出登录逻辑*/
export function logoutRole() {
  const {
    commonModel: { SystemTheme = {} }
  } = window.g_app._store.getState();
  // console.log(SystemTheme.logoutLocation);
  window.location.href = SystemTheme.logoutLocation;
}
