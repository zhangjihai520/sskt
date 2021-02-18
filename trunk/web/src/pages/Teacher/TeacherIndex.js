import React, { Component } from 'react';
import { connect } from 'dva';
import { Layout } from 'antd';
import { Link } from 'dva/router';
import styles from './TeacherIndex.less';
import classNames from 'classnames';
import MyIcon from '../../components/MyIcon';
import { Authorization } from 'utils/authorization';
import { roleEnum, systemEnum } from 'config/enum';
import teacherBjBlue from '../../../public/images/home/img_home_teacher_bj.png';
import teacherBackGroundOnline from '../../../public/images/home/img_home_teacher_background_online.jpg';

const { Header, Content, Footer } = Layout;

/**
 * 教师首页
 */
class TeacherIndex extends Component {
  state = {
    message: '暂无'
  };

  componentWillMount() {
    //老师端权限验证
    Authorization(roleEnum.teacher);
  }

  componentDidMount() {
    this.getMessageData();
    if (this.props.CurUserSystem == systemEnum.online) {
      document.title = '在线互动实验室- 南昌市现代教育技术中心';
    }
  }

  /**
   * 获取提醒消息
   */
  getMessageData() {
    const { dispatch } = this.props;
    dispatch({
      type: 'classScheduleModel/GetCurrentSubiject'
    }).then(data => {
      if (data.SubjectList && data.SubjectList.length > 0) {
        let msgList = [];
        data.SubjectList.map(msg => {
          msgList.push(msg.SubjectName);
        });
        this.setState({
          message: msgList.join('、')
        });
      }
    });
  }

  render() {
    const { message } = this.state;
    const { CurUserSystem, TeaBasePath, UserBaseInfo, SystemTheme } = this.props;
    const isOnline = CurUserSystem == systemEnum.online;
    return (
      <Layout
        className={styles.layout}
        style={{ backgroundImage: isOnline ? `url(${teacherBackGroundOnline})` : `url(${teacherBjBlue})` }}
      >
        <Header className={styles.header}>
          <div className={styles.logo}>
            <img alt={SystemTheme.titleSuffix} src={SystemTheme.homeImg} />
          </div>
          <div className={styles.message}>
            <MyIcon type="trumpet" />
            &nbsp;今天课程：{message}
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
          <img className={styles.logo} alt="logo" src={SystemTheme.bannerImg} />
          <div className={styles.menulist}>
            <Link className={styles.item} to={`${TeaBasePath}/AntiVirusPublicClass`}>
              <img alt="中高考复习课" src="/images/home/img_home_teacher_smalllesson.png" />
              <div className={styles.title}>中高考复习课</div>
            </Link>
            <Link className={styles.item} to={`${TeaBasePath}/ClassSchedule`}>
              <img alt="课表管理" src="/images/home/img_home_teacher_classschedule.png" />
              <div className={styles.title}>课表管理</div>
            </Link>
            <Link className={styles.item} to={`${TeaBasePath}/Curriculum`}>
              <img alt="课程管理" src="/images/home/img_home_teacher_curriculum.png" />
              <div className={styles.title}>课程管理</div>
            </Link>
            {CurUserSystem === 1 && (
              <Link className={styles.item} to={`${TeaBasePath}/SmallLesson`}>
                <img alt="在线微课管理" src="/images/home/img_home_teacher_smalllesson.png" />
                <div className={styles.title}>在线微课管理</div>
              </Link>
            )}
            <Link className={styles.item} to={`${TeaBasePath}/TeachingEvaluation/Management`}>
              <img alt="评估教学管理" src="/images/home/img_home_teacher_evaluate.png" />
              <div className={styles.title}>评估教学管理</div>
            </Link>
            <Link className={styles.item} to={`${TeaBasePath}/Situation`}>
              <img alt="学情监控" src="/images/home/img_home_teacher_learninganalysis.png" />
              <div className={styles.title}>学情监控</div>
            </Link>
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
    TeaBasePath: state.commonModel.TeaBasePath,
    UserBaseInfo: state.commonModel.UserBaseInfo,
    SystemTheme: state.commonModel.SystemTheme
  };
}

export default connect(stateToProp)(TeacherIndex);
