import React, { Component } from 'react';
import { Route, Switch } from 'dva/router';
import { connect } from 'dva';
import { routerRedux } from 'dva/router';
import { Layout, Menu, Icon, Button, Tooltip, Dropdown, Affix } from 'antd';
import { getRouteData } from '../routerNav';
import style from './index.less';
import cmstyle from '../index.less';
import MyIcon from 'components/MyIcon';
import Breadcrumbs from 'components/Breadcrumbs';
import { Authorization, logoutRole } from 'utils/authorization';
import { clearAllStorage } from 'utils/storage';
import { roleEnum, systemEnum } from 'config/enum';
import cloudPlatform from 'assets/common/cloud_platform.png';
import router from 'umi/router';
import classnames from 'classnames';
import pathToRegexp from 'path-to-regexp';

const { Header, Sider, Content, Footer } = Layout;
const SubMenu = Menu.SubMenu;
class TeacherLayout extends Component {
  constructor(props) {
    super(props);
    this.state = {
      /**
       * 左侧菜单收缩状态
       */
      //collapsed: false
    };
  }
  componentWillMount() {
    //老师端权限验证
    Authorization(roleEnum.teacher);
  }
  /**
   * 收缩展开菜单
   */
  menuToggle = () => {
    console.info(this.props.Collapsed);
    this.props.dispatch({
      type: 'commonModel/changeCollapsed',
      payload: !this.props.Collapsed
    });
  };

  /**
   * 跳转到消息中心
   */
  linkMessage = () => {
    const { dispatch, CurUserSystem } = this.props;
    const path = CurUserSystem === systemEnum.online ? '/TeaOnline' : '/TeaOffline';
    dispatch(
      routerRedux.push({
        pathname: `${path}/TeachingEvaluation/Message`
      })
    );
  };
  back = () => {
    const { location } = this.props;
    if (location.pathname == '/TeaOnline/Curriculum/Work') {
      router.push(`/TeaOnline/Curriculum`);
    } else if (location.pathname == '/TeaOffline/Curriculum/Work') {
      router.push(`/TeaOffline/Curriculum`);
    } else {
      window.history.back();
    }
  };
  /**
   * 点击菜单跳转页面
   * @param {Object} 单击的菜单项
   */
  menuItemClick = ({ item, key }) => {
    if (key.length < 2) return;
    const { dispatch } = this.props;
    dispatch(routerRedux.push(key));
  };
  /**
   * 退出登录
   */
  logoutPage = () => {
    clearAllStorage();
    logoutRole();
  };
  render() {
    const {
      CurUserSystem,
      TeaBasePath,
      location,
      UserBaseInfo,
      LastBreadcrumbName,
      SystemTheme,
      DledcPlatformUrl
    } = this.props;
    const breadcrumbNameMap = getRouteData('TeacherLayout', CurUserSystem);
    const pathname = location.pathname.split('?')[0];
    const current = breadcrumbNameMap.filter(item => {
      return item.path === pathname || pathToRegexp(item.path).test(pathname);
    })[0];
    const isOnline = CurUserSystem == systemEnum.online;
    const menuInfo = (
      <Menu>
        <Menu.Item key="0">
          <a href="javascript:void(0);" onClick={this.logoutPage}>
            退出登录
          </a>
        </Menu.Item>
      </Menu>
    );
    return (
      <Layout className={classnames({ [cmstyle.online]: isOnline })}>
        <Layout>
          <Sider
            trigger={null}
            collapsible
            collapsed={this.props.Collapsed}
            width={280}
          >
            <img className={style.leftImage} alt={SystemTheme.titleSuffix} src={SystemTheme.teaImg} />
            <Menu
              className={style.menu}
              theme="dark"
              mode="inline"
              defaultOpenKeys={['4']}
              defaultSelectedKeys={[location.pathname]}
              selectedKeys={[`${TeaBasePath}/${location.pathname.split('/')[2]}`, location.pathname]}
              onClick={this.menuItemClick}
            >
              <Menu.Item key="0" title="收缩展开菜单" onClick={this.menuToggle}>
                <div className="tc">
                  <Icon
                    className="trigger"
                    type={this.props.Collapsed ? 'menu-unfold' : 'menu-fold'}
                    style={{ fontSize: '20px' }}
                  />
                </div>
              </Menu.Item>
              <Menu.Item className={style.flexCenter} key={`${TeaBasePath}/AntiVirusPublicClass`}>
                <MyIcon style={{ fontSize: '20px' }} type="book" />
                <span>中高考复习课</span>
              </Menu.Item>
              <Menu.Item className={style.flexCenter} key={`${TeaBasePath}/ClassSchedule`}>
                <MyIcon style={{ fontSize: '20px' }} type="calendar" />
                <span>课表管理</span>
              </Menu.Item>
              <Menu.Item className={style.flexCenter} key={`${TeaBasePath}/Curriculum`}>
                <MyIcon style={{ fontSize: '20px' }} type="book" />
                <span>课程管理</span>
              </Menu.Item>
              {//线上角色权限
              CurUserSystem === 1 && (
                <Menu.Item className={style.flexCenter} key={`${TeaBasePath}/SmallLesson`}>
                  <MyIcon style={{ fontSize: '20px' }} type="video" />
                  <span>在线微课管理</span>
                </Menu.Item>
              )}
              <SubMenu
                key="4"
                title={
                  <span className={style.flexCenter}>
                    <MyIcon style={{ fontSize: '20px' }} type="assess" />
                    <span>评估教学管理</span>
                  </span>
                }
              >
                <Menu.Item key={`${TeaBasePath}/TeachingEvaluation/Management`}>评价管理</Menu.Item>
                <Menu.Item key={`${TeaBasePath}/TeachingEvaluation/Lesson`}>评价课程</Menu.Item>
              </SubMenu>
              <Menu.Item className={style.flexCenter} key={`${TeaBasePath}/Situation`}>
                <MyIcon style={{ fontSize: '20px' }} type="surveillance" />
                <span>学情监控</span>
              </Menu.Item>
            </Menu>
            <div className={style.footerWrap}>
              <Dropdown overlay={menuInfo} trigger={['hover']}>
                <div className={style.user}>
                  <img
                    alt={UserBaseInfo && UserBaseInfo.UserTrueName}
                    src={
                      UserBaseInfo && UserBaseInfo.UserFace
                        ? UserBaseInfo.UserFace
                        : '/images/common/img_user_default.png'
                    }
                  />
                  <span className={style.userName}>{UserBaseInfo && UserBaseInfo.UserTrueName}</span>
                </div>
              </Dropdown>
              <div
                className={style.cloudPlatform}
                onClick={() => {
                  window.location.href = DledcPlatformUrl;
                }}
              >
                云平台
              </div>
              <div className={style.feedbackInfo}>
                <Tooltip placement="bottom" title="信息反馈" arrowPointAtCenter>
                  <a style={{ color: '#fff' }} href={SystemTheme.teaPath} target="_blank">
                    <span className={style.desc}>信息反馈</span>
                  </a>
                </Tooltip>
              </div>
            </div>
          </Sider>
          <Layout style={{ backgroundColor: '#f1f2f6' }}>
            <Header className={style.breadcrumb}>
              <div>
                {Breadcrumbs({
                  data: breadcrumbNameMap,
                  location,
                  LastBreadcrumbName
                })}
                <div className={style.name}>{current.name}</div>
              </div>
              <div className={style.back} onClick={() => this.back()}>
                <span className={style.backDesc}>
                  <Icon type="left" /> 返回
                </span>
              </div>
            </Header>
            <Content className={style.content}>
              <Switch>
                {getRouteData('TeacherLayout', CurUserSystem, SystemTheme).map(item => (
                  <Route exact={item.exact} key={item.path} path={item.path} component={item.component} />
                ))}
              </Switch>
            </Content>
            <Footer className="tc">
              <p>{SystemTheme.copyrightTextTitle}</p>
              <p>{SystemTheme.copyrightText}</p>
            </Footer>
          </Layout>
        </Layout>
      </Layout>
    );
  }
}

function stateToProp(state) {
  console.info(state.commonModel.Collapsed);
  return {
    CurUserSystem: state.commonModel.CurUserSystem,
    TeaBasePath: state.commonModel.TeaBasePath,
    UserBaseInfo: state.commonModel.UserBaseInfo,
    UnReadCount: state.commonModel.UnReadCount,
    LastBreadcrumbName: state.commonModel.LastBreadcrumbName,
    SystemTheme: state.commonModel.SystemTheme,
    DledcPlatformUrl: state.commonModel.DledcPlatformUrl,
    Collapsed: state.commonModel.Collapsed
  };
}

export default connect(stateToProp)(TeacherLayout);
