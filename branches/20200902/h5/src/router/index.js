import AccessLogin from '../pages/Access/Login';
import PasswordLogin from '../pages/Access/PasswordLogin';
import { onlineTeacher, offlineTeacher } from './teacher';
import { onlineStudent, offlineStudent } from './student';
import OpenClassIndex from '../pages/Openclass/Index';
import OpenClassList from '../pages/Openclass/List';
import OpenClassDetail from '../pages/Openclass/Detail';
import { getToken } from "../utils/storage";
import VueRouter from 'vue-router';
import store from '../store';
import { roleEnum } from '../config/const';
import Index from '../pages/Index';

/**
 * 全站路由配置
 */
const routes = [{
  path: '/',
  name: 'Index',
  meta: { title: '首页' },
  component: Index
}, {
  path: '/Online/Login',
  name: 'OnlineLogin',
  meta: { title: '登录' },
  component: PasswordLogin
}, {
  path: '/Offline/Login',
  name: 'OfflineLogin',
  meta: { title: '登录' },
  component: PasswordLogin
}, {
  path: '/AccessOnline/Login',
  name: 'AccessOnlineLogin',
  meta: { title: '登录' },
  component: AccessLogin
}, {
  path: '/AccessOffline/Login',
  name: 'AccessOfflineLogin',
  meta: { title: '登录' },
  component: AccessLogin
}, {
  path: '/OpenClass',
  name: 'OpenClassIndex',
  meta: { title: '中高考复习课' },
  redirect: '/OpenClass/Index',
  component: OpenClassIndex,
  children: [{
    path: '/OpenClass/Index',
    name: 'OpenClassIndex',
    meta: { title: '中高考复习课' },
    component: OpenClassList
  }, {
    path: '/OpenClass/Detail',
    name: 'OpenClassDetail',
    component: OpenClassDetail
  }]
},
...onlineTeacher,
...offlineTeacher,
...offlineStudent,
...onlineStudent
];

//实例化Router
const router = new VueRouter({ routes });

/**
 * 验证当前角色与路由是否匹配
 * @param {} userInfo
 * @param {*} path
 */
function checkRole(userInfo, path) {
  let urlRole = path.indexOf('student/') > -1 && roleEnum.student;
  urlRole = path.indexOf('teacher/') > -1 ? roleEnum.teacher : urlRole;

  // 判断是是否公益课
  const isPublicClass = path.indexOf('openclass/') > -1
  //验证权限
  if (
    !userInfo ||
    userInfo.UserRoles.indexOf(urlRole.toString()) == -1
  ) {
    if (isPublicClass) {
      return true
    } else {
      return false
    }
  }
  return true;
}

/**
 * 路由前置守卫
 */
router.beforeEach(async function (to, from, next) {
  if (window._hmt) {
    if (to.path) {
      _hmt.push(['_trackPageview', '/#' + to.fullPath]);
    }
  }
  const path = to.path.toLowerCase();
  /** 不需要验证登录的路由集合 */
  const NoLoginPathList = ["/accessonline", "/accessoffline", "/online/login", "/offline/login", "/test"];

  /** 更新当前系统标识 */
  if (path.indexOf('/online') > -1 || path.indexOf('/accessonline') > -1) {
    store.commit('updateSystem', 1);
  } else if (path.indexOf('/offline') > -1 || path.indexOf('/accessoffline') > -1) {
    store.commit('updateSystem', 2);
  }

  /** 判断用户是否登录 */
  if (
    path !== "/" &&
    NoLoginPathList.filter(p => path.indexOf(p) > -1).length === 0
  ) {
    if (!getToken()) {
      //未登录，退出
      next('/');
      return;
    } else {
      const userInfo = await store.dispatch('getUserInfo');
      //判断角色是否正确
      if (!checkRole(userInfo, path)) {
        next('/');
        return;
      }
    }
  }
  /** 统一设置标题 */
  let title = to.meta.title;
  if (title != undefined) {
    document.title = title;
  }
  next();
});

export default router;
