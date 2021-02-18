import React, { Component } from "react";
import style from "./ClassStudent.less";
import ListPagination from "components/ListPagination";
import { Table, Modal, Typography, Button, Input, Rate, message } from "antd";
import { connect } from "dva";
const { Title } = Typography;
const { TextArea } = Input;
const desc = ["一星", "两星", "三星", "四星", "五星"];
let studentTitle = "";
@connect(({ curriculumModel, commonModel, classScheduleModel }) => ({
	curriculumModel,
	commonModel,
	classScheduleModel
}))
class ClassStudent extends Component {
	constructor(props) {
		super(props);
		this.state = {
			title: "",
			content: [],
			total: 0,
			visible: false,
			inputVal: "",
			inputValLeng: 0,
			tips: "",
			value: 0,
			evaluatestate: false,
			PageIndex: 1,
			PageSize: 25,
			stuname: "",
			student: 0
		};
	}
	componentDidMount() {
		if (parseInt(this.props.location.query.TypeEnum) === 0) {
			studentTitle = "报名学生";
		} else {
			studentTitle = "课堂学生";
		}
		this.setState({
			title: studentTitle
		});
		// 更改面包屑标题
		this.props.dispatch({
			type: "commonModel/updateBreadCrumbName",
			payload: studentTitle
		});
		this.tablecontent();
	}

	componentWillUnmount() {
		// 更改面包屑标题
		this.props.dispatch({
			type: "commonModel/updateBreadCrumbName",
			payload: null
		});
	}
	/**
	 * 评分
	 */
	handleChange = (value) => {
		this.setState({
			value: value
		});
	};
	getstudentName = (e) => {
		this.setState({
			stuname: e.target.value
		});
	};
	/**
	 * 点击查询按钮
	 */
	search = () => {
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
	 * 评价留言
	 */
	getValue = (e) => {
		this.setState({
			inputVal: e.target.value,
			inputValLeng: e.target.value.length
		});
	};

	/**
	 * 关闭弹框
	 */
	handleCancel = () => {
		this.setState({
			visible: false
		});
	};
	/**
	 * 翻页
	 */
	onChange = (current) => {
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
	 * 保存评价
	 */
	handleOk = () => {
		let Tips = "";
		const { inputVal, inputValLeng, value, student } = this.state;
		const { dispatch, location } = this.props;
		if (inputValLeng < 5) {
			Tips = "最少输入五个字符";
		} else if (value == 0) {
			Tips = "请给学生打分";
		} else {
			Tips = " ";
			dispatch({
				type: "curriculumModel/EvaluateStudent",
				payload: {
					SubjectId: location.query.SubjectId,
					StudentId: student,
					EvaluateLevel: value,
					EvaluateComment: inputVal
				}
			}).then((result) => {
				if (result === 1) {
					message.info("评价成功");
					this.setState(
						{
							inputVal: "",
							inputValLeng: "",
							value: 0
						},
						() => {
							this.tablecontent();
						}
					);
				} else {
					message.error("评价失败");
				}
				this.setState({
					visible: false
				});
			});
		}
		this.setState({
			tips: Tips
		});
	};
	/**
	 * 点击评价按钮
	 */
	evaluate = (value) => {
		this.setState({
			student: value,
			visible: true
		});
	};
	/**
	 * 渲染表格
	 */
	tablecontent = () => {
		const { dispatch, location } = this.props;
		const { stuname, PageIndex, PageSize } = this.state;
		let Content = [];
		dispatch({
			type: "curriculumModel/GetStudentList",
			payload: {
				SubjectRoomId: location.query.SubjectRoomId,
				TypeEnum: location.query.TypeEnum,
				SubjectId: location.query.SubjectId, //课程加密ID 同教室ID二选一必传
				StudentName: stuname, //学生名称 搜索用，可不传
				PageSize: PageSize, //页码大小
				PageIndex: PageIndex //页码 从1开始
			}
		}).then((result) => {
			result.StudentList.map((item, index) => {
				return Content.push({
					key: index,
					studentName: item.StudentName,
					schoolName: item.SchoolName,
					gradeName: item.GradeName,
					createDateTime: item.CreateDateTime,
					studentId: item.StudentId,
					HasEvaluateRecord: item.HasEvaluateRecord
				});
			});
			this.setState({
				total: result.Count,
				content: Content
			});
		});
	};

	render() {
		const { title, visible, tips, value, total, PageIndex, inputVal } = this.state;
		let columns = [
            {
                title: "学生名称",
                dataIndex: "studentName",
                key: "studentName"
            },
            {
                title: "学校",
                dataIndex: "schoolName",
                key: "schoolName"
            },
            {
                title: "年级",
                dataIndex: "gradeName",
                key: "gradeName"
            },
            {
                title: "报名时间",
                dataIndex: "createDateTime",
                key: "createDateTime"
            },
            {
                title: "课堂评价",
                dataIndex: "HasEvaluateRecord",
                key: "HasEvaluateRecord",
                render: (model, record) => {
                    if (model == 0) {
                        return <a onClick={() => this.evaluate(record.studentId)}>评价</a>;
                    } else {
                        return <span>已评价</span>;
                    }
                }
            }            
        ];
		// if (this.props.location.query.TypeEnum == 1) {
		// 	columns = [
		// 		{
		// 			title: "学生名称",
		// 			dataIndex: "studentName",
		// 			key: "studentName"
		// 		},
		// 		{
		// 			title: "学校",
		// 			dataIndex: "schoolName",
		// 			key: "schoolName"
		// 		},
		// 		{
		// 			title: "年级",
		// 			dataIndex: "gradeName",
		// 			key: "gradeName"
		// 		},
		// 		{
		// 			title: "报名时间",
		// 			dataIndex: "createDateTime",
		// 			key: "createDateTime"
		// 		},
		// 		{
		// 			title: "课堂评价",
		// 			dataIndex: "HasEvaluateRecord",
		// 			key: "HasEvaluateRecord",
		// 			render: (model, record) => {
		// 				if (model == 0) {
		// 					return <a onClick={() => this.evaluate(record.studentId)}>评价</a>;
		// 				} else {
		// 					return <span>已评价</span>;
		// 				}
		// 			}
		// 		}
		// 	];
		// } else {
		// 	columns = [
		// 		{
		// 			title: "学生名称",
		// 			dataIndex: "studentName",
		// 			key: "studentName"
		// 		},
		// 		{
		// 			title: "学校",
		// 			dataIndex: "schoolName",
		// 			key: "schoolName"
		// 		},
		// 		{
		// 			title: "年级",
		// 			dataIndex: "gradeName",
		// 			key: "gradeName"
		// 		},
		// 		{
		// 			title: "报名时间",
		// 			dataIndex: "createDateTime",
		// 			key: "createDateTime"
		// 		}
		// 	];
		// }
		return (
			<div className={style.main}>
				<div>
					<Title level={4}>{title}</Title>
					<Input
						placeholder=" 请输入学生名称"
						className={style.input}
						onChange={this.getstudentName}
					/>
					<Button type="primary" onClick={this.search}>
						查询
					</Button>
					<Table columns={columns} dataSource={this.state.content} pagination={false} />
					<div className={style.pag}>
						<ListPagination
							total={total}
							pageIndex={PageIndex}
							onChange={this.onChange}
						/>
					</div>
					<Modal
						title="学生课堂评价"
						visible={visible}
						onOk={this.handleOk}
						onCancel={this.handleCancel}
						destroyOnClose={true}
					>
						<div style={{ marginBottom: "10px" }}>
							<label className={style.score}>评分： </label>
							<Rate
								tooltips={desc}
								onChange={this.handleChange}
								defaultValue={value}
								allowClear={false}
							/>
							{value ? <span className="ant-rate-text">{desc[value - 1]}</span> : ""}
						</div>
						<div>
							<label className={style.explain}>评价详情： </label>
							<TextArea
								className={style.textArea}
								onChange={this.getValue}
								value={inputVal}
								autosize={{ minRows: 5, maxRows: 10 }}
								placeholder="请最少输入五个字符"
							/>
							<p className={style.warn}>{tips}</p>
						</div>
					</Modal>
				</div>
			</div>
		);
	}
}
export default ClassStudent;
