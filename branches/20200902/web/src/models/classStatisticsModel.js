import * as services from '../services/classStatisticsServices';
import { routerRedux } from 'dva/router';
import { message } from 'antd';

export default {
	namespace: 'classStatisticsModel',
	state: {
    Days:"7",
    SubjectList:[],
    UserRole:3,
    //热门数据
    HotData1:{
      data:{},
      PageIndex:1,
      PageSize:10
    },
    HotData2:{
      data:{},
      PageIndex:1,
      PageSize:10
    },
    HotData3:{
      data:{},
      PageIndex:1,
      PageSize:10
    },
    //年级比数据
    PieData1:undefined,
    PieData2:undefined
  },
  reducers: {
    update(state, action) {
      return {
        ...state,
        ...action.payload,
      };
    }
  },
  effects: {
     /**
     * 获取
     */
    *fetch({ payload}, { call , put ,select}) {
      let response = yield call(services.GetHotSubjectList, payload);
      let changeHotData = {};
      //添加表格需要的key值
      response.Details.map((item,i)=>{item.key = item.Name + i});
      if(payload.TypeEnum == 1){
        changeHotData = yield select(state => {      
          state.classStatisticsModel.UserRole = payload.UserRole;    
          state.classStatisticsModel.HotData1.data = response;
          return state.classStatisticsModel.HotData1;
        });
      }else if(payload.TypeEnum == 2){
        changeHotData = yield select(state =>{
          state.classStatisticsModel.UserRole = payload.UserRole;
          state.classStatisticsModel.HotData2.data = response;
          return state.classStatisticsModel.HotData2;
        });
      }else if(payload.TypeEnum == 3){
        changeHotData = yield select(state => {
          state.classStatisticsModel.UserRole = payload.UserRole;
          state.classStatisticsModel.HotData3.data = response;
          return state.classStatisticsModel.HotData3;
        });
      }

      yield put({
        type: 'update',
        payload: {changeHotData},
      });
    },
    /**
     * 修改分页
     */
    *updatePageSize({ payload}, { call , put , select}) {
      const {TypeEnum,PageIndex,PageSize} = payload;
      let response = yield call(services.GetHotSubjectList, payload);
      let changeDataPage = {};
      let revData = {
        PageIndex,PageSize,
        data:response
      }
      if(TypeEnum == 1){
        changeDataPage = yield select(state =>
           state.classStatisticsModel.HotData1 = revData
        );
      }else if(TypeEnum == 2){
        changeDataPage = yield select(state =>
           state.classStatisticsModel.HotData2 = revData
        );
      }else if(TypeEnum == 3){
        changeDataPage = yield select(state =>
          state.classStatisticsModel.HotData3 = revData
        );
      }

      yield put({
        type: 'update',
        payload: {changeDataPage},
      });
    },
    /**
     * 获取人数详情
     */
    *fetchChartNumber({ payload}, { call , put , select }) {
        let data = yield call(services.GetSubjectListStatistics, payload);

        yield put({
          type: 'update',
          payload: {SubjectList:data.SubjectList,Days:payload.Days},
        });
    },
    /**
     * 获取学校年级占比
     */
    *fetchShare({ payload}, { call , put, select }) {
      let response = yield call(services.GetStudentSchoolShare, payload);
      let changeData =""
      if(payload.TypeEnum == 1){
        changeData =  yield select(state => state.classStatisticsModel.PieData1 = response);
      }else if(payload.TypeEnum == 2){
         changeData =  yield select(state => state.classStatisticsModel.PieData2 = response);
      }
      yield put({
        type: 'update',
        payload: changeData,
      });
    },
    /**
     * 点播课程统计
     */
    *GetPublicBenefitStatistics({ payload }, { call }) {
      return yield call(services.GetPublicBenefitStatistics, payload);
    },
  },
  subscriptions: {

	}
}
