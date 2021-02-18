import React, { Component } from 'react';
import { connect } from 'dva';
import { Table, Button, Select, DatePicker, Input, Divider, Pagination, message, Tag } from 'antd';
import styles from './ClassScheduleList.less';
import broadcastImg from '../../../assets/common/img_img_teacher_broadcast.png';
import ListPagination from 'components/ListPagination';
import { checkCourse } from 'utils/utils';

const Option = Select.Option;
const { RangePicker } = DatePicker;
const Search = Input.Search;

@connect(({ commonModel, classScheduleModel }) => ({
  ...classScheduleModel,
  GradeList: commonModel.GradeList,
  SubjectStatusList: commonModel.SubjectStatusList,
  TeaBasePath: commonModel.TeaBasePath
}))
class ClassSeheduleList extends Component {
  constructor(props) {
    super(props);
    this.state = {
      selectedRowKeys: [], // 多选，用作批量删除
      ClassInfoList: [],
      loading: true,
      GradeId: 0,
      StatusFlag: 0,
      Keyword: '',
      PageIndex: 1,
      PageSize: 25,
      Total: null,
      SubjectList: []
    };
  }

  componentWillMount() {
    const { SubjectStatusList } = this.props;
    this.GetSubjectList(); //课程列表

    let SubjectList = [];
    SubjectStatusList.map(item => {
      if (item.Name == '报名中' || item.Name == '待上课' || item.Name == '已结课') {
        SubjectList.push(item);
      }
    });

    this.setState({ SubjectList });
  }

  //课程列表
  GetSubjectList = () => {
    const { dispatch } = this.props;
    const { GradeId, StatusFlag, PageIndex, PageSize, Keyword } = this.state;
    dispatch({
      type: 'classScheduleModel/GetSubjectList',
      payload: {
        StatusFlag: parseInt(StatusFlag), //课程状态枚举值,0为全部
        Key: Keyword, //关键字查询,老师名称 课程名称
        GradeId: GradeId, //年级枚举值
        CourseId: 0, //所属学科枚举值
        PageIndex: PageIndex,
        PageSize: PageSize,
        IsOnlySelf: 0, //是否仅获取本身课程数据，否则获取全部，0为全部，1为获取当前用户
        UserRole: 2 //1学生，2老师，3管理员，4家长
      }
    }).then(result => {
      this.setState({
        ClassInfoList: result.ClassInfoList,
        Total: parseInt(result.Count),
        loading: false
      });
    });
  };
  //导出
  ExportSubjectList = () => {
    const { dispatch } = this.props;
    const { selectedRowKeys } = this.state;

    if (selectedRowKeys.length == 0) {
      message.warning('请选择数据导出');
      return true;
    }
    dispatch({
      type: 'classScheduleModel/ExportSubjectList',
      payload: {
        SubjectIds: selectedRowKeys //课程状态枚举值
      }
    });
  };

  //下拉选择
  handleChange = (value, type) => {
    if (type == 1) {
      this.setState(
        {
          GradeId: value,
          loading: true,
          PageIndex: 1
        },
        () => {
          this.GetSubjectList();
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
          this.GetSubjectList();
        }
      );
    }
  };
  //关键字
  onChangeClassName = e => {
    this.setState({
      Keyword: e.target.value,
      PageIndex: 1
    });
  };
  //选择
  onSelectChange = selectedRowKeys => {
    this.setState({ selectedRowKeys });
  };
  //分页
  onPag = pageNumber => {
    this.setState(
      {
        PageIndex: parseInt(pageNumber),
        loading: true
      },
      () => {
        this.GetSubjectList(); //课程列表
      }
    );
  };
  onShowSizeChange = current => {
    this.setState(
      {
        PageIndex: parseInt(current.PageIndex),
        PageSize: parseInt(current.PageSize),
        loading: true
      },
      () => {
        this.GetSubjectList(); //课程列表
      }
    );
  };
  //列表状态展示
  showSubjectList = id => {
    const { SubjectStatusList } = this.props;
    let Name = '';
    SubjectStatusList.map(item => {
      if (parseInt(item.Id) == id) {
        Name = item.Name;
      }
    });
    return <span style={{ color: 'red' }}>{Name}</span>;
  };
  render() {
    const { ClassInfoList, SubjectList, Total, PageIndex, selectedRowKeys, loading } = this.state;
    const { GradeList, TeaBasePath } = this.props;

    const rowSelection = {
      selectedRowKeys,
      onChange: this.onSelectChange
    };

    const shortCodeFilter = shortCode => {
      if (checkCourse(shortCode)) {
        return 'other';
      } else {
        return shortCode;
      }
    };

    const columns = [
      {
        title: '课程名称',
        dataIndex: 'name',
        render: (name, record) => {
          return (
            <span>
              <img
                alt="封面图片"
                src={record.pic ? record.pic : `/images/common/img_${shortCodeFilter(record.shortCode)}.jpg`}
              ></img>
              <span className={styles.name}>{name}</span>
            </span>
          );
        },
        key: 'name'
      },
      { title: '年级', dataIndex: 'grade', key: 'grade' },
      { title: '学科', dataIndex: 'subject', key: 'subject' },
      {
        title: '教师名称',
        dataIndex: 'chapter',
        render: tags => (
          <span>
            {tags.map(tag => (
              <Tag color="#fff4dc" key={tag}>
                {tag}
              </Tag>
            ))}
          </span>
        ),
        key: 'chapter'
      },
      {
        title: '课程时间',
        dataIndex: 'tags',
        render: time => {
          return '课程时间/' + time;
        },
        key: 'tags'
      },
      { title: '课程状态', dataIndex: 'type', render: Id => this.showSubjectList(Id), key: 'typedesc' },
      {
        title: '操作',
        dataIndex: 'type',
        render: (id, data) => {
          if (id == 5 && !checkCourse(data.shortCode)) {
            return (
              <a
                className={styles.record}
                onClick={() => {
                  window.open(`${TeaBasePath}/ClassSchedule/RecordStream?SubjectId=${data.key}`, '_blank');
                }}
              >
                回放
              </a>
            );
          } else if (id == 6 && !checkCourse(data.shortCode)) {
            return (
              <a
                className={styles.record}
                onClick={() => {
                  window.open(`${TeaBasePath}/Curriculum/Listen?SubjectId=${data.key}`, '_blank');
                }}
              >
                听课
              </a>
            );
          }
        },
        dataIndex: 'type'
      }
    ];
    const data = [];
    ClassInfoList &&
      ClassInfoList.map(i => {
        data.push({
          key: i.SubjectId,
          pic: i.ImagePath,
          name: i.SubjectName,
          subject: i.GradeName,
          grade: i.CourseName,
          chapter: i.TeacherNameList,
          tags: i.BeginTime,
          type: i.StatusFlag,
          shortCode: i.ShortCode
        });
      });
    return (
      <div className={styles.ClassScheduleList}>
        <div className={styles.tabheader}>
          <Select className={styles.width180} defaultValue="请选择年级" onChange={val => this.handleChange(val, '1')}>
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
          <Select className={styles.width180} defaultValue="请选择状态" onChange={val => this.handleChange(val, '2')}>
            <Option value={0} key={'SubjectList' + 0}>
              全部
            </Option>
            {SubjectList &&
              SubjectList.map(a => {
                return (
                  <Option value={a.Id} key={'SubjectList' + a.Id}>
                    {a.Name}
                  </Option>
                );
              })}
          </Select>
          <Input style={{ width: 300 }} placeholder="请输入课程名称或教师名称" onChange={this.onChangeClassName} />
          <Button type="primary" className={styles.left16} onClick={this.GetSubjectList}>
            查询
          </Button>
          <Button className={styles.left14} onClick={() => this.ExportSubjectList()}>
            导出
          </Button>
        </div>
        <div className={styles.footContent}>
          <Table
            className={styles.tableborder}
            rowSelection={rowSelection}
            columns={columns}
            dataSource={data}
            pagination={false}
            loading={loading}
          />
          {ClassInfoList.length > 0 ? (
            <div className={styles.pag}>
              <ListPagination total={Total} pageIndex={PageIndex} onChange={this.onShowSizeChange} />
            </div>
          ) : (
            ''
          )}
        </div>
      </div>
    );
  }
}
export default connect()(ClassSeheduleList);
