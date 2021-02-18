import React, { PureComponent, Fragment } from 'react';
import { Card, Row, Col } from 'antd';
import styles from './HotClass.less';
import StandardTable from 'components/StandardTable';
import classnames from 'classnames';

class HotClass extends PureComponent {
  /**
   * 翻页事件处理
   */
  handleStandardTableChange = pagination => {
    const { dispatch, typeEnum, UserRole } = this.props;
    const params = {
      TypeEnum: typeEnum,
      PageIndex: pagination.current,
      PageSize: pagination.pageSize,
      UserRole: UserRole
    };

    dispatch({
      type: 'classStatisticsModel/updatePageSize',
      payload: params
    });
  };

  render() {
    const { columns, title, hotData, CurUserSystem, typeEnum } = this.props;
    const { data, PageSize, PageIndex } = hotData;

    if (Object.keys(data).length == 0 || data == undefined) return false;

    const Info = ({ title, value, bordered }) => (
      <div className={styles.headerInfo}>
        <span>{title}</span>
        <p>{value}</p>
        {bordered && <em />}
      </div>
    );
    const GetOfflineTitle = () => {
      if (typeEnum == 3 && CurUserSystem == 2) {
        return (
          <Row className={styles.lessonnum}>
            <Col className={classnames(styles.item, styles.blue, styles.first)} span={4}>
              <div className={styles.desc}>
                <div className={styles.descTitle}>上课人数</div>
                <div className={styles.descContent}></div>
              </div>
              <div className={styles.rightNum}>{data.ClassAttendanceTotal}</div>
            </Col>
          </Row>
        );
      } else {
        return (
          <Row className={styles.lessonnum}>
            <Col className={classnames(styles.item, styles.blue, styles.first)} span={4}>
              <div className={styles.desc}>
                <div className={styles.descTitle}>上课人数</div>
                <div className={styles.descContent}></div>
              </div>
              <div className={styles.rightNum}>{data.ClassAttendanceTotal}</div>
            </Col>
            {CurUserSystem == 2 ? (
              <Col className={classnames(styles.item, styles.green)} span={4}>
                <div className={styles.desc}>
                  <div className={styles.descTitle}>好评度</div>
                  <div className={styles.descContent}></div>
                </div>
                <div className={styles.rightNum}>{data.AveragePraise}</div>
              </Col>
            ) : (
              <Col className={classnames(styles.item, styles.red)} span={4}>
                <div className={styles.desc}>
                  <div className={styles.descTitle}>出勤率</div>
                  <div className={styles.descContent}></div>
                </div>
                <div className={styles.rightNum}>{`${data.AverageAttendanceRate}%`}</div>
              </Col>
            )}
          </Row>
        );
      }
    };
    const dataGroup = {
      list: data.Details,
      pagination: {
        total: data.Count,
        pageSize: PageSize,
        current: PageIndex,
        pageSizeOptions: ['10', '25', '50']
      }
    };

    return (
      <Fragment>
        <div bordered={false}>
          {CurUserSystem == 2 || typeEnum == 3 ? (
            <GetOfflineTitle />
          ) : (
            <Row className={styles.lessonnum}>
              <Col className={classnames(styles.item, styles.blue, styles.first)} span={4}>
                <div className={styles.desc}>
                  <div className={styles.descTitle}>上课人数</div>
                  <div className={styles.descContent}></div>
                </div>
                <div className={styles.rightNum}>{data.ClassAttendanceTotal}</div>
              </Col>
              <Col className={classnames(styles.item, styles.red)} span={4}>
                <div className={styles.desc}>
                  <div className={styles.descTitle}>出勤率</div>
                  <div className={styles.descContent}></div>
                </div>
                <div className={styles.rightNum}>{`${data.AverageAttendanceRate}%`}</div>
              </Col>
              <Col className={classnames(styles.item, styles.green)} span={4}>
                <div className={styles.desc}>
                  <div className={styles.descTitle}>好评度</div>
                  <div className={styles.descContent}></div>
                </div>
                <div className={styles.rightNum}>{data.AveragePraise}</div>
              </Col>
            </Row>
          )}
        </div>
        <div className={styles.hotBox}>
          {/* 表格及翻页 */}
          <StandardTable
            columns={columns}
            data={dataGroup}
            isRowSelection={false}
            onChange={this.handleStandardTableChange}
          />
        </div>
      </Fragment>
    );
  }
}

export default HotClass;
