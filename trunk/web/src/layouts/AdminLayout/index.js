import React, { Component } from 'react';
import { Route, Switch } from 'dva/router';
import { connect } from 'dva';
import { routerRedux } from 'dva/router';
import { Layout, Menu, Icon, Dropdown} from 'antd';
import { getRouteData } from '../routerNav';
import style from './index.less';
import cmstyle from '../index.less';
import MyIcon from 'components/MyIcon';
import Breadcrumbs from 'components/Breadcrumbs';
import { Authorization, logoutRole} from 'utils/authorization';
import { clearAllStorage } from 'utils/storage';
import { roleEnum,systemEnum } from 'config/enum';
import classnames from 'classnames';

const { Header, Sider, Content, Footer } = Layout;

class AdminLayout extends Component {

  state = {
    /**
     * 左侧菜单收缩状态
     */
    collapsed: false
  };
	componentWillMount(){
		//管理员端权限验证
		Authorization(roleEnum.admin);
	}
  /**
   * 收缩展开菜单
   */
  menuToggle = () => {
    this.setState({
      collapsed: !this.state.collapsed,
    });
  }

  /**
   * 点击菜单跳转页面
   * @param {Object} 单击的菜单项
   */
  menuItemClick = ({ item, key }) => {
    if (key.length < 2) return;
    const { dispatch } = this.props;
    dispatch(routerRedux.push(key));
  }
  /**
   * 退出登录
   */
  logoutPage = () => {
    clearAllStorage();
    logoutRole();
  }
  render() {
    const { CurUserSystem , AdminBasePath,location, UserBaseInfo,SystemTheme } = this.props;
    const breadcrumbNameMap = getRouteData('AdminLayout', CurUserSystem);
    const isOnline = CurUserSystem == systemEnum.online;

    const menuInfo = (
      <Menu>
        <Menu.Item key="0">
          <a href="javascript:void(0);" onClick={this.logoutPage}>退出登录</a>
        </Menu.Item>
      </Menu>
    )
    return (
        <Layout className={classnames({[cmstyle.online]:isOnline})}>
          <Header>
            <img alt={SystemTheme.titleSuffix} src={SystemTheme.adminImg} />
            <Dropdown overlay={menuInfo} trigger={['hover']}>
              <div className={style.user}>
                <img alt={UserBaseInfo && UserBaseInfo.UserTrueName}
                src={UserBaseInfo && UserBaseInfo.UserFace ? UserBaseInfo.UserFace : '/images/common/img_user_default.png'} />
                <span>{UserBaseInfo && UserBaseInfo.UserTrueName}</span>
              </div>
            </Dropdown>
          </Header>
          <Layout>
            <Sider trigger={null} collapsible collapsed={this.state.collapsed} >
              <Menu className={style.menu} theme="dark" mode="inline" defaultSelectedKeys={[location.pathname]} selectedKeys={[`${AdminBasePath}/${location.pathname.split('/')[2]}`, location.pathname]} onClick={this.menuItemClick} >
                <Menu.Item key="0" title="收缩展开菜单" onClick={this.menuToggle}>
                  <div className="tc">
                    <Icon className="trigger" type={this.state.collapsed ? 'menu-unfold' : 'menu-fold'}
                      style={{ fontSize: "20px" }} /></div>
                </Menu.Item>
                <Menu.Item key={`${AdminBasePath}/ClassSchedule`}>
                  <MyIcon style={{ fontSize: "20px" }} type="calendar" /><span>课表管理</span>
                </Menu.Item>
                <Menu.Item key={`${AdminBasePath}/ClassStatistics`}>
                  <MyIcon style={{ fontSize: "20px" }} type="book" /><span>课程统计</span>
                </Menu.Item>
                <Menu.Item key={`${AdminBasePath}/SmallLessonList`}>
                  <MyIcon style={{ fontSize: "20px" }} type="video" /><span>在线微课管理</span>
                </Menu.Item>
              </Menu>
            </Sider>
            <Layout>
              <Header className={style.breadcrumb}>
                { Breadcrumbs({data: breadcrumbNameMap, location}) }
              </Header>
              <Content className={style.content} >
                <Switch>
                  {SystemTheme && getRouteData('AdminLayout', CurUserSystem, SystemTheme).map(item => (
                    <Route exact={item.exact} key={item.path} path={item.path} component={item.component} />
                  ))}
                </Switch>
              </Content>
              <Footer className='tc'>
                <p>{SystemTheme.copyrightTextTitle}</p>
                <p>{SystemTheme.copyrightText}</p>
              </Footer>
            </Layout>
          </Layout>
        </Layout>
    )
  };
}

function stateToProp(state) {
  return {
    CurUserSystem: state.commonModel.CurUserSystem,
    AdminBasePath: state.commonModel.AdminBasePath,
    UserBaseInfo: state.commonModel.UserBaseInfo,
    SystemTheme: state.commonModel.SystemTheme
  }
};

export default connect(stateToProp)(AdminLayout);
