import React, { Component } from 'react';
import { connect } from 'dva';
import { Modal, Rate, Input, Row, Col, Pagination, message, Spin } from 'antd';
import styles from './index.less';
import EvaluationList from './evaluationListToMe.js';
const { TextArea } = Input;

@connect()
export default class EvaluationManagement extends Component {
  state = {
    pageIndex: 1,
    pageSize: 10,
    currentTag: 'default' //默认 default，对我的评价 evaluateMe
  };
  componentDidMount() {
    this.queryData();
  }
  //查询列表方法
  queryData = () => {
    const { pageIndex, pageSize, currentTag } = this.state;
    const { dispatch } = this.props;
    this.setState(
      {
        listLoading: true
      },
      () => {
        dispatch({
          type: 'curriculumModel/GetTeacherToStudentEvaluateList',
          payload: {
            pageIndex,
            pageSize
          }
        }).then(res => {
          this.setState({
            evaluationList: res.EvaluateList,
            totalCount: res.TotalCount,
            listLoading: false
          });
        });
      }
    );
  };
  //打开评价弹窗
  evaluate = (subjectId, subjectRoomId, teacherId) => {
    this.setState({
      visible: true,
      subjectId,
      subjectRoomId,
      teacherId
    });
  };
  //设置评星
  starRating = value => {
    this.setState({
      starNum: value
    });
  };
  //发布评价
  release = () => {
    const { evaluateComment, starNum, subjectId, subjectRoomId, teacherId } = this.state;
    const { dispatch } = this.props;
    let mark = true;
    if (!starNum) {
      message.warning('请选择评星');
      mark = false;
      return false;
    }
    if (!evaluateComment) {
      message.warning('请输入备注');
      mark = false;
      return false;
    }
    if (mark) {
      dispatch({
        type: 'curriculumModel/EvaluateSubject',
        payload: {
          EvaluateLevel: starNum,
          EvaluateComment: evaluateComment,
          SubjectId: subjectId,
          SubjectRoomId: subjectRoomId,
          TeacherId: teacherId
        }
      }).then(res => {
        if (res === 1) {
          message.success('发布成功');
          this.setState(
            {
              visible: false,
              starNum: 0,
              evaluateComment: ''
            },
            () => {
              this.queryData();
            }
          );
        } else {
          message.warning('发布失败');
        }
      });
    }
  };
  //切换tag
  switchTag = key => {
    this.setState(
      {
        currentTag: key,
        pageIndex: 1,
        pageSize: 10,
        evaluationList: []
      },
      () => {
        this.queryData();
      }
    );
  };
  render() {
    const {
      visible = false,
      starNum = 0,
      evaluationList = [],
      totalCount = 0,
      pageIndex,
      pageSize,
      currentTag = 'default',
      evaluateComment = '',
      listLoading = false
    } = this.state;
    const starArr = ['一星', '二星', '三星', '四星', '五星'];
    return (
      <div>
        <div className={styles.bg}>
          <div className={styles.bgImg} />
          <div className={styles.bgContent} />
        </div>
        <div className={styles.root}>
          <div className={styles.rightBox}>
            <Spin tip="Loading..." spinning={listLoading}>
              <EvaluationList dataList={evaluationList} onClick={this.evaluate} type={currentTag} />
              <div className={styles.pagingBar}>
                <span>{`显示${pageSize * (pageIndex - 1) + 1}-${pageSize * pageIndex}，共 ${totalCount} 条记录`}</span>
                <Pagination
                  showSizeChanger
                  showQuickJumper
                  onShowSizeChange={(current, size) =>
                    this.setState({ pageIndex: current, pageSize: size }, () => this.queryData())
                  }
                  onChange={pageNumber => this.setState({ pageIndex: pageNumber }, () => this.queryData())}
                  current={pageIndex}
                  total={totalCount}
                  pageSize={pageSize}
                />
              </div>
            </Spin>
          </div>

          <Modal
            title="评价"
            visible={visible}
            onOk={this.release}
            onCancel={() => this.setState({ visible: false })}
            okText="发布"
          >
            <Row>
              <Col span={24}>
                <span>
                  <Rate onChange={value => this.setState({ starNum: value })} value={starNum} />
                  {starNum ? <span>{starArr[starNum - 1]}</span> : ''}
                </span>
              </Col>
            </Row>
            <Row className="mt-l">
              <Col span={24}>
                <TextArea
                  rows={4}
                  value={evaluateComment}
                  placeholder="请输入备注"
                  onChange={e => this.setState({ evaluateComment: e.target.value })}
                />
              </Col>
            </Row>
          </Modal>
        </div>
      </div>
    );
  }
}
