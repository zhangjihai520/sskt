import React, { Component } from 'react';
import zhCN from 'antd/lib/locale-provider/zh_CN';
import { connect } from 'dva';
import { Link } from 'dva/router';
import { Modal, Table, Button, Select, DatePicker, Input, Divider, message, LocaleProvider } from 'antd';
import styles from './index.less';
import ListPagination from 'components/ListPagination';
import Classbegins from '../../Curriculum/Classbegins';
import classnames from 'classnames';

const Option = Select.Option;
const { RangePicker } = DatePicker;
const Search = Input.Search;
const confirm = Modal.confirm;

@connect(({ commonModel, classScheduleModel }) => ({
  ...classScheduleModel,
  GradeList: commonModel.GradeList
}))
class SmallLessonList extends Component {
  constructor(props) {
    super(props);
    this.state = {
      selectedRowKeys: [], // 多选，用作批量删除
      loading: true,
      GradeId: 0,
      StatusFlag: 0,
      WeiKeList: [], //微课列表
      Keyword: '', //关键字
      BeginTime: '', //开始时间
      EndTime: '', //结束时间
      PageIndex: 1,
      PageSize: 25,
      Total: null, // 消息总条数
      Modalvisible: false, //录制视频弹窗
      videoId: null,
      OriginSubjectList: [],
      SubjectList: [],
      deviceId: '', //设备id，用来判断是否二次更换设备
      dataList: []
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
            return item.Id != 0;
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
    const { StatusFlag, GradeId, Keyword, BeginTime, EndTime, PageIndex, PageSize, Total } = this.state;

    dispatch({
      type: 'smallnModel/GetWeiKeList',
      payload: {
        StatusFlag: StatusFlag,
        GradeId: GradeId,
        CourseId: 0,
        BeginTime: BeginTime,
        EndTime: EndTime,
        Keyword: Keyword,
        PageIndex: PageIndex,
        PageSize: PageSize
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
  onUpdateWeiKeStatus = (id, type) => {
    const { selectedRowKeys } = this.state;
    const { dispatch } = this.props;

    var UpdateIds = [];
    if (id == 'all') {
      UpdateIds = selectedRowKeys;
    } else {
      UpdateIds.push(id);
    }
    if (UpdateIds.length < 1) {
      message.warning('请选择');
      return true;
    }
    var x = '';
    switch (type) {
      case 1:
        x = '下架';
        break;
      case 2:
        x = '上架';
        break;
      case 0:
        x = '删除';
        break;
    }

    dispatch({
      type: 'smallnModel/UpdateWeiKeStatus',
      payload: {
        UpdateIds: UpdateIds,
        StatusFlag: type
      }
    }).then(result => {
      if (result == 1) {
        message.success(x + '成功');
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

  handleChange = (value, type) => {
    if (type == 1) {
      this.setState(
        {
          GradeId: value,
          loading: true,
          PageIndex: 1
        },
        () => {
          this.GetWeiKeList(); //课程列表
        }
      );
    } else {
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
    }
  };

  //选择时间
  onDate(date, dateString) {
    this.setState(
      {
        BeginTime: dateString[0],
        EndTime: dateString[1],
        loading: true,
        PageIndex: 1
      },
      () => {
        this.GetWeiKeList(); //课程列表
      }
    );
  }

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
  onSearch(value) {
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
  }

  //选择
  onSelectChange = selectedRowKeys => {
    this.setState({ selectedRowKeys });
  };

  //课程状态
  transforStatus = id => {
    const name = this.state.OriginSubjectList.find(item => {
      return item.Id == id;
    }).Name;
    let styleContent = '';
    switch (id) {
      case 1:
      case 2:
      case 3:
        styleContent = styles.green;
        break;
      case 4:
        styleContent = styles.red;
        break;
    }
    return <span className={classnames(styles.stateItem, styleContent)}>{name}</span>;
  };

  //删除提醒
  showConfirm = id => {
    const sef = this;
    confirm({
      title: '删除后无法恢复，是否确认删除？',
      onOk() {
        sef.onUpdateWeiKeStatus(id, 0);
      },
      onCancel() {}
    });
  };
  //上架提醒
  upshowConfirm = (id, type) => {
    const sef = this;
    if (type == 1) {
      sef.onUpdateWeiKeStatus(id, 2);
    } else {
      Modal.error({
        title: '无法上架，请上传视频源再上架？',
        content: '该微课没有上传视频或者没有选择录制文件。',
        okText: '确定'
      });
    }
  };
  //录制视频弹窗
  showModal = (id, deviceid) => {
    this.GetDevicesist();
    this.setState({
      Modalvisible: true,
      videoId: id,
      deviceId: deviceid
    });
  };
  //录制视频回调方法
  close = (value, type) => {
    if (!value) {
      this.setState(
        {
          Modalvisible: false
        },
        () => {
          if (type == 1) {
            this.GetWeiKeList(); //课程列表
          }
        }
      );
    }
  };
  render() {
    const {
      loading,
      SubjectList,
      selectedRowKeys,
      WeiKeList,
      PageIndex,
      Total,
      Modalvisible,
      videoId,
      deviceId,
      dataList
    } = this.state;
    const { GradeList } = this.props;
    const rowSelection = {
      selectedRowKeys,
      onChange: this.onSelectChange
    };
    const columns = [
      {
        title: '微课名称',
        dataIndex: 'name',
        render: (name, record) => {
          return (
            <span>
              <img
                className={styles.img}
                alt="封面图片"
                src={record.pic ? record.pic : `/images/common/img_${record.shortCode}.jpg`}
              ></img>
              <span className={styles.name}>{name}</span>
            </span>
          );
        }
      },
      { title: '学科学段', dataIndex: 'subject' },
      { title: '年级', dataIndex: 'grade' },
      { title: '章节', dataIndex: 'chapter', render: SectionName => (SectionName ? SectionName : '--') },
      { title: '课程状态', dataIndex: 'type', render: StatusFlag => this.transforStatus(StatusFlag) },
      { title: '创建时间', dataIndex: 'time' },
      {
        title: '操作',
        dataIndex: 'tags',
        render: i => (
          <span>
            {(i.StatusFlag == 1 || i.StatusFlag == 4) && (
              <span>
                <Link
                  to={{
                    pathname: `/TeaOnline/SmallLesson/Edit`,
                    state: { WeiKeId: i.WeiKeId, StatusFlag: i.StatusFlag }
                  }}
                >
                  编辑
                </Link>
                <Divider type="vertical" />
                <a onClick={() => this.upshowConfirm(i.WeiKeId, i.CanActive)}>上架</a>
                <Divider type="vertical" />
                <a className={styles.red} onClick={() => this.showConfirm(i.WeiKeId)}>
                  删除
                </a>
              </span>
            )}
            {(i.StatusFlag == 2 || i.StatusFlag == 3) && (
              <span>
                <a onClick={() => this.onUpdateWeiKeStatus(i.WeiKeId, 1)}>下架</a>
              </span>
            )}
            {i.CanActive == 0 ? (
              <span>
                <Divider type="vertical" />
                <a onClick={() => this.showModal(i.WeiKeId, i.DeviceId)}>录制</a>
              </span>
            ) : (
              ''
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
          pic: i.ImagePath,
          name: i.WeiKeName,
          subject: i.CourseName,
          grade: i.GradeName,
          chapter: i.SectionName,
          type: i.StatusFlag,
          time: i.CreateDateTime,
          tags: i,
          shortCode: i.ShortCode
        });
      });
    return (
      <div className={styles.SmallLessonList}>
        <div className={styles.tabheader}>
          <Select
            className={styles.width180}
            defaultValue="请选择所属年级"
            onChange={val => this.handleChange(val, '1')}
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
            defaultValue="请选择课程状态"
            onChange={val => this.handleChange(val, '2')}
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
          创建时间：
          <RangePicker onChange={(date, dateString) => this.onDate(date, dateString)} />
          <Search
            className={styles.left14}
            style={{ width: 300 }}
            placeholder="请输入微课名称"
            enterButton="搜索"
            onSearch={value => this.onSearch(value)}
          />
          <Button className={styles.left14} onClick={() => (window.location.href = '/TeaOnline/SmallLesson/Add')}>
            创建微课
          </Button>
          {/* <Button className={styles.left14} onClick={() => this.showConfirm('all')}>批量删除</Button> */}
        </div>
        <div className={styles.dataWrap}>
          <Table
            className={styles.tableborder}
            rowSelection={rowSelection}
            columns={columns}
            dataSource={data}
            pagination={false}
            loading={loading}
          />
          {WeiKeList.length > 0 ? (
            <div className={styles.pag}>
              <ListPagination total={Total} pageIndex={PageIndex} onChange={this.onShowSizeChange} />
            </div>
          ) : (
            ''
          )}
        </div>
        <Classbegins
          visible={Modalvisible}
          isSubject={2}
          Id={videoId}
          hasDeviceId={deviceId}
          close={this.close}
          dataList={dataList}
        />
      </div>
    );
  }
}

export default connect()(SmallLessonList);
