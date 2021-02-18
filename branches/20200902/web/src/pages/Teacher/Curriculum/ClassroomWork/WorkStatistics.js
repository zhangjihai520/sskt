import React, { Component } from 'react';
import style from './WorkStatistics.less';
import { Link } from 'dva/router';
import ListPagination from 'components/ListPagination';
import { Table, Typography, Button, Input, Pagination } from 'antd';
import { connect } from 'dva';
const { Title } = Typography;
@connect(({ curriculumModel, commonModel }) => ({
  curriculumModel,
  TeaBasePath: commonModel.TeaBasePath,
}))

class WorkStatistics extends Component {
  constructor(props) {
    super(props);
    this.state = {
      content: [],
      inputval: "",
      Count: 0,
      PageIndex: 1,
      PageSize: 25
    }
  }
  componentDidMount() {
    this.tablecontent()
  }

  /**
   * 查询
   */
  submit = () => {
    this.setState({
      PageIndex: 1
    }, () => {
      this.tablecontent()
    })

  }
  /**
   * 名称检索
   */
  getValue = (e) => {
    this.setState({
      inputval: e.target.value
    })
  }
  /**
   * 翻页
   */
  onChange = (current) => {
    this.setState({
      PageIndex: current.PageIndex,
      PageSize: current.PageSize
    }, () => {
      this.tablecontent();
    })
  }

  /**
   * 获取表格数据
   */
  tablecontent = () => {
    const { dispatch, location } = this.props;
    const { inputval, PageIndex, PageSize } = this.state;
    const Content = [];
    dispatch({
      type: "curriculumModel/GetExamSetStatistList",
      payload: {
        "ExamSetId": location.query.ExamSetId, //作业ID
        "StudentName": inputval, //学生名称
        "PageSize": PageSize, //每页显示数
        "PageIndex": PageIndex //当前页数
      }
    }).then(result => {
      result.ExamSetStatistLists.map((item, index) => {
        Content.push({
          key: index,
          StudentName: item.StudentName,
          Score: item.Score,
          UpdateDateTime: item.UpdateDateTime,
          StudentId: item.StudentId
        })
      })
      this.setState({
        content: Content,
        Count: result.Count,
      })
    })
  }
  render() {
    const { TeaBasePath, location } = this.props
    const { content, Count, PageIndex } = this.state
    const columns = [{
      title: '学生名称', dataIndex: 'StudentName', key: 'StudentName',
    }, {
      title: '成绩', dataIndex: 'Score', key: 'Score',
    }, {
      title: '提交时间', dataIndex: 'UpdateDateTime', key: 'UpdateDateTime',
    }, {
      title: '答题详情', dataIndex: 'details', key: 'details',
      render: (model, record) => {
        return (
          <Link to={
            `${TeaBasePath}/Curriculum/Work/Statistics/analysis?ExamSetId=${location.query.ExamSetId}&StudentId=${record.StudentId}&SubjectId=${location.query.SubjectId}`
          }> <span>详情</span></Link >
        )
      }
    }]
    return (
      <div className={style.main}>
        <div className={style.classmsg}>
          <Title level={4}>作业统计</Title>
          <Input placeholder=" 请输入学生名称" className={style.input} onChange={this.getValue} />
          <Button type="primary" onClick={this.submit}>查询</Button>
          <Table
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
        </div>
      </div>
    )
  }
}
export default WorkStatistics;