import React, { Component } from "react";
import { PageHeader, Select, Button, Table, message } from "antd";
import { connect } from "dva";
import ConditionBar from "components/ConditionBar";
import ListPagination from "components/ListPagination";
import styles from "./StudentGrouping.less";
const ConditionItem = ConditionBar.ConditionItem;
const { Option } = Select;

@connect(({ loading }) => ({
	tableLoading: loading.effects["classScheduleModel/GetGroupByStudentList"]
}))
class StudentGrouping extends Component {
	constructor(props) {
		super(props);
		const { subjectId } = props.location.query;
		this.state = {
			subjectId: subjectId, //课程ID
			subjectRoomId: "", //助教ID
            assistantList: [], //助教列表
            studentIds: [], //选中学生ID集合
            groupingTeacherId: undefined,  //移动分组老师ID
			pageIndex: 1, //页数
			pageSize: 25, //条数
			totalCount: 0 //总数
		};
	}
	componentDidMount() {
		const { dispatch } = this.props;
		const { subjectId } = this.state;
		dispatch({
			type: "classScheduleModel/GetSubjectRoomList",
			payload: {
				SubjectId: subjectId
			}
		}).then((res) => {
			this.setState({
				assistantList: res.SubjectRooms
			});
		});
		this.queryData();
	}
	/**
	 * 获取数据方法
	 */
	queryData = () => {
		const { subjectId, subjectRoomId, pageIndex, pageSize } = this.state;
		const { dispatch } = this.props;
		dispatch({
			type: "classScheduleModel/GetGroupByStudentList",
			payload: {
				SubjectId: subjectId,
				SubjectRoomId: subjectRoomId,
				PageIndex: pageIndex,
				PageSize: pageSize
			}
		}).then((res) => {
			const { TotalCount, Students } = res;
			this.setState({
				totalCount: TotalCount,
				studentList: Students
			});
		});
    };
    /**
     * 条件搜索
     */
    search = () => {
        this.state.pageIndex = 1;
        this.queryData();
    }
    /**
     * 移动分组
     */
    moveGrouping = (value) => {        
        const { subjectId, studentIds } = this.state;
        const { dispatch } = this.props;        
        if(studentIds.length > 0){
            dispatch({
                type: "classScheduleModel/SaveGroupByStudentList",
                payload: {
                    Students: studentIds,
                    SubjectRoomId: value,
                    SubjectId: subjectId
                }
            }).then((res)=>{
                if(res.StatusFlag === 1){
                    message.success("分组成功");
                    //清空选中列表
                    this.state.studentIds = [];
                    //操作成功后页面默认回到1
                    this.state.pageIndex = 1;
                    //只有成功了移动分组的下拉框才选中值
                    //this.state.groupingTeacherId = value;
                    //刷新列表                    
                    this.queryData();
                }else if(res.StatusFlag === 2){
                    message.warning(`移动学员失败，该助教分组人数还剩${res.Number}名`);
                }else{
                    message.error("分组移动失败");
                }
            });
        }else{
            message.warning("请先勾选需要分组的学生");
        }
    }
	render() {
		const { assistantList, studentList, totalCount, pageIndex, pageSize, groupingTeacherId, studentIds } = this.state;
        const { tableLoading } = this.props;
        //表格列配置
		const columns = [
			{
				title: "学生名称",
				dataIndex: "Name",
				key: "Name"
			},
			{
				title: "报名时间",
				dataIndex: "RegisterTime",
				key: "RegisterTime"
			},
			{
				title: "助教分组",
				dataIndex: "TeacherName",
				key: "TeacherName"
			}
        ];
        //表格选中框配置
		const rowSelection = {
            selectedRowKeys: studentIds.map((obj=>obj.UserId)),
			onChange: (selectedRowKeys, selectedRows) => {
                const arr = selectedRows.map((item)=>{
                    return {    
                        UserId: item.UserId,
                        SubjectRoomId: item.SubjectRoomId
                    }
                }); 
                this.setState({
                    studentIds: arr
                });
			},
		};
		return (
			<div>
				<PageHeader title="课堂学生" />
				<ConditionBar className={styles.condition_bar}>
					<ConditionItem title="助教分组">
						<Select
							onChange={(value) => (this.state.subjectRoomId = value)}
							defaultValue=""
							style={{ width: 120 }}
						>
							<Option value="">全部</Option>
							{assistantList.map((item, i) => {
								return (
									<Option key={item.SubjectRoomId} value={item.SubjectRoomId}>
										{item.TeacherName}
									</Option>
								);
							})}
						</Select>
					</ConditionItem>
					<ConditionItem>
						<Button onClick={this.search} type="primary">
							搜索
						</Button>
					</ConditionItem>
					<ConditionItem className="fr" title="移动分组">
						<Select
                            value={groupingTeacherId}
							onChange={this.moveGrouping}							
                            style={{ width: 120 }}
                            placeholder="移动到"
						>							
							{assistantList.map((item, i) => {
								return (
									<Option key={item.SubjectRoomId} value={item.SubjectRoomId}>
										{item.TeacherName}
									</Option>
								);
							})}
						</Select>
					</ConditionItem>
				</ConditionBar>
				<Table
                    rowKey="UserId"
					dataSource={studentList}
					columns={columns}
					loading={tableLoading}
					pagination={false}
					rowSelection={rowSelection}
				/>
				<ListPagination
					total={totalCount}
					pageIndex={pageIndex}
					defaultPageSize={pageSize}
					onChange={({ PageIndex, PageSize }) => {
						this.setState({ pageIndex: PageIndex, pageSize: PageSize }, () =>
							this.queryData()
						);
					}}
				/>
			</div>
		);
	}
}

export default StudentGrouping;
