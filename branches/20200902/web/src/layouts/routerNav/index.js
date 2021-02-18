import cloneDeep from "lodash/cloneDeep";
import { adminOnlineNav, adminOfflineNav } from './adminNav';
import { studentOnlineNav, studentOfflineNav } from './studentNav';
import { teaOnlineNav, teaOfflineNav } from './teacherNav';
import { systemEnum } from 'config/enum';

/**
 * 指定layout,通过Nav配置获取对应的路由集合
 * {[route,name],[route,name]}
 * @param {*} layout
 */
export function getRouteData(layout = "BasicLayout", userSystem = 1) {
    const navData = getNavData(layout, userSystem);
    let dataList = cloneDeep(navData);
    dataList.documentTitle =  dataList.name;
    const nodeList = getPlainNode(dataList,userSystem);
    return nodeList;
}

/**
 * 指定父级目录，获取子级路由信息,格式化为react-router格式
 * @param {*} children
 * @param {*} path
 * @param {*} name
 * @param {*} parent
 * @param documentTitle
 * @param isLink
 */
function getPlainNode({ children, path, name, parent, documentTitle, isLink },userSystem) {
    const arr = [];
    children &&
        children.forEach(node => {
            const item = node;
            item.path = `/${path}/${item.path || ""}`.replace(/\/+/g, "/");
            item.exact = node.exact !== false;
            item.isLink = item.component !== undefined;
            if(userSystem == systemEnum.offline){
              item.documentTitle =
                "教辅双师课堂 - " + item.name + (documentTitle ? " | " + documentTitle : "");
            }else{
              item.documentTitle = "在线互动实验室 - " + item.name + (documentTitle ? " | " + documentTitle : "");
            }
            item.parent = { path, name, parent, isLink: isLink };
            if (item.children) {
                if (item.component) {
                    arr.push(item);
                }
                arr.push(...getPlainNode(item));
            } else {
                if (item.children && item.component) {
                    item.exact = false;
                }
                arr.push(item);
            }
        });
    return arr;
}

/**
 * 获取路由配置数据
 * @param {*} layout
 */
function getNavData(layout, userSystem) {
    switch (layout) {
        case 'AdminLayout': {
          return userSystem === 1 ? adminOnlineNav : adminOfflineNav;
        }
        case 'StudentLayout': {
            return userSystem === 1 ? studentOnlineNav : studentOfflineNav;
        }
        case 'TeacherLayout': {
            return userSystem === 1 ? teaOnlineNav : teaOfflineNav;
        }
        default: {
            return studentNav;
        }
    }
}
