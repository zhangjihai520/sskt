import React, { Component } from 'react';
import { Radio, Tabs, Icon, Button, Row, Col, Tag, Input } from 'antd';
import style from './ReservationCourse.less';
import ListPagination from 'components/ListPagination';
import StuCourseQueryResult from '../../../components/StuCourseQueryResult';
import { connect } from 'dva';
import { filterCourse } from 'utils/utils';

const TabPane = Tabs.TabPane;
const { CheckableTag } = Tag;

class ReservationCourse extends Component {
	constructor(props) {
		super(props);
		this.state = {
			pageSize: 10,
			pageIndex: 1,
			checkGrade: 0,
			checkCourse: 0,
			checkType: 0,
			count: 0,
			data: undefined,
			keyWord: '',
			courseList: _.cloneDeep(this.props.CourseList)
		};

		if (props.UserBaseInfo && props.UserBaseInfo.GradeId) {
			this.state.checkGrade = props.UserBaseInfo.GradeId * 1;
		}
	}

	componentDidMount() {
		const { GradeList } = this.props;
		const { checkGrade } = this.state;
		let result = _.find(GradeList, function(item) {
			return item.Id == checkGrade;
		});
		if (!result) {
			result = { Id: 0 };
		}
		this.handleGradeChange(result);
	}

	handleGradeChange = item => {
		let courseList = _.cloneDeep(this.props.CourseList);
		if (item.Id != 0) {
			_.remove(courseList, function(course) {
				return course.CourseTypeId != item.CourseTypeId;
			});
		}

		this.setState({ courseList: courseList, checkGrade: item.Id, checkCourse: 0, pageIndex: 1 }, () => {
			this.queryResult();
		});
	};

	handleCourseChange = id => {
		this.setState({ checkCourse: id, pageIndex: 1 }, () => {
			this.queryResult();
		});
	};

	handleTabChang = activeKey => {
		this.setState({ checkType: activeKey, pageIndex: 1 }, () => {
			this.queryResult();
		});
	};

	handleChangePage = state => {
		this.setState(
			{
				pageIndex: state.PageIndex,
				pageSize: state.PageSize
			},
			() => {
				this.queryResult();
			}
		);
	};

	handleKeyWordChange = e => {
		this.setState({
			keyWord: e.target.value
				.replace(/(\\*)(\/*)(\:*)(\**)(\?*)("*)(<*)(>*)(\|*)(\#*)(\`*)(&*)(\'*)/g, '')
				.substring(0, 50)
		});
	};

	handleSearch = () => {
		this.setState({ pageIndex: 1 }, () => {
			this.queryResult();
		});
	};

	queryResult = () => {
		this.setState(
			{
				count: 0,
				data: []
			},
			() => {
				const { checkGrade, checkCourse, pageSize, pageIndex, checkType, keyWord } = this.state;
				let param = {
					PageSize: pageSize,
					PageIndex: pageIndex,
					CourseId: checkCourse,
					GradeId: checkGrade,
					TypeEnum: checkType,
					IsMySubject: 0,
					Keyword: keyWord,
					SubjectGenreId: 1
				};
				this.props
					.dispatch({
						type: 'stuModel/GetStudentSubjectListLogin',
						payload: param
					})
					.then(result => {
						this.setState({
							count: result.Count,
							data: result.SubjectList
						});
					});
			}
		);
	};

	render() {
		const { GradeList, UserBaseInfo } = this.props;
		const { checkGrade, checkCourse, pageIndex, count, data, keyWord, courseList } = this.state;

		filterCourse(courseList);

		const GradeQuery = () => {
			let result;
			if (GradeList) {
				result = GradeList.map(item => {
					return (
						<CheckableTag
							key={item.Id}
							checked={item.Id == checkGrade}
							onChange={() => {
								this.handleGradeChange(item);
							}}
						>
							{item.Name}
						</CheckableTag>
					);
				});
			}

			return [
				<CheckableTag
					key={0}
					checked={checkGrade == 0}
					onChange={() => {
						this.handleGradeChange({ Id: 0 });
					}}
				>
					全部
				</CheckableTag>,
				result
			];
		};

		const CourseQuery = () => {
			let result;
			if (courseList) {
				result = courseList.map(item => {
					return (
						<CheckableTag
							key={item.Id}
							checked={item.Id == checkCourse}
							onChange={() => {
								this.handleCourseChange(item.Id);
							}}
						>
							{item.LongName}
						</CheckableTag>
					);
				});
			}

			return [
				<CheckableTag
					key={0}
					checked={checkCourse == 0}
					onChange={() => {
						this.handleCourseChange(0);
					}}
				>
					全部
				</CheckableTag>,
				result
			];
		};

		const dataContent = () => {
			if (data) {
				if (data.length == 0) {
					return <div className={style.nullPage}>暂无数据</div>;
				}
				return (
					<div className={style.listbox}>
						{data.map((item, index) => {
							return (
								<StuCourseQueryResult
									key={index}
									data={item}
									dispatch={this.props.dispatch}
									CurUserSystem={this.props.CurUserSystem}
								/>
							);
						})}
					</div>
				);
			}
		};

		return (
			<div className={style.CourseManager}>
				<div className={style.bg}>
					<div className={style.bgImg} />
					<div className={style.bgContent} />
				</div>
				<div className={style.search_filter}>
					<div className={style.keywordPanel}>
						<Input
							className={style.keyword}
							placeholder="请输入课程名称"
							value={keyWord}
							onChange={this.handleKeyWordChange}
						/>
						<Button className={style.btn} type="primary" onClick={this.handleSearch}>
							搜索
						</Button>
					</div>
					<div className={style.tagWrap}>
						<div className={style.tag_lst}>
							<Row>
								<Col>
									<i className="mr-xl" style={{ marginRight: '20px' }}>
										<span className={style.strongLal}>年级：</span>
									</i>
									<div className={style.checkedBox}>{GradeQuery()}</div>
								</Col>
							</Row>
						</div>
						<div className={style.tag_lst}>
							<Row>
								<Col>
									<i className="mr-xl" style={{ marginRight: '20px' }}>
										<span className={style.strongLal}>课程：</span>
									</i>
									<div className={style.checkedBox}>{CourseQuery()}</div>
								</Col>
							</Row>
						</div>
					</div>
					<div className={style.resultPanel}>
						<Tabs defaultActiveKey="0" onChange={this.handleTabChang}>
							<TabPane tab="未开始" key="0"></TabPane>
							<TabPane tab="已开始" key="1"></TabPane>
							<TabPane tab="已结课" key="2"></TabPane>
						</Tabs>
						<div className={style.resultList}>{dataContent()}</div>
						<ListPagination
							total={count}
							defaultPageSize={10}
							pageIndex={pageIndex}
							pageSizeOptions={['10', '25', '50']}
							onChange={this.handleChangePage}
						/>
					</div>
				</div>
			</div>
		);
	}
}

function stateToProp(state) {
	return {
		CurUserSystem: state.commonModel.CurUserSystem,
		GradeList: state.commonModel.GradeList,
		CourseList: state.commonModel.CourseList,
		UserBaseInfo: state.commonModel.UserBaseInfo
	};
}

export default connect(stateToProp)(ReservationCourse);
