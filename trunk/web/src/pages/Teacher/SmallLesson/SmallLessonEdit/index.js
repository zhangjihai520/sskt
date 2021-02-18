import React, { Component } from 'react';
import { connect } from 'dva';
import styles from './index.less';
import { Form, Input, Tooltip, Select, Checkbox, message, Button, Row, Col, Card, Upload, Icon } from 'antd';
import moment from 'moment';

const Option = Select.Option;
const { TextArea } = Input;

@connect(({ commonModel, classScheduleModel }) => ({
  ...classScheduleModel,
  GradeList: commonModel.GradeList,
  CourseList: commonModel.CourseList
  // SubjectList:commonModel.SubjectStatusList
}))
class SmallLessonEdit extends Component {
  constructor(props) {
    super(props);
    this.state = {
      WeiKeId: '',
      BookVersionList: [], //教材版本
      CourseMappingList: [], //课本列表
      ChapterSectionList: [], //章节列表
      WeiKeName: '', //微课名称
      GradeId: '', //年级
      CourseId: '', //学段学科
      Remarks: '', //备注
      BookVersionId: '', //教材版本id
      CourseMappingId: '', //课本id
      SectionId: '', //所在章节id
      StatusFlag: 1,
      fileList: [],
      video: [],
      previewVisible: false, //上传图片
      previewImage: '',
      OverViewfileList: [],
      token: '', //上传文件提供给第三方的token
      filePath: `${SYSTEM_CONFIG.upload.filepath}${moment(new Date()).format('YYYYMM')}/`,
      courseList: _.cloneDeep(this.props.CourseList)
    };
  }

  componentWillMount() {
    if (this.props.location.state) {
      this.setState(
        {
          WeiKeId: this.props.location.state.WeiKeId
        },
        () => {
          this.GetWeiKeInfo();
        }
      );
    }
    //获取上传文件第三方token
    this.getUploadToken();
  }

  /**
   * 获取上传文件第三方token
   */
  getUploadToken() {
    const { dispatch } = this.props;
    const { userInfo } = SYSTEM_CONFIG.upload;
    dispatch({
      type: 'smallnModel/getUploadToken',
      payload: userInfo
    }).then(result => {
      if (result) {
        this.setState({
          token: result.token
        });
      }
    });
  }

  //获取教材版本
  GetBookVersionList = (CourseId, type) => {
    const { dispatch } = this.props;
    dispatch({
      type: 'smallnModel/GetBookVersionList',
      payload: {
        CourseId: CourseId
      }
    }).then(result => {
      this.setState({
        BookVersionList: result
      });
      if (type == 2) {
        this.setState({
          BookVersionId: '', //教材版本id
          CourseMappingId: '', //课本id
          SectionId: '' //所在章节id
        });
      }
    });
  };
  //根据教材获取课本列表
  GetCourseMappingList = Id => {
    const { dispatch } = this.props;
    dispatch({
      type: 'smallnModel/GetCourseMappingList',
      payload: {
        BookVersionId: Id
      }
    }).then(result => {
      if (result) {
        this.setState({
          CourseMappingList: result
        });
      }
    });
  };
  //根据课本获取章节列表
  GetChapterSectionList = Id => {
    const { dispatch } = this.props;
    dispatch({
      type: 'smallnModel/GetChapterSectionList',
      payload: {
        CourseMappingId: Id
      }
    }).then(result => {
      if (result) {
        this.setState({
          ChapterSectionList: result
        });
      }
    });
  };
  //添加/编辑微课
  SetWeiKeInfo = values => {
    const { dispatch } = this.props;
    const { WeiKeId, video, OverViewfileList, filePath } = this.state;
    var VideoUrl = [];
    var fileList = video.fileList;

    fileList &&
      fileList.map(item => {
        if (item.response) {
          //拼接视频播放地址
          let url = SYSTEM_CONFIG.upload.savepath + filePath + item.urlFileName;
          VideoUrl.push({ filename: item.name, url: url });
        } else {
          VideoUrl.push({ filename: item.name, url: item.url });
        }
      });

    values.Grade = values.Grade.key;
    values.Course = values.Course.key;
    values.BookVersionId = values.BookVersionId.key;
    values.CourseMappingId = values.CourseMappingId.key;
    values.SectionId = values.SectionId.key;
    dispatch({
      type: 'smallnModel/SetWeiKeInfo',
      payload: {
        WeiKeId: WeiKeId, //可为空,加密
        WeiKeName: values.WeiKeName, //微课名称
        GradeId: values.Grade, //年级id
        CourseId: values.Course, //学科Id
        Remarks: values.Remarks, //备注
        BookVersionId: values.BookVersionId, //教材版本id
        CourseMappingId: values.CourseMappingId, //课本id
        SectionId: values.SectionId, //所在章节id
        VideoUrl: VideoUrl, //微课视频url,可以有多个
        OverViewUrl: OverViewfileList && OverViewfileList.length >= 1 ? OverViewfileList[0].url : '' //上传图片
      }
    }).then(result => {
      if (result != 1) {
        message.error('操作失败');
      } else {
        message.success('操作成功');
        window.location.href = '/TeaOnline/SmallLesson';
      }
    });
  };
  //获取微课详情
  GetWeiKeInfo = () => {
    const { dispatch, GradeList } = this.props;
    const { WeiKeId, video, OverViewfileList } = this.state;

    dispatch({
      type: 'smallnModel/GetWeiKeInfo',
      payload: {
        WeiKeId: WeiKeId //可为空,加密
      }
    }).then(result => {
      if (!result) {
        return true;
      }
      var VideoUrl = [];
      result.VideoUrl.map((item, a) => {
        //视频
        var VideoUrlList = {
          uid: -a,
          name: 'yyy.png',
          status: 'done',
          type: 'video/mp4',
          url: 'http://www.baidu.com/xxx.mp4'
        };
        VideoUrlList.url = item.Url;
        VideoUrlList.name = item.Filename ? item.Filename : item.Url;
        VideoUrl.push(VideoUrlList);
      });
      video.fileList = VideoUrl;
      video.file = VideoUrl;
      if (result.OverViewUrl) {
        //图片
        var OverViewurl = [
          {
            uid: '-1',
            name: 'xxx.png',
            status: 'done',
            url: result.OverViewUrl
          }
        ];
      }

      this.setState({
        WeiKeName: result.WeiKeName, //微课名称
        GradeId: result.GradeId.toString(), //年级ID
        CourseId: result.CourseId.toString(), //学段学科
        Remarks: result.Remarks, //备注
        BookVersionId: result.BookVersionId, //教材版本id
        CourseMappingId: result.CourseMappingId, //课本id
        SectionId: result.SectionId, //所在章节id
        StatusFlag: result.StatusFlag,
        fileList: VideoUrl,
        video: video,
        OverViewfileList: OverViewurl
      });
      this.GetCourseMappingList(result.BookVersionId); //根据教材获取课本列表
      this.GetChapterSectionList(result.CourseMappingId); //根据课本获取章节列表
      this.GetBookVersionList(result.CourseId, 1); //根据学科获取教材版本列表

      const gradeResult = _.find(this.props.GradeList, item => {
        return item.Id == result.GradeId;
      });

      let courseList = _.cloneDeep(this.props.CourseList);
      if (gradeResult) {
        _.remove(courseList, function(course) {
          return course.CourseTypeId != gradeResult.CourseTypeId;
        });
      }

      this.setState({
        courseList: courseList
      });
    });
  };
  //返回列表
  handleReset = () => {
    window.location.href = '/TeaOnline/SmallLesson';
  };
  //提交表单
  handleSubmit = e => {
    const { video } = this.state;
    e.preventDefault();
    this.props.form.validateFieldsAndScroll((err, values) => {
      if (!err) {
        this.SetWeiKeInfo(values);
      }
      return false;
    });
  };
  dropDown = (val, type) => {
    if (type == 1) {
      this.GetCourseMappingList(val.key);
    } else if (type == 2) {
      this.GetChapterSectionList(val.key);
    }
  };
  selectOption = list => {
    if (list) {
      return list.map(item => (
        <Option key={item.Id} value={item.Id}>
          {item.Name}
        </Option>
      ));
    }
  };
  //上传视频
  handleChange = info => {
    const { video, WeiKeId } = this.state;
    let fileList = info.fileList;

    // fileList = fileList.slice(-2);

    fileList = fileList.map(file => {
      if (file.response) {
        file.url = file.response.url;
      }
      return file;
    });

    if (info.file.status === 'removed') {
      this.state.fileList.map((item, a) => {
        if (item.name == info.file.name) {
          this.state.fileList.splice(a, 1);
          return false;
        }
      });
    }

    if (info.file.status === 'done' && info.file.response && info.file.response.nResult == 1) {
      this.setState({ fileList });
      video.fileList = fileList;
      video.file = fileList;
      message.success(`${info.file.name} 上传成功`);
    } else if (info.file.status === 'error') {
      message.error(`${info.file.name} 上传失败`);
    }

    fileList = fileList.filter(file => {
      const ismp4 = file.type === 'video/mp4';
      if (!ismp4) {
        message.error('请上传mp4格式视频文件');
        return false;
      }
      const isLt2M = file.size / 1024 / 1024 < 500;
      if (file.size && !isLt2M) {
        message.error('文件不能超过500M');
        return false;
      }

      if (file.response) {
        return file.response.status === 'success';
      }
      if (file.status === 'uploading') {
        this.setState({ fileList });
      }
      return false;
    });

    // this.setState({ fileList});
  };
  //删除视频
  onRemoveVideo = () => {
    const { StatusFlag } = this.state;
    let isRemove = true;

    if (StatusFlag == 2) {
      isRemove = false;
      message.error('已上架的微课不能删除视频');
    }
    return isRemove;
  };
  //点击视频链接的回调
  onPreview = () => {};
  //上传图片
  onOverView = info => {
    if (info.file.status === 'uploading') {
      const isJPG = info.file.type === 'image/jpeg';
      if (!isJPG) {
        message.error('请上传jpg格式图片');
        return false;
      }
    }

    const isLt2M = info.file.size / 1024 / 1024 < 2;
    if (info.file.size && !isLt2M) {
      message.error('文件不能超过2M');
      return false;
    }

    this.setState({ OverViewfileList: info.fileList });
    if (info.file.status === 'done') {
      if (info.file.response.ResultType === 1) {
        var fileList = [
          {
            uid: '1',
            name: info.file.response.ReturnEntity.FileName,
            status: 'done',
            url: info.file.response.ReturnEntity.FilePath
          }
        ];

        this.setState({ OverViewfileList: fileList });
      }
    }
  };

  /**
   * 获取上传视频的其它参数
   */
  getFileUploadData = file => {
    const { token, filePath } = this.state;
    file.urlFileName = `${file.uid}${file.name.substring(file.name.lastIndexOf('.'))}`;
    return { token: token, filepath: filePath, filename: file.urlFileName };
  };

  render() {
    const {
      WeiKeId,
      BookVersionList,
      CourseMappingList,
      ChapterSectionList,
      fileList,
      video,
      WeiKeName,
      GradeId,
      CourseId,
      Remarks,
      BookVersionId,
      CourseMappingId,
      SectionId,
      OverViewfileList,
      courseList
    } = this.state;
    const { GradeList, CourseList } = this.props;
    const { getFieldDecorator,setFieldsValue } = this.props.form;
    const formItemLayout = {
      labelCol: {
        xs: { span: 24 },
        sm: { span: 8 }
      },
      wrapperCol: {
        xs: { span: 24 },
        sm: { span: 16 }
      }
    };
    const uploadButton = (
      <div>
        <Icon type="plus" />
        <div className="ant-upload-text">上传图片</div>
      </div>
    );

    return (
      <div className={styles.SmallLessonEdit}>
        <div className={styles.title}>{WeiKeId ? '编辑微课' : '添加微课'}</div>
        <Form {...formItemLayout} onSubmit={this.handleSubmit} className={styles.formbox}>
          <Form.Item label="微课名称">
            {getFieldDecorator('WeiKeName', {
              initialValue: WeiKeName,
              rules: [
                {
                  required: true,
                  message: '请输入微课名称!'
                }
              ]
            })(<Input />)}
          </Form.Item>
          <Row>
            <Col span={12}>
              <Form.Item label="年级">
                {getFieldDecorator('Grade', {
                  initialValue: (GradeId && { key: GradeId }) || undefined,
                  rules: [{ required: true, message: '请选择年级!' }]
                })(
                  <Select
                    labelInValue
                    onChange={value => {
                      const gradeResult = _.find(this.props.GradeList, item => {
                        return item.Id == value.key;
                      });

                      let courseList = _.cloneDeep(this.props.CourseList);
                      if (gradeResult) {
                        _.remove(courseList, function(course) {
                          return course.CourseTypeId != gradeResult.CourseTypeId;
                        });
                      }
                      setFieldsValue({ Course: undefined });
                      this.setState({
                        courseList: courseList
                      });
                    }}
                  >
                    {this.selectOption(GradeList)}
                  </Select>
                )}
              </Form.Item>
            </Col>
            <Col span={12}>
              <Form.Item label="学段学科">
                {getFieldDecorator('Course', {
                  initialValue: (CourseId && { key: CourseId }) || undefined,
                  rules: [{ required: true, message: '请选择学段学科!' }]
                })(
                  <Select labelInValue onChange={val => this.GetBookVersionList(val.key, 2)}>
                    {courseList &&
                      courseList.map(item => {
                        return (
                          <Option value={item.Id} key={'Course' + item.Id}>
                            {item.LongName}
                          </Option>
                        );
                      })}
                  </Select>
                )}
              </Form.Item>
            </Col>
          </Row>
          <Form.Item label="上传图片">
            {getFieldDecorator('OverView', {
              initialValue: OverViewfileList ? OverViewfileList.fileList : '' || undefined
            })(
              <div>
                <Upload
                  action="/api/Common/UploadFile"
                  listType="picture-card"
                  fileList={OverViewfileList}
                  onChange={this.onOverView}
                >
                  {OverViewfileList && OverViewfileList.length >= 1 ? null : uploadButton}
                </Upload>
              </div>
            )}
          </Form.Item>
          <Form.Item label="备注">
            {getFieldDecorator('Remarks', {
              initialValue: Remarks || '',
              rules: [
                {
                  message: '请输入备注信息!'
                }
              ]
            })(<TextArea placeholder="请输入备注信息" autosize={{ minRows: 6, maxRows: 12 }} maxLength={600} />)}
          </Form.Item>
          <Card type="inner" title="章节">
            <Form.Item label="教材版本">
              {getFieldDecorator('BookVersionId', {
                initialValue: (BookVersionId && { key: BookVersionId }) || undefined,
                rules: [{ required: true, message: '请选择教材版本!' }]
              })(
                <Select labelInValue onChange={val => this.dropDown(val, '1')}>
                  {BookVersionList &&
                    BookVersionList.map(item => {
                      return (
                        <Option value={item.BookVersionId} key={'BookVersion' + item.BookVersionId}>
                          {item.BookVersionName}
                        </Option>
                      );
                    })}
                </Select>
              )}
            </Form.Item>
            <Form.Item label="选择课本">
              {getFieldDecorator('CourseMappingId', {
                initialValue: (CourseMappingId && { key: CourseMappingId }) || undefined,
                rules: [{ required: true, message: '请选择课本!' }]
              })(
                <Select labelInValue onChange={val => this.dropDown(val, '2')}>
                  {CourseMappingList &&
                    CourseMappingList.map(item => {
                      return (
                        <Option value={item.CourseMappingId} key={'CourseMapping' + item.CourseMappingId}>
                          {item.CourseMappingName}
                        </Option>
                      );
                    })}
                </Select>
              )}
            </Form.Item>
            <Form.Item label="所在章节">
              {getFieldDecorator('SectionId', {
                initialValue: (SectionId && { key: SectionId }) || undefined,
                rules: [{ required: true, message: '请选择章节!' }]
              })(
                <Select labelInValue onChange={val => this.dropDown(val, '3')}>
                  {ChapterSectionList &&
                    ChapterSectionList.map(item => {
                      return (
                        <Option value={item.NodeId} key={'ChapterSection' + item.NodeId}>
                          {item.NodeName}
                        </Option>
                      );
                    })}
                </Select>
              )}
            </Form.Item>
          </Card>

          <Card type="inner" title="内容" extra={'以下方式二选一'} className={styles.margintop}>
            <p className={styles.red}>方式一</p>
            <Form.Item label="微课视频" className={styles.margintop}>
              {getFieldDecorator('video', {
                initialValue: video.file || ''
              })(
                <Upload
                  action={SYSTEM_CONFIG.upload.uploadUrl}
                  fileList={fileList}
                  onChange={this.handleChange}
                  onRemove={this.onRemoveVideo}
                  onPreview={this.onPreview}
                  name="file"
                  data={this.getFileUploadData}
                  withCredentials={true}
                >
                  <Button>
                    <Icon type="upload" /> 上传文件
                  </Button>
                </Upload>
              )}
            </Form.Item>
            <p className={styles.red}>支持扩展名：.mp4；文件大小：500M</p>
            <p className={styles.red}>方式二</p>
            <p>不上传文件，直接点击保存后在微课管理页面点击录制</p>
          </Card>
          <Form.Item className={styles.butbox}>
            <Button type="primary" htmlType="submit">
              保存
            </Button>
            <Button style={{ marginLeft: 20 }} onClick={this.handleReset}>
              返回列表
            </Button>
          </Form.Item>
        </Form>
      </div>
    );
  }
}

export default connect()(Form.create()(SmallLessonEdit));
