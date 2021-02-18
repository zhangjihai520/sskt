import React, { Component } from 'react';
import { Icon, Button, Dropdown, Menu } from 'antd';
import { connect } from 'dva';
import style from './index.less';
import MyIcon from 'components/MyIcon';
import router from 'umi/router';
import broadcastImg from 'assets/common/img_img_teacher_broadcast.png';
import _ from 'lodash';

@connect(({ commonModel }) => ({
  studentBasePath: commonModel.StudentBasePath
}))
export default class MyCourseResult extends Component {
  constructor(props) {
    super(props);
    this.state = {
      data: props.data
    };
  }

  handlePreTestClick = preTestObj => {
    const { studentBasePath } = this.props;
    if (preTestObj) {
      if (preTestObj.IsFinish) {
        window.location = `${studentBasePath}/CourseManager/ViewAssessment?ExamSetId=${preTestObj.ExamSetId}`;
      } else {
        window.location = `${studentBasePath}/CourseManager/ClassroomExercises?ExamSetId=${preTestObj.ExamSetId}`;
      }
    }
  };

  handleEnterToWatch = () => {
    const { data } = this.state;
    const { studentBasePath } = this.props;

    this.props.changeStatusCallback(data.SubjectId, () => {
      if (data.StatusFlag == 5) {
        router.push(`${studentBasePath}/CourseManager/recordStream?SubjectId=${data.SubjectId}`);
        // window.location = ;
      } else {
        router.push(`${studentBasePath}/CourseManager/liveStream?SubjectId=${data.SubjectId}`);
        // window.location =
      }
    });
  };

  handleDownload = () => {
    const { data } = this.state;
    this.props.downloadCallBack(data.SubjectFileList);
  };

  render() {
    const { data } = this.state;
    const { studentBasePath } = this.props;
    let preTestObj, midTestObj, afterTestObj;
    preTestObj = _.find(data.ExamSetList, item => {
      return item.ExamSetTypeId === 1;
    });
    midTestObj = _.find(data.ExamSetList, item => {
      return item.ExamSetTypeId === 2;
    });
    afterTestObj = _.find(data.ExamSetList, item => {
      return item.ExamSetTypeId === 3;
    });

    let midTestUrl, afterTestUrl;
    if (midTestObj) {
      if (midTestObj.IsFinish) {
        midTestUrl = `${studentBasePath}/CourseManager/ViewAssessment?ExamSetId=${midTestObj.ExamSetId}`;
      } else {
        midTestUrl = `${studentBasePath}/CourseManager/ClassroomExercises?ExamSetId=${midTestObj.ExamSetId}`;
      }
    } else {
      midTestUrl = '#';
    }
    if (afterTestObj) {
      if (afterTestObj.IsFinish) {
        afterTestUrl = `${studentBasePath}/CourseManager/ViewAssessment?ExamSetId=${afterTestObj.ExamSetId}`;
      } else {
        afterTestUrl = `${studentBasePath}/CourseManager/ClassroomExercises?ExamSetId=${afterTestObj.ExamSetId}`;
      }
    } else {
      afterTestUrl = '#';
    }
    const btnState = () => {
        if (data.StatusFlag == 5) {
          return false;
        } else if (data.StatusFlag == 6){
          return false;
        }else
        {
          return true
        }
    };

    const btn = () => {
      let name = '';
        if (data.StatusFlag == 6) {
          name = '进入课堂';
        } else if (data.StatusFlag == 5) {
          name = '回看';
        }else{
          name = '待开课';
        }

        return (
          <Button className={style.rightBtn} type="primary" onClick={this.handleEnterToWatch} disabled={btnState()}>
          {name}
        </Button>
        );
    };
    const menu = (
      <Menu>
        <Menu.Item disabled={midTestObj ? false : true}>
          <a href={midTestUrl} disabled={midTestObj ? false : true}>
            随堂测试
          </a>
        </Menu.Item>
        <Menu.Item disabled={afterTestObj ? false : true}>
          <a href={afterTestUrl} disabled={afterTestObj ? false : true}>
            课后测试
          </a>
        </Menu.Item>
        <Menu.Item>
          <a onClick={this.handleDownload}>下载附件</a>
        </Menu.Item>
      </Menu>
    );

    return (
      <div className={style.coursePanel}>
        <div className={style.courseImg}>
          <img src={data.ImagePath ? data.ImagePath : require(`assets/common/courseImg/${data.CourseShortCode}.png`)} />
        </div>
        <div className={style.courseItem}>
          <div className={style.courseName}>{data.SubjectName}</div>
          <div className={style.school}>
            {data.SchoolName} <br /> {data.TeacherName}
          </div>
          <div className={style.courseState}>
            {/* <MyIcon className={style.icon} style={{ fontSize: "20px" }} type="clock" /> */}
            <span className={style.text}>课程时间：{data.BeginTime}</span>
            <div className={style.myCourseSetBtn}>
              <Dropdown overlay={menu}>
                <Button
                  type="primary"
                  onClick={() => {
                    this.handlePreTestClick(preTestObj);
                  }}
                >
                  课前测试 <Icon type="down" />
                </Button>
              </Dropdown>
              {btn()}
            </div>
          </div>
        </div>
      </div>
    );
  }
}
