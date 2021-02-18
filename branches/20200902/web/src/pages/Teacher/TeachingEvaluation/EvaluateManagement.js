import React, { Component } from 'react';
import { connect } from 'dva';
import { Radio, List, Avatar, Rate } from 'antd';
import classNames from 'classnames/bind';
import classnames from 'classnames';
import styles from './EvaluateManagement.less';

/**
 * 教学评估-评价管理
 */
class EvaluateManagement extends Component {
  state = {
    // 左面课程加载效果
    courseLoading: true,
    /**
     * 左侧课程列表
     */
    curriculumData: [],
    leftMaxHeight: 872, // 左侧最大高度

    /**
     * 评价请求相关
     */
    loading: true,
    currentSubjectId: '', // 当前课程
    currentEvaluateTypeId: 0, // 当前角色
    currentPageSize: 25, // 当前每页显示条数
    currentPageIndex: 1, // 当前分页
    /**
     * 课程对应评价数据
     */
    totalCount: 0,
    evaluateList: []
  };

  componentDidMount() {
    this.loadData();
  }

  /**
   * 初始数据加载
   */
  loadData() {
    this.getAllCourses();
  }

  /**
   * 获取所有课程
   */
  getAllCourses = () => {
    const { dispatch } = this.props;
    const params = {
      // 一次性加载全部课程，此分页参数无用
      PageIndex: 1,
      PageSize: 1000
    };
    this.setState(
      {
        courseLoading: true
      },
      () => {
        dispatch({
          type: 'teachModel/getAllSubjectEvaluate',
          payload: params
        }).then(res => {
          if (res.SubjectEvaluateList && res.SubjectEvaluateList.length > 0) {
            const SubjectId = res.SubjectEvaluateList[0].SubjectId;
            this.setState({
              currentSubjectId: SubjectId,
              curriculumData: this.state.curriculumData.concat(res.SubjectEvaluateList),
              courseLoading: false
            });
            this.getSubjectEvaluate(SubjectId, 0, 1, 10); // 0 全部评论  1是第一页
          } else {
            // 没有课程时， 课程列表和评价列表都设置为空
            this.setState({
              curriculumData: [],
              evaluateList: [],
              courseLoading: false,
              loading: false
            });
          }
        });
      }
    );
  };

  /**
   * 获取课程评价
   */
  getSubjectEvaluate = (SubjectId, EvaluateTypeId, PageIndex, PageSize) => {
    const { dispatch } = this.props;
    this.setState(
      {
        loading: true,
        currentSubjectId: SubjectId,
        currentEvaluateTypeId: EvaluateTypeId,
        currentPageSize: PageSize,
        currentPageIndex: PageIndex
      },
      () => {
        dispatch({
          type: 'teachModel/getSubjectEvaluate',
          payload: {
            SubjectId,
            EvaluateTypeId,
            PageIndex,
            PageSize
          }
        }).then(res => {
          // 73为每一行消息的高度，142其余部部分高度
          let maxHeight = res.EvaluateList.length * 73 + 142;
          if (res.PageCount >= PageSize) {
            this.setState({
              leftMaxHeight: maxHeight
            });
          }

          this.setState({
            totalCount: res.PageCount,
            evaluateList: res.EvaluateList,
            loading: false
          });
        });
      }
    );
  };

  /** 每页数据发生变化 */
  onShowSizeChange = (current, pageSize) => {
    const { currentSubjectId, currentEvaluateTypeId } = this.state;
    this.setState(
      {
        loading: true,
        currentPageSize: pageSize,
        currentPageIndex: current
      },
      () => {
        this.getSubjectEvaluate(currentSubjectId, currentEvaluateTypeId, current, pageSize);
      }
    );
  };

  /**
   * 切换课程
   */
  onChangeCourseHandler(SubjectId) {
    this.setState({
      currentSubjectId: SubjectId
    });
    this.getSubjectEvaluate(SubjectId, 0, 1, 10);
  }

  /**
   * 切换角色
   */
  onChangeRole(ev) {
    const EvaluateTypeId = parseInt(ev.target.value);
    const { currentSubjectId } = this.state;

    if (currentSubjectId && currentSubjectId.length > 0) {
      this.setState({
        currentEvaluateTypeId: EvaluateTypeId
      });
      this.getSubjectEvaluate(currentSubjectId, EvaluateTypeId, 1, 10);
    }
  }
  /**
   * 左侧课程列表
   */
  courseRender() {
    const { curriculumData, currentSubjectId, leftMaxHeight, courseLoading } = this.state;
    let cx = classNames.bind(styles);
    return (
      <div className={styles.leftAside}>
        <div className={styles.leftName}>全部课程</div>
        <List
          dataSource={curriculumData}
          renderItem={item => {
            return (
              <div
                className={cx('item', {
                  cur: currentSubjectId === item.SubjectId
                })}
                onClick={() => this.onChangeCourseHandler(item.SubjectId)}
              >
                <div className={styles.leftDesc}>
                  <p>{item.SubjectName}</p>
                  <span className={styles.time}>{item.BeginTime}</span>
                </div>
                <span className={styles.number}>{item.EvaluateCount}条</span>
              </div>
            );
          }}
          loading={courseLoading}
          split={true}
          className={styles.curriculum}
          style={{ maxHeight: leftMaxHeight }}
        />
      </div>
    );
  }

  render() {
    const { evaluateList, totalCount, currentPageSize, currentPageIndex, currentEvaluateTypeId, loading } = this.state;
    const { UserBaseInfo } = this.props;

    return (
      <div>
        <div className={styles.title}>
          <div className={styles.user}>
            <img
              alt={UserBaseInfo && UserBaseInfo.UserTrueName}
              src={
                UserBaseInfo && UserBaseInfo.UserFace ? UserBaseInfo.UserFace : '/images/common/img_user_default.png'
              }
            />
            <span className={styles.userName}>{UserBaseInfo && UserBaseInfo.UserTrueName}</span>
          </div>
        </div>
        <div className={styles.content}>
          {this.courseRender()}
          <div className={classnames(styles.evaluate, 'bg-fff')}>
            <div className={styles.tool}>
              <span className={styles.desc}>全部评价</span>
              <Radio.Group
                defaultValue={0}
                value={currentEvaluateTypeId}
                buttonStyle="solid"
                className={styles.sel}
                onChange={ev => this.onChangeRole(ev)}
              >
                <Radio.Button value={0}>全部</Radio.Button>
                <Radio.Button value={2}>老师</Radio.Button>
                <Radio.Button value={1}>学生</Radio.Button>
                {/* <Radio.Button value={3}>家长</Radio.Button> */}
              </Radio.Group>
            </div>
            <List
              dataSource={evaluateList}
              renderItem={item => (
                <List.Item>
                  <List.Item.Meta
                    avatar={
                      <Avatar
                        shape="square"
                        size={48}
                        icon="user"
                        src="https://zos.alipayobjects.com/rmsportal/ODTLcjxAfvqbxHnVXCYX.png"
                      />
                    }
                    title={<span className={styles.userName}>{item.UserTrueName}</span>}
                    description={<span className={styles.userComment}>{item.EvaluateComment}</span>}
                  />
                  <Rate disabled value={item.EvaluateLevel} />
                </List.Item>
              )}
              loading={loading}
              className={styles.evaluateList}
              pagination={{
                total: totalCount,
                current: currentPageIndex,
                pageSize: currentPageSize,
                defaultCurrent: 1,
                showSizeChanger: true,
                showQuickJumper: true,
                hideOnSinglePage: true,
                onChange: this.onShowSizeChange,
                onShowSizeChange: this.onShowSizeChange
              }}
            />
          </div>
        </div>
      </div>
    );
  }
}

function stateToProp(state) {
  return {
    CurUserSystem: state.commonModel.CurUserSystem,
    UserBaseInfo: state.commonModel.UserBaseInfo,
    TeaBasePath: state.commonModel.TeaBasePath
  };
}

export default connect(stateToProp)(EvaluateManagement);
