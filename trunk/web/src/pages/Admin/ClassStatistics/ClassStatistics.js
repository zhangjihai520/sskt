import React,{Component,Fragment} from 'react';
import {Card,Radio } from 'antd';
import styles from './ClassStatistics.less';
import HotClass from './HotClass';
import { connect } from 'dva';
import ColumnDiagram from './ColumnDiagram';
import NullData from '../../../components/NullData';
import {roleEnum} from '../../../config/enum'

const params = {
  UserRole:roleEnum.admin
}

@connect(({classStatisticsModel,commonModel}) =>({
  ...classStatisticsModel,
  CurUserSystem: commonModel.CurUserSystem
}))
class ClassStatistics extends Component{
    constructor(props) {
      super(props);

      this.state ={
        tabKey:"ClassAttendance"
      }
    }
    componentDidMount() {
      const {Days} = this.props;

      this.props.dispatch({
        type: 'classStatisticsModel/fetchChartNumber',
        payload:{Days:Days,...params}
      })

      this.props.dispatch({
        type: 'classStatisticsModel/fetch',
        payload:{TypeEnum:1,...params}
      })

      this.props.dispatch({
        type: 'classStatisticsModel/fetch',
        payload:{TypeEnum:2,...params}
      })
      this.props.dispatch({
        type: 'classStatisticsModel/fetch',
        payload:{TypeEnum:3,...params}
      })
    }

    handleDataChange =(e)=>{
      this.props.dispatch({
        type: 'classStatisticsModel/fetchChartNumber',
        payload:{Days:e.target.value,...params}
      })
    }
    getColumns =(type)=>{
      const {CurUserSystem} = this.props;
      const titleName = {
         1:"课程名称",
         2:"教师名称",
         3:"学校名称"
       };

       let tableColumns = [{
         title: titleName[type], dataIndex: 'Name', key: 'Name',width: 500
        }, {
          title: '上课人数', dataIndex: 'ClassAttendance', key: 'ClassAttendance',align: 'center',
        }];

      if(type == 1 || CurUserSystem == 2){
        if(type == 1 && CurUserSystem == 1){
          tableColumns.push({
            title: '出勤率', dataIndex: 'AttendanceRate', key: 'AttendanceRate',align: 'center',render: (text, list) => text+'%'
          },{
            title: '好评度', dataIndex: 'Praise', key: 'Praise',align: 'center',
          })
        }else if(type !== 3){
          tableColumns.push({
            title: '好评度', dataIndex: 'Praise', key: 'Praise',align: 'center',
          })
        }
      }else if(type == 2){
        tableColumns.push({
          title: '出勤率', dataIndex: 'AttendanceRate', key: 'AttendanceRate',align: 'center',render: (text, list) => text+'%'
        },{
          title: '好评度', dataIndex: 'Praise', key: 'Praise',align: 'center',
        })
      }else if(type == 3){
        tableColumns.push({
          title: '出勤率', dataIndex: 'AttendanceRate', key: 'AttendanceRate',align: 'center',render: (text, list) => text+'%'
        })
      }
      return tableColumns;
    }
    onTabChange = (key) =>{
      this.setState({ tabKey: key });
    }
    render(){
      const {SubjectList,HotData1,HotData2,HotData3,Days,CurUserSystem} = this.props;
      const {tabKey} = this.state;
      let tabListNoTitle = [{
        key: 'ClassAttendance',
        tab: '上课人数',
      },{
        key: 'AttendanceRate',
        tab: '出勤率',
      }]
      if(CurUserSystem == 2){
        tabListNoTitle = [{
          key: 'ClassAttendance',
          tab: '上课人数',
        }]
      }

      return  (
          <div className={styles.Statistics}>
              <Card
                style={{ width: '100%'}}
                tabList={tabListNoTitle}
                type="inner"
                activeTabKey={tabKey}
                extra={
                  <div className={styles.salesExtra}>
                    <Radio.Group onChange={this.handleDataChange} value={Days} buttonStyle="solid">
                      <Radio.Button value="7">近7天</Radio.Button>
                      <Radio.Button value="30">近30天</Radio.Button>
                    </Radio.Group>
                  </div>
                }
                onTabChange={this.onTabChange}
              >
                {
                  SubjectList.length > 0 ? <ColumnDiagram data = {SubjectList} title ={tabKey == "AttendanceRate" ? "出勤率": "上课人数趋势"} x={"BeginTime"} y={tabKey} {...this.props}/>
                    : <NullData isShow = {true} message={"暂无数据"} />
                }
              </Card>

            {/* 热门课程 */}
            <HotClass columns={this.getColumns(1)} key="hot1" hotData = {HotData1} title = {"热门课程"} typeEnum = {1} {...this.props} />
            {/* 热门主讲老师 */}
            <HotClass columns={this.getColumns(2)} key="hot2" hotData = {HotData2} title = {"热门主讲老师"} typeEnum = {2} {...this.props} />
            {/* 热门学校 */}
            <HotClass columns={this.getColumns(3)} key="hot3" hotData = {HotData3} title = {"热门学校"} typeEnum = {3} {...this.props} />
          </div>
      )
    }
}
export default ClassStatistics;
