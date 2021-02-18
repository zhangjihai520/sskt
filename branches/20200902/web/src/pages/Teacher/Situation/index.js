import React, { Component, Fragment } from 'react';
import { Card, Row, Col, Radio } from 'antd';
import styles from '../../Admin/ClassStatistics/ClassStatistics.less';
import HotClass from '../../Admin/ClassStatistics/HotClass';
import { connect } from 'dva';
import ColumnDiagram from '../../Admin/ClassStatistics/ColumnDiagram';
import Pie from '../../Admin/ClassStatistics/Pie';
import NullData from '../../../components/NullData';
import { roleEnum } from '../../../config/enum';
import classnames from 'classnames';

const params = {
  UserRole: roleEnum.teacher
};

@connect(({ classStatisticsModel, commonModel }) => ({
  ...classStatisticsModel,
  CurUserSystem: commonModel.CurUserSystem
}))
class Monitoring extends Component {
  constructor(props) {
    super(props);

    this.state = {
      tabKey: 'ClassAttendance'
    };
  }
  componentDidMount() {
    const { Days } = this.props;

    this.props.dispatch({
      type: 'classStatisticsModel/fetchChartNumber',
      payload: { Days: Days, ...params }
    });

    this.props.dispatch({
      type: 'classStatisticsModel/fetch',
      payload: { TypeEnum: 1, ...params }
    });

    this.props.dispatch({
      type: 'classStatisticsModel/fetchShare',
      payload: { TypeEnum: 1, PageSize: 6 }
    });

    this.props.dispatch({
      type: 'classStatisticsModel/fetchShare',
      payload: { TypeEnum: 2, PageSize: 6 }
    });
  }
  handleDataChange = e => {
    this.props.dispatch({
      type: 'classStatisticsModel/fetchChartNumber',
      payload: { Days: e.target.value, ...params }
    });
  };
  onTabChange = key => {
    this.setState({ tabKey: key });
  };
  render() {
    const { SubjectList, HotData1, PieData1, PieData2, Days, CurUserSystem } = this.props;
    const { tabKey } = this.state;
    let columns;
    if (CurUserSystem == 1) {
      columns = [
        {
          title: '课程名称',
          dataIndex: 'Name',
          key: 'Name',
          width: 500
        },
        {
          title: '上课人数',
          dataIndex: 'ClassAttendance',
          key: 'ClassAttendance',
          align: 'center'
        },
        {
          title: '出勤率',
          dataIndex: 'AttendanceRate',
          key: 'AttendanceRate',
          align: 'center',
          render: (text, list) => text + '%'
        },
        {
          title: '好评度',
          dataIndex: 'Praise',
          key: 'Praise',
          align: 'center'
        }
      ];
    } else {
      columns = [
        {
          title: '课程名称',
          dataIndex: 'Name',
          key: 'Name',
          width: 500
        },
        {
          title: '上课人数',
          dataIndex: 'ClassAttendance',
          key: 'ClassAttendance',
          align: 'center'
        },
        {
          title: '好评度',
          dataIndex: 'Praise',
          key: 'Praise',
          align: 'center'
        }
      ];
    }

    let tabListNoTitle = [
      {
        key: 'ClassAttendance',
        tab: '上课人数'
      },
      {
        key: 'AttendanceRate',
        tab: '出勤率'
      }
    ];
    if (CurUserSystem == 2) {
      tabListNoTitle = [
        {
          key: 'ClassAttendance',
          tab: '上课人数'
        }
      ];
    }

    return (
      <div className={classnames(styles.chartBg, styles.Statistics)}>
        <Card
          style={{ width: '100%' }}
          tabList={tabListNoTitle}
          type="inner"
          activeTabKey={tabKey}
          extra={
            <div className={styles.salesExtra}>
              <Radio.Group onChange={this.handleDataChange} value={Days} buttonStyle="solid">
                <Radio.Button value="7">近7天</Radio.Button>
                <Radio.Button value="30">近30天</Radio.Button>
              </Radio.Group>
            </div>
          }
          onTabChange={this.onTabChange}
        >
          {SubjectList.length > 0 ? (
            <ColumnDiagram
              data={SubjectList}
              title={tabKey == 'AttendanceRate' ? '出勤率' : '上课人数趋势'}
              x={'BeginTime'}
              y={tabKey}
              {...this.props}
            />
          ) : (
            <NullData isShow={true} message={'暂无数据'} />
          )}
        </Card>

        {/* 热门课程 */}
        <HotClass columns={columns} hotData={HotData1} typeEnum={1} {...this.props} />

        <div className="u-dividerLine"></div>
        {/* pie 图 */}
        <Row gutter={12} className={styles.pieBox}>
          <Col span={6}>
            <Card className={styles.numberCard} bordered={false} title={'学生学校占比'}>
              {PieData1 && PieData1.PieData.length > 0 ? (
                <Pie data={PieData1.PieData} subTitle="人数" total={PieData1.ValueCount} />
              ) : (
                <NullData isShow={true} message={'暂无数据'} />
              )}
            </Card>
          </Col>
          <Col span={6}>
            <Card className={styles.numberCard} bordered={false} title={'学生年级占比'}>
              {PieData2 && PieData2.PieData.length > 0 ? (
                <Pie data={PieData2.PieData} subTitle="人数" total={PieData2.ValueCount} />
              ) : (
                <NullData isShow={true} message={'暂无数据'} />
              )}
            </Card>
          </Col>
        </Row>
      </div>
    );
  }
}
export default Monitoring;
