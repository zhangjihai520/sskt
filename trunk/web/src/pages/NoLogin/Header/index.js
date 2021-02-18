import React from 'react';
import { Layout, Menu, Avatar, Badge, Icon, Button } from 'antd';
import router from 'umi/router';
import { getSessionStorage } from 'utils/storage';
import { connect } from 'dva';
import classnames from 'classnames';
import MyIcon from 'components/MyIcon';
import style from './index.less';

const { Header } = Layout,
  AntiVirusPath = '/AccessOnline/AntiVirusPublicClass',
  HomePath = '/AccessOnline/Home',
  MinLessonPath = '/AccessOnline/MinLesson';
const { SubMenu } = Menu;
/**
 * 学生头组件
 */
@connect(({ commonModel }) => ({
  StudentBasePath: commonModel.StudentBasePath,
  CurUserSystem: commonModel.CurUserSystem,
  SystemTheme: commonModel.SystemTheme
}))
export default class HeaderMenu extends React.Component {
  /**
   * 构造函数
   * @param {*} props
   */
  constructor(props) {
    super(props);
    this.state = {
      selectKey: props.location.pathname
    };
  }

  /**
   * 组件挂载后
   */
  componentDidMount() {
    const { changeModalStatus, location } = this.props;
    const pathname = location.pathname;
    const modalFlag = location.params && location.params.modalshow;
    if (pathname == HomePath) {
      // 从微课跳转点击时候默认弹框
      if (modalFlag) {
        changeModalStatus(true);
      }
    }
    this.setState({
      selectKey: pathname
    });
  }

  /**防止内存泄漏，离开组件之后不让重新设置属性 */
  componentWillUnmount() {
    this.setState = (state, callback) => {
      return;
    };
  }
  /**
   * 菜单栏点击事件
   */
  goLessonList = (context, path) => {
    const { dispatch } = this.props;
    this.setState(
      {
        selectKey: context.key
      },
      () => {
        router.push(path);
      }
    );
  };

  /**
   * 展示登录弹框
   */

  changeLoginModal = () => {
    const { dispatch, changeModalStatus } = this.props;
    this.setState({
      selectKey: HomePath
    });
    // 跳转到未登录的主页
    router.push({
      pathname: HomePath,
      params: {
        modalshow: true
      }
    });
    changeModalStatus(true);
  };

  /**
   * 渲染DOM
   */
  render() {
    const { selectKey } = this.state;
    const { StudentBasePath, CurUserSystem, SystemTheme, changeModalStatus } = this.props;
    const ScheduleManager = `${StudentBasePath}/ScheduleManager`,
      CourseManager = `${StudentBasePath}/CourseManager`,
      EvaluationManager = `${StudentBasePath}/EvaluationManager`;
    return (
      <Header className={style.studentHeader} style={{ position: 'fixed', zIndex: 1, width: '100%' }}>
        <div>
          <div className="logo">
            <img src={SystemTheme.stuImg} alt="双师课堂" />
          </div>
          <Menu
            theme="dark"
            mode="horizontal"
            defaultSelectedKeys={[selectKey]}
            className={classnames(style.menu, 'hedaer-menu')}
            selectable={false}
          >
            {CurUserSystem == 1
              ? [
                  <Menu.Item
                    key={AntiVirusPath}
                    onClick={key => this.goLessonList(key, AntiVirusPath)}
                    className={selectKey == AntiVirusPath ? style.menuActive : ''}
                  >
                    中高考复习课
                  </Menu.Item>,
                  <Menu.Item
                    key={MinLessonPath}
                    onClick={key => this.goLessonList(key, MinLessonPath)}
                    className={selectKey == MinLessonPath|| selectKey == '/'  ? style.menuActive : ''}
                  >
                    课程资源
                  </Menu.Item>,
                  <SubMenu
                    className={style.shortMenu}
                    title={
                      <span>
                        课程
                        <Icon className={style.down} type="down" />
                      </span>
                    }
                  >
                    <Menu.Item key={HomePath} onClick={key => this.goLessonList(key, HomePath)}>
                      预约课程
                    </Menu.Item>
                    <Menu.Item key={`${HomePath}2`} onClick={this.changeLoginModal}>
                      我的课程
                    </Menu.Item>
                  </SubMenu>,
                  <Menu.Item
                    key={ScheduleManager}
                    onClick={this.changeLoginModal}
                    className={selectKey == ScheduleManager ? style.menuActive : ''}
                  >
                    课表
                  </Menu.Item>,
                  <SubMenu
                    className={style.shortMenu}
                    title={
                      <span>
                        评价
                        <Icon className={style.down} type="down" />
                      </span>
                    }
                  >
                    <Menu.Item key={`${StudentBasePath}/MyEvaluation`} onClick={this.changeLoginModal}>
                      我的评价
                    </Menu.Item>
                    <Menu.Item key={`${StudentBasePath}/EvaluationToMe`} onClick={this.changeLoginModal}>
                      对我的评价
                    </Menu.Item>
                  </SubMenu>
                ]
              : [
                  <Menu.Item key={ScheduleManager}>课表</Menu.Item>,
                  <SubMenu
                    className={style.shortMenu}
                    title={
                      <span>
                        课程
                        <Icon className={style.down} type="down" />
                      </span>
                    }
                  >
                    <Menu.Item key={HomePath} onClick={key => this.goLessonList(key, HomePath)}>
                      预约课程
                    </Menu.Item>
                    <Menu.Item key={`${HomePath}2`} onClick={this.changeLoginModal}>
                      我的课程
                    </Menu.Item>
                  </SubMenu>,
                  <SubMenu
                    className={style.shortMenu}
                    title={
                      <span>
                        评价
                        <Icon className={style.down} type="down" />
                      </span>
                    }
                  >
                    <Menu.Item key={`${StudentBasePath}/MyEvaluation`} onClick={this.changeLoginModal}>
                      我的评价
                    </Menu.Item>
                    <Menu.Item key={`${StudentBasePath}/EvaluationToMe`} onClick={this.changeLoginModal}>
                      对我的评价
                    </Menu.Item>
                  </SubMenu>
                ]}
          </Menu>
          <div className={style.avatarInfo}>
            <div>
              <Badge count={0} className={classnames(style.badge, { noread: true })}>
                <Icon type="bell" style={{ fontSize: '20px' }} />
              </Badge>
            </div>
            <Button onClick={() => changeModalStatus(true)}>登录</Button>
          </div>
        </div>
      </Header>
    );
  }
}
