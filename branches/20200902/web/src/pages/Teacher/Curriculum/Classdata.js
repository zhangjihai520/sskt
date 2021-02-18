import React, { Component, Fragment } from 'react';
import style from './Classdata.less';
import ListPagination from 'components/ListPagination';
import { Table, Typography, Button, Input, Select, Divider, Pagination, Modal, Upload, message, Icon, } from 'antd';
import { connect } from 'dva';
const { Title } = Typography;
const Option = Select.Option;
let fileName = "";
let Value = []
@connect(({ curriculumModel }) => ({
  curriculumModel
}))

class WorkStatistics extends Component {
  constructor(props) {
    super(props);
    this.state = {
      inputVal: "",
      content: [],
      typeSelect: "",
      PageIndex: 1,
      visible: false,
      Count: 0,
      fileName: "",
      filePath: "",
      subjectFileIdList: [],
      disabledState: true,
      delvisible: false,
      fileList: [],
      signdelvisible: false,
      PageSize: 25,
      warn: "",
      checkname: ""
    }
  }
  componentDidMount() {
    this.tablecontent()
  }
  onChangeUserName = (e) => {
    this.setState({
      inputVal: e.target.value
    }, () => {
      if (this.stateinputVal != "") {
        this.setState({
          warn: ""
        })
      }
    })
  }
  checkUserName = (e) => {
    this.setState({
      checkname: e.target.value
    })
  }
  submit = () => {
    this.tablecontent()
  }
  delect = () => {
    this.setState({
      delvisible: true,
    })
  }
  // 上传弹框
  updata = () => {
    this.setState({
      visible: true
    })
  }
  onChange = (current) => {
    this.setState({
      PageIndex: current.PageIndex,
      PageSize: current.PageSize
    }, () => {
      this.tablecontent();
    });
  }
  //类型检索下拉框
  typeChange = (value) => {
    this.setState({
      typeSelect: value
    })
  }
  //确定上传
  handleOk = () => {
    const { dispatch, location } = this.props;
    const { filePath, inputVal, fileList } = this.state;
    if (inputVal == "") {
      this.setState({
        warn: <p className={style.warn}>资料名称不能为空</p>
      })
    } else if (fileList.length == 0) {
      this.setState({
        warn: <p className={style.warn}>资料不能为空</p>
      })
    } else {
      dispatch({
        type: "curriculumModel/SaveSubjectFile",
        payload: {
          "SubjectId": location.query.SubjectId,//课程ID
          "FileName": inputVal + "." + fileName,
          "FilePath": filePath
        }
      }).then(result => {
        if (result === 1) {
          message.success(`保存成功`);
          this.setState({
            warn: "",
            visible: false,
            checkname: "",
            inputVal: "",
            fileList: [],
            subjectFileIdList: []
          }, () => {
            this.tablecontent()
          })
        } else {
          message.error(`保存失败`);
        }

      })
    }
  }
  //取消上传
  handleCancel = () => {
    this.setState({
      visible: false
    })
  }
  /**
   * 获取列表数据
   */
  tablecontent = () => {
    let Content = []
    const { typeSelect, inputVal, PageIndex, PageSize, checkname } = this.state
    const { dispatch, location } = this.props;
    dispatch({
      type: "curriculumModel/GetSubjectFileList",
      payload: {
        "SubjectId": location.query.SubjectId,//课程ID
        "FileType": typeSelect,//资料类型 传空位全部
        "FileName": checkname,//资料名称
        "PageSize": PageSize,//传0为全部
        "PageIndex": PageIndex,//从1开始
      }
    }).then(result => {
      result.FileList.map((item) => {
        Content.push({
          key: item.FileId,
          FileId: item.FileId,
          FileName: item.FileName,
          FileType: item.FileType,
          FilePath: item.FilePath,//下载地址
          UpdateDateTime: item.UpdateDateTime,
        })
      })

      this.setState({
        Count: result.Count,
        content: Content
      })
    })
  }
  //上传
  handleChange = (info) => {
    const fileSize = info.file.size
    if (fileSize / 1024 / 1024 > 50) {
      message.error("文件不得大于50M")
    } else {
      if (info.file.status === 'uploading') {
        this.setState({ fileList: info.fileList })
      }
      if (info.file.status === 'removed') {
        this.setState({ fileList: info.fileList })
      }
      if (info.file.status === 'done') {
        message.success(`${info.file.name} 上传成功`);
        this.setState({
          fileName: info.fileList[0].response.ReturnEntity.FileName,
          filePath: info.fileList[0].response.ReturnEntity.FilePath,
        }, () => {
          let str = info.fileList[0].name
          fileName = str.substring(str.lastIndexOf(".") + 1)
        });
        this.setState({ fileList: info.fileList, }, () => {
          if (this.state.fileList.length > 0) {
            this.setState({
              warn: ""
            })
          }
        })      
      } 
      if (info.file.status === 'error') {
        message.error(`${info.file.name} 上传失败.`);
      }
    }
  }
  //文件上传之前的操作
  beforeUpload = (file,fileList) => {    
    const { name } = file;
    const division =  name.split('.');
    const suffix = division[division.length - 1].toLowerCase();
    if(
      suffix === 'doc' || 
      suffix === 'docx' ||
      suffix === 'xls' ||
      suffix === 'xlsx' ||
      suffix === 'ppt' ||
      suffix === 'pptx' ||
      suffix === 'pdf' ||
      suffix === 'txt'
    ){ 
      return true;
    }else{
      message.error(`不支持上传该类型（${suffix}）文件`);
      return false;
    }    
  }
  changerow = (selectedRowKeys, selectedRows) => {
    let SubjectFileIdList = [];
    selectedRows.map((item) => {
      SubjectFileIdList.push(item.FileId)
    })
    if (selectedRowKeys.length === 0) {
      this.setState({
        disabledState: true,
        subjectFileIdList: SubjectFileIdList
      })
    } else {
      this.setState({
        disabledState: false,
        subjectFileIdList: SubjectFileIdList
      })
    }

  }
  //删除操接口
  delectOperation = (val) => {
    const { dispatch } = this.props;
    dispatch({
      type: "curriculumModel/DeleteExamSetOrFile",
      payload: ({
        "SubjectFileIdList": val
      })
    }).then(result => {
      if (result === 1) {
        message.success('删除成功');
        this.setState({
          signdelvisible: false,
        }, () => {

          this.tablecontent();
        })
      } else {
        message.error('删除失败');
      }
    })
  }
  //单个删除
  signdelect = (value) => {
    Value = []
    Value.push(value)
    this.setState({
      signdelvisible: true,
    })
  }
  signdelhandleOk = () => {
    this.setState({
      delvisible: false,
    }, () => {
      this.delectOperation(Value)
    })
  }
  signdelhandleCancel = () => {
    this.setState({
      signdelvisible: false,
    })
  }
  //删除弹框
  delhandleOk = () => {
    const { subjectFileIdList } = this.state
    this.setState({
      delvisible: false,
    }, () => {
      this.delectOperation(subjectFileIdList);
    })
  }
  //取消删除
  delhandleCancel = () => {
    this.setState({
      delvisible: false,
    })
  }

  render() {
    const { Count, PageIndex, content, visible, disabledState, signdelvisible, delvisible, fileList, inputVal, warn, checkname } = this.state
    const columns = [{
      title: '资料名称', dataIndex: 'FileName', key: 'FileName',
    }, {
      title: '文件类型', dataIndex: 'FileType', key: 'FileType',
    }, {
      title: '更新时间', dataIndex: 'UpdateDateTime', key: 'UpdateDateTime',
    }, {
      title: '操作', dataIndex: 'operation', key: 'operation',
      render: (model, record) => {
        return (
          <div>
            <a href={record.FilePath} download="" >下载</a>
            <Divider type="vertical" />
            <a href="javascript:;" className="c-red" onClick={() => this.signdelect(record.FileId)}>删除</a>
          </div>
        )
      }
    },]
    const rowSelection = {
      onChange: this.changerow,
      selectedRowKeys: this.state.subjectFileIdList
    };
    const uploadButton = (
      <Button>
        <Icon type="upload" /> 上传文件
      </Button>
    )
    return (
      <div className={style.main}>
        <div className={style.classmsg}>
          <Title level={4}>课件资料</Title>
          <span className={style.cityList}>文件类型: </span>
          <Select defaultValue="" style={{ width: 180 }} onChange={this.typeChange}>
            <Option value="">请选择</Option>
            <Option value={['doc', 'docx']}>Word</Option>
            <Option value={['xls', 'xlsx']}>Excel</Option>
            <Option value={['ppt', 'pptx']}>PPT</Option>
            <Option value={['pdf']}>PDF</Option>
            <Option value={['txt']}>TXT</Option>

          </Select>
          <Input onChange={this.checkUserName} placeholder=" 请输入资料名称" value={checkname} className={style.input} />
          <Button type="primary" onClick={this.submit}>查询</Button>
          <Button onClick={this.updata}>上传</Button>
          <Button onClick={this.delect} disabled={disabledState}>批量删除</Button>
        </div>
        <Table
          rowSelection={rowSelection}
          columns={columns}
          dataSource={content}
          pagination={false}
        />

        <div className={style.pag}>
          <ListPagination
            total={Count}
            pageIndex={PageIndex}
            onChange={this.onChange}
          />
        </div>
        <Modal
          title="上传文件"
          visible={visible}
          onOk={this.handleOk}
          onCancel={this.handleCancel}
        >
          <div>资料名称：<Input onChange={this.onChangeUserName} value={inputVal} placeholder=" 请输入资料名称" className={style.dataInput} /></div>
          <div>附件上传： 
            <Upload
              accept=".doc,.docx,.xls,.xlsx,.ppt,.pptx,.pdf,.txt"
              action="/api/Common/UploadFile"
              fileList={fileList}
              onChange={this.handleChange}
              beforeUpload={this.beforeUpload}
            >
              {fileList.length >= 1 ? null : uploadButton}
            </Upload>
            {warn}
          </div>
        </Modal>
        <Modal
          title="删除"
          visible={delvisible}
          onOk={this.delhandleOk}
          onCancel={this.delhandleCancel}
        >
          <p>是否确定批量删除</p>
        </Modal>
        <Modal
          title="删除"
          visible={signdelvisible}
          onOk={this.signdelhandleOk}
          onCancel={this.signdelhandleCancel}
        >
          <p>是否确定删除此项</p>
        </Modal>
      </div>
    );
  }
}
export default WorkStatistics;