import React, { Component } from 'react';
import PropTypes from 'prop-types';
import styles from './index.less';
import $ from 'jquery';
/**
 * 题目列表组件 
 */

export default class TopicList extends Component {
    constructor(props) {
        super(props);
        this.listBoxDom = React.createRef();
        this.state = {
            serialNumber: 0,
        }
    }    
    componentDidMount(){
        const {currentIndex, showType} = this.props;
        //滑到指定序号题目
        if(currentIndex && showType === 'readOnly'){
            $("html,body").animate({scrollTop:200 * currentIndex  - 45 },600);
        }
    }
    componentWillReceiveProps(nextProps) {
        const { currentIndex } = this.props;
        const { serialNumber } = this.state;
        //题目序号改变时滚动条滑到指定题目        
        if (nextProps.currentIndex != currentIndex && nextProps.currentIndex != serialNumber) {
            this.designatedLocation(nextProps.currentIndex);
        }
    }
    //滚动到索引指定题目
    designatedLocation = (index) => {
        const currentIndexDom = this.listBoxDom.current.children[index - 1];        
        const top = currentIndexDom.offsetTop - 65;
        $("html,body").animate({scrollTop:top},600);
    }
    //选择选项
    selectionOption = (QuestionId, Answer, index) => {
        let number = index + 1;
        let { answerList = [], onChange } = this.props;
        let mark = true;
        answerList.forEach((obj, i) => {
            if (obj.QuestionId === QuestionId) {
                obj.Answer = Answer;
                mark = false;
            }
        });
        if (mark) {
            answerList.unshift({
                QuestionId,
                Answer
            });
        }        
        //回调
        if (onChange) onChange(answerList, number);
    }
    //选项是否选中样式
    isOptionsStyle = (QuestionId, currentOption) => {
        const { answerList = [] } = this.props;
        const mark = answerList.some(obj => {
            if (obj.QuestionId === QuestionId) {
                if (obj.Answer === currentOption) {
                    return true;
                }
            }
            return false;
        });
        return mark;
    }
    render() {        
        const {            
            topicListData = {},
            showType = 'default'  //默认类型： default，只读: readOnly
        } = this.props;
        let statusArr = '';
        //判断状态
        if (topicListData.StatusFlag == 1) {
            statusArr = '待上架';
        } else {
            statusArr = '已上架';
        }
        return (
            <div className={styles.root}>
                <div className={styles.titleBar}>
                    <span className={styles.bigTitle}>{topicListData.ExamSetName}</span>
                    <span className={styles.state}>（{statusArr}）</span>
                </div>
                <div id="listBox" ref={this.listBoxDom}>
                    {
                        (topicListData.Questions || []).map((item, index) => {
                            return (
                                <div key={item.QuestionId} className={styles.listItem}>
                                    <div className={styles.upperHalf}>
                                        <div dangerouslySetInnerHTML={{__html: `${item.Order}. ${item.QuestionContent}`}}></div>
                                        <ul className={styles.answerList}>
                                            {item.Options.map((obj, i) => {
                                                return (
                                                    <li
                                                        key={obj.QuestionOptionId}
                                                        dangerouslySetInnerHTML={{ __html: `${obj.QuestionOptionId}：${obj.QuestionOptionText}`  }}
                                                    >
                                                    </li>
                                                )
                                            })}
                                        </ul>
                                    </div>
                                    {
                                        showType === 'default' &&
                                        <ul className={styles.lowerHalf}>
                                            {item.Options.map((obj, i) => {
                                                return (
                                                    <li
                                                        key={obj.QuestionOptionId}
                                                        onClick={() => this.selectionOption(item.QuestionId, obj.QuestionOptionId, index)}
                                                        className={`${this.isOptionsStyle(item.QuestionId, obj.QuestionOptionId) ? styles.liActive : ''}`}
                                                    >
                                                        {obj.QuestionOptionId}
                                                    </li>
                                                )
                                            })}
                                        </ul>
                                    }
                                </div>
                            )
                        })
                    }
                </div>
            </div>
        )
    }
}


TopicList.propTypes = {
    topicListData: PropTypes.object,    //列表数据
    showType: PropTypes.string,         //显示类型
    answerList: PropTypes.array,        //答案列表
    onChange: PropTypes.func,           //选中选项回调
    currentIndex: PropTypes.number,     //当前题名索引
}
