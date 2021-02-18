import React, { Component } from 'react';
import { Icon, Button } from 'antd';
import { connect } from 'dva';
import style from './index.less';
import MyIcon from 'components/MyIcon';
import router from 'umi/router';
import alreadySign from 'assets/common/already_sign.png';

@connect(({ commonModel }) => ({
  studentBasePath: commonModel.StudentBasePath
}))
export default class StuCourseQueryResult extends Component {
  constructor(props) {
    super(props);
    this.state = {
      data: props.data,
      loading: false
    };
  }

  handleClick = () => {
    const { data } = this.state;
    const { onCallback, studentBasePath, changeModalStatus } = this.props;
    if (changeModalStatus) {
      // 未登录状态弹窗提醒
      changeModalStatus(true);
      return;
    }
    if (data.StatusFlag == 5) {
      router.push(`${studentBasePath}/CourseManager/recordStream?SubjectId=${data.SubjectId}`);
      return;
    }
    if (data.StatusFlag == 6) {
      router.push(`${studentBasePath}/CourseManager/LiveStream?SubjectId=${data.SubjectId}`);
      return;
    }
    if (data.IsRegister) {
      return;
    }

    this.setState({ loading: true });
    let param = {
      SubjectId: data.SubjectId
    };

    this.props
      .dispatch({
        type: 'stuModel/RegistSubject',
        payload: param
      })
      .then(result => {
        this.setState({ loading: false });
        if (result == 1) {
          data.IsRegister = true;
          data.RegisterNumber += 1;
          this.setState({ data: data });
          onCallback && onCallback();
        }
      });
  };

  render() {
    const { data, loading } = this.state;

    const btnState = () => {
      if (data.IsRegister) {
        if (data.StatusFlag == 5) {
          return false;
        } else if (data.StatusFlag == 6){
          return false;
        }else if (data.StatusFlag == 4) {
          return true;
        }else
        {
          return true
        }
      } else {
        if (data.StatusFlag == 5) {
          return false;
        } else if (data.StatusFlag == 6) {
          return true;
        } else if (data.StatusFlag == 2) {
          return true;
        } else if (data.StatusFlag == 3) {
          return false;
        }else if (data.StatusFlag == 4) {
          return true;
        }else {
          if (data.MaxRegisterNumber == 0 || data.MaxRegisterNumber > data.RegisterNumber) {
            return false;
          } else {
            return true;
          }
        }
      }
    };

    const btn = () => {
      let name = '';
      if (data.IsRegister) {
        if (data.StatusFlag == 6) {
          name = '进入课堂';
        } else if (data.StatusFlag == 5) {
          name = '回看';
        }else if (data.StatusFlag == 4) {
          name = '待上课';
        }else
        {
          name = '已报名';
        }
      } else {
        if (data.StatusFlag == 2) {
          name = "待报名"
        }else if (data.StatusFlag == 3) {
          name = '报名';
        }else if (data.StatusFlag == 4) {
          name = '待上课';
        }else if (data.StatusFlag == 5) {
          name = '回看';
        }else if (data.StatusFlag == 6){
          name = '上课中';
        }
      }

      if (this.props.CurUserSystem != 1 && name == '报名') {
        return '';
      } else {
        return (
          <Button className={style.signBtn} type="primary" onClick={this.handleClick} loading={loading} disabled={btnState()} >
            {name}
          </Button>
        );
      }
    };

    return (
      <div className={style.coursePanel}>
        {data.IsRegister && <img className={style.sign} src={alreadySign}/>}
        <div className={style.courseImg}>
          <img src={data.ImagePath ? data.ImagePath : require(`assets/common/courseImg/${data.CourseShortCode}.png`)} />
        </div>
        <div className={style.people}>
          {data.MaxRegisterNumber != 0 ? (
            <span key="doc">
              {data.RegisterNumber}/{data.MaxRegisterNumber}人参加{' '}
            </span>
          ) : (
            ''
          )}
        </div>
        <div className={style.courseItem}>
          <div className={style.courseName}>{data.SubjectName}</div>
          <div className={style.school}>
            {data.SchoolName} {data.TeacherName}
          </div>
          <div className={style.courseState}>
            <div className={style.text}>截止时间：{data.RegistEndTime}</div>
            {btn()}
          </div>
        </div>
      </div>
    );
  }
}
