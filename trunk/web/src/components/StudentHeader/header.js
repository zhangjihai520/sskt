import React from 'react';
import { Layout, Menu, Avatar, Badge, Icon, Tooltip, Dropdown } from 'antd';
import { routerRedux } from 'dva/router';
import { getSessionStorage, clearAllStorage } from 'utils/storage';
import { logoutRole } from 'utils/authorization';
import { connect } from 'dva';
import classnames from 'classnames';
import MyIcon from 'components/MyIcon';
import style from './header.less';
import cloudPlatform from 'assets/common/cloud_platform.png';

const { Header } = Layout;
const { SubMenu } = Menu;
let userInfo = {};
/**
 * 学生头组件
 */
@connect(({ commonModel }) => ({
  StudentBasePath: commonModel.StudentBasePath,
  CurUserSystem: commonModel.CurUserSystem,
  SystemTheme: commonModel.SystemTheme,
  DledcPlatformUrl: commonModel.DledcPlatformUrl
}))
export default class HeaderMenu extends React.Component {
  /**
   * 构造函数
   * @param {*} props
   */
  constructor(props) {
    super(props);
    this.state = {
      pathname: this.props.props.location.pathname
    };
    userInfo = getSessionStorage('UserBaseInfo');
  }

  /**
   * 组件挂载后
   */
  componentWillMount() {
    const { pathname } = this.state;
    const { StudentBasePath } = this.props;
    // if (pathname === `${StudentBasePath}/Message` || `${StudentBasePath}`) {
    //   this.setState({ pathname: '' });
    // }

    if (pathname === `${StudentBasePath}/MinLesson/MinLessonVideo`) {
      this.setState({ pathname: `${StudentBasePath}/MinLesson` });
    }
  }

  /**     * 菜单栏点击事件
   */
  menuItemClick = ({ item, key }) => {
    const { dispatch } = this.props.props;
    dispatch(routerRedux.push(key));
  };
  /**
   * 跳转到消息中心
   */
  linkMessage = () => {
    const { dispatch } = this.props.props;
    const { StudentBasePath } = this.props;
    dispatch(
      routerRedux.push({
        pathname: `${StudentBasePath}/Message`
      })
    );

    const menuBox = document.getElementsByClassName('hedaer-menu')[0];
    const selecedMenu = menuBox.getElementsByClassName('ant-menu-item-selected')[0];
    if (!selecedMenu) {
      return false;
    }
    selecedMenu.className = 'ant-menu-item';
  };

  /**
   * 跳转到个人中心
   */
  linkPersonalCenter = () => {
    const { StudentBasePath } = this.props;
    this.props.props.dispatch(
      routerRedux.push({
        pathname: `${StudentBasePath}`
      })
    );

    const menuBox = document.getElementsByClassName('hedaer-menu')[0];
    const selecedMenu = menuBox.getElementsByClassName('ant-menu-item-selected')[0];
    if (!selecedMenu) {
      return false;
    }
    selecedMenu.className = 'ant-menu-item';
  };
  /**
   * 退出登录
   */
  logoutPage = () => {
    clearAllStorage();
    logoutRole();
  };
  /**
   * 渲染DOM
   */
  render() {
    const { StudentBasePath, CurUserSystem, SystemTheme, DledcPlatformUrl } = this.props;
    const menuInfo = (
      <Menu>
        <Menu.Item key="0">
          <a href="javascript:;" onClick={this.logoutPage}>
            退出登录
          </a>
        </Menu.Item>
      </Menu>
    );
    return (
      <Header className={style.studentHeader} style={{ position: 'fixed', zIndex: 1, width: '100%' }}>
        <div>
          <div className="logo">
            <img src={SystemTheme.stuImg} alt="双师课堂" />
          </div>
          <Menu
            theme="dark"
            mode="horizontal"
            defaultSelectedKeys={[this.state.pathname]}
            className={classnames(style.menu, 'hedaer-menu')}
            onClick={this.menuItemClick}
          >
            {CurUserSystem == 1
              ? [
                  <Menu.Item key={`${StudentBasePath}/AntiVirusPublicClass`}>中高考复习课</Menu.Item>,
                  <Menu.Item key={`${StudentBasePath}/MinLesson`}>课程资源</Menu.Item>,
                  <SubMenu
                    className={style.shortMenu}
                    title={
                      <span>
                        课程
                        <Icon className={style.down} type="down" />
                      </span>
                    }
                  >
                    <Menu.Item className={style.shortMenuItem} key={`${StudentBasePath}/ReservationCourse`}>
                      预约课程
                    </Menu.Item>
                    <Menu.Item className={style.shortMenuItem} key={`${StudentBasePath}/MyCourse`}>
                      我的课程
                    </Menu.Item>
                  </SubMenu>,
                  <Menu.Item key={`${StudentBasePath}/ScheduleManager`}>课表</Menu.Item>,
                  <SubMenu
                    className={style.shortMenu}
                    title={
                      <span>
                        评价
                        <Icon className={style.down} type="down" />
                      </span>
                    }
                  >
                    <Menu.Item className={style.shortMenuItem} key={`${StudentBasePath}/MyEvaluation`}>
                      我的评价
                    </Menu.Item>
                    <Menu.Item className={style.shortMenuItem} key={`${StudentBasePath}/EvaluationToMe`}>
                      对我的评价
                    </Menu.Item>
                  </SubMenu>
                ]
              : [
                  <Menu.Item key={`${StudentBasePath}/AntiVirusPublicClass`}>中高考复习课</Menu.Item>,
                  <Menu.Item key={`${StudentBasePath}/ScheduleManager`}>课表</Menu.Item>,
                  <Menu.Item key={`${StudentBasePath}/CourseManager`}>课程</Menu.Item>,
                  <SubMenu
                    className={style.shortMenu}
                    title={
                      <span>
                        评价
                        <Icon className={style.down} type="down" />
                      </span>
                    }
                  >
                    <Menu.Item className={style.shortMenuItem} key={`${StudentBasePath}/MyEvaluation`}>
                      我的评价
                    </Menu.Item>
                    <Menu.Item className={style.shortMenuItem} key={`${StudentBasePath}/EvaluationToMe`}>
                      对我的评价
                    </Menu.Item>
                  </SubMenu>
                ]}
          </Menu>
          <div className={style.avatarInfo}>
            <div className={style.feedbackInfo}>
              <Tooltip placement="bottom" title="反馈信息" arrowPointAtCenter>
                <a style={{ color: '#fff' }} href={SystemTheme.stuPath} target="_blank">
                  <MyIcon type="feedback" />
                </a>
              </Tooltip>
            </div>
            <Dropdown overlay={menuInfo} trigger={['hover']}>
              <div onClick={this.linkPersonalCenter}>
                {userInfo && userInfo.UserFace !== null ? <Avatar src={userInfo.UserFace} /> : <Avatar icon="user" />}
                <span>{(userInfo.UserTrueName !== null && userInfo.UserTrueName) || '未设置姓名'}</span>
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
          </div>
        </div>
      </Header>
    );
  }
}
