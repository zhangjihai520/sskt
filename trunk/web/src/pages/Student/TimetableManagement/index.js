import React, { Component } from "react";
import { Calendar, Popover, message, Tag, Spin } from "antd";
import { connect } from "dva";
import moment from "moment";
import styles from "./index.less";

@connect(({ loading }) => ({
	dateLoading: loading.effects["curriculumModel/GetUserSubjectList"]
}))
export default class TimetableManagement extends Component {
	state = {
		selectedMonth: moment().format("YYYY-MM"),
		calendarMode: "month",
		dateList: []
	};
	componentDidMount() {
		this.queryData();
	}
	//数据查询
	queryData = () => {
		const { dispatch } = this.props;
		const { selectedMonth } = this.state;
		dispatch({
			type: "curriculumModel/GetUserSubjectList",
			payload: {
				TypeEnum: 0,
				SelectedMonth: selectedMonth
			}
		}).then((res) => {
			if (res) {
				this.setState({
					dateList: res.DateList
				});
			} else {
				message.error("获取数据异常");
			}
		});
	};

	//获取对应日期的课程列表
	getListData = (value) => {
		const { dateList } = this.state;
		let currentData = dateList.find((obj, i) => {
			if (obj.BeginDate === value.format("YYYY-MM-DD")) {
				return true;
			}
			return false;
		});
		return currentData ? currentData.SubjectList : [];
	};
	//日期内容渲染
	dateCellRender = (value) => {
		const listData = this.getListData(value);
		return (
			<ul className="events">
				{listData.map((item) => {
					const content = (
						<div>
							<p>主讲老师：{item.TeacherName}</p>
							<p>上课时间：{item.BeginTime}</p>
							<p>上课地点：{item.SubjectRoomName}</p>
						</div>
					);
					return (
						<Popover
							key={item.SubjectId}
							placement="left"
							title={<h3 className={styles.courseTitle}>{item.SubjectName}</h3>}
							content={content}
						>
							<li>
								<Tag color="green">{item.SubjectName}</Tag>
							</li>
						</Popover>
					);
				})}
			</ul>
		);
	};
	//日历面板改变事件
	panelChange = (date, mode) => {
		const { selectedMonth, calendarMode } = this.state;
		//不一样的月份才重新请求接口
		if (date.format("YYYY-MM") != selectedMonth) {
			this.setState(
				{
					calendarMode: "month",
					selectedMonth: date.format("YYYY-MM")
				},
				() => {
					this.queryData();
				}
			);
		}
		//日历面包类型不一样就切换
		if (mode != calendarMode) {
			this.setState({
				calendarMode: mode
			});
		}
	};
	render() {
		const { calendarMode } = this.state;
		const { dateLoading } = this.props;
		return (
			<div className={styles.wrap}>
				<div className={styles.root}>
					<Spin tip="Loading..." spinning={dateLoading}>
						<Calendar
							mode={calendarMode}
							dateCellRender={this.dateCellRender}
							onPanelChange={this.panelChange}
						/>
					</Spin>
				</div>
			</div>
		);
	}
}
