import React, { Component } from 'react';
import { connect } from 'dva';
import { List, Avatar, Button, Modal, Pagination, Input, message, Rate, Row, Col, Spin } from 'antd';
import broadcastImg from 'assets/common/img_img_teacher_broadcast.png';
import styles from './index.less';
import classnames from 'classnames';
import NullData from '../../../../components/NullData';
const { TextArea } = Input;

@connect(({ teachModel }) => ({
  teachModel
}))
class EvaluateCourses extends Component {
  state = {
    reset: 0,
    loading: true,
    visible: false, // 弹框的显示状态
    total: null, // 消息总条数
    infoList: [], // 消息数据
    rateValue: 0, // 评分值
    textAreaVal: '',
    currentInfo: {
      SubId: '', //课程Id
      TeacherId: '' //教师Id
    }
  };

  componentDidMount() {
    this.getData(1, 10);
  }
  /**获取后台数据 */
  getData = (PageIndex, PageSize) => {
    const { dispatch } = this.props;
    dispatch({
      type: 'teachModel/getSubjectEvaluateList',
      payload: {
        PageIndex,
        PageSize
      }
    }).then(result => {
      this.setState({
        total: result.TotalCount,
        infoList: result.SubjectList,
        loading: false
      });
    });
  };
  /** 每页数据发生变化 */
  onShowSizeChange = (current, pageSize) => {
    this.setState(
      {
        loading: true
      },
      () => {
        this.getData(current, pageSize);
      }
    );
  };

  transforStatus = item => {
    let transforTitle = '',
      styleContent = '';
    switch (item.StatusFlag) {
      case 1:
        styleContent = styles.green;
        transforTitle = '待上架';
        break;
      case 2:
        styleContent = styles.green;
        transforTitle = '待报名';
        break;
      case 3:
        styleContent = styles.green;
        transforTitle = '报名中';
        break;
      case 4:
        styleContent = styles.red;
        transforTitle = '待上课';
        break;
      case 5:
        styleContent = styles.red;
        transforTitle = '已结课';
        break;
      case 6:
        styleContent = styles.red;
        transforTitle = '上课中';
        break;
    }
    return <div className={classnames(styles.status, styleContent)}>{transforTitle}</div>;
  };
  /**
   * SubId: 课程Id
   * TeacherId: 老师Id
   */
  modalShow = (SubId, TeacherId) => {
    let { currentInfo } = this.state;
    currentInfo.SubId = SubId;
    currentInfo.TeacherId = TeacherId;
    this.setState({
      reset: Math.random(),
      currentInfo,
      rateValue: 0,
      textAreaVal: '',
      visible: true
    });
  };

  /**修改评分等级 */
  handleRateChange = value => {
    this.setState({
      rateValue: value
    });
  };

  /**修改textarea的值 */
  textAreaChange = e => {
    this.setState({
      textAreaVal: e.target.value.trim()
    });
  };

  // 评价课程
  answerInfo = params => {
    const { dispatch } = this.props;

    const list = this.state.infoList.map(item => {
      if (item.SubjectId === params.SubjectId && item.TeacherId === params.TeacherId) {
        return { ...item, IsEvaluate: 1 };
      } else {
        return item;
      }
    });

    dispatch({
      type: 'teachModel/evaluateSubject',
      payload: params
    }).then(result => {
      this.setState({
        visible: false,
        infoList: list
      });
      message.success('消息评价成功');
    });
  };

  /**弹框点击确定 */
  handleOk = e => {
    const { textAreaVal, currentInfo, rateValue } = this.state;
    if (!rateValue) {
      message.info('你还没有评星');
      return;
    }
    if (!textAreaVal) {
      message.info('你还没有输入信息');
      return;
    }
    const params = {
      SubjectId: currentInfo.SubId,
      TeacherId: currentInfo.TeacherId,
      EvaluateLevel: rateValue,
      EvaluateComment: textAreaVal
    };
    this.answerInfo(params);
  };

  /**弹框点击取消 */
  handleCancel = e => {
    this.setState({
      visible: false
    });
  };

  render() {
    const { reset, loading, visible, total, rateValue, infoList } = this.state;
    const descText = ['一星', '二星', '三星', '四星', '五星'];
    return (
      <div className={styles.Infoset}>
        <Spin spinning={loading}>
          <div className={styles.dataContent}>
            {infoList.map(item => {
              return (
                <div className={styles.item}>
                  <div className={styles.time}>课程时间: {item.BeginTime}</div>
                  {this.transforStatus(item)}
                  <div className={styles.content}>
                    <img
                      className={styles.descImage}
                      src={item.Image ? item.Image : `/images/common/img_${item.ShortCode}.jpg`}
                    />
                    <div className={styles.descName}>{item.SubjectName}</div>
                    <div className={styles.classInfo}>
                      <span>{item.GradeName}</span>
                      <span className={styles.teacherName}>{item.TeacherName}</span>
                    </div>
                    <Button className={styles.btn} type="primary" disabled={item.IsEvaluate} onClick={() => this.modalShow(item.SubjectId, item.TeacherId)}>
                      {item.IsEvaluate ? '已评价' : '评价'}
                    </Button>
                  </div>
                </div>
              );
            })}
          </div>
        </Spin>
        {infoList.length > 0 ? (
          <Pagination
            className={styles.paginationStyle}
            showSizeChanger
            showQuickJumper
            showTotal={(total, range) => `共${total}条`}
            onChange={this.onShowSizeChange}
            onShowSizeChange={this.onShowSizeChange}
            defaultCurrent={1}
            total={total}
          />
        ) : (
          <NullData isShow={true} message={'暂无数据'} />
        )}
        <Modal title="评价" visible={visible} onOk={this.handleOk} onCancel={this.handleCancel}>
          <span>
            <Rate className={styles.rateClass} tooltips={descText} onChange={this.handleRateChange} value={rateValue} />
            {rateValue ? <span className={styles.rateText}>{descText[rateValue - 1]}</span> : ''}
          </span>
          <TextArea
            key={reset}
            autosize={{ minRows: 4, maxRows: 4 }}
            placeholder="请输入回复信息"
            onChange={this.textAreaChange}
          />
        </Modal>
      </div>
    );
  }
}

function stateToProp(state) {
  return {
    CurUserSystem: state.commonModel.CurUserSystem,
    TeaBasePath: state.commonModel.TeaBasePath
  };
}

export default connect(stateToProp)(EvaluateCourses);
