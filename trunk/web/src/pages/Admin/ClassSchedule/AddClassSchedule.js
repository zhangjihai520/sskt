import React, { Component, Fragment } from 'react';
import { connect } from 'dva';
import { routerRedux } from 'dva/router';
import moment from 'moment';
import {
	Form,
	Row,
	Col,
	Input,
	Button,
	Icon,
	DatePicker,
	Select,
	Upload,
	message,
	InputNumber,
	Spin,
	Card,
	Checkbox,
} from 'antd';
import styles from './AddClassSchedule.less';
import debounce from 'lodash/debounce';
import _ from 'lodash';
import { RemoveMessage } from '@/services/curriculumServices';

const { RangePicker } = DatePicker;
const Option = Select.Option;

@connect(({ commonModel, classScheduleModel, loading }) => ({
	...classScheduleModel,
	loading: loading.effects['classScheduleModel/saveClass'],
	GradeList: commonModel.GradeList,
	CourseList: commonModel.CourseList,
	AdminBasePath: commonModel.AdminBasePath,
	CurUserSystem: commonModel.CurUserSystem
}))
@Form.create()
class AddClassSchedule extends Component {
	constructor(props) {
		super(props);
		this.fetchUser = debounce(this.fetchUser, 800);
	}
	/**
	 * state
	 */
	state = {
		expand: false,
		imageUrl: '',
		fetching: false,
		TeacherList: [],
		imgList: [],
		courseList: _.cloneDeep(this.props.CourseList),
		checkde:false
	};
	/**
	 * 初始化生命周期
	 */
	componentDidMount() {
		const { dispatch, match, form, location } = this.props;
		const SubjectId = match.params.SubjectId;
		dispatch({
			type: 'classScheduleModel/saveListQuery',
			payload: location.query
		})

		if (SubjectId) {
			dispatch({
				type: 'classScheduleModel/getClassInfo',
				payload: { SubjectId }
			}).then(data => {
				const gradeResult = _.find(this.props.GradeList, item => {
					return item.Id == data.GradeId
				});

				let courseList = _.cloneDeep(this.props.CourseList);
				if (gradeResult) {
					_.remove(courseList, function (course) {
						return course.CourseTypeId != gradeResult.CourseTypeId;
					});
				};

				this.setState({
					imgList: data.ImagePath,
					courseList: courseList
				});
			});
		} else {
			dispatch({
				type: 'classScheduleModel/clearInfo'
			});
		}
	}

	/**
	 * 获取select 数据
	 */
	getOption = (list, isNullDisplay = true) => {
		if (!list || (list.length < 1 && isNullDisplay)) {
			return (
				<Option key={0} value={0}>
					暂无数据
				</Option>
			);
		}
		return list.map(item => (
			<Option key={item.Id} value={item.Id}>
				{item.SchoolName ? item.Name + item.SchoolName : item.Name}
			</Option>
		));
	};
	/**
	 * 教师搜索
	 */
	fetchUser = value => {
		const { dispatch } = this.props;

		this.setState({ TeacherList: [], fetching: true });
		dispatch({
			type: 'classScheduleModel/getTeacher',
			payload: { TeacherName: value }
		}).then(data => {
			this.setState({
				TeacherList: data.TeacherList,
				fetching: false
			});
		});
	};
	/**
	 * 信息
	 */
	handleChange = info => {
		if (info.file.status === 'uploading') {
			const isJPG = info.file.type === 'image/jpeg';
			if (!isJPG) {
				message.error('请上传jpg格式图片');
				return false;
			}
		}
		if (info.file.status === 'error') {
			message.error(`${info.file.name} 上传失败.`);
		}
		this.setState({ imgList: info.fileList });
	};

	/**
	 * 删除图片
	 */
	fileRemove = info => {
		this.setState({ imgList: [] });
	};

	disabledDate = current => {
		return current < moment().startOf('day');
	};

	/**
	 * 获取表单
	 */
	getFields() {
		const {
			form: { getFieldDecorator, resetFields, setFieldsValue },
			GradeList,
			InfoData,
			CurUserSystem
		} = this.props;
		const { fetching, TeacherList, imgList, disableBeginTime = null, disableEndTime = null, pageType, courseList } = this.state;
		const uploadButton = (
			<div>
				<Icon type={this.state.loading ? 'loading' : 'plus'} />
				<div className="ant-upload-text">上传图片</div>
			</div>
		);
		return (
			<Fragment>
				<Row gutter={12}>
					<Col span={4} key={'SubjectName'}>
						<Form.Item label={'课程名称'}>
							{getFieldDecorator(`SubjectName`, {
								initialValue: InfoData.SubjectName || undefined,
								rules: [
									{
										required: true,
										message: '请输入课程名称'
									}
								]
							})(<Input placeholder="请输入课程名称" maxLength={20} />)}
						</Form.Item>
					</Col>
					<Col span={4} key={'RegistBeginTime'}>
						<Form.Item label={'报名时间'}>
							{getFieldDecorator(`RegistBeginTime`, {
								initialValue:
									(InfoData.RegistBeginTime && [moment(InfoData.RegistBeginTime), moment(InfoData.RegistEndTime)]) ||
									undefined,
								rules: [
									{
										required: true,
										message: '请选择报名时间段'
									}
								]
							})(
								<RangePicker
									style={{ width: '100%' }}
									disabledDate={this.disabledDate}
									onChange={(date, dateString) => {
										resetFields(['BeginTime']);
										this.setState({ disableBeginTime: date[1] });
									}}
								/>
							)}
						</Form.Item>
					</Col>
					<Col span={4} key={'BeginTime'} className={styles.picker}>
						<Form.Item label={'课程时间'}>
							{getFieldDecorator(`BeginTime`, {
								initialValue: (InfoData.BeginTime && moment(InfoData.BeginTime)) || undefined,
								rules: [
									{
										required: true,
										message: '请选择课程时间'
									}
								]
							})(
								<DatePicker
									showTime
									style={{ width: '100%' }}
									disabledDate={current => current < (disableBeginTime || moment(InfoData.RegistEndTime))}
									onChange={(date, dateString) => {
										setFieldsValue({ EndTime: date });
										this.setState({ disableEndTime: date });
									}}
								/>
							)}
						</Form.Item>
					</Col>
					<Col span={4} key={'GradeId'}>
						<Form.Item label={'使用年级'}>
							{getFieldDecorator(`GradeId`, {
								initialValue: (InfoData.GradeId && InfoData.GradeId + '') || undefined,
								rules: [
									{
										required: true,
										message: '请选择年级'
									}
								]
							})(<Select placeholder="请选择" onChange={(value) => {
								const gradeResult = _.find(this.props.GradeList, item => {
									return item.Id == value
								});

								let courseList = _.cloneDeep(this.props.CourseList);
								if (gradeResult) {
									_.remove(courseList, function (course) {
										return course.CourseTypeId != gradeResult.CourseTypeId;
									});
								}
								setFieldsValue({ CourseId: undefined });
								this.setState({
									courseList: courseList
								});

							}}>{this.getOption(GradeList)}</Select>)}
						</Form.Item>
					</Col>
					<Col span={4} key={'CourseId'}>
						<Form.Item label={'学段学科'}>
							{getFieldDecorator(`CourseId`, {
								initialValue: (InfoData.CourseId && InfoData.CourseId + '') || undefined,
								rules: [
									{
										required: true,
										message: '请选择学段学科'
									}
								]
							})(
								<Select placeholder="请选择学段学科">
									{courseList.map(item => (
										<Option key={item.Id} value={item.Id}>
											{item.LongName}
										</Option>
									))}
								</Select>
							)}
						</Form.Item>
					</Col>
					<Col span={4} key={'TeacherId'}>
						<Form.Item label={'主讲老师'}>
							{getFieldDecorator(`TeacherId`, {
								initialValue:
									(InfoData.TeacherName && {
										key: InfoData.TeacherId,
										label: InfoData.TeacherName
									}) ||
									undefined,
								rules: [
									{
										required: true,
										message: '请选择主讲老师'
									}
								]
							})(
								<Select
									labelInValue
									showSearch
									placeholder="请输入主讲老师名称"
									notFoundContent={fetching ? <Spin size="small" /> : null}
									filterOption={false}
									onSearch={this.fetchUser}
									style={{ width: '60%' }}
								>
									{this.getOption(TeacherList, false)}
								</Select>
							)}
						</Form.Item>
					</Col>
					<Col span={4} key={'SubjectGenreId'}>
						<Form.Item label={'课程类型'}>
							{getFieldDecorator(`SubjectGenreId`, {
								initialValue: (InfoData.SubjectGenreId && InfoData.SubjectGenreId + '') || '1',
								rules: [
									{
										required: false,
										message: '请选择课程类型'
									}
								]
							})(
								<Select placeholder="请选择课程类型">
									<Option key="SubjectGenreId1" value="1">
										直播室直播
									</Option>
									<Option key="SubjectGenreId2" value="2">
										录播
									</Option>
									<Option key="SubjectGenreId3" value="3">
										在线直播
									</Option>
								</Select>
							)}
						</Form.Item>
					</Col>
					<Col span={4} key={'EndTime'} className={styles.picker}>
						<Form.Item label={'结课时间（结课时间不得早于上课时间）'}>
							{getFieldDecorator(`EndTime`, {
								initialValue: (InfoData.EndTime && moment(InfoData.EndTime)) || undefined,
								rules: [
									{
										required: false,
										message: '请选择结课时间'
									}
								]
							})(
								<DatePicker
									showTime
									style={{ width: '100%' }}
									disabledDate={current => current < (disableEndTime || moment(InfoData.BeginTime))}
								/>
							)}
						</Form.Item>
					</Col>
					<Col span={4} key={'VideoUrl'}>
						<Form.Item label={'录播视频URL'}>
							{getFieldDecorator(`VideoUrl`, {
								initialValue: InfoData.VideoUrl ? decodeURI(InfoData.VideoUrl) : '',
								rules: [
									{
										required: false
									}
								]
							})(<Input.TextArea rows={4} style={{ width: '80%' }} placeholder="请输入录播视频URL" maxLength={256} />)}
						</Form.Item>
					</Col>
					<Col span={4} key={'ImagePath'}>
						<Form.Item label={'上传图片'}>
							{getFieldDecorator(`ImagePath`, {
								initialValue: InfoData.ImagePath || undefined,
								rules: [
									{
										required: false,
										message: '请上传图片'
									}
								]
							})(
								<Upload
									name="file"
									listType="picture-card"
									className="avatar-uploader"
									fileList={imgList}
									action="/api/Common/UploadFile"
									onChange={this.handleChange}
									onRemove={this.fileRemove}
								>
									{imgList && imgList.length >= 1 ? null : uploadButton}
								</Upload>
							)}
						</Form.Item>
					</Col>
					<Col span={4} key={'Comment'}>
						<Form.Item label={'课程描述（备注）'}>
							{getFieldDecorator(`Comment`, {
								initialValue: InfoData.Comment || '',
								rules: [
									{
										required: false
									}
								]
							})(<Input.TextArea rows={4} style={{ width: '80%' }} placeholder="请输入备注信息" maxLength={300} />)}
						</Form.Item>
					</Col>
				</Row>
				{this.getClassInfo()}
			</Fragment>
		);
	}
	/**
	 * 删除课堂
	 * index 删除数组索引
	 */
	delClass = index => {
		const { dispatch } = this.props;

		dispatch({
			type: 'classScheduleModel/delClassInfo',
			payload: { index }
		});
	};
	/**
	 * 效验助教
	 */
	validateHelpTeacher = (rule, value, callback) => {
		const { form } = this.props;
		let currentValue = value ? value.key : undefined;
		let obj = form.getFieldsValue();
		let arr = [];


		//取出所有助教信息人数
		for (let key in obj) {
			if (key.indexOf('HelpTeacherId') >= 0) {
				if (obj[key] && obj[key].key) {
					arr.push(obj[key].key);
				}
			}

		}
		//筛选出和当前value值一样的数据，数量大于1等于有重复的助教
		if (arr.length > 0 && arr.filter(item => item === currentValue).length > 1) {
			callback('不能选择相同的助教老师');
		} else {
			callback();
		}
	};
	/**
	 * 效验人数
	 */
	validateNumber = (rule, value, callback) => {
		const { CurUserSystem } = this.props;
		//CurUserSystem为2线下环境不做效验
		if (CurUserSystem == 2) {
			callback();
		} else {
			if (value === 0) {
				callback('人数限制不能为0');
				return false;
			}
			const { form } = this.props;
			let obj = form.getFieldsValue();
			let arr = [];
			//取出所有助教信息人数
			for (let key in obj) {
				if (key.indexOf('MaxRegisterNumber') >= 0) {
					arr.push(obj[key]);
				}
			}
			//求和
			const total = arr.reduce(function (prev, cur) {
				return prev + cur;
			}, 0);
			callback();
		}
	};

	// 选择是否分派人数
	Change = (rule, value, callback) => {
		const { form } = this.props;
		let obj = form.getFieldsValue();
		let arr = [];
		//取出所有状态
		for (let key in obj) {
			if (key.indexOf('Automatic') >= 0) {
				arr.push(obj[key]);
			}
		}
	}
	/**
	 * 教室信息
	 */
	getClassInfo() {
		const {
			form: { getFieldDecorator },
			InfoData: { SubjectRooms },
			CurUserSystem
		} = this.props;
		const { fetching, TeacherList } = this.state;
		const chiren = [];
		console.log(this.props)
		SubjectRooms.map((room, i) => {
			chiren.push(
				<Fragment key={i}>
					<div className="u-dividerLine"></div>
					<Card
						className={styles.info}
						bordered={false}
						title={'助教信息'}
						extra={
							i !== 0 && (
								<div
									className={styles.closeIcon}
									onClick={() => {
										this.delClass(i);
									}}
								>
									<Icon type="close" />
								</div>
							)
						}
					>
						<Row data-index={i} key={`row${i}`}>
							{/* <Col span={4} key={room.SubjectRoomName + "教室名称"}>
								<Form.Item label={"教室名称"}>
									{getFieldDecorator(`SubjectRoomName${i}`, {
										initialValue: room.SubjectRoomName || "",
										rules: [
											{
												required: true,
												message: "请输入教室名称"
											}
										]
									})(<Input placeholder="请输入教室名称" maxLength={20} />)}
								</Form.Item>
							</Col>
							<Col span={4} key={room.SubjectRoomId + "学校名"}>
								<Form.Item label={"学校名"}>
									{getFieldDecorator(`SchoolName${i}`, {
										initialValue: room.SchoolName || "",
										rules: [
											{
												required: true,
												message: "请输入学校名"
											}
										]
									})(<Input placeholder="请输入教室名称" maxLength={20} />)}
								</Form.Item>
							</Col> */}
							<Col span={4} key={room.SubjectRoomId + '助教老师'}>
								<Form.Item label={'助教老师'}>
									{getFieldDecorator(`HelpTeacherId${i}`, {
										initialValue:
											(room.HelpTeacherName && {
												key: room.HelpTeacherId,
												label: room.HelpTeacherName
											}) ||
											undefined,
										rules: [
											{
												required: true,
												message: '请输入助教老师名称'
											},
											{
												validator: this.validateHelpTeacher
											}
										]
									})(
										<Select
											labelInValue
											showSearch
											placeholder="请输入助教老师名称"
											notFoundContent={fetching ? <Spin size="small" /> : null}
											filterOption={false}
											onSearch={this.fetchUser}
											style={{ width: '60%' }}
										>
											{this.getOption(TeacherList, false)}
										</Select>
									)}
								</Form.Item>
							</Col>
							{
								<Col span={4} key={room.SubjectRoomId + '人数限制'} className={CurUserSystem == 2 ? 'none' : 'db'}>
									<Form.Item label={'人数限制'}>
										{getFieldDecorator(`MaxRegisterNumber${i}`, {
											initialValue: room.MaxRegisterNumber || 0,
											rules: [
												{
													required: true,
													message: '请输入人数限制'
												},
												{
													validator: this.validateNumber
												}
											]
										})(<InputNumber precision={0} min={0} />)}
										
										{/* <Checkbox  checked={room.Automatic ==1 ? true :false} style={{ marginLeft: '100px', color: '#989b9b' }}  onChange={this.Change} value='0'>
											分派人数
         				 		</Checkbox> */}
									</Form.Item>
								</Col>
							}
							<Col span={4} key={room.Automatic + 'id'} >
								<Form.Item >
									{getFieldDecorator(`Automatic${i}`, {
										initialValue: room.Automatic || false,
										rules: [
											{
												required: false,
											},
											
										]
									})(<Checkbox  style={{ marginLeft: '100px', color: '#989b9b',marginTop:'24px' }} onChange={this.Change}>
									自动扩班
									</Checkbox>)}
								</Form.Item>
							</Col>
							{/* <col span={4} >
								<Form.Item>
									<Checkbox style={{ marginLeft: '10px', color: '#989b9b' }} >
										禁言当前房间
         				 	</Checkbox>
								</Form.Item>
							</col> */}

							<Col span={4} key={room.SubjectRoomId + 'id'} className={'none'}>
								<Form.Item label={'教室名称id'}>
									{getFieldDecorator(`SubjectRoomId${i}`, {
										initialValue: room.SubjectRoomId || '',
										rules: [
											{
												required: false
											}
										]
									})(<Input placeholder="记录教室id" />)}
								</Form.Item>
							</Col>
							<Col span={12} key={`null${i}`}></Col>
						</Row>
					</Card>
				</Fragment>
			);
		});
		return chiren;
	}
	/**
	 * 提交表单
	 */
	handleSubmit = e => {
		const { dispatch, form, match } = this.props;
		e.preventDefault();
		const { imgList } = this.state;
		if (imgList.length == 0) {
			form.setFieldsValue({ ImagePath: [] });
		}
		form.validateFieldsAndScroll((err, values) => {
			if (!err && values.ImagePath) {
				let begindt = moment(values.RegistBeginTime[0]).format('YYYY-MM-DD');
				let enddt = moment(values.RegistBeginTime[1]).format('YYYY-MM-DD');
				let BeginTime = moment(values.BeginTime).format('YYYY-MM-DD HH:mm:ss');
				let EndTime = moment(values.EndTime).format('YYYY-MM-DD HH:mm:ss');
				let Teacher = values.TeacherId;
				if (begindt == enddt) {
					form.setFields({
						RegistBeginTime: {
							value: values.RegistBeginTime,
							errors: [new Error('报名时间段不能在同一天')]
						}
					});
					return false;
				}
				if (moment(values.BeginTime).isAfter(values.EndTime)) {
					form.setFields({
						EndTime: {
							value: values.EndTime,
							errors: [new Error('结课时间不得早于上课时间')]
						}
					});
					return false;
				}

				if (values.ImagePath.fileList && values.ImagePath.fileList[0].response.ReturnEntity.Result === 1) {
					values.ImagePath = values.ImagePath.fileList[0].response.ReturnEntity.FilePath;
				} else if (values.ImagePath.length > 0) {
					values.ImagePath = values.ImagePath[0].url;
				} else {
					values.ImagePath = null;
				}
				values.TeacherId = Teacher.key;
				values.TeacherName = Teacher.label;
				values.SubjectId = match.params.SubjectId;
				values.RegistBeginTime = begindt;
				values.RegistEndTime = enddt;

				values.BeginTime = BeginTime;
				values.EndTime = EndTime;
				const test = /^(SubjectRoomName)|(SchoolName)|(HelpTeacherId)|(MaxRegisterNumber)|(SubjectRoomId)|(Automatic) \d$/;
				const params = {
					...values,
					SubjectRooms: []
				};
				//处理助教信息数据
				const dataValue = [];
				const dataKey = [];
				console.log(params);
				for (const key in params) {
					if (test.test(key) || key.indexOf('Automatic') >= 0) {
						dataValue.push(params[key]);
						dataKey.push(key);
					}
				}
				console.log(dataValue);
				const newData = dataValue
					.map((ele, i, a) => {
						//因为一个助教有三个数据，所以每隔三次赋值一次
						if (i % 4 === 0) {
							const obj = {};
							let helpTeacher = ele;
							obj.HelpTeacherId = helpTeacher.key;
							obj.HelpTeacherName = helpTeacher.label;
							obj.MaxRegisterNumber = a[i + 1];
							obj.Automatic = a[i+2] ? 1 :0;
							obj.SubjectRoomId = a[i +3];
							obj.SubjectRoomName = ''; //废弃字段
							obj.SchoolName = ''; //废弃字段
							// obj.Automatic = this.state.Automatic ? 1 :0;
							return obj;
						}
						return null;
					})
					.filter(ele => ele !== null);
					console.log(newData)
				params.SubjectRooms = newData;
				dataKey.forEach(ele => delete params[ele]); // 删除多余键值对
				dispatch({
					type: 'classScheduleModel/saveClass',
					payload: params
				});
			}
		});
	};
	/**
	 * 新增教室
	 */
	handleAdd = () => {
		this.props.dispatch({
			type: 'classScheduleModel/addClassRoom'
		});
		// this.setState({
		// 	Automatic: false
		// })
	};
	/**
	 * 返回
	 */
	handleBack = () => {
		const { AdminBasePath } = this.props;

		this.props.dispatch(
			routerRedux.push({
				pathname: `${AdminBasePath}/ClassSchedule`
			})
		);
	};

	render() {
		const {
			InfoData: { SubjectRooms },
			loading
		} = this.props;
		return (
			<div className={styles.addClass}>
				<div className={styles.title}>课程信息</div>
				<Form key={'form1'} className="ant-advanced-search-form" onSubmit={this.handleSubmit}>
					{this.getFields()}
					<div className="u-dividerLine"></div>
					{SubjectRooms.length >= 6 ? (
						<div className={styles.add + ' ' + styles.limit}>
							<Icon type="exclamation-circle" /> &nbsp;最多只能添加六个助教
						</div>
					) : (
							<div className={styles.add} onClick={this.handleAdd}>
								<Icon type={'plus'} /> &nbsp;新增助教
							</div>
						)}

					<div className="u-dividerLine"></div>
					<Row>
						<Col span={12} style={{ textAlign: 'center', padding: '15px' }}>
							<Button type="primary" loading={loading} htmlType="submit">
								确定
							</Button>
							<Button style={{ marginLeft: 8 }} onClick={this.handleBack}>
								返回
							</Button>
						</Col>
					</Row>
				</Form>
			</div>
		);
	}
}

export default AddClassSchedule;
