import React, { Component } from 'react';
import style from './WorkStatistics.less';
import { Link } from 'dva/router';
import ListPagination from 'components/ListPagination';
import { Table, Typography, Button, Input,  Row, Col, Icon } from 'antd';
import { connect } from 'dva';
import classnames from 'classnames';
const { Title } = Typography;
@connect(({ curriculumModel, commonModel }) => ({
  curriculumModel,
  TeaBasePath: commonModel.TeaBasePath,
}))

class WorkStatistics extends Component {
  constructor(props) {
    super(props);
    this.state = {
      StudentCount:0,
      AnswerCount:0,
      UnAnswerCount:0,
      QuestionResults: [],
      content: [],
      showAnalysisArr: [],
      inputval: "",
      Count: 0,
      PageIndex: 1,
      PageSize: 25
    }
  }
  componentDidMount() {
    this.tablecontent();
    this.answerStatisticsContent();
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
  answerStatisticsContent = () => {
    const { dispatch, location } = this.props;
    var QuestionResults = [];
    dispatch({
      type: "curriculumModel/GetAnswersStatistics",
      payload: {
        "ExamSetId": location.query.ExamSetId //作业ID
      }
    }).then(result => {
      if(result&&result.QuestionResults)
      {
        result.QuestionResults.map((item, index) => {
          QuestionResults.push({
            key: index,
            Order: item.Order,
            CorrectNum: item.CorrectNum,
            ErrorNum: item.ErrorNum,
            QuestionId: item.QuestionId,
            QuestionContent: item.QuestionContent,
            Options:item.Options,
            Answer:item.Answer,
            Analysis:item.Analysis
          })
        })
        this.setState({
          StudentCount:result.StudentCount,
          AnswerCount:result.AnswerCount,
          UnAnswerCount:result.UnAnswerCount,
          QuestionResults: QuestionResults
        })
      }
    })
  }
  //展开解析
  showAnalysis = (id) => {
      const { showAnalysisArr } = this.state;
      if (!showAnalysisArr.includes(id)) {
          showAnalysisArr.push(id);
      } else {
          const index = showAnalysisArr.indexOf(id);
          showAnalysisArr.splice(index, 1);
      }
      this.setState({
          showAnalysisArr,
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
    const { content, Count, PageIndex, StudentCount, AnswerCount, UnAnswerCount, QuestionResults,showAnalysisArr = []  } = this.state

    const questionDetail= (item) =>{
      return (
        <div className={style.answertit} key={item.QuestionId}>
          <div dangerouslySetInnerHTML={{ __html: item.QuestionContent }}></div>
          <ul className={style.optionList}>
            {item.Options.map((obj) => {
              return (<li key={obj.QuestionOptionId} dangerouslySetInnerHTML={{ __html: `${obj.QuestionOptionId}：${obj.QuestionOptionText}`}} ></li>)
            })}
          </ul>
          <div className={style.chooseanswer}>
            <p className={style.analysisInfo}>正确答案：<span className="c-green">{item.Answer}</span></p>
            <Button type="primary" className={style.showanalysis} onClick={() => this.showAnalysis(item.QuestionId)}>
              {showAnalysisArr.includes(item.QuestionId) ? '关闭解析' : '展开解析'}
              {showAnalysisArr.includes(item.QuestionId) ? <Icon type="up" /> : <Icon type="down" />}
            </Button>
          </div>
          {showAnalysisArr.includes(item.QuestionId) && <p className={style.analysitext}  dangerouslySetInnerHTML={{ __html: item.Analysis}} ></p>}
        </div>
      )
    }

    const GetOfflineTitle = () => {
      return (
        <Row className={style.lessonnum}>
          <Col className={classnames(style.item, style.blue, style.first)} span={4}>
            <div className={style.desc}>
              <div className={style.descTitle}>上课人数</div>
              <div className={style.descContent}></div>
            </div>
            <div className={style.rightNum}>{StudentCount}</div>
          </Col>
          <Col className={classnames(style.item, style.green)} span={4}>
            <div className={style.desc}>
              <div className={style.descTitle}>作答人数</div>
              <div className={style.descContent}></div>
            </div>
            <div className={style.rightNum}>{AnswerCount}</div>
          </Col>
          <Col className={classnames(style.item, style.red)} span={4}>
            <div className={style.desc}>
              <div className={style.descTitle}>未答人数</div>
              <div className={style.descContent}></div>
            </div>
            <div className={style.rightNum}>{UnAnswerCount}</div>
          </Col>
        </Row>
      );
    };
    const topColumns = [{
      title: '题号', dataIndex: 'Order', key: 'Order',
    }, {
      title: '答对人数', dataIndex: 'CorrectNum', key: 'CorrectNum',
    }, {
      title: '答错人数', dataIndex: 'ErrorNum', key: 'ErrorNum',
    }];

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
        <div>
          <Title level={4}>作答情况</Title>
          <GetOfflineTitle />
          <Table
            columns={topColumns}
            dataSource={QuestionResults}
            pagination={false}
            expandedRowRender={record => questionDetail(record)}
          />
        </div>
        <div className={style.classmsg}>
          <Title level={4}>学生明细</Title>
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