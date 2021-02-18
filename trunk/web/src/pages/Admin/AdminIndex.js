import React, { Component } from 'react';
import { connect } from 'dva';
import { Layout } from 'antd';
import { Link } from 'dva/router';
import styles from './AdminIndex.less';
import { Authorization } from 'utils/authorization';
import { roleEnum, systemEnum } from 'config/enum';
import classnames from 'classnames';

const { Header, Content, Footer } = Layout;

/**
 * 教师首页
 */
class AdminIndex extends Component {
  state = {
    message: '暂无'
  };

  componentWillMount() {
    //管理员端权限验证
    Authorization(roleEnum.admin);
    if (this.props.CurUserSystem == systemEnum.online) {
      document.title = '在线互动实验室- 南昌市现代教育技术中心';
    }
  }

  render() {
    const { AdminBasePath, UserBaseInfo, SystemTheme, CurUserSystem } = this.props;
    const isOnline = CurUserSystem == systemEnum.online;

    return (
      <Layout className={classnames(styles.layout, { [styles.offline]: isOnline })}>
        <Header className={styles.header}>
          <div className={styles.logo}>
            <img alt={SystemTheme.titleSuffix} src={SystemTheme.homeImg} />
          </div>
          <div className={styles.user}>
            <img
              alt={UserBaseInfo && UserBaseInfo.UserTrueName}
              src={
                UserBaseInfo && UserBaseInfo.UserFace ? UserBaseInfo.UserFace : '/images/common/img_user_default.png'
              }
            />
            <span>{UserBaseInfo && UserBaseInfo.UserTrueName}</span>
          </div>
        </Header>
        <Content className={styles.main}>
          {isOnline ? <h1>在线互动实验室直播.课堂</h1> : <h1>双师直播.课堂</h1>}
          <h2>全国一线名师面授课</h2>
          <div className={styles.menulist}>
            <div className={styles.item}>
              <div className={styles.pic}>
                <img alt="课表管理" src="/images/home/img_home_classschedule.png" />
              </div>
              <div className={styles.title}>课表管理</div>
              <div>
                <Link to={`${AdminBasePath}/ClassSchedule`}>查看课表管理</Link>
              </div>
            </div>
            <div className={styles.item}>
              <div className={styles.pic}>
                <img alt="课程统计" src="/images/home/img_home_learninganalysis.png" />
              </div>
              <div className={styles.title}>课程统计</div>
              <div>
                <Link to={`${AdminBasePath}/ClassStatistics`}>查看课程统计</Link>
              </div>
            </div>
            <div className={styles.item}>
              <div className={styles.pic}>
                <img alt="在线微课管理" src="/images/home/img_home_smalllessonlist.png" />
              </div>
              <div className={styles.title}>在线微课管理</div>
              <div>
                <Link to={`${AdminBasePath}/SmallLessonList`}>查看在线微课管理</Link>
              </div>
            </div>
          </div>
        </Content>
        <Footer className={[styles.footer, 'tc']}>
          <p>{SystemTheme.copyrightTextTitle}</p>
          <p>{SystemTheme.copyrightText}</p>
        </Footer>
      </Layout>
    );
  }
}

function stateToProp(state) {
  return {
    CurUserSystem: state.commonModel.CurUserSystem,
    AdminBasePath: state.commonModel.AdminBasePath,
    UserBaseInfo: state.commonModel.UserBaseInfo,
    SystemTheme: state.commonModel.SystemTheme
  };
}

export default connect(stateToProp)(AdminIndex);
