import React, { Component } from 'react';
import zhCN from 'antd/lib/locale-provider/zh_CN';
import classnames from 'classnames';
import { connect } from 'dva';
import { Link } from 'dva/router';
import { Modal, Table, Button, Select, DatePicker, Input, Divider, message, LocaleProvider } from 'antd';
import styles from './SmallLessonList.less';
import ListPagination from 'components/ListPagination';
import { roleEnum } from "../../../config/enum";

const Option = Select.Option;
const { RangePicker } = DatePicker;
const Search = Input.Search;
const confirm = Modal.confirm;

@connect(({ commonModel, classScheduleModel }) => ({
  ...classScheduleModel,
  GradeList: commonModel.GradeList,
  CourseList: commonModel.CourseList,
  CourseTypesList: commonModel.CourseTypesList
}))
class SmallLessonList extends Component {
  constructor(props) {
    super(props);
    this.state = {
      loading: true,
      GradeId: 0,
      StatusFlag: 0,
      WeiKeList: [], //微课列表
      Keyword: '', //关键字
      PageIndex: 1,
      PageSize: 25,
      Total: null, // 消息总条数
      Modalvisible: false, //录制视频弹窗
      videoId: null,
      OriginSubjectList: [],
      SubjectList: [],
      dataList: [],
      CourseList: props.CourseList,
      CourseId: undefined
    };
  }
  componentWillMount() {
    this.getGetWeiKeStatusList(); // 获取状态类型
  }

  //获取设备列表数据
  GetDevicesist = () => {
    const { dispatch } = this.props;
    dispatch({
      type: 'smallnModel/GetDeviceList',
      payload: {}
    }).then(result => {
      this.setState({
        dataList: result.DeviceList
      });
    });
  };

  // 获取状态类型
  getGetWeiKeStatusList = () => {
    const self = this;
    const { dispatch } = this.props;
    dispatch({
      type: 'commonModel/GetWeiKeStatusList'
    }).then(result => {
      self.setState(
        {
          OriginSubjectList: result,
          SubjectList: result.filter(item => {
            return item.Id != 0 && item.Id != 1;
          })
        },
        () => {
          self.GetWeiKeList(); //在线微课列表
        }
      );
    });
  };

  //在线微课列表
  GetWeiKeList = () => {
    const { dispatch } = this.props;
    const { StatusFlag, GradeId, CourseId, Keyword, PageIndex, PageSize, Total } = this.state;

    dispatch({
      type: 'smallnModel/GetWeiKeList',
      payload: {
        StatusFlag: StatusFlag,
        GradeId: GradeId,
        CourseId: CourseId,
        Keyword: Keyword,
        PageIndex: PageIndex,
        PageSize: PageSize,
        UserRole: roleEnum.admin
      }
    }).then(result => {
      if (result) {
        this.setState({
          WeiKeList: result.WeiKeList,
          Total: result.TotalCount,
          loading: false
        });
      }
    });
  };

  //上架、下架、删除操作
  onUpdateWeiKeStatus = (id, type, alertMessage) => {
    const { dispatch } = this.props;

    dispatch({
      type: 'smallnModel/UpdateWeiKeStatus',
      payload: {
        UpdateIds: [id],
        StatusFlag: type
      }
    }).then(result => {
      if (result == 1) {
        message.success(alertMessage + '成功');
        if (type != 0) {
          this.setState(
            {
              loading: true
            },
            () => {
              this.GetWeiKeList(); //课程列表
            }
          );
        } else {
          this.setState(
            {
              loading: true,
              PageIndex: 1
            },
            () => {
              this.GetWeiKeList(); //课程列表
            }
          );
        }
      } else {
        message.error(x + '失败');
      }
    });
  };

  handleGradeChange = value => {
    let tempCourseList = [],
      CourseId = this.state.CourseId;
    if (value == 0) {
      tempCourseList = this.props.CourseList;
    } else {
      let CourseTypeId = this.props.GradeList.find(item => {
        return item.Id == value;
      }).CourseTypeId;
      tempCourseList = this.props.CourseList.filter(item => {
        return item.CourseTypeId == CourseTypeId;
      });
      CourseId = 0;
    }
    this.setState(
      {
        GradeId: value,
        CourseList: tempCourseList,
        CourseId: CourseId,
        loading: true,
        PageIndex: 1
      },
      () => {
        this.GetWeiKeList(); //课程列表
      }
    );
  };

  handleCourseChange = value => {
    this.setState(
      {
        CourseId: value,
        loading: true,
        PageIndex: 1
      },
      () => {
        this.GetWeiKeList(); //课程列表
      }
    );
  };

  handleStatusChange = value => {
    this.setState(
      {
        StatusFlag: value,
        loading: true,
        PageIndex: 1
      },
      () => {
        this.GetWeiKeList(); //课程列表
      }
    );
  };

  //翻页
  onShowSizeChange = current => {
    this.setState(
      {
        PageIndex: current.PageIndex,
        PageSize: current.PageSize,
        loading: true
      },
      () => {
        this.GetWeiKeList();
      }
    );
  };

  //搜索
  onSearch = value => {
    this.setState(
      {
        Keyword: value,
        loading: true,
        PageIndex: 1
      },
      () => {
        this.GetWeiKeList(); //课程列表
      }
    );
  };

  //课程状态
  transforStatus = id => {
    return this.state.OriginSubjectList.find(item => {
      return item.Id == id;
    }).Name;
  };

  render() {
    const {
      loading,
      SubjectList,
      WeiKeList,
      PageIndex,
      Total,
      Modalvisible,
      videoId,
      dataList,
      CourseList,
      CourseId
    } = this.state;
    const { GradeList } = this.props;
    const columns = [
      { title: '微课名称', dataIndex: 'name' },
      { title: '学科学段', dataIndex: 'subject' },
      { title: '年级', dataIndex: 'grade' },
      { title: '章节', dataIndex: 'chapter', render: SectionName => (SectionName ? SectionName : '--') },
      { title: '老师', dataIndex: 'createUserName' },
      { title: '课程状态', dataIndex: 'type', render: StatusFlag => this.transforStatus(StatusFlag) },
      { title: '创建时间', dataIndex: 'time' },
      {
        title: '操作',
        dataIndex: 'tags',
        render: i => (
          <span>
            <Link
              to={{
                pathname: `/AdminOnline/SmallLessonList/${i.WeiKeId}/Info`
              }}
            >
              查看
            </Link>

            {i.StatusFlag == 2 && (
              <span>
                <Divider type="vertical" />
                <a onClick={() => this.onUpdateWeiKeStatus(i.WeiKeId, 3, '通过')}>通过</a>
                <Divider type="vertical" />
                <a className={styles.red} onClick={() => this.onUpdateWeiKeStatus(i.WeiKeId, 4, '拒绝')}>
                  拒绝
                </a>
              </span>
            )}
            {i.StatusFlag == 3 && (
              <span>
                <Divider type="vertical" />
                <a className={styles.red} onClick={() => this.onUpdateWeiKeStatus(i.WeiKeId, 1, '下架')}>
                  下架
                </a>
              </span>
            )}
            {i.StatusFlag == 4 && (
              <span>
                <Divider type="vertical" />
                <a onClick={() => this.onUpdateWeiKeStatus(i.WeiKeId, 3, '通过')}>通过</a>
              </span>
            )}
          </span>
        )
      }
    ];
    const data = [];
    WeiKeList &&
      WeiKeList.map(i => {
        data.push({
          key: i.WeiKeId,
          name: i.WeiKeName,
          subject: i.CourseName,
          grade: i.GradeName,
          chapter: i.SectionName,
          createUserName: i.CreateUserName,
          type: i.StatusFlag,
          time: i.CreateDateTime,
          tags: i
        });
      });
    return (
      <div className={styles.SmallLessonList}>
        <div className={styles.title}>在线微课管理</div>
        <div className={styles.tabheader}>
          <Select
            className={styles.width180}
            defaultValue="请选择所属年级"
            onChange={val => this.handleGradeChange(val)}
          >
            <Option value={0} key={'GradeList' + 0}>
              全部
            </Option>
            {GradeList.map(a => {
              return (
                <Option value={a.Id} key={'GradeList' + a.Id}>
                  {a.Name}
                </Option>
              );
            })}
          </Select>
          <Select
            className={styles.width180}
            value={CourseId != undefined ? CourseId : '请选择学段学科'}
            onChange={val => this.handleCourseChange(val)}
          >
            <Option value={0} key={'CourseList' + 0}>
              全部
            </Option>
            {CourseList.map(a => {
              return (
                <Option value={a.Id} key={'CourseList' + a.Id}>
                  {a.LongName}
                </Option>
              );
            })}
          </Select>
          <Select
            className={styles.width180}
            defaultValue="请选择课程状态"
            onChange={val => this.handleStatusChange(val)}
          >
            <Option value={0} key={'SubjectList' + 0}>
              全部
            </Option>
            {SubjectList.map(a => {
              return (
                <Option value={a.Id} key={'SubjectList' + a.Id}>
                  {a.Name}
                </Option>
              );
            })}
          </Select>
          <Search
            className={classnames(styles.left14, 'fr')}
            style={{ width: 300 }}
            placeholder="请输入微课名称或老师名称"
            enterButton="搜索"
            onSearch={value => this.onSearch(value)}
          />
        </div>
        <Table
          className={styles.tableborder}
          columns={columns}
          dataSource={data}
          pagination={false}
          loading={loading}
        />
        {WeiKeList.length > 0 ? (
          <ListPagination total={Total} pageIndex={PageIndex} onChange={this.onShowSizeChange} />
        ) : (
          ''
        )}
      </div>
    );
  }
}

export default connect()(SmallLessonList);
