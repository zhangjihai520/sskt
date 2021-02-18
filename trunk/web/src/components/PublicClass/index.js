import React, { Component } from 'react';
import { Tag, Row, Col, Button, Icon, Affix, Tooltip } from 'antd';
import $ from 'jquery';
import style from './index.less';
import { connect } from 'dva';
import { systemEnum } from 'config/enum';
import _ from 'lodash';
import router from 'umi/router';
import classnames from 'classnames';
import moment from 'moment';
import Banner from 'assets/common/img_antivirus_banner.jpg';
import NullData from 'components/NullData';
// import MotkIMG from 'assets/common/AntiVirus/motk_img.png';

const { CheckableTag } = Tag;
const noLoginKey = 2; //我的课程状态

class PublicClass extends Component {
  constructor(props) {
    super(props);
    this.state = {
      queryDate: [],
      currentDate: '',
      transformLength: 0,
      transformMin: -900,
      transfromBase: 200,
      currentGrade: 3, // 初三Id 3，高三Id 6
      dateGroup: undefined
    };
  }

  componentDidMount() {
    // reset style
    $('.gd').css({ width: '100%' });
    document.title = '在线互动实验室- 南昌市现代教育技术中心';
    this.getDateQuery();
  }

  componentWillUnmount() {
    $('.gd').css({ width: '' });
  }

  getDateQuery = () => {
    const { dispatch } = this.props;
    const self = this;

    dispatch({
      type: 'stuModel/CurrentTime'
    }).then(result => {
      const beginDate = moment(result.MinDate);
      const endDate = moment(result.MaxDate);
      const diffDay = endDate.diff(beginDate, 'days');

      const queryDate = [];
      for (var i = 0; i < diffDay + 1; i++) {
        let item = moment(result.MinDate).add(i, 'd');
        queryDate.push(item);
      }

      this.setState(
        {
          queryDate: queryDate,
          currentDate: moment(result.CurrentDate).isBetween(result.MinDate, result.MaxDate, null, '[]')
            ? moment(result.CurrentDate)
            : moment(queryDate[0])
        },
        () => {
          var scrollWrap = $('#scrollWrap');
          const min =
            scrollWrap.children(0).outerWidth(true) * scrollWrap.children().length - scrollWrap.parent().width();
          this.setState({ transformMin: -1 * min < 0 ? -1 * min : 0 });

          const index = _.findIndex(self.state.queryDate, item => {
            return item.isSame(self.state.currentDate);
          });
          this.setState({
            transformLength:
              -1 * index * scrollWrap.children(0).outerWidth(true) < -1 * min
                ? -1 * min
                : -1 * index * scrollWrap.children(0).outerWidth(true)
          });

          self.queryLessonDate();
        }
      );
    });
  };

  queryLessonDate = () => {
    const { currentDate, currentGrade } = this.state;
    const { dispatch } = this.props;

    dispatch({
      type: 'stuModel/GetPublicBenefitSubjectList',
      payload: {
        Date: currentDate.format('YYYY/MM/DD'),
        GradeId: currentGrade,
        CourseId: 0,
        UserRole: 1,
        IsWeb: 1
      }
    }).then(result => {
      this.setState({
        dateGroup: result.DateGroup
      });
    });
  };

  handleLeftArrowClick = () => {
    const { transformLength, transfromBase } = this.state;
    let result = transformLength;
    if (transformLength + transfromBase <= 0) {
      result = transformLength + transfromBase;
    } else {
      result = 0;
    }
    this.setState({
      transformLength: result
    });
  };

  handleRightrrowClick = () => {
    const { transformLength, transfromBase, transformMin } = this.state;
    let result = transformLength;
    if (transformLength - transfromBase > transformMin) {
      result = transformLength - transfromBase;
    } else {
      result = transformMin;
    }
    this.setState({
      transformLength: result
    });
  };

  handleTagClick = date => {
    this.setState(
      {
        currentDate: date
      },
      () => {
        this.queryLessonDate();
      }
    );
  };

  handleGradeClick = date => {
    this.setState(
      {
        currentGrade: date
      },
      () => {
        this.queryLessonDate();
      }
    );
  };

  handleLessonClick = lessonItem => {
    const { currentDate, currentGrade } = this.state;
    const { changeModalStatus } = this.props;
    if (changeModalStatus) {
      // 未登录状态弹窗提醒
      changeModalStatus(true);
      return;
    }
    if (lessonItem.PopupUrl) {
      window.open('javascript:window.name;', '<script>location.replace("'+lessonItem.PopupUrl+'")<\/script>');
    } else if (lessonItem.FileDownUrl) {
      window.open(lessonItem.FileDownUrl);
    } else {
      router.push(
        `/${window.location.pathname.substring(1).split('/')[0]}/AntiVirusPublicClass/PublicClassLiveStream?SubjectId=${
          lessonItem.SubjectId
        }&currentDate=${currentDate.format('YYYY/MM/DD')}&currentGrade=${currentGrade}`
      );
    }
  };

  getSrc = lessonItem => {
    const { currentGrade } = this.state;
    let gradeName = 'cz';
    currentGrade == 3 ? (gradeName = 'cz') : (gradeName = 'gz');
    const src = require(`assets/common/AntiVirus/${gradeName}${lessonItem.ShortCode}.png`);
    return <img className={style.imageIcon} src={src} />;
  };

  handleQuestionClick = (IsZzzy) => {
    if (!IsZzzy) {
      window.open(
        'http://sskt.motk.com/course/%E5%8A%A0%E5%85%A5%E7%8F%AD%E7%BA%A7%26%E8%8E%B7%E5%8F%96%E8%AF%BE%E5%90%8E%E4%BD%9C%E4%B8%9A%E6%93%8D%E4%BD%9C%E6%8C%87%E5%8D%97.pdf'
      );
    }
  };

  render() {
    const { queryDate, currentDate, currentGrade, transformLength, dateGroup } = this.state;

    return (
      <div className={style.wrap}>
        <div className={style.bannerbox}>
          <img className={style.banner} src={Banner} />
        </div>
        <div className={style.content}>
          <div className={style.queryContent}>
            <div className={style.gradeQueryContent}>
              <span className={style.strongLal}>年级：</span>
              <CheckableTag
                key={'t1'}
                checked={currentGrade == 3}
                onChange={() => {
                  this.handleGradeClick(3);
                }}
              >
                初三年级中考复习
              </CheckableTag>
              <CheckableTag
                key={'t2'}
                checked={currentGrade == 6}
                onChange={() => {
                  this.handleGradeClick(6);
                }}
              >
                高三年级专题课
              </CheckableTag>
            </div>
            <div className={style.dateQueryContent}>
              <span className={style.strongLal}>星期：</span>
              <Icon className={style.switchIcon} type="left" onClick={this.handleLeftArrowClick} />
              <div className={style.scrollContent}>
                <div
                  id="scrollWrap"
                  className={style.dateQueryScrollWrap}
                  style={{ transform: `translate3d(${transformLength}px, 0px, 0px)` }}
                >
                  {queryDate &&
                    queryDate.map((item, index) => {
                      return (
                        <CheckableTag
                          key={index}
                          checked={currentDate.isSame(item)}
                          onChange={() => {
                            this.handleTagClick(item);
                          }}
                        >
                          <span className={style.queryDate}>
                            <span>{item.format('dddd')}</span>
                            <span>{item.format('YYYY-MM-DD')}</span>
                          </span>
                        </CheckableTag>
                      );
                    })}
                </div>
              </div>
              <Icon className={style.switchIcon} type="right" onClick={this.handleRightrrowClick} />
            </div>
            
          </div>
          {dateGroup ? (
            dateGroup.length > 0 ? (
              dateGroup.map((item, index) => {
                return (
                  <div className={style.resultContent} key={`group${index}`}>
                    <div className={style.title}>{item.Date}</div>
                    <div className={style.lessonContent}>
                      {item.GroupClassInfoList &&
                        item.GroupClassInfoList.map((lessonItem, lessonIndex) => {
                          let curstyle = style.lessonItem,
                            btnName = '暂未开课',
                            btnDisable = true,
                            showTips = false,
                            tipsName = '点击查看加入班级&获取课后作业操作指南';
                          if (lessonItem.StatusFlag == 6) {
                            curstyle = classnames(style.lessonItem, style.cur);
                            btnName = '正在直播';
                            if (lessonItem.FileDownUrl) {
                              btnName = '立刻练习';
                              showTips = true;
                            }
                            btnDisable = false;
                          } else if (lessonItem.StatusFlag == 5) {
                            curstyle = classnames(style.lessonItem, style.review);
                            btnName = '点击回看';
                            if (lessonItem.FileDownUrl) {
                              btnName = '立刻练习';
                              showTips = true;
                            }
                            btnDisable = false;
                          }

                          if (lessonItem.IsZzzy) {
                            curstyle = style.lessonItem;
                            if (lessonItem.StatusFlag == 6 || lessonItem.StatusFlag == 5) {
                              curstyle = classnames(style.lessonItem, style.init);
                            }
                            btnName = '无直播';
                            btnDisable = true;
                            showTips = true;
                            tipsName = '无课程视频，学生自主练习';
                          }

                          return (
                            <div className={curstyle} key={`lesson${lessonIndex}`}>
                              {this.getSrc(lessonItem)}
                              <span className={style.courseName}>{`${lessonItem.CourseName} ${moment(
                                lessonItem.BeginTime
                              ).format('H:mm')}`}</span>
                              <span className={style.lessonName}>{lessonItem.SubjectName}</span>
                              <Button
                                className={style.btn}
                                disabled={btnDisable}
                                onClick={() => {
                                  this.handleLessonClick(lessonItem);
                                }}
                              >
                                {btnName}
                              </Button>
                              {showTips && (
                                <Tooltip title={tipsName}>
                                  <Icon
                                    className={style.question}
                                    type="question-circle"
                                    onClick={() => {this.handleQuestionClick(lessonItem.IsZzzy)}}
                                  />
                                </Tooltip>
                              )}
                            </div>
                          );
                        })}
                    </div>
                  </div>
                );
              })
            ) : (
              <div className={style.nullContent}>
                <NullData isShow={true} message={'暂无数据'} />
              </div>
            )
          ) : (
            ''
          )}
        </div>
      </div>
    );
  }
}

function stateToProp(state) {
  return {
    studentBasePath: state.commonModel.StudentBasePath
  };
}

export default connect(stateToProp)(PublicClass);
