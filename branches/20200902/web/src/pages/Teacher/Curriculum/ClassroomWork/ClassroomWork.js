import React, { Component, Fragment } from 'react';
import ListPagination from 'components/ListPagination';
import { Table, Select, Input, Button, Modal, message, Divider } from 'antd';
import style from './ClassroomWork.less';
import { connect } from 'dva';
import { Link } from 'dva/router';
import router from 'umi/router';
const Option = Select.Option;
let Value = [];
@connect(({ commonModel, classScheduleModel, curriculumModel }) => ({
  ...classScheduleModel,
  ...curriculumModel,
  CourseTypesList: commonModel.CourseTypesList,
  GradeList: commonModel.GradeList,
  CourseList: commonModel.CourseList,
  CurUserSystem: commonModel.CurUserSystem,
  SubjectStatusList: commonModel.SubjectStatusList,
  TeaBasePath: commonModel.TeaBasePath
}))
class ClassroomWork extends Component {
  constructor(props) {
    super(props);
    this.state = {
      total: 0,
      totalWeek: 0,
      completeclass: 0,
      inputVal: '',
      subjectSelect: 0,
      stateSelect: 0,
      content: [],
      Count: 0,
      PageIndex: 1,
      visible: false,
      subjectContent: [],
      sectionContent: [],
      param: '',
      examSetId: [],
      disabledState: true,
      delvisible: false,
      url: '',
      jumpother: false,
      newvisible: false,
      graSelect: [],
      graSelectval: 0,
      gradeSelect: [],
      newsubject: '',
      testresult: '',
      examSetTypeId: 0,
      workname: '',
      warn: '',
      subjectId: '',
      infoExamid: '',
      newFlag: 0,
      signexamSetId: [],
      signdelvisible: false,
      PageSize: 25,
      savestate: true,
      backid: ''
    };
  }
  componentDidMount() {
    const { CourseList, dispatch, GradeList, location } = this.props;
    const GraSelect = [];
    const GradeSelect = [];
    if (location.query.teid && location.query.backUrl) {
      this.setState({
        subjectId: location.query.backUrl.split('_')[0]
      });
      dispatch({
        type: 'curriculumModel/GetExamSetInfo',
        payload: {
          teid: location.query.teid, // MOTK返回的题集ID teid
          backurl: location.query.backUrl //MOTK返回的，直接传过来就OK  可能要做UrlEncode解码
        }
      }).then(result => {
        this.setState({
          newsubject: result.CourseName,
          newFlag: result.StatusFlag,
          workname: result.ExamSetName,
          infoExamid: result.ExamSetId,
          backid: result.ExamSetId
        });

        // filter grade
        const courseResult = _.find(CourseList, item => {
          return item.Id == result.CourseId;
        });

        const tempGradeList = _.cloneDeep(GradeList);
        if (courseResult) {
          _.remove(tempGradeList, grade => {
            return courseResult.CourseTypeId != grade.CourseTypeId;
          });
        }

        tempGradeList.map(item => {
          return GraSelect.push(
            <Option value={item.Id} key={item.Id}>
              {item.Name}
            </Option>
          );
        });

        this.setState({
          jumpother: false,
          newvisible: true,
          graSelect: GraSelect,
          backUrl: location.query.backUrl
        });
      });
    } else {
      this.setState({
        subjectId: location.query.SubjectId
      });
    }
    CourseList.map(item => {
      return GradeSelect.push(
        <Option value={item.Id} key={item.Id}>
          {item.LongName}
        </Option>
      );
    });
    this.setState(
      {
        gradeSelect: GradeSelect
      },
      () => {
        this.tablecontent();
      }
    );
  }
  //获取列表内容
  tablecontent = () => {
    const { dispatch } = this.props;
    const { subjectSelect, stateSelects, inputVal, PageIndex, subjectId, PageSize } = this.state;
    const Content = [];
    dispatch({
      type: 'curriculumModel/GetExamSetList',
      payload: {
        SubjectId: subjectId, //课程加密ID
        CourseId: subjectSelect, //非必填 学科id
        ExamSetName: inputVal, //非必填作业名称
        StatusFlag: stateSelects, //非必填 状态 1待上架，2已上架
        PageSize: PageSize, //每页显示数
        PageIndex: PageIndex //当前页数
      }
    }).then(result => {
      result.ExamSetLists.map((item, index) => {
        return Content.push({
          key: item.ExamSetId,
          ExamSetId: item.ExamSetId,
          ExamSetName: item.ExamSetName,
          CourseName: item.CourseName,
          GradeName: item.GradeName,
          ExamSetTypeId: item.ExamSetTypeId,
          QuestionCount: item.QuestionCount,
          StatusFlag: item.StatusFlag,
          CreateTime: item.CreateTime,
          AnswerCount: item.AnswerCount
        });
      });
      this.setState({
        Count: result.Count,
        content: Content
      });
    });
  };

  /**
   * 学段学科下拉框
   */
  subjectChange = value => {
    this.setState({
      subjectSelect: value
    });
  };
  /**
   * 选择状态
   */
  stateChange = value => {
    this.setState({
      stateSelects: value
    });
  };
  /**
   * 获取学科名字
   */
  onChangeName = e => {
    this.setState({
      workname: e.target.value
    });
  };
  /**
   * 检索作业名称
   */
  onChangeClassName = e => {
    this.setState({
      inputVal: e.target.value
    });
  };
  /**
   * 翻页
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
   * 查询
   */
  submit = () => {
    this.setState(
      {
        PageIndex: 1
      },
      () => {
        this.tablecontent();
      }
    );
  };
  /**
   * 弹出框取消
   */
  handleCancel = () => {
    this.setState({
      visible: false
    });
  };
  /**
   * 弹出框确定
   */
  handleOk = () => {
    const { dispatch, CurUserSystem } = this.props;
    const { CourseId, subjectId } = this.state;
    this.setState({
      visible: false
    });
    dispatch({
      type: 'curriculumModel/GetAddExamSetUrl',
      payload: {
        CourseId: CourseId,
        SubjectId: subjectId,
        Online: CurUserSystem
      }
    }).then(result => {
      this.setState({
        url: result.AddExamSetUrl,
        jumpother: true
      });
    });
  };

  chooseSubject = param => {
    this.setState({
      CourseId: param
    });
  };
  /**
   * 回调弹框
   */
  newhandleOk = () => {
    const { dispatch, TeaBasePath } = this.props;
    const { infoExamid, workname, graSelectval, examSetTypeId } = this.state;
    if (graSelectval == 0 || examSetTypeId == 0 || workname == '') {
      this.setState({
        warn: (
          <p className="c-red" style={{ float: 'left' }}>
            必填项不能为空
          </p>
        )
      });
    } else {
      this.setState({
        warn: ''
      });
      dispatch({
        type: 'curriculumModel/SaveExamSet',
        payload: {
          ExamSetId: infoExamid, // 作业加密ID
          ExamSetName: workname, //作业名称
          GradeId: graSelectval, // 年级ID
          ExamSetTypeId: examSetTypeId // 作业类型，1课前作业，2随堂练习，3课后作业
        }
      }).then(result => {
        if (result === 1) {
          message.success('保存成功');
          this.setState(
            {
              newvisible: false,
              savestate: true
            },
            () => {
              const { subjectId = '' } = this.state;
              window.location.href = `${TeaBasePath}/Curriculum/Work?SubjectId=${subjectId}`;
            }
          );
        } else {
          message.error('保存失败');
          this.setState({
            savestate: false
          });
        }
      });
    }
  };
  newhandleCancel = () => {
    const { backid } = this.state;
    const { TeaBasePath } = this.props;
    let BackId = [];
    BackId.push(backid);
    this.delectOperation(BackId, 2);

    this.setState(
      {
        newvisible: false
      },
      () => {
        const { subjectId = '' } = this.state;
        window.location.href = `${TeaBasePath}/Curriculum/Work?SubjectId=${subjectId}`;
      }
    );
  };

  /**
   * 录入新题
   */
  addNew = () => {
    let SectionContent = [];
    const { CourseList, CourseTypesList } = this.props;
    CourseTypesList.map((item, index) => {
      let SubjectContent = [];
      SectionContent.push(
        <div key={index}>
          <p key={index}>{item.Name}</p>
          <p>{SubjectContent}</p>
        </div>
      );
      CourseList.map((i, ind) => {
        if (item.Id === i.CourseTypeId) {
          SubjectContent.push(
            <Button
              key={ind}
              onClick={() => {
                this.chooseSubject(i.Id);
              }}
              className={style.bottomMargin}
            >
              {i.LongName}
            </Button>
          );
        }
      });
    });
    this.setState({
      visible: true,
      sectionContent: SectionContent
    });
  };
  //单个删除
  signdelect = value => {
    Value = [];
    Value.push(value);
    this.setState({
      signdelvisible: true
    });
  };
  signdelhandleOk = () => {
    this.setState(
      {
        signdelvisible: false
      },
      () => {
        this.delectOperation(Value, 1);
      }
    );
  };
  signdelhandleCancel = () => {
    this.setState({
      signdelvisible: false
    });
  };
  /**
   * 保存下拉框信息
   */
  graChange = value => {
    this.setState({
      graSelectval: value
    });
  };
  attribute = value => {
    this.setState({
      examSetTypeId: value
    });
  };
  //删除弹框
  delhandleOk = () => {
    const { examSetId } = this.state;
    this.setState(
      {
        delvisible: false
      },
      () => {
        this.delectOperation(examSetId, 1);
      }
    );
  };
  //批量删除
  delect = () => {
    this.setState({
      delvisible: true
    });
  };
  //取消删除
  delhandleCancel = () => {
    this.setState({
      delvisible: false
    });
  };
  //上下架操作
  upperDown = (value, examSetId, ExamSetTypeId) => {
    const { dispatch, location } = this.props;
    dispatch({
      type: 'curriculumModel/SetExamSetOnline',
      payload: {
        SubjectId: location.query.SubjectId,
        StatusFlag: value,
        ExamSetId: examSetId,
        ExamSetTypeId: ExamSetTypeId
      }
    }).then(result => {
      if (result === 1) {
        message.success('操作成功');
        this.tablecontent();
      } else if (result === 2) {
        Modal.error({
          title: '同一属性的作业只能上架一份,请下架后再上架',
          okText: '确定'
        });
      } else {
        message.error('操作失败');
      }
    });
  };
  //删除操接口
  delectOperation = (val, statue) => {
    const { dispatch } = this.props;
    dispatch({
      type: 'curriculumModel/DeleteExamSetOrFile',
      payload: {
        ExamSetIdList: val
      }
    }).then(result => {
      if (statue == 1) {
        if (result === 1) {
          message.success('删除成功');
          this.tablecontent();
        } else {
          message.error('删除失败');
        }
      } else {
        this.tablecontent();
      }
    });
  };
  changerow = (selectedRowKeys, selectedRows) => {
    let ExamSetid = [];
    selectedRows.map(item => {
      ExamSetid.push(item.ExamSetId);
    });
    if (selectedRowKeys.length === 0) {
      this.setState({
        disabledState: true,
        examSetId: ExamSetid
      });
    } else {
      this.setState({
        disabledState: false,
        examSetId: ExamSetid
      });
    }
  };
  render() {
    let text = '';
    let disabled = false;
    const { TeaBasePath, location } = this.props;
    const {
      Count,
      sectionContent,
      graSelect,
      disabledState,
      newsubject,
      workname,
      newFlag,
      signdelvisible,
      newvisible,
      jumpother,
      delvisible,
      PageIndex,
      url,
      warn,
      gradeSelect
    } = this.state;
    const rowSelection = {
      onChange: this.changerow,
      selectedRowKeys: this.state.examSetId
    };
    const columns = [
      {
        title: '作业名称',
        dataIndex: 'ExamSetName',
        key: 'ExamSetName'
      },
      {
        title: '学段学科',
        dataIndex: 'CourseName',
        key: 'CourseName'
      },
      {
        title: '适用年级',
        dataIndex: 'GradeName',
        key: 'GradeName'
      },
      {
        title: '作业属性',
        dataIndex: 'ExamSetTypeId',
        key: 'ExamSetTypeId',
        render: model => {
          switch (model) {
            case 1:
              return '课前作业';
            case 2:
              return '随堂练习';
            default:
              return '课后作业';
          }
        }
      },
      {
        title: '题目数量',
        dataIndex: 'QuestionCount',
        key: 'QuestionCount'
      },
      {
        title: '状态',
        dataIndex: 'StatusFlag',
        key: 'StatusFlag',
        render: model => {
          switch (model) {
            case 1:
              return '待上架';
            default:
              return '已上架';
          }
        }
      },
      {
        title: '创建时间',
        dataIndex: 'CreateTime',
        key: 'CreateTime'
      },
      {
        title: '作答数',
        dataIndex: 'AnswerCount',
        key: 'AnswerCount'
      },
      {
        title: '操作',
        dataIndex: 'operation',
        key: 'operation',
        render: (model, record) => {
          return (
            <div>
              {record.StatusFlag === 1 && (
                <a href="javascript:;" onClick={() => this.upperDown(2, record.ExamSetId, record.ExamSetTypeId)}>
                  上架
                </a>
              )}
              {record.StatusFlag === 2 && (
                <a href="javascript:;" onClick={() => this.upperDown(1, record.ExamSetId, record.ExamSetTypeId)}>
                  下架
                </a>
              )}
              <Divider type="vertical" />
              <Link
                to={`${TeaBasePath}/Curriculum/Work/Statistics?ExamSetId=${record.ExamSetId}&SubjectId=${location.query.SubjectId}`}
              >
                <span>统计</span>
              </Link>
              <Divider type="vertical" />
              <Link
                to={`${TeaBasePath}/Curriculum/Work/Preview?ExamSetId=${record.ExamSetId}&SubjectId=${location.query.SubjectId}`}
              >
                预览
              </Link>
              <Divider type="vertical" />
              <a href="javascript:;" className="c-red" onClick={() => this.signdelect(record.ExamSetId)}>
                删除
              </a>
            </div>
          );
        }
      }
    ];
    if (newFlag === 1) {
      text = '成功';
      disabled = false;
    } else {
      text = '仅支持单选(不包括有小题)';
      disabled = true;
    }
    return (
      <div>
        {!jumpother && (
          <div className={style.content}>
            <h2>课堂作业</h2>
            <span className={style.cityList}>学段学科:</span>
            <Select defaultValue="0" style={{ width: 180 }} onChange={this.subjectChange}>
              <Option value="0" key="0">
                请选择
              </Option>
              {gradeSelect}
            </Select>
            <span className={style.cityList}>状态:</span>
            <Select defaultValue="0" style={{ width: 180 }} onChange={this.stateChange}>
              <Option value="0" key="0">
                请选择
              </Option>
              <Option value="1" key="1">
                待上架
              </Option>
              <Option value="2" key="2">
                已上架
              </Option>
            </Select>
            <Input onChange={this.onChangeClassName} placeholder=" 请输入作业名称" className={style.tableinput} />
            <Button type="primary" onClick={this.submit}>
              查询
            </Button>
            <Button onClick={this.addNew}>录入新题</Button>
            <Button disabled={disabledState} onClick={this.delect}>
              批量删除
            </Button>
            <Table rowSelection={rowSelection} columns={columns} dataSource={this.state.content} pagination={false} />
            <div className={style.pag}>
              <ListPagination total={Count} pageIndex={PageIndex} onChange={this.onChange} />
            </div>
          </div>
        )}

        {jumpother && <iframe src={url} className={style.iframe} frameborder="no"></iframe>}

        <Modal title="选择学科" visible={this.state.visible} onOk={this.handleOk} onCancel={this.handleCancel}>
          {sectionContent}
        </Modal>
        <Modal title="删除" visible={delvisible} onOk={this.delhandleOk} onCancel={this.delhandleCancel}>
          <p>是否确定批量删除</p>
        </Modal>
        <Modal title="删除" visible={signdelvisible} onOk={this.signdelhandleOk} onCancel={this.signdelhandleCancel}>
          <p>是否确定删除此项</p>
        </Modal>
        <Modal
          title="录入新题"
          visible={newvisible}
          maskClosable={false}
          onCancel={this.newhandleCancel}
          footer={[
            <Button onClick={this.newhandleCancel}>取消</Button>,
            <Button key="submit" type="primary" onClick={this.newhandleOk} disabled={disabled}>
              确定
            </Button>
          ]}
        >
          <div>
            <span className="c-red">*</span>检验结果：<span className="c-red">{text}</span>
          </div>
          <div>
            <span className="c-red">*</span>作业名称：
            <Input className={style.modalinput} value={workname} onChange={this.onChangeName}></Input>
          </div>
          <div>
            <span className="c-red">*</span>适用年级：
            <Select style={{ width: 180 }} placeholder="请选择年级" onChange={this.graChange}>
              {/* <Option value="0" key="0">请选择</Option> */}
              {graSelect}
            </Select>
          </div>
          <div>
            <span className="c-red">*</span>作业属性：
            <Select
              style={{ width: '180px', margin: '10px 0 10px 0' }}
              placeholder="请选择属性"
              onChange={this.attribute}
            >
              <Option value="1" key="1">
                课前作业
              </Option>
              <Option value="2" key="2">
                随堂练习
              </Option>
              <Option value="3" key="3">
                课后作业
              </Option>
            </Select>
          </div>
          <div>
            <span className="c-red">*</span>学段学科：{newsubject}
          </div>
          {warn}
        </Modal>
      </div>
    );
  }
}
export default ClassroomWork;
