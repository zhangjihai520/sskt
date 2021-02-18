import React, { Component, Fragment } from 'react';
import { DatePicker, Button, Row, Col, Table } from 'antd';
import styles from './index.less';
import { connect } from 'dva';
import classnames from 'classnames';
import moment from 'moment';

@connect(({}) => ({}))
class DemandCourseStatistics extends Component {
  constructor(props) {
    super(props);
    this.state = {
      queryDate: moment(),
      data: {},
      loading: false
    };
  }

  componentDidMount() {
    this.queryDate();
  }

  queryDate = () => {
    const { queryDate } = this.state;
    const { dispatch } = this.props;

    this.setState({
      loading: true
    });

    dispatch({
      type: 'classStatisticsModel/GetPublicBenefitStatistics',
      payload: {
        CurrentDate: queryDate.format('YYYY/MM/DD')
      }
    }).then(result => {
      const resWithKey = result.VideoPlayInfoList.map(item => {
        return {
          ...item,
          key: item.SubjectId
        }
      });
      result.VideoPlayInfoList = resWithKey;
      this.setState({
        data: result,
        loading: false
      });
    });
  };

  handleDateChange = date => {
    this.setState({
      queryDate: date
    })
  };

  render() {
    const { queryDate, data, loading } = this.state;

    const columns = [
      {
        title: '课程名称',
        dataIndex: 'SubjectName',
        key: 'SubjectName'
      },
      {
        title: '课程时间',
        dataIndex: 'BeginTime',
        key: 'BeginTime'
      },
      {
        title: '观看次数',
        dataIndex: 'PlayCount',
        key: 'PlayCount'
      }
    ];

    return (
      <div>
        <div className={styles.queryWrap}>
          <DatePicker onChange={this.handleDateChange} value={queryDate}/>
          <Button type="primary" className={styles.btn} onClick={this.queryDate}>查询</Button>
        </div>
        <Row className={styles.peopleNumWrap}>
          <Col className={classnames(styles.item, styles.blue, styles.first)} span={4}>
            <div className={styles.desc}>
              <div className={styles.descTitle}>上课总人数</div>
              <div className={styles.descContent}></div>
            </div>
            <div className={styles.rightNum}>{data.Headcount}</div>
          </Col>
          <Col className={classnames(styles.item, styles.red)} span={4}>
            <div className={styles.desc}>
              <div className={styles.descTitle}>初中人数</div>
              <div className={styles.descContent}></div>
            </div>
            <div className={styles.rightNum}>{data.JuniorHeadcount}</div>
          </Col>
          <Col className={classnames(styles.item, styles.green)} span={4}>
            <div className={styles.desc}>
              <div className={styles.descTitle}>高中人数</div>
              <div className={styles.descContent}></div>
            </div>
            <div className={styles.rightNum}>{data.HighHeadcount}</div>
          </Col>
        </Row>
        <Table dataSource={data.VideoPlayInfoList} columns={columns} pagination={false} loading={loading} />
      </div>
    );
  }
}
export default DemandCourseStatistics;
