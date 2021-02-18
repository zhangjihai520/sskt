import React, { Component } from "react";
import { message, Modal, Spin } from "antd";
import { connect } from "dva";
import router from "umi/router";
import styles from "./index.less";
import TopicList from "../../../components/TopicList";
import NumberKeyboard from "../../../components/NumberKeyboard";
const confirm = Modal.confirm;

@connect(({ loading, commonModel }) => ({
	listLoading: loading.effects["curriculumModel/GetExamSetPreview"],
	studentBasePath: commonModel.StudentBasePath
}))
export default class ClassPractice extends Component {
	state = {};
	componentDidMount() {
		const {
			dispatch,
			location: {
				query: { ExamSetId = "" }
			}
		} = this.props;
		dispatch({
			type: "curriculumModel/GetExamSetPreview",
			payload: {
				ExamSetId
			}
		}).then((res) => {
			this.setState({
				topicListData: res
			});
		});
	}
	//题目列表选中
	topicListChange = (answerList, number) => {
		this.setState({
			answerList,
			serialNumber: number
		});
	};
	//根据题目序号切换
	changeNumber = (num) => {
		this.setState({
			serialNumber: num
		});
	};
	//点击提交按钮
	submit = () => {
		const self = this;
		const { answerList = [], topicListData = {} } = this.state;
		//判断是不是每题都做了
		if (answerList.length === topicListData.Questions.length) {
			self.submitFn();
		} else {
			confirm({
				title: "还有未作答的题目",
				content: "",
				okText: "坚持提交",
				cancelText: "继续作答",
				onOk() {
					self.submitFn();
				}
			});
		}
	};
	//提交接口方法
	submitFn = () => {
		const {
			dispatch,
			studentBasePath,
			location: {
				query: { ExamSetId = "" }
			}
		} = this.props;
		const { answerList = [] } = this.state;
		dispatch({
			type: "curriculumModel/SubmitExamSet",
			payload: {
				ExamSetId: ExamSetId,
				QuestionList: answerList
			}
		}).then((res) => {
			if (res === 1) {
				const {
					location: {
						query: { ExamSetId = "" }
					}
				} = this.props;
				message.success("提交成功", 1.5).then(() => {
					router.push(
						`${studentBasePath}/CourseManager/ViewAssessment?ExamSetId=${ExamSetId}`
					);
				});
			} else {
				message.success("提交失败");
			}
		});
	};
	render() {
		const { topicListData = {}, answerList = [], serialNumber } = this.state;
		const { listLoading } = this.props;
		const numLength = topicListData.Questions && topicListData.Questions.length;
		return (
			<div className={`${styles.root} f-clear`}>
				<div className={styles.leftBox}>
					<Spin tip="Loading..." spinning={listLoading}>
						<TopicList
							topicListData={topicListData}
							answerList={answerList}
							onChange={this.topicListChange}
							currentIndex={serialNumber}
						/>
					</Spin>
				</div>
				<div className={styles.rightBox}>
					<NumberKeyboard
						numLength={numLength}
						currentNum={serialNumber}
						onChange={this.changeNumber}
						onSubmit={this.submit}
					/>
				</div>
			</div>
		);
	}
}
