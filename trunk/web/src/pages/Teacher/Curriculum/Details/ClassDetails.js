import React, { Component } from "react";
import style from "./ClassDetails.less";
import { Link } from "dva/router";
import { Table, Row, Col, Typography, List } from "antd";
import { connect } from "dva";
const { Title } = Typography;
@connect(({ curriculumModel, commonModel, classScheduleModel }) => ({
	curriculumModel,
	commonModel,
	classScheduleModel,
	CurUserSystem: commonModel.CurUserSystem,
	TeaBasePath: commonModel.TeaBasePath
}))
class ClassDetails extends Component {
	constructor(props) {
		super(props);
		this.state = {
			classroom: [],
			content: [],
			subjectName: "",
			beginTime: "",
			teacherName: "",
			gradeName: "",
			courseName: "",
			statusFlag: ""
		};
	}
	componentDidMount() {
		this.tablecontent();
	}

	/**
	 * 获取列表
	 */
	tablecontent = () => {
		const { dispatch } = this.props;
		const Content = [];
		dispatch({
			type: "curriculumModel/GetSubjectInfoForShow",
			payload: {
				SubjectId: this.props.location.query.SubjectId
			}
		}).then((result) => {
			result.ExamSetList.map((i, index) => {
				return Content.push({
					key: index,
					ExamSetName: i.ExamSetName,
					ExamSetTypeId: i.ExamSetTypeId,
					AnswerNumber: i.AnswerNumber,
					ExamSetId: i.ExamSetId
				});
			});
			let Statu = "";
			switch (result.StatusFlag) {
				case 0:
					Statu = "删除";
					break;
				case 1:
					Statu = "待上架";
					break;
				case 2:
					Statu = "待报名";
					break;
				case 3:
					Statu = "报名中";
					break;
				case 4:
					Statu = "待上课";
					break;
				case 5:
					Statu = "已结课";
					break;
				default:
					Statu = "上课中";
					break;
			}

			this.setState({
				subjectName: result.SubjectName,
				beginTime: result.BeginTime,
				teacherName: result.TeacherName,
				gradeName: result.GradeName,
				courseName: result.CourseName,
				statusFlag: Statu,
				content: Content,
				classroom: result.SubjectRooms,
				registerTotalCount: result.RegisterTotalCount,
				isShowNumber: result.IsShowNumber,
				subjectId: result.SubjectId
			});
		});
	};

	render() {
		const { TeaBasePath, location } = this.props;
		const {
			subjectName,
			beginTime,
			teacherName,
			gradeName,
			courseName,
			statusFlag,
			classroom,
			content,
			registerTotalCount,
			isShowNumber,
			subjectId
		} = this.state;
		const columns = [
			{
				title: "作业名称",
				dataIndex: "ExamSetName",
				key: "ExamSetName"
			},
			{
				title: "作业属性",
				dataIndex: "ExamSetTypeId",
				key: "ExamSetTypeId",
				render: (model) => {
					switch (model) {
						case 1:
							return "课前作业";
						case 2:
							return "随堂练习";
						default:
							return "课后作业";
					}
				}
			},
			{
				title: "作答数",
				dataIndex: "AnswerNumber",
				key: "AnswerNumber"
			},
			{
				title: "详情",
				dataIndex: "details",
				key: "details",
				render: (model, record) => (
					<Link
						to={`${TeaBasePath}/Curriculum/Work/Statistics?ExamSetId=${record.ExamSetId}`}
					>
						<span className={style.checkmore}>查看详情</span>
					</Link>
				)
			}
		];
		return (
			<div className={style.main}>
				<div className={style.classmsg}>
					<Title level={4}>课堂信息</Title>
					<Row>
						<Col span={4}>
							课堂名称： <span>{subjectName}</span>
						</Col>
						<Col span={4}>
							课程时间： <span>{beginTime}</span>
						</Col>
						<Col span={4}>
							主讲老师： <span>{teacherName}</span>
						</Col>
					</Row>
					<Row >
						<Col span={4}>
							年级： <span>{gradeName}</span>
						</Col>
						<Col span={4}>
							学科学段： <span>{courseName}</span>
						</Col>
						<Col span={4}>
							课堂状态： <span>{statusFlag}</span>
						</Col>
					</Row>
					<Row >
						<Col span={4}>
							报名人数： <span>{registerTotalCount}</span>
							{isShowNumber && (
								<Link
									className={style.checkmore}
									to={`${TeaBasePath}/Curriculum/Details/Student?TypeEnum=0&SubjectId=${subjectId}`}
								>
									查看详情
								</Link>
							)}
						</Col>
					</Row>
				</div>

				<div className={style.workmsg}>					
                    <List                        
                        header={<Title level={4}>助教信息</Title>}                                                
                        dataSource={classroom}
                        renderItem={item => (
                            <List.Item>
                                <div className={style.lessonnum}>
                                    助教老师： <span>{item.TeacherName}</span>
                                </div>
                                <div>
                                    学生成员： <span>{item.RegisterNumber}</span>
                                    {item.IsShowRoomNumber && (
                                        <Link
                                            className={style.checkmore}
                                            to={`${TeaBasePath}/Curriculum/Details/Student?TypeEnum=0&SubjectId=${subjectId}&SubjectRoomId=${item.SubjectRoomId}`}
                                        >
                                            查看详情
                                        </Link>
                                    )}
                                </div>
                            </List.Item>
                        )}
                    />                    					
				</div>
				<div>		
					<Title level={4}>作业信息</Title>
					<Table columns={columns} dataSource={content} pagination={false} />
				</div>
			</div>
		);
	}
}
export default ClassDetails;
