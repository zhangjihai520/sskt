import React, { Component } from 'react';
import { Radio, Tabs, Icon, Button, Row, Col, Tag, Modal } from 'antd';
import style from './ReservationCourse.less';
import ListPagination from 'components/ListPagination';
import MyCourseResult from '../../../components/StuCourseQueryResult/MyCourseResult';
import { connect } from 'dva';
import { filterCourse } from 'utils/utils';

const TabPane = Tabs.TabPane;
const { CheckableTag } = Tag;

class MyCourse extends Component {
  constructor(props) {
    super(props);
    this.state = {
      pageSize: 10,
      pageIndex: 1,
      checkCourse: 0,
      checkType: 0,
      count: 0,
      data: undefined,
      visable: false,
      downloadData: undefined,
      loading: true,
      courseList: _.cloneDeep(this.props.CourseList)
    };

    if (props.UserBaseInfo && props.UserBaseInfo.GradeId) {
      this.state.checkGrade = props.UserBaseInfo.GradeId * 1;
    }
  }

  componentDidMount() {
    const { GradeList } = this.props;
    const { checkGrade } = this.state;
    let result = _.find(GradeList, function(item) {
      return item.Id == checkGrade;
    });
    if (!result) {
      result = { Id: 0 };
    }
    this.handleGradeChange(result);
  }

  handleGradeChange = item => {
    let courseList = _.cloneDeep(this.props.CourseList);
    if (item.Id != 0) {
      _.remove(courseList, function(course) {
        return course.CourseTypeId != item.CourseTypeId;
      });
    }

    this.setState({ courseList: courseList, checkGrade: item.Id, checkCourse: 0, pageIndex: 1 }, () => {
      this.queryResult();
    });
  };

  handleCourseChange = id => {
    this.setState({ checkCourse: id, pageIndex: 1 }, () => {
      this.queryResult();
    });
  };

  handleTabChang = activeKey => {
    this.setState({ checkType: activeKey, pageIndex: 1 }, () => {
      this.queryResult();
    });
  };

  handleChangePage = state => {
    this.setState(
      {
        pageIndex: state.PageIndex,
        pageSize: state.PageSize
      },
      () => {
        this.queryResult();
      }
    );
  };

  queryResult = () => {
    this.setState(
      {
        count: 0,
        data: []
      },
      () => {
        const { checkCourse, pageSize, pageIndex, checkType } = this.state;
        let param = {
          PageSize: pageSize,
          PageIndex: pageIndex,
          CourseId: checkCourse,
          GradeId: 0,
          TypeEnum: checkType,
          IsMySubject: 1,
          SubjectGenreId: 1
        };

        this.props
          .dispatch({
            type: 'stuModel/GetStudentSubjectListLogin',
            payload: param
          })
          .then(result => {
            this.setState({
              count: result.Count,
              data: result.SubjectList
            });
          });
      }
    );
  };

  handleClose = () => {
    this.setState({ visable: false });
  };

  downloadCallBack = SubjectFileList => {
    this.setState({
      downloadData: SubjectFileList,
      visable: true
    });
  };

  handleChangeStatus = (SubjectId, callback) => {
    const param = {
      SubjectId: SubjectId, //加密课程Id
      IsAbsent: 1 //上课状态,0到席，1缺席
    };
    this.props
      .dispatch({
        type: 'commonModel/UpdateSubjectStudentStatus',
        payload: param
      })
      .then(result => {
        callback && callback();
      });
  };

  render() {
    const { GradeList, UserBaseInfo } = this.props;
    const { checkGrade, checkCourse, pageIndex, count, data, visable, downloadData, courseList } = this.state;

    filterCourse(courseList);

    const GradeQuery = () => {
      let result;
      if (GradeList) {
        result = GradeList.map(item => {
          return (
            <CheckableTag
              key={item.Id}
              checked={item.Id == checkGrade}
              onChange={() => {
                this.handleGradeChange(item);
              }}
            >
              {item.Name}
            </CheckableTag>
          );
        });
      }

      return [
        <CheckableTag
          key={0}
          checked={checkGrade == 0}
          onChange={() => {
            this.handleGradeChange({ Id: 0 });
          }}
        >
          全部
        </CheckableTag>,
        result
      ];
    };

    const CourseQuery = () => {
      let result;
      if (courseList) {
        result = courseList.map(item => {
          return (
            <CheckableTag
              key={item.Id}
              checked={item.Id === checkCourse}
              onChange={() => {
                this.handleCourseChange(item.Id);
              }}
            >
              {item.LongName}
            </CheckableTag>
          );
        });
      }

      return [
        <CheckableTag
          key={0}
          checked={checkCourse === 0}
          onChange={() => {
            this.handleCourseChange(0);
          }}
        >
          全部
        </CheckableTag>,
        result
      ];
    };

    const dataContent = () => {
      if (data) {
        if (data.length == 0) {
          return <div className={style.nullPage}>暂无数据</div>;
        }
        return (
          <div className={style.listbox}>
            {data.map((item, index) => {
              return (
                <MyCourseResult
                  key={index}
                  data={item}
                  dispatch={this.props.dispatch}
                  downloadCallBack={this.downloadCallBack}
                  changeStatusCallback={this.handleChangeStatus}
                  CurUserSystem={this.props.CurUserSystem}
                />
              );
            })}
          </div>
        );
        // return data.map((item, index) => {
        //  return (
        //    <MyCourseResult key={index} data={item} dispatch={this.props.dispatch} downloadCallBack={this.downloadCallBack} changeStatusCallback={this.handleChangeStatus} CurUserSystem={this.props.CurUserSystem}/>
        //  )
        // });
      }
    };

    const downloadDataContent = () => {
      if (downloadData) {
        return downloadData.map(item => {
          return (
            <Row>
              <Col span={10}>{item.SubjectFileName}</Col>
              <Col span={2}>
                <a target="_blank" rel="noopener noreferrer" href={item.FilePath}>
                  下载
                </a>
              </Col>
            </Row>
          );
        });
      }
    };

    return (
      <div className={style.CourseManager}>
        <div className={style.bg}>
          <div className={style.bgImg} />
          <div className={style.bgContent} />
        </div>
        <div className={style.search_filter}>
          <div className={style.tagWrap}>
            <div className={style.tag_lst}>
              <Row>
                <Col>
                  <i className="mr-xl" style={{ marginRight: '20px' }}>
                    <span className={style.strongLal}>年级：</span>
                  </i>
                  <div className={style.checkedBox}>{GradeQuery()}</div>
                </Col>
              </Row>
            </div>
            <div className={style.tag_lst}>
              <Row>
                <Col>
                  <i className="mr-xl" style={{ marginRight: '20px' }}>
                    <span className={style.strongLal}>课程：</span>
                  </i>
                  <div className={style.checkedBox}>{CourseQuery()}</div>
                </Col>
              </Row>
            </div>
          </div>
          <div className={style.resultPanel}>
            <Tabs defaultActiveKey="0" onChange={this.handleTabChang}>
              <TabPane tab="未开始" key="0"></TabPane>
              <TabPane tab="已开始" key="1"></TabPane>
            </Tabs>
            <div className={style.resultList}>{dataContent()}</div>
            <ListPagination
              total={count}
              defaultPageSize={10}
              pageIndex={pageIndex}
              pageSizeOptions={['10', '25', '50']}
              onChange={this.handleChangePage}
            />
          </div>
          <Modal
            centered
            title={'下载附件'}
            footer={null}
            visible={visable}
            onCancel={this.handleClose}
            maskClosable={true}
          >
            <div className={style.downloadPannel}>{downloadDataContent()}</div>
          </Modal>
        </div>
      </div>
    );
  }
}

function stateToProp(state) {
  return {
    CurUserSystem: state.commonModel.CurUserSystem,
    GradeList: state.commonModel.GradeList,
    CourseList: state.commonModel.CourseList,
    UserBaseInfo: state.commonModel.UserBaseInfo
  };
}

export default connect(stateToProp)(MyCourse);
