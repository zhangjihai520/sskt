import React, { Component, Fragment } from 'react';
import { connect } from 'dva';
import { routerRedux } from 'dva/router';
import { Button, message, Select, DatePicker, Input, Divider, Form, Row, Col, Tooltip, Modal } from 'antd';
import styles from './ClassScheduleList.less';
import StandardTable from '../../../components/StandardTable';
import moment from 'moment';
import _ from 'lodash';
import { roleEnum } from '../../../config/enum';
import router from 'umi/router';

const Option = Select.Option;
const { RangePicker } = DatePicker;
const Search = Input.Search;
const FormItem = Form.Item;
const statusShow = {
	1: '待上架',
	2: '待报名',
	3: '报名中',
	4: '待上课',
	5: '已结课',
	6: '上课中'
};

@connect(({ commonModel, classScheduleModel, loading }) => ({
	...classScheduleModel,
	GradeList: commonModel.GradeList,
	SubjectStatusList: commonModel.SubjectStatusList,
	AdminBasePath: commonModel.AdminBasePath,
	loading: loading.effects['classScheduleModel/fetch']
}))
@Form.create()
class ClassScheduleList extends Component {
	state = {
		selectedRows: [], //选择的行数
		formValues: '', //form 表单值
		UserRole: roleEnum.admin
	};
	columns = [
		{
			title: '课程名称',
			width: 150,
			dataIndex: 'SubjectName'
		},
		{
			title: '学科学段',
			dataIndex: 'CourseName'
		},
		{
			title: '教师名称',
			dataIndex: 'TeacherNameList',
			width: 300,
			render: text => _.join(text, '、')
		},
		{
			title: '年级',
			dataIndex: 'GradeName'
		},
		{
			title: '课程状态',
			dataIndex: 'StatusFlag',
			render: text => {
				return statusShow[text];
			}
		},
		{
			title: '报名截止时间',
			dataIndex: 'RegistEndTime'
		},
		{
			title: '创建时间',
			dataIndex: 'CreateDateTime'
		},
		{
			title: '操作',
			dataIndex: 'Operation',
			render: (text, list) => (
				<Fragment>
					<a
						href="javascript:;"
						onClick={() => this.enterClassInfo('edit', list.SubjectId)}
						disabled={list.StatusFlag === 6}
					>
						编辑
					</a>
					<Divider type="vertical" />
					{this.operation(list)}
					<Divider type="vertical" />
					{list.StatusFlag === 4 ? (
						<a href="javascript:;" onClick={() => this.jumpGroup(list.SubjectId)}>
							分组
						</a>
					) : (
						<Tooltip placement="bottom" title={'报名时间截止到课程开始前更改学生分组'}>
							<a href="javascript:;" className={styles.disable_color}>
								分组
							</a>
						</Tooltip>
					)}
					<Divider type="vertical" />
					<a
						href="javascript:;"
						className="c-red"
						onClick={() => this.confirmClassModal(0, list)}
						disabled={list.StatusFlag === 6}
					>
						删除
					</a>
				</Fragment>
			)
		}
	];
	/**
	 * 初始化生命周期
	 */
	componentDidMount() {
		const {
			dispatch,
			PageIndex,
			PageSize,
			GradeId,
			StatusFlag,
			BeginTimeMin,
			BeginTimeMax,
			Key,
			form: { getFieldValue },
		} = this.props;
		const { UserRole } = this.state;

		let begindt = BeginTimeMin && moment(BeginTimeMin).format('YYYY-MM-DD');
		let enddt = BeginTimeMax && moment(BeginTimeMax).format('YYYY-MM-DD');

		dispatch({
			type: 'classScheduleModel/fetch',
			payload: { 
				PageSize, 
				PageIndex,
				UserRole,
				GradeId,
				StatusFlag,
				BeginTimeMin: begindt,
				BeginTimeMax: enddt,
				Key,
			}
		});
	}
	/**
	 * 跳转分组页面
	 */
	jumpGroup = id => {
		const { AdminBasePath } = this.props;
		let url = `${AdminBasePath}/ClassSchedule/StudentGrouping?subjectId=${id}`;
		router.push(url);
	};
	/**
	 * 操作
	 */
	operation = list => {
		if (list.StatusFlag == 1) {
			return (
				<a href="javascript:;" onClick={() => this.confirmClassModal(1, list)} disabled={list.StatusFlag === 6}>
					上架
				</a>
			);
		} else {
			return (
				<a href="javascript:;" onClick={() => this.confirmClassModal(2, list)} disabled={list.StatusFlag === 6}>
					下架
				</a>
			);
		}
	};
	/**
	 * 修改课程状态
	 */
	confirmClassModal = (status, list) => {
		let txt = '';
		if (status == 0) {
			txt = '刪除';
		} else if (status == 1) {
			txt = '上架';
		} else if (status == 2) {
			txt = '下架';
		}
		Modal.confirm({
			title: `${txt}课表`,
			content: `确定${txt}该课表吗？`,
			okText: '确认',
			cancelText: '取消',
			onOk: () => this.updateStatus(status, list)
		});
	};
	/**
	 * 修改上/下/刪除状态
	 */
	updateStatus = (StatusFlag, list) => {
		const { PageSize } = this.state;
		this.props.dispatch({
			type: 'classScheduleModel/update',
			payload: { StatusFlag, SubjectId: list.SubjectId }
		});
	};
	/**
	 * 获取select 数据
	 */
	getOption = list => {
		if (!list || list.length < 1) {
			return (
				<Option key={0} value={0}>
					暂无数据
				</Option>
			);
		}

		return list.map(item => (
			<Option key={item.Id} value={item.Id}>
				{item.Name}
			</Option>
		));
	};
	/**
	 * 搜索
	 */
	handleSearch = e => {
		e.preventDefault();
		const { dispatch, form } = this.props;
		const { UserRole } = this.state;
		form.validateFields((err, fieldsValue) => {
			if (err) return;
			if (fieldsValue.BeginTimeMin && fieldsValue.BeginTimeMin.length) {
				let begindt = moment(fieldsValue.BeginTimeMin[0]).format('YYYY-MM-DD');
				let enddt = moment(fieldsValue.BeginTimeMin[1]).format('YYYY-MM-DD');
				fieldsValue.BeginTimeMin = begindt;
				fieldsValue.BeginTimeMax = enddt;
			} else {
				fieldsValue.BeginTimeMin = undefined;
				fieldsValue.BeginTimeMax = undefined;
			}

			const values = {
				...fieldsValue
			};

			this.setState({
				formValues: values
			});

			dispatch({
				type: 'classScheduleModel/fetch',
				payload: {
					...fieldsValue,
					PageIndex: 1,
					PageSize: this.props.PageSize,
					UserRole
				}
			});
		});
	};
	/**
	 * 进入课程详情
	 * sbid 课表id
	 */
	enterClassInfo(key, sbid) {
		const {
			AdminBasePath,
		} = this.props;

		let editLink = `${AdminBasePath}/ClassSchedule/${sbid}/Edit`,
			addLink = `${AdminBasePath}/ClassSchedule/Add`;

		if (key == 'edit') {
			this.props.dispatch(
				routerRedux.push({
					pathname: editLink
				})
			);
		} else {
			this.props.dispatch(
				routerRedux.push({
					pathname: addLink
				})
			);
		}
	}
	/**
	 * 渲染查询表单
	 */
	renderAdvancedForm() {
		const {
			form: { getFieldDecorator },
			GradeList,
			SubjectStatusList,
			GradeId,
			StatusFlag,
			BeginTimeMin,
			BeginTimeMax,
			Key
		} = this.props;

		return [
			<Form onSubmit={this.handleSearch} layout="inline" key="Form1">
				<Row gutter={{ md: 2, lg: 8, xl: 24 }}>
					<Col md={2} sm={6}>
						<FormItem label="年级" key="GradeId">
							{getFieldDecorator('GradeId', {
								initialValue: GradeId
							})(
								<Select placeholder="请选择" style={{ width: '100%' }}>
									<Option key="allGradeId" value="0">
										全部
									</Option>
									{this.getOption(GradeList)}
								</Select>
							)}
						</FormItem>
					</Col>
					<Col md={2} sm={6}>
						<FormItem key="StatusFlag" label="课程状态">
							{getFieldDecorator('StatusFlag', {
								initialValue: StatusFlag
							})(
								<Select placeholder="请选择" style={{ width: '100%' }}>
									<Option key="allStatusFlag" value="">
										全部
									</Option>
									{this.getOption(SubjectStatusList)}
								</Select>
							)}
						</FormItem>
					</Col>
					<Col md={3} sm={6}>
						<FormItem key="BeginTimeMin" label="课程时间">
							{getFieldDecorator('BeginTimeMin', {
								initialValue: ((BeginTimeMin && BeginTimeMax) && [moment(BeginTimeMin), moment(BeginTimeMax)]) || undefined
							})(<RangePicker style={{ width: '100%' }} />)}
						</FormItem>
					</Col>
					<Col md={2} sm={6}>
						<FormItem key="Key">
							{getFieldDecorator('Key', {
								initialValue: Key
							})(<Search placeholder="请输入课程名称" />)}
						</FormItem>
					</Col>
					<Col md={3} sm={6}>
						<div style={{ overflow: 'hidden' }}>
							<div style={{ marginBottom: 24 }}>
								<Button type="primary" htmlType="submit">
									查询
								</Button>
								<Button
									style={{ marginLeft: 8 }}
									onClick={() => {
										this.enterClassInfo('add');
									}}
								>
									添加课程
								</Button>
								<Button style={{ marginLeft: 8 }} onClick={this.exportSubjectList}>
									导出
								</Button>
							</div>
						</div>
					</Col>
				</Row>
			</Form>
		];
	}
	/**
	 * 选择的行数
	 */
	handleSelectRows = rows => {
		this.setState({
			selectedRows: rows
		});
	};
	/**
	 * 搜索事件
	 */
	handleStandardTableChange = pagination => {
		const { dispatch } = this.props;
		const { formValues, UserRole } = this.state;

		const params = {
			PageIndex: pagination.current,
			PageSize: pagination.pageSize,
			...formValues,
			UserRole
		};

		dispatch({
			type: 'classScheduleModel/fetch',
			payload: params
		});
	};

	/**
	 * 导出课表数据
	 */
	exportSubjectList = () => {
		const { dispatch, loading } = this.props;
		const { selectedRows } = this.state;

		if (selectedRows.length == 0) {
			message.warning('请选择数据导出');
			return true;
		}

		let SubjectIds = _.map(selectedRows, 'SubjectId');

		dispatch({
			type: 'classScheduleModel/ExportSubjectList',
			payload: {
				SubjectIds: SubjectIds
			}
		});
	};

	render() {
		const { selectedRows } = this.state;
		const { ClassInfoList, Count, loading, PageSize, PageIndex } = this.props;
		const dataGroup = {
			list: ClassInfoList,
			pagination: {
				total: Count,
				pageSize: PageSize,
				current: PageIndex
			}
		};
		return (
			<div className={styles.ClassScheduleList}>
				<div className={styles.title}>课表管理</div>
				<div className={styles.tabHeader}>{this.renderAdvancedForm()}</div>
				<StandardTable
					selectedRows={selectedRows}
					loading={loading}
					data={dataGroup}
					columns={this.columns}
					onSelectRow={this.handleSelectRows}
					onChange={this.handleStandardTableChange}
				/>
			</div>
		);
	}
}
export default ClassScheduleList;
