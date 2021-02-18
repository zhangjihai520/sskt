import React, { Component, Fragment } from 'react';
import style from './index.less';
import { Link } from 'dva/router';
import Classbegins from '../Classbegins';
import ListPagination from 'components/ListPagination';
import copy from 'copy-to-clipboard';
import {
  Table,
  Select,
  Input,
  Button,
  Row,
  Col,
  Divider,
  Menu,
  Dropdown,
  Icon,
  Modal,
  Upload,
  message,
  Spin
} from 'antd';
import { connect } from 'dva';
import { roleEnum } from 'config/enum';
import classnames from 'classnames';
import { checkCourse } from 'utils/utils';

const Option = Select.Option;

@connect(({ commonModel, classScheduleModel, curriculumModel, loading }) => ({
  ...curriculumModel,
  ...classScheduleModel,
  listLoading: loading.effects['classScheduleModel/fetch'],
  GradeList: commonModel.GradeList,
  SubjectStatusList: commonModel.SubjectStatusList,
  CurUserSystem: commonModel.CurUserSystem,
  TeaBasePath: commonModel.TeaBasePath
}))
class CurriculumList extends Component {
  constructor(props) {
    super(props);
    this.state = {
      total: 0,
      totalWeek: 0,
      completeclass: 0,
      inputVal: '',
      gradeSelectlist: [],
      gradeSelect: 0,
      stateSelect: 0,
      completedTotalWeek: 0,
      content: [],
      PageIndex: 1,
      PageSize: 25,
      TotalNumber: 0,
      SubjectId: '',
      filePath: '',
      fileList: [],
      visible: false,
      Param: {},
      responseContent: [],
      pushcontent: [],
      msgvisible: false,
      content: [],
      teachvisible: false,
      thisId: '',
      authCode: '',
      acvisible: false,
      deviceId: '', //设备id，用来判断是否二次更换设备
      dataList: []
    };
  }
  componentDidMount() {
    const GradeSelect = [];
    const { GradeList, dispatch } = this.props;
    this.tablecontent();
    // 获取完成课程
    dispatch({
      type: 'curriculumModel/GetSubjectListCount',
      payload: {}
    }).then(result => {
      this.setState({
        total: result.Total,
        totalWeek: result.TotalWeek,
        completedTotalWeek: result.CompletedTotalWeek
      });
    });
    GradeList.map(item => {
      return GradeSelect.push(
        <Option value={item.Id} key={item.Id}>
          {item.Name}
        </Option>
      );
    });
    this.setState({
      gradeSelectlist: GradeSelect
    });
  }
  /**
   * 保存输入框信息
   */
  onChangeUserName = e => {
    this.setState({
      inputVal: e.target.value
    });
  };
  /**
   * 获取列表信息
   */
  tablecontent = () => {
    const { PageSize, PageIndex, gradeSelect, stateSelect, inputVal } = this.state;
    const { dispatch } = this.props;
    dispatch({
      type: 'classScheduleModel/fetch',
      payload: {
        PageSize: PageSize,
        PageIndex: PageIndex,
        GradeId: gradeSelect,
        StatusFlag: stateSelect,
        Key: inputVal,
        UserRole: roleEnum.teacher,
        IsOnlySelf: 1
      }
    });
  };
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

  //获取授权号码
  GetAuthCode = value => {
    const { dispatch } = this.props;
    dispatch({
      type: 'smallnModel/GetAuthCode',
      payload: {
        SubjectId: value
      }
    }).then(result => {
      this.setState({
        authCode: result.AuthCode
      });
    });
  };
  /**
   * 保存下拉框信息
   */
  gradeChange = value => {
    this.setState({
      gradeSelect: value
    });
  };
  stateChange = value => {
    this.setState({
      stateSelect: parseInt(value)
    });
  };
  info = () => {
    const { responseContent } = this.state;
    let content = [];
    responseContent.map(item => {
      content.push(<p className={style.mescontent}>{item}</p>);
    });
  };
  /**
   * 导入学生
   */
  updata = (value, SubId) => {
    this.setState({
      visible: true,
      Param: JSON.stringify({ SubjectId: SubId, SubjectRoomId: value })
    });
  };
  /**
   * 提交查询信息
   */
  submit = () => {
    this.setState({
      PageIndex: 1
    });
    this.tablecontent();
  };

  //确定上传
  handleOk = () => {
    const { fileList, Param } = this.state;
    const formData = new FormData();
    const { dispatch } = this.props;
    formData.append('param', Param);
    fileList.forEach(file => {
      formData.append('file', file);
    });
    dispatch({
      type: 'curriculumModel/ImportStudent',
      payload: formData
    }).then(res => {
      this.setState(
        {
          responseContent: res.MessageList,
          fileList: [],
          msgvisible: true
        },
        () => {
          const { responseContent } = this.state;
          let Content = [];
          responseContent.map((item, index) => {
            Content.push(
              <p key={index} className={style.mescontent}>
                {item}
              </p>
            );
          });
          this.setState({
            content: Content
          });
        }
      );
    });
  };
  /**
   * 取消上传
   */
  handleCancel = () => {
    this.setState({
      fileList: [],
      visible: false
    });
  };

  msghandleCancel = () => {
    this.setState({
      msgvisible: false
    });
  };
  achandleCancel = () => {
    this.setState({
      acvisible: false
    });
  };
  /**
   * 老师授课
   */
  Teaching = (value, id) => {
    this.GetDevicesist();
    this.setState({
      thisId: value,
      teachvisible: true,
      deviceId: id
    });
  };

  /**
* 老师授课
*/
  OnlineTeaching = (value) => {
    this.GetAuthCode(value);
    this.setState({
      thisId: value,
      acvisible: true
    });
  };

  //上课回调方法
  close = (value, type) => {
    if (!value) {
      this.setState(
        {
          teachvisible: false
        },
        () => {
          if (type == 1) {
            //只有更新了设备，才重新请求列表
            this.tablecontent();
          }
        }
      );
    }
  };

  // 当课程是已开课时，用来改变上课按钮状态，现弃用
  Inclass = value => {
    // if (value) this.tablecontent()
  };
  /**
   * 页码变化
   */
  onChange = current => {
    this.setState(
      {
        PageIndex: current.PageIndex,
        PageSize: current.PageSize
      },
      () => {
        this.tablecontent();
      }
    );
  };

  /**
   * 复制
   */
  copyAuthCode = () => {
    const { authCode } = this.state;
    if(copy(authCode)){
      message.success(`复制成功`);
      this.setState({
        acvisible: false
      });
    }

  }

  render() {
    const { ClassInfoList, Count, CurUserSystem, TeaBasePath, listLoading } = this.props;
    const {
      fileList,
      PageIndex,
      visible,
      msgvisible,
      acvisible,
      thisId,
      content,
      authCode,
      gradeSelectlist,
      teachvisible,
      deviceId,
      dataList
    } = this.state;
    const props = {
      accept: '.xls,.xlsx',
      onRemove: file => {
        this.setState(state => {
          const index = state.fileList.indexOf(file);
          const newFileList = state.fileList.slice();
          newFileList.splice(index, 1);
          return {
            fileList: newFileList
          };
        });
      },
      beforeUpload: file => {
        const { name } = file;
        const division = name.split('.');
        const suffix = division[division.length - 1].toLowerCase();
        if (suffix === 'xls' || suffix === 'xlsx') {
          this.setState(state => ({
            fileList: [...state.fileList, file]
          }));
          return true;
        } else {
          message.error(`不支持上传该类型（${suffix}）文件`);
          return false;
        }
      },
      fileList
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
        dataIndex: 'SubjectName',
        key: 'SubjectName',
        render: (name, record) => {
          return (
            <span>
              <img
                className={style.img}
                alt=""
                src={record.ImagePath ? record.ImagePath : `/images/common/img_${shortCodeFilter(record.ShortCode)}.jpg`}
              ></img>
              <span className={style.name}>{name}</span>
            </span>
          );
        }
      },
      {
        title: '年级',
        dataIndex: 'GradeName',
        key: 'GradeName'
      },
      {
        title: '学科',
        dataIndex: 'CourseName',
        key: 'CourseName'
      },
      {
        title: '课程时间',
        dataIndex: 'BeginTime',
        key: 'BeginTime',
        render: model => {
          return `课程时间/${model}`;
        }
      },
      {
        title: '状态',
        dataIndex: 'StatusFlag',
        key: 'StatusFlag',
        render: model => {
          let content = '',
            styleContent = '';
          switch (model) {
            case 1:
              styleContent = style.green;
              content = '待上架';
              break;
            case 2:
              styleContent = style.green;
              content = '待报名';
              break;
            case 3:
              styleContent = style.green;
              content = '报名中';
              break;
            case 4:
              styleContent = style.red;
              content = '待上课';
              break;
            case 5:
              styleContent = style.red;
              content = '已结课';
              break;
            case 6:
              styleContent = style.red;
              content = '上课中';
              break;
          }
          return <span className={classnames(style.stateItem, styleContent)}>{content}</span>;
        }
      },
      {
        title: '操作',
        dataIndex: 'operation',
        key: 'operation',
        render: (
          model,
          record //record.StatusFlag === 6 上课中，也可以操作上课按钮
        ) => (
            <Fragment>
              <Link to={`${TeaBasePath}/Curriculum/Details?SubjectId=${record.SubjectId}`}>
                <span>查看</span>
              </Link>
              <Divider type="vertical" />

              {record.IsLecturer && record.SubjectGenreId < 3 && (
                <a onClick={() => this.Teaching(record.SubjectId, record.DeviceId)} disabled={record.StatusFlag === 5}>
                  上课
                </a>
              )}

              {record.IsLecturer && record.SubjectGenreId === 3 && (
                <a onClick={() => this.OnlineTeaching(record.SubjectId)} disabled={record.StatusFlag === 5}>
                  上课
                </a>
              )}

              {!record.IsLecturer && (
                <a
                  href={`${TeaBasePath}/Curriculum/Live?SubjectId=${record.SubjectId}`}
                  disabled={record.StatusFlag != 6}
                >
                  听课
                </a>
              )}
              <Divider type="vertical" />
              <Dropdown
                overlay={
                  <Menu>
                    {CurUserSystem === 2 && !record.IsLecturer && (
                      <Menu.Item onClick={() => this.updata(record.SubjectRoomId, record.SubjectId)}>导入学生</Menu.Item>
                    )}
                    {record.IsLecturer && (
                      <Menu.Item>
                        <Link to={`${TeaBasePath}/Curriculum/Work?SubjectId=${record.SubjectId}`}>课堂作业</Link>
                      </Menu.Item>
                    )}
                    <Menu.Item>
                      <Link to={`${TeaBasePath}/Curriculum/Data?SubjectId=${record.SubjectId}`}>课件资料</Link>
                    </Menu.Item>
                  </Menu>
                }
              >
                <a href="javascript:;">
                  更多
                <Icon type="down" />
                </a>
              </Dropdown>
            </Fragment>
          )
      }
    ];
    return (
      <div>
        <Row className={style.lessonnum}>
          <Col className={classnames(style.item, style.blue, style.first)} span={4}>
            <div className={style.desc}>
              <div className={style.descTitle}>我的课程</div>
              <div className={style.descContent}>目前完成课程总数</div>
            </div>
            <div className={style.rightNum}>{this.state.total}</div>
          </Col>
          <Col className={classnames(style.item, style.red)} span={4}>
            <div className={style.desc}>
              <div className={style.descTitle}>本周课程</div>
              <div className={style.descContent}>目前需要完成课程</div>
            </div>
            <div className={style.rightNum}>{this.state.totalWeek}</div>
          </Col>
          <Col className={classnames(style.item, style.green)} span={4}>
            <div className={style.desc}>
              <div className={style.descTitle}>本周完成课程</div>
              <div className={style.descContent}>本周已完成课程数</div>
            </div>
            <div className={style.rightNum}>{this.state.completedTotalWeek}</div>
          </Col>
        </Row>
        <div className={style.content}>
          <div className={style.tabheader}>
            <Select className={style.queryMargin} defaultValue="请选择年级" style={{ width: 180 }} onChange={this.gradeChange}>
              <Option value="0" key="0">
                全部
              </Option>
              {gradeSelectlist}
            </Select>
            <Select defaultValue="请选择状态" style={{ width: 180 }} onChange={this.stateChange}>
              <Option value="0" key="0">
                全部
              </Option>
              <Option value="2" key="2">
                待报名
              </Option>
              <Option value="3" key="3">
                报名中
              </Option>
              <Option value="4" key="4">
                待上课
              </Option>
              <Option value="5" key="5">
                已结课
              </Option>
            </Select>
            <Input onChange={this.onChangeUserName} placeholder=" 请输入课程名称" className={style.input} />
            <Button type="primary" onClick={this.submit}>
              查询
            </Button>
          </div>
          <div className={style.dataWrap}>
            <Spin tip="Loading..." spinning={listLoading}>
              <Table
                columns={columns}
                dataSource={ClassInfoList}
                className={style['ant-pagination']}
                pagination={false}
              />
            </Spin>
            <div className={style.pag}>
              <ListPagination total={Count} pageIndex={PageIndex} onChange={this.onChange} />
            </div>
          </div>
          <Modal
            title="导入学生"
            visible={visible}
            onCancel={this.handleCancel}
            onOk={this.handleOk}
            okButtonProps={{ disabled: fileList.length === 0 }}
          >
            <div>
              学生表格需要包含学生资料及账号
              <a href="/template/ImportStudent.xlsx" download="">
                点击下载实例文件
              </a>
            </div>
            <div className={style.up}>
              <span className={'c-red'}>*</span>附件上传：
              <Upload {...props}>
                {fileList.length >= 1 ? null : (
                  <Button>
                    <Icon type="upload" />
                    导入学生
                  </Button>
                )}
              </Upload>
            </div>
          </Modal>
          <Modal
            className={style.mescontent}
            visible={msgvisible}
            onCancel={this.msghandleCancel}
            footer={null}
            title="导入结果"
          >
            <div className={style.msgcontent}> {content}</div>
          </Modal>
          <Modal
            className={style.mescontent}
            visible={acvisible}
            onCancel={this.achandleCancel}
            footer={null}
            title="授权码"
          >
            <div className={style.msgcontent}> {authCode}</div>
            <Button style={{ float: 'right' }} onClick={this.copyAuthCode}>复制</Button>
          </Modal>

          <Classbegins
            visible={teachvisible}
            isSubject={1}
            Id={thisId}
            hasDeviceId={deviceId}
            close={this.close}
            Inclass={this.Inclass}
            dataList={dataList}
          />
        </div>
      </div>
    );
  }
}
export default CurriculumList;
