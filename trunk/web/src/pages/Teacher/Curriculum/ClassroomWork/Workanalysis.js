import React, { Component } from 'react';
import styles from './Workanalysis.less';
import { connect } from 'dva';
import { Table, Spin, Button, Icon, Card } from 'antd';
import echarts from 'echarts';
import $ from 'jquery';


@connect(({ loading }) => ({
    dataLoading: loading.effects['curriculumModel/GetEvaluationResult'],
}))
export default class WorkStatistics extends Component {
    constructor(props) {
        super(props);
        this.listBoxDom = React.createRef();
        this.state = {
            showAnalysisArr: [],
        }
    }
    componentWillMount() {
        this.queryData();
    }
    //获取数据方法
    queryData = () => {
        const { dispatch, location: { query: { ExamSetId = '', StudentId = '' } } } = this.props;
        dispatch({
            type: 'curriculumModel/GetEvaluationResult',
            payload: {
                ExamSetId,
                StudentId
            }
        }).then((res) => {
            const { ExamSetName, QuestionCount, CorrectCount, KnowledgePointScores = [], KnowledgePointMasterAnalysis = [], QuestionResult = [] } = res;
            const ChartData = KnowledgePointScores.map((item) => {
                return {
                    name: item.KnowledgePointName,
                    value: item.Score
                }
            });
            this.setState({
                ExamSetName,
                ChartData,
                CorrectCount,
                QuestionCount,
                TableData: KnowledgePointMasterAnalysis,
                AnswerList: QuestionResult,
            }, () => {
                this.initChart();
            });
        });

        dispatch({
            type: "curriculumModel/GetExamSetPreview",
            payload: ({
                ExamSetId,
            })
        }).then(result => {
            this.setState({
                TopicAnalysisList: result.Questions,
            })
        });
    }
    //初始化图表
    initChart = () => {
        const { ChartData = [] } = this.state;
        const myChart = echarts.init(document.getElementById('pieChart'));
        myChart.setOption({
            title: {
                text: '知识点占比',
                left: 'center',
                top: 10,
                textStyle: {
                    fontWeight: 100,
                    fontSize: 15,
                }
            },
            tooltip: {
                trigger: 'item',
                formatter: "{b} : {d}% "
            },
            calculable: true,
            series: [
                {
                    name: '',
                    type: 'pie',
                    radius: ['50%', '70%'],
                    data: ChartData,
                }
            ]
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
    //选中题号
    selectionNumber = (index) => {
        const currentIndexDom = this.listBoxDom.current.children[index];
        if (currentIndexDom) {
            const top = currentIndexDom.offsetTop;
            $("html,body").animate({ scrollTop: top }, 600, function () {
                currentIndexDom.style.background = '#e6f6ff';
                setTimeout(() => {
                    currentIndexDom.style.background = '#fff';
                }, 1000);
            });
        }

    }
    render() {
        const { ExamSetName = '', CorrectCount = 0, QuestionCount = 0, TableData = [], TopicAnalysisList = [], AnswerList = [], showAnalysisArr = [] } = this.state;
        const { dataLoading } = this.props;
        const columns = [
            {
                title: '掌握分析', dataIndex: 'MasterAnalysis', key: 'MasterAnalysis', render: (model) => {
                    switch (model) {
                        case 1:
                            return '已掌握';
                        case 2:
                            return '部分掌握';
                        default:
                            return '未掌握';
                    }
                }
            },
            { title: '二级知识点', dataIndex: 'ParentKnowledgePointName', key: 'ParentKnowledgePointName' },
            { title: '三级知识点', dataIndex: 'KnowledgePointName', key: 'KnowledgePointName' },
            {
                title: '难度系数', dataIndex: 'KnowledgePointLevel', key: 'KnowledgePointLevel', render: (model) => {
                    switch (model) {
                        case 1:
                            return '容易';
                        case 2:
                            return '较易';
                        case 3:
                            return '一般';
                        case 4:
                            return '较难';
                        default:
                            return '困难';
                    }
                }
            },
            { title: '题目数', dataIndex: 'NumberOfQuestion', key: 'NumberOfQuestion' }
        ]
        return (
            <div className={styles.root}>
                <div className={styles.coreBox}>
                    <Spin tip="Loading..." spinning={dataLoading}>
                        <h5 className={styles.title}>{ExamSetName}</h5>
                        <div className={styles.chartBox}>
                            <div id="pieChart" className={styles.pieChart}></div>
                            <div className={styles.emphasize}>
                                他的成绩<span> {CorrectCount} </span> / {QuestionCount}
                            </div>
                        </div>
                        <div className={styles.tableBox}>
                            <Table
                                bordered
                                rowKey='KnowledgePointName'
                                title={() => '知识点掌握分析'}
                                columns={columns}
                                dataSource={TableData}
                                pagination={false}
                            />
                        </div>
                        <div className={styles.analysisBox}>
                            <p className={styles.answerTips}>答题记录</p>
                            <div ref={this.listBoxDom}>
                                {TopicAnalysisList.map((item, i) => {
                                    return (
                                        <div className={styles.answertit} key={item.QuestionId}>
                                            <div dangerouslySetInnerHTML={{ __html: `${i + 1}. ${item.QuestionContent}` }}></div>
                                            <ul className={styles.optionList}>
                                                {item.Options.map((obj) => {
                                                    return (
                                                        <li key={obj.QuestionOptionId} dangerouslySetInnerHTML={{ __html: `${obj.QuestionOptionId}：${obj.QuestionOptionText}` }} ></li>
                                                    )
                                                })}
                                            </ul>
                                            <div className={styles.chooseanswer}>
                                                <p className={styles.analysisInfo}>
                                                    该生选择：
                                                    {AnswerList.map((obj) => {
                                                        if (obj.QuestionId === item.QuestionId) {
                                                            return (
                                                                <span key={obj.QuestionId} className={`${obj.StudentAnswer !== item.Answer ? 'c-red' : 'c-green'}`}>{obj.StudentAnswer}</span>
                                                            )
                                                        }
                                                    })}
                                                    ，正确答案：
                                                   <span className="c-green">{item.Answer}</span>
                                                </p>
                                                <Button
                                                    type="primary"
                                                    className={styles.showanalysis}
                                                    onClick={() => this.showAnalysis(item.QuestionId)}
                                                >
                                                    {showAnalysisArr.includes(item.QuestionId) ? '关闭解析' : '展开解析'}
                                                    {showAnalysisArr.includes(item.QuestionId) ? <Icon type="up" /> : <Icon type="down" />}
                                                </Button>
                                            </div>
                                            {
                                                showAnalysisArr.includes(item.QuestionId) &&
                                                <p className={styles.analysitext} dangerouslySetInnerHTML={{ __html: item.Analysis }}></p>
                                            }
                                        </div>
                                    )
                                })}
                            </div>
                        </div>
                    </Spin>
                </div>
                <div className={styles.rightBox}>
                    <Card
                        size="small"
                        title="答题卡"
                    >
                        <div className={styles.cardBox}>
                            {AnswerList.map((item, i) => {
                                return (
                                    <p
                                        key={i}
                                        onClick={() => this.selectionNumber(i)}
                                        className={`${styles.cardItem} ${item.IsCorrect ? styles.bgRed : styles.bgGreen}`}
                                    >
                                        {i + 1}
                                    </p>
                                )
                            })}
                        </div>
                    </Card>
                </div>
            </div>
        )
    }
}
